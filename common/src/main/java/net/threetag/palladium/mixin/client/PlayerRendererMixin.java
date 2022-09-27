package net.threetag.palladium.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.HumanoidArm;
import net.threetag.palladium.client.model.animation.HumanoidAnimationsManager;
import net.threetag.palladium.client.renderer.renderlayer.IPackRenderLayer;
import net.threetag.palladium.client.renderer.renderlayer.PackRenderLayerManager;
import net.threetag.palladium.power.ability.Abilities;
import net.threetag.palladium.power.ability.Ability;
import net.threetag.palladium.power.ability.AbilityEntry;
import net.threetag.palladium.power.ability.RenderLayerAbility;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("ConstantConditions")
@Mixin(PlayerRenderer.class)
public class PlayerRendererMixin {

    @Inject(at = @At("RETURN"), method = "setupRotations(Lnet/minecraft/client/player/AbstractClientPlayer;Lcom/mojang/blaze3d/vertex/PoseStack;FFF)V")
    public void setupRotations(AbstractClientPlayer player, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTicks, CallbackInfo ci) {
        PlayerRenderer playerRenderer = (PlayerRenderer) (Object) this;
        HumanoidAnimationsManager.setupRotations(playerRenderer, player, poseStack, ageInTicks, rotationYaw, partialTicks);
    }

    @Inject(at = @At("HEAD"), method = "renderHand")
    public void renderHandPre(PoseStack poseStack, MultiBufferSource buffer, int combinedLight, AbstractClientPlayer player, ModelPart rendererArm, ModelPart rendererArmwear, CallbackInfo ci) {
        PlayerRenderer playerRenderer = (PlayerRenderer) (Object) this;

        if (playerRenderer.getModel() instanceof AgeableListModelInvoker invoker) {
            HumanoidAnimationsManager.resetModelParts(invoker.invokeHeadParts(), invoker.invokeBodyParts());
        }
    }

    @Inject(at = @At("RETURN"), method = "renderHand")
    public void renderHandPost(PoseStack poseStack, MultiBufferSource buffer, int combinedLight, AbstractClientPlayer player, ModelPart rendererArm, ModelPart rendererArmwear, CallbackInfo ci) {
        PlayerRenderer playerRenderer = (PlayerRenderer) (Object) this;

        for (AbilityEntry entry : Ability.getEnabledEntries(player, Abilities.RENDER_LAYER.get())) {
            IPackRenderLayer layer = PackRenderLayerManager.getInstance().getLayer(entry.getProperty(RenderLayerAbility.RENDER_LAYER));
            if (layer != null) {
                layer.renderArm(rendererArm == playerRenderer.getModel().rightArm ? HumanoidArm.RIGHT : HumanoidArm.LEFT, player, playerRenderer, poseStack, buffer, combinedLight);
            }
        }
    }

}
