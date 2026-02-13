package net.threetag.palladium.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.ClientAvatarEntity;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.HumanoidArm;
import net.threetag.palladium.client.AbilityClientEventHandler;
import net.threetag.palladium.client.renderer.entity.layer.pack.ClientEntityRenderLayers;
import net.threetag.palladium.client.renderer.entity.layer.pack.PackRenderLayer;
import net.threetag.palladium.client.renderer.entity.layer.pack.VibrationPackRenderLayer;
import net.threetag.palladium.client.renderer.entity.state.PalladiumRenderStateKeys;
import net.threetag.palladium.client.util.PerspectiveAwareConditions;
import net.threetag.palladium.entity.data.PalladiumEntityData;
import net.threetag.palladium.entity.data.PalladiumEntityDataTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AvatarRenderer.class)
public abstract class AvatarRendererMixin<AvatarlikeEntity extends Avatar & ClientAvatarEntity> {

    @Inject(at = @At("RETURN"), method = "renderHand")
    private void renderHand(
            PoseStack poseStack, SubmitNodeCollector nodeCollector, int packedLight, Identifier skinTexture, ModelPart armPart, boolean renderSleeve, CallbackInfo ci
    ) {
        this.palladium$renderHandLayers(poseStack, nodeCollector, packedLight, skinTexture, armPart, true);
    }

    @Unique
    @SuppressWarnings({"DataFlowIssue", "rawtypes"})
    private void palladium$renderHandLayers(PoseStack poseStack, SubmitNodeCollector nodeCollector, int packedLight, Identifier skinTexture, ModelPart armPart, boolean root) {
        var player = Minecraft.getInstance().player;
        var entityLayers = PalladiumEntityData.get(player, PalladiumEntityDataTypes.RENDER_LAYERS.get());
        AvatarRenderer<?> playerRenderer = (AvatarRenderer<?>) (Object) this;

        if (entityLayers instanceof ClientEntityRenderLayers layers) {
            boolean rightArm = armPart == playerRenderer.getModel().rightArm;
            var arm = rightArm ? HumanoidArm.RIGHT : HumanoidArm.LEFT;

            layers.getLayerStates().forEach((layer, state) -> {
                if (layer.shouldRender(state, PerspectiveAwareConditions.Perspective.FIRST_PERSON)) {
                    PackRenderLayer l = layer;
                    var rand = RandomSource.create();

                    if (l instanceof VibrationPackRenderLayer vibration && state instanceof VibrationPackRenderLayer.State vibrationState) {
                        if (root) {
                            AbilityClientEventHandler.OVERRIDDEN_OPACITY = 0.15F;
                            var progress = vibrationState.getProgress(Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaTicks());

                            for (int i = 0; i < vibration.intensity; i++) {
                                poseStack.pushPose();
                                poseStack.translate(((rand.nextFloat() - 0.5F) / 1500) * progress, 0, ((rand.nextFloat() - 0.5F) / 1500) * progress);
                                nodeCollector.submitModelPart(armPart, poseStack, RenderTypes.entityTranslucent(skinTexture), packedLight, OverlayTexture.NO_OVERLAY, null);
                                this.palladium$renderHandLayers(poseStack, nodeCollector, packedLight, skinTexture, armPart, false);
                                poseStack.popPose();
                            }
                        }
                    } else {
                        layer.submitArm(state.getContext(), poseStack, nodeCollector, arm, armPart, playerRenderer, state, packedLight);
                    }
                }
            });
        }
    }

    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;F)V",
            at = @At("RETURN"))
    private void extractRenderState(AvatarlikeEntity entity, AvatarRenderState reusedState, float partialTick, CallbackInfo ci) {
        PalladiumRenderStateKeys.manipulatePlayerState(entity, reusedState, partialTick);
    }

}
