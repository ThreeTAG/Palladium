package net.threetag.palladium.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.*;
import net.threetag.palladium.power.ability.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Map;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class BlockStateBaseMixin {

    @Unique
    private static final Map<Integer, VoxelShape> palladium$SHAPES = new HashMap<>();

    @Shadow
    protected abstract BlockState asState();

    @Inject(method = "getCollisionShape(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/phys/shapes/CollisionContext;)Lnet/minecraft/world/phys/shapes/VoxelShape;",
            at = @At("RETURN"), cancellable = true)
    private void getCollisionShape(BlockGetter level, BlockPos pos, CollisionContext context, CallbackInfoReturnable<VoxelShape> cir) {
        VoxelShape blockShape = cir.getReturnValue();
        var state = this.asState();

        if (context instanceof EntityCollisionContext entityCollisionContext && entityCollisionContext.getEntity() instanceof LivingEntity living) {
            if (!blockShape.isEmpty()) {
                boolean isAbove = palladium$isAbove(living, blockShape, pos);
                for (AbilityInstance<IntangibilityAbility> instance : AbilityUtil.getEnabledInstances(living, AbilitySerializers.INTANGIBILITY.get())) {
                    if (!isAbove || instance.getAbility().vertical) {
                        if (IntangibilityAbility.canGoThrough(instance, level.getBlockState(pos))) {
                            cir.setReturnValue(Shapes.empty());
                            return;
                        }
                    }
                }
            }

            if (state.getBlock() instanceof LiquidBlockContainer && state.getFluidState().is(FluidTags.WATER)) {
                if (FluidWalkingAbility.canWalkOn(living, state.getFluidState())) {
                    cir.setReturnValue(Shapes.join(cir.getReturnValue(), palladium$SHAPES.computeIfAbsent(15, i -> Block.box(0.0, 0.0, 0.0, 16.0, i, 16.0)), BooleanOp.OR));
                }
            } else if (state.getBlock() instanceof LiquidBlock && !state.getFluidState().isEmpty()) {
                if (FluidWalkingAbility.canWalkOn(living, state.getFluidState())) {
                    cir.setReturnValue(palladium$SHAPES.computeIfAbsent((int) (state.getFluidState().getHeight(level, pos) * 15) + 2, i -> Block.box(0.0, 0.0, 0.0, 16.0, i, 16.0)));
                }
            }
        }
    }

    @Unique
    private boolean palladium$isAbove(Entity entity, VoxelShape shape, BlockPos pos) {
        return entity.getY() > (double) pos.getY() + shape.max(Direction.Axis.Y) - (entity.onGround() ? 8.05 / 16.0 : 0.0015);
    }

    @Inject(method = "entityInside", at = @At("HEAD"), cancellable = true)
    private void preventCollisionWhenPhasing(Level level, BlockPos pos, Entity entity, CallbackInfo ci) {
        if (entity instanceof LivingEntity living) {
            for (AbilityInstance<IntangibilityAbility> instance : AbilityUtil.getEnabledInstances(living, AbilitySerializers.INTANGIBILITY.get())) {
                if (IntangibilityAbility.canGoThrough(instance, level.getBlockState(pos))) {
                    ci.cancel();
                    return;
                }
            }
        }
    }

}
