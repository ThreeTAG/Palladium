package net.threetag.palladium.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.threetag.palladium.entity.SuitStand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("DataFlowIssue")
@Mixin(ArmorStand.class)
public class ArmorStandMixin {

    @Inject(method = "readPose", at = @At("HEAD"), cancellable = true)
    private void readPose(CompoundTag nbt, CallbackInfo ci) {
        var armorStand = (ArmorStand) (Object) this;

        if (armorStand instanceof SuitStand suitStand) {
            suitStand.readSuitStandPose(nbt);
            ci.cancel();
        }
    }

    @Inject(method = "brokenByPlayer", at = @At("HEAD"), cancellable = true)
    private void brokenByPlayer(DamageSource damageSource, CallbackInfo ci) {
        var armorStand = (ArmorStand) (Object) this;

        if (armorStand instanceof SuitStand suitStand) {
            suitStand.suitStandBrokenByPlayer(damageSource);
            ci.cancel();
        }
    }

    @Inject(method = "showBreakingParticles", at = @At("HEAD"), cancellable = true)
    private void showBreakingParticles(CallbackInfo ci) {
        var armorStand = (ArmorStand) (Object) this;

        if (armorStand instanceof SuitStand suitStand) {
            suitStand.suitStandShowBreakingParticles();
            ci.cancel();
        }
    }

}
