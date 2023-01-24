package net.threetag.palladium.mixin;

import net.minecraft.world.damagesource.DamageSource;
import net.threetag.palladium.power.ability.DamageImmunityAbility;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DamageSource.class)
public class DamageSourceMixin {

    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(String name, CallbackInfo ci) {
        DamageImmunityAbility.DAMAGE_SOURCE_IDS.add(name);
    }

}
