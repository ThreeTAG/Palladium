package net.threetag.palladium.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.threetag.palladium.power.ability.Abilities;
import net.threetag.palladium.power.ability.AbilityEntry;
import net.threetag.palladium.power.ability.AbilityUtil;
import net.threetag.palladium.power.ability.IntangibilityAbility;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.BlockStateBase.class)
public class BlockStateBaseMixin {

    @Inject(method = "getCollisionShape(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/phys/shapes/CollisionContext;)Lnet/minecraft/world/phys/shapes/VoxelShape;",
            at = @At("RETURN"), cancellable = true)
    private void getCollisionShape(BlockGetter level, BlockPos pos, CollisionContext context, CallbackInfoReturnable<VoxelShape> cir) {
        VoxelShape blockShape = cir.getReturnValue();
        if (!blockShape.isEmpty() && context instanceof EntityCollisionContext ctx) {
            if (ctx.getEntity() instanceof LivingEntity entity) {
                boolean isAbove = isAbove(entity, blockShape, pos, false);
                for (AbilityEntry entry : AbilityUtil.getEnabledEntries(entity, Abilities.INTANGIBILITY.get())) {
                    if (!isAbove || entry.getProperty(IntangibilityAbility.VERTICAL)) {
                        if (IntangibilityAbility.canGoThrough(entry, level.getBlockState(pos))) {
                            cir.setReturnValue(Shapes.empty());
                            return;
                        }
                    }
                }
            }
        }
    }

    @Unique
    private boolean isAbove(Entity entity, VoxelShape shape, BlockPos pos, boolean defaultValue) {
        return entity.getY() > (double) pos.getY() + shape.max(Direction.Axis.Y) - (entity.onGround() ? 8.05 / 16.0 : 0.0015);
    }

    @Inject(method = "entityInside", at = @At("HEAD"), cancellable = true)
    private void preventCollisionWhenPhasing(Level level, BlockPos pos, Entity entity, CallbackInfo ci) {
        if (entity instanceof LivingEntity living) {
            for (AbilityEntry entry : AbilityUtil.getEnabledEntries(living, Abilities.INTANGIBILITY.get())) {
                if (IntangibilityAbility.canGoThrough(entry, level.getBlockState(pos))) {
                    ci.cancel();
                    return;
                }
            }
        }
    }

}
