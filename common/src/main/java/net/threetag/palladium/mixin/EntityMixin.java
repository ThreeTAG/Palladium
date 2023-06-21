package net.threetag.palladium.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.threetag.palladium.power.ability.Abilities;
import net.threetag.palladium.power.ability.AbilityEntry;
import net.threetag.palladium.power.ability.AbilityUtil;
import net.threetag.palladium.power.ability.IntangibilityAbility;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("ConstantValue")
@Mixin(Entity.class)
public class EntityMixin {

    @Shadow
    public Level level;

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/core/BlockPos;<init>(DDD)V"), method = "moveTowardsClosestSpace", cancellable = true)
    protected void pushOutOfBlocks(double x, double y, double z, CallbackInfo ci) {
        if ((Object) this instanceof LivingEntity living) {
            for (AbilityEntry entry : AbilityUtil.getEnabledEntries(living, Abilities.INTANGIBILITY.get())) {
                if (IntangibilityAbility.canGoThrough(entry, this.level.getBlockState(new BlockPos(x, y, z)))) {
                    ci.cancel();
                    return;
                }
            }
        }
    }

}
