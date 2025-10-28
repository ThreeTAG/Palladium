package net.threetag.palladium.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.ClientAvatarEntity;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.HumanoidArm;
import net.threetag.palladium.client.renderer.entity.layer.ClientEntityRenderLayers;
import net.threetag.palladium.client.renderer.entity.state.PalladiumRenderStateKeys;
import net.threetag.palladium.client.util.PerspectiveAwareConditions;
import net.threetag.palladium.entity.data.PalladiumEntityData;
import net.threetag.palladium.entity.data.PalladiumEntityDataTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AvatarRenderer.class)
public abstract class AvatarRendererMixin<AvatarlikeEntity extends Avatar & ClientAvatarEntity> {

    @Inject(at = @At("RETURN"), method = "renderHand")
    private void renderHand(
            PoseStack poseStack, SubmitNodeCollector nodeCollector, int packedLight, ResourceLocation skinTexture, ModelPart armPart, boolean renderSleeve, CallbackInfo ci
    ) {
        var player = Minecraft.getInstance().player;
        var l = PalladiumEntityData.get(player, PalladiumEntityDataTypes.RENDER_LAYERS.get());
        AvatarRenderer<?> playerRenderer = (AvatarRenderer<?>) (Object) this;

        if (l instanceof ClientEntityRenderLayers layers) {
            boolean rightArm = armPart == playerRenderer.getModel().rightArm;
            var arm = rightArm ? HumanoidArm.RIGHT : HumanoidArm.LEFT;

            layers.getLayerStates().forEach((layer, state) -> {
                if (layer.shouldRender(state, PerspectiveAwareConditions.Perspective.FIRST_PERSON)) {
                    layer.submitArm(state.getContext(), poseStack, nodeCollector, arm, armPart, playerRenderer, state, packedLight);
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
