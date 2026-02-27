package net.threetag.palladium.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.threetag.palladium.attachment.PalladiumAttachments;
import net.threetag.palladium.power.ability.AbilitySerializers;
import net.threetag.palladium.power.ability.AbilityUtil;
import net.threetag.palladium.power.ability.RestrictSlotsAbility;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Shadow
    protected abstract boolean canGlide();

    @Shadow
    protected int fallFlyTicks;

    @Inject(method = "canUseSlot", at = @At("HEAD"), cancellable = true)
    private void canUseSlot(EquipmentSlot slot, CallbackInfoReturnable<Boolean> cir) {
        if (RestrictSlotsAbility.isRestricted((LivingEntity) (Object) this, slot)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "canGlide", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;hasEffect(Lnet/minecraft/core/Holder;)Z", shift = At.Shift.AFTER), cancellable = true)
    private void canGlideHook(CallbackInfoReturnable<Boolean> cir) {
        if (AbilityUtil.isTypeEnabled((LivingEntity) (Object) this, AbilitySerializers.GLIDING.get())) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "updateFallFlying", at = @At("HEAD"), cancellable = true)
    private void updateFallFlying(CallbackInfo ci) {
        var entity = (LivingEntity) (Object) this;

        if (AbilityUtil.isTypeEnabled(entity, AbilitySerializers.GLIDING.get())) {
            ci.cancel();

            entity.checkFallDistanceAccumulation();
            if (!entity.level().isClientSide()) {
                if (!this.canGlide()) {
                    this.setSharedFlag(7, false);
                    return;
                }

                int i = this.fallFlyTicks + 1;
                if (i % 10 == 0) {
                    this.gameEvent(GameEvent.ELYTRA_GLIDE);
                }
            }
        }
    }

    @Inject(method = "onClimbable", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;blockPosition()Lnet/minecraft/core/BlockPos;"), cancellable = true)
    private void onClimbable(CallbackInfoReturnable<Boolean> cir) {
        if (this.getData(PalladiumAttachments.IS_CLIMBING.get())) {
            cir.setReturnValue(true);
        }
    }

}
