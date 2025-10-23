package net.threetag.palladium.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.threetag.palladium.power.ability.AbilitySerializers;
import net.threetag.palladium.power.ability.AbilityUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public abstract class ItemInHandRendererMixin {

    @Shadow
    private ItemStack offHandItem;

    @Shadow
    private ItemStack mainHandItem;

    @Shadow
    protected abstract void renderPlayerArm(PoseStack poseStack, SubmitNodeCollector nodeCollector, int packedLight, float equippedProgress, float swingProgress, HumanoidArm arm);

    @Inject(method = "renderArmWithItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isEmpty()Z", ordinal = 0))
    private void renderArmWithItem(
            AbstractClientPlayer player, float partialTick, float pitch, InteractionHand hand, float swingProgress, ItemStack item, float equippedProgress, PoseStack poseStack, SubmitNodeCollector nodeCollector, int packedLight, CallbackInfo ci
    ) {
        if (hand == InteractionHand.MAIN_HAND && AbilityUtil.isTypeEnabled(player, AbilitySerializers.SHOW_BOTH_ARMS.get()) && this.offHandItem.isEmpty() && !player.isInvisible() && !this.mainHandItem.is(Items.FILLED_MAP)) {
            HumanoidArm humanoidArm = player.getMainArm().getOpposite();
            poseStack.pushPose();
            this.renderPlayerArm(poseStack, nodeCollector, packedLight, 0, 0, humanoidArm);
            poseStack.popPose();
        }
    }

}
