package net.threetag.palladium.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.threetag.palladium.client.beam.Beam;
import net.threetag.palladium.client.beam.BeamManager;
import net.threetag.palladium.client.renderer.entity.state.SwingAnchorRenderState;
import net.threetag.palladium.entity.SwingAnchor;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.util.EntityScaleUtil;
import org.jetbrains.annotations.NotNull;

public class SwingAnchorRenderer extends EntityRenderer<SwingAnchor, SwingAnchorRenderState> {

    public SwingAnchorRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void submit(SwingAnchorRenderState renderState, PoseStack poseStack, SubmitNodeCollector nodeCollector, CameraRenderState cameraRenderState) {
        super.submit(renderState, poseStack, nodeCollector, cameraRenderState);

        if (renderState.owner instanceof AbstractClientPlayer player) {
            poseStack.pushPose();
            var ownerPos = renderState.owner.getPosition(renderState.partialTick);
            var anchorPos = new Vec3(renderState.x, renderState.y, renderState.z);
            var diff = ownerPos.subtract(anchorPos);
            poseStack.translate(diff.x, diff.y, diff.z);
            anchorPos = anchorPos.add(0, 50, 0);
            var beamRenderer = BeamManager.INSTANCE.get(renderState.beamRendererId);

            if (beamRenderer != null) {
                var sizeMultiplier = new Vec2(
                        EntityScaleUtil.getInstance().getModelWidthScale(player, renderState.partialTick),
                        EntityScaleUtil.getInstance().getModelHeightScale(player, renderState.partialTick)
                );

                if (renderState.despawning) {
                    for (Beam beam : beamRenderer.beams()) {
                        var targetPos = renderState.beamAnchors.get(beam);
                        var distance = targetPos.distanceTo(anchorPos);
                        var belowPos = anchorPos.subtract(0, distance, 0);
                        targetPos = targetPos.add(targetPos.subtract(belowPos).scale(renderState.opacity / 3F)).subtract(ownerPos);

                        beam.submit(DataContext.forEntity(player), targetPos,
                                anchorPos.subtract(ownerPos), sizeMultiplier, 1F, renderState.opacity, poseStack, nodeCollector,
                                renderState.lightCoords, player.tickCount, renderState.partialTick);
                    }
                } else {
                    beamRenderer.submitOnPlayer(player, ownerPos,
                            anchorPos, 1F, renderState.opacity, poseStack, nodeCollector,
                            renderState.lightCoords, renderState.partialTick);
                }

                beamRenderer.submit(DataContext.forEntity(player), anchorPos.subtract(ownerPos),
                        anchorPos.subtract(ownerPos).add(0, 100, 0), sizeMultiplier, 1F, renderState.opacity, poseStack, nodeCollector,
                        renderState.lightCoords, player.tickCount, renderState.partialTick);
            }

            poseStack.popPose();
        }
    }

    @Override
    public @NotNull SwingAnchorRenderState createRenderState() {
        return new SwingAnchorRenderState();
    }

    @Override
    public void extractRenderState(SwingAnchor entity, SwingAnchorRenderState reusedState, float partialTick) {
        super.extractRenderState(entity, reusedState, partialTick);
        reusedState.owner = entity.getOwner();
        reusedState.beamRendererId = entity.getBeamRendererId();
        reusedState.partialTick = partialTick;
        reusedState.opacity = entity.getOpacity(partialTick);
        reusedState.despawning = entity.isMarkedToDespawn();
        reusedState.beamAnchors = entity.beamAnchorCache;

        if (entity.isMarkedToDespawn() && entity.beamAnchorCache.isEmpty() && entity.getOwner() instanceof AbstractClientPlayer player) {
            var beamRenderer = BeamManager.INSTANCE.get(entity.getBeamRendererId());

            if (beamRenderer != null) {
                entity.beamAnchorCache.putAll(beamRenderer.getAnchorPositions(player, partialTick));
            }
        }
    }

    @Override
    protected boolean affectedByCulling(@NotNull SwingAnchor entity) {
        return false;
    }
}
