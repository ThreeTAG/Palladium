package net.threetag.palladium.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.threetag.palladium.entity.PalladiumAttributes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Shadow
    public abstract AttributeMap getAttributes();

    @Inject(method = "getJumpPower", at = @At("RETURN"), cancellable = true)
    protected void getJumpPower(CallbackInfoReturnable<Float> cir) {
        if (this.getAttributes().hasAttribute(PalladiumAttributes.JUMP_POWER.get())) {
            var instance = this.getAttributes().getInstance(PalladiumAttributes.JUMP_POWER.get());
            Objects.requireNonNull(instance).setBaseValue(cir.getReturnValue());
            cir.setReturnValue((float) instance.getValue());
        }
    }

}
