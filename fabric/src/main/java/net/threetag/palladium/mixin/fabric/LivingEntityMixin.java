package net.threetag.palladium.mixin.fabric;

import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.Fluid;
import net.threetag.palladium.entity.fabric.ForgeAttributes;
import net.threetag.palladium.power.ability.Abilities;
import net.threetag.palladium.power.ability.AbilityUtil;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("ConstantConditions")
@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(at = @At("HEAD"), method = "getVisibilityPercent", cancellable = true)
    private void getVisibilityPercent(@Nullable Entity pLookingEntity, CallbackInfoReturnable<Double> ci) {
        if (!AbilityUtil.getEnabledEntries((LivingEntity) (Object) this, Abilities.INVISIBILITY.get()).isEmpty()) {
            ci.setReturnValue(0D);
        }
    }

    @Inject(method = "jumpInLiquid", at = @At("RETURN"))
    protected void jumpInLiquid(TagKey<Fluid> fluidTag, CallbackInfo ci) {
        var entity = (LivingEntity) (Object) this;

        if (entity.getAttributes().hasAttribute(ForgeAttributes.SWIM_SPEED)) {
            entity.setDeltaMovement(entity.getDeltaMovement().subtract(0.0, 0.04F, 0.0).add(0, 0.04F * entity.getAttributeValue(ForgeAttributes.SWIM_SPEED), 0));
        }
    }

    @Inject(method = "goDownInWater", at = @At("RETURN"))
    protected void goDownInWater(CallbackInfo ci) {
        var entity = (LivingEntity) (Object) this;

        if (entity.getAttributes().hasAttribute(ForgeAttributes.SWIM_SPEED)) {
            entity.setDeltaMovement(entity.getDeltaMovement().add(0.0, 0.04F, 0.0).add(0, -0.04F * entity.getAttributeValue(ForgeAttributes.SWIM_SPEED), 0));
        }
    }

    @ModifyArg(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;moveRelative(FLnet/minecraft/world/phys/Vec3;)V"), index = 0)
    private float modifyMoveRelativeSpeed(float f) {
        var entity = (LivingEntity) (Object) this;

        if (entity.getAttributes().hasAttribute(ForgeAttributes.SWIM_SPEED)) {
            return (float) (f * entity.getAttributeValue(ForgeAttributes.SWIM_SPEED));
        }

        return f;
    }

}
