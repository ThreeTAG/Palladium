package net.threetag.palladium.mixin;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.threetag.palladium.entity.SuitStand;
import net.threetag.palladium.item.PalladiumItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArmorStand.class)
public abstract class ArmorStandMixin {

    @Shadow
    protected abstract void brokenByAnything(ServerLevel level, DamageSource damageSource);

    @Inject(method = "brokenByPlayer", at = @At("HEAD"), cancellable = true)
    private void brokenByPlayer(ServerLevel level, DamageSource damageSource, CallbackInfo ci) {
        if ((Object) this instanceof SuitStand suitStand) {
            ItemStack itemStack = PalladiumItems.SUIT_STAND.get().getDefaultInstance();
            itemStack.set(DataComponents.CUSTOM_NAME, suitStand.getCustomName());
            Block.popResource(suitStand.level(), suitStand.blockPosition(), itemStack);
            this.brokenByAnything(level, damageSource);
            ci.cancel();
        }
    }

    @Inject(method = "showBreakingParticles", at = @At("HEAD"), cancellable = true)
    private void showBreakingParticles(CallbackInfo ci) {
        if ((Object) this instanceof SuitStand suitStand) {
            if (suitStand.level() instanceof ServerLevel) {
                ((ServerLevel) suitStand.level())
                        .sendParticles(
                                new BlockParticleOption(ParticleTypes.BLOCK, Blocks.QUARTZ_BLOCK.defaultBlockState()),
                                suitStand.getX(),
                                suitStand.getY(0.6666666666666666),
                                suitStand.getZ(),
                                10,
                                suitStand.getBbWidth() / 4.0F,
                                suitStand.getBbHeight() / 4.0F,
                                suitStand.getBbWidth() / 4.0F,
                                0.05
                        );
            }

            ci.cancel();
        }
    }

}
