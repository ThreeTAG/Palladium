package net.threetag.palladium.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.threetag.palladium.client.renderer.entity.effect.EntityEffectRenderer;
import net.threetag.palladium.entity.EffectEntity;
import net.threetag.palladium.entity.effect.EntityEffect;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class EffectEntityRenderer extends EntityRenderer<EffectEntity, EffectEntityRenderer.EffectEntityRenderState> {

    public EffectEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void submit(EffectEntityRenderState renderState, PoseStack poseStack, SubmitNodeCollector nodeCollector, CameraRenderState cameraRenderState) {
        Entity anchor = renderState.anchor;
        if (anchor != null) {
            var mc = Minecraft.getInstance();
            poseStack.pushPose();
            EntityEffectRenderer.renderEffect(renderState.effect, renderState, anchor, poseStack, nodeCollector, renderState.lightCoords,
                    mc.player == anchor && mc.options.getCameraType().isFirstPerson(),
                    Mth.frac(renderState.ageInTicks));
            poseStack.popPose();
        }
    }

    @Override
    public @NotNull EffectEntityRenderState createRenderState() {
        return new EffectEntityRenderState();
    }

    @Override
    public void extractRenderState(@NotNull EffectEntity entity, @NotNull EffectEntityRenderState reusedState, float partialTick) {
        super.extractRenderState(entity, reusedState, partialTick);
        reusedState.effect = entity.entityEffect;
        reusedState.anchor = Objects.requireNonNull(Minecraft.getInstance().level).getEntity(entity.anchorId);
        reusedState.position = entity.getPosition(partialTick);
        reusedState.extraData = entity.getExtraData();
    }

    @Override
    protected boolean affectedByCulling(@NotNull EffectEntity entity) {
        return false;
    }

    public static class EffectEntityRenderState extends EntityRenderState {

        public Vec3 position;
        public EntityEffect effect;
        public Entity anchor;
        public CompoundTag extraData;

    }

}
