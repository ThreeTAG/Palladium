package net.threetag.palladium.mixin.client;

import com.google.common.base.MoreObjects;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.threetag.palladium.entity.PalladiumPlayerExtension;
import net.threetag.palladium.item.IAddonItem;
import net.threetag.palladium.power.ability.Abilities;
import net.threetag.palladium.power.ability.AbilityUtil;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(ItemInHandRenderer.class)
public abstract class ItemInHandRendererMixin {

    @Shadow
    protected abstract void renderPlayerArm(PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, float equippedProgress, float swingProgress, HumanoidArm side);

    @Shadow
    private ItemStack offHandItem;

    @Shadow
    private ItemStack mainHandItem;

    @Shadow
    @Final
    private Minecraft minecraft;

    @Shadow
    private float offHandHeight;

    @Shadow
    private float oOffHandHeight;

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
            float k = 1.0F - Mth.lerp(partialTicks, this.oOffHandHeight, this.offHandHeight);

            matrixStack.pushPose();
            this.renderPlayerArm(matrixStack, buffer, combinedLight, k, swing, humanoidArm);
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

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;clamp(FFF)F", ordinal = 3))
    private float modifyOffHandClamp(float value, float min, float max) {
        LocalPlayer localPlayer = this.minecraft.player;
        ItemStack itemStack2 = Objects.requireNonNull(localPlayer).getOffhandItem();
        float f = 1;

        if (ItemStack.matches(this.offHandItem, itemStack2)) {
            this.offHandItem = itemStack2;
        }

        if (AbilityUtil.isTypeEnabled(localPlayer, Abilities.DUAL_WIELDING.get()) && localPlayer instanceof PalladiumPlayerExtension ext) {
            f = ext.palladium$getDualWieldingHandler().getOffHandAttackStrengthScale(1F);
        }

        return Mth.clamp((this.offHandItem == itemStack2 ? f * f * f : 0F) - this.offHandHeight, min, max);
    }
}
