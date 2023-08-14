package net.threetag.palladium.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.*;
import net.threetag.palladium.power.ability.Abilities;
import net.threetag.palladium.power.ability.AbilityUtil;
import net.threetag.palladium.power.ability.FluidWalkingAbility;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Map;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class BlockBehaviourMixin {

    @Unique
    private static final Map<Integer, VoxelShape> palladium$SHAPES = new HashMap<>();

    @Shadow
    protected abstract BlockState asState();

    @Inject(method = "getCollisionShape(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/phys/shapes/CollisionContext;)Lnet/minecraft/world/phys/shapes/VoxelShape;", at = @At("RETURN"), cancellable = true)
    public void getCollisionShape(BlockGetter level, BlockPos pos, CollisionContext context, CallbackInfoReturnable<VoxelShape> cir) {
        var state = this.asState();

        if (state.getBlock() instanceof LiquidBlockContainer && state.getFluidState().is(FluidTags.WATER)) {
            if (context instanceof EntityCollisionContext entityCollisionContext && entityCollisionContext.getEntity() instanceof LivingEntity living && AbilityUtil.isTypeEnabled(living, Abilities.WATER_WALK.get())) {
                cir.setReturnValue(Shapes.join(cir.getReturnValue(), palladium$SHAPES.computeIfAbsent(15, i -> Block.box(0.0, 0.0, 0.0, 16.0, i, 16.0)), BooleanOp.OR));
            }
        } else if (state.getBlock() instanceof LiquidBlock && !state.getFluidState().isEmpty()) {
            if (context instanceof EntityCollisionContext entityCollisionContext && entityCollisionContext.getEntity() instanceof LivingEntity living && FluidWalkingAbility.canWalkOn(living, state.getFluidState())) {
                cir.setReturnValue(palladium$SHAPES.computeIfAbsent((int) (state.getFluidState().getHeight(level, pos) * 15) + 2, i -> Block.box(0.0, 0.0, 0.0, 16.0, i, 16.0)));
            }
        }


    }

}
