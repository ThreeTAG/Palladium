package net.threetag.palladium.mixin.client;

import com.google.common.base.MoreObjects;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.threetag.palladium.item.IAddonItem;
import net.threetag.palladium.power.ability.Abilities;
import net.threetag.palladium.power.ability.AbilityUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public abstract class ItemInHandRendererMixin {

    @Shadow
    protected abstract void renderPlayerArm(PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, float equippedProgress, float swingProgress, HumanoidArm side);

    @Shadow
    private ItemStack offHandItem;

    @Shadow
    private ItemStack mainHandItem;

    @Inject(method = "renderArmWithItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isEmpty()Z", ordinal = 0))
    private void renderArmWithItem(
            AbstractClientPlayer player, float partialTicks, float pitch, InteractionHand hand, float swingProgress, ItemStack stack, float equippedProgress, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, CallbackInfo ci
    ) {
        if (hand == InteractionHand.MAIN_HAND &&
                (AbilityUtil.isTypeEnabled(player, Abilities.SHOW_BOTH_ARMS.get()) || AbilityUtil.isTypeEnabled(player, Abilities.DUAL_WIELDING.get())) &&
                this.offHandItem.isEmpty() && !player.isInvisible() &&
                !this.mainHandItem.is(Items.FILLED_MAP)) {
            HumanoidArm humanoidArm = player.getMainArm().getOpposite();
            float attackAnim = player.getAttackAnim(partialTicks);
            InteractionHand interactionHand = MoreObjects.firstNonNull(player.swingingArm, InteractionHand.MAIN_HAND);
            float swing = interactionHand == InteractionHand.OFF_HAND ? attackAnim : 0.0F;

            matrixStack.pushPose();
            this.renderPlayerArm(matrixStack, buffer, combinedLight, 0, swing, humanoidArm);
            matrixStack.popPose();
        }

        if (stack.getItem() instanceof IAddonItem addonItem && !addonItem.shouldRenderModel()) {
            HumanoidArm arm = hand == InteractionHand.MAIN_HAND ? player.getMainArm() : player.getMainArm().getOpposite();
            this.renderPlayerArm(matrixStack, buffer, combinedLight, equippedProgress, swingProgress, arm);
        }
    }

    @Inject(method = "renderItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/ItemRenderer;renderStatic(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/world/level/Level;III)V"), cancellable = true)
    private void renderItem(LivingEntity entity, ItemStack itemStack, ItemDisplayContext displayContext, boolean leftHand, PoseStack poseStack, MultiBufferSource buffer, int seed, CallbackInfo ci) {
        if (itemStack.getItem() instanceof IAddonItem addonItem && !addonItem.shouldRenderModel()) {
            ci.cancel();
        }
    }
}
