package net.threetag.palladium.mixin.fabric;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.threetag.palladium.entity.PalladiumAttributes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("DataFlowIssue")
@Mixin(Player.class)
public class PlayerMixin {

    @Inject(method = "getDestroySpeed", at = @At("RETURN"), cancellable = true)
    private void getDestroySpeed(BlockState state, CallbackInfoReturnable<Float> cir) {
        var player = (Player) (Object) this;

        if (player.getAttributes().hasAttribute(PalladiumAttributes.DESTROY_SPEED.get())) {
            cir.setReturnValue((float) (cir.getReturnValue() * player.getAttributeValue(PalladiumAttributes.DESTROY_SPEED.get())));
        }
    }

    @Inject(method = "createAttributes", at = @At("RETURN"))
    private static void createAttributes(CallbackInfoReturnable<AttributeSupplier.Builder> cir) {
        cir.getReturnValue().add(Attributes.ATTACK_KNOCKBACK);
    }

    @ModifyExpressionValue(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;getKnockbackBonus(Lnet/minecraft/world/entity/LivingEntity;)I", ordinal = 0))
    private int injected(int i) {
        return (int) (((Player) (Object) this).getAttributeValue(Attributes.ATTACK_KNOCKBACK) + i);
    }

}
