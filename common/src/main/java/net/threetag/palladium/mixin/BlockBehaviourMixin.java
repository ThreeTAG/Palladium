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
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class BlockBehaviourMixin {

    @Unique
    private static final VoxelShape FULL_SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 15.0, 16.0);

    @Shadow protected abstract BlockState asState();

    @Inject(method = "getCollisionShape(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/phys/shapes/CollisionContext;)Lnet/minecraft/world/phys/shapes/VoxelShape;", at = @At("RETURN"), cancellable = true)
    public void getCollisionShape(BlockGetter level, BlockPos pos, CollisionContext context, CallbackInfoReturnable<VoxelShape> cir) {
        var state = this.asState();

        if(state.getBlock() instanceof LiquidBlock liquid && liquid.getFluidState(state).is(FluidTags.WATER)) {
            if (state.getValue(LiquidBlock.LEVEL) == 0 && context instanceof EntityCollisionContext entityCollisionContext && entityCollisionContext.getEntity() instanceof LivingEntity living && AbilityUtil.isTypeEnabled(living, Abilities.WATER_WALK.get())) {
                cir.setReturnValue(FULL_SHAPE);
            }
        } else if(state.getBlock() instanceof LiquidBlockContainer && state.getFluidState().is(FluidTags.WATER)) {
            if (context instanceof EntityCollisionContext entityCollisionContext && entityCollisionContext.getEntity() instanceof LivingEntity living && AbilityUtil.isTypeEnabled(living, Abilities.WATER_WALK.get())) {
                cir.setReturnValue(Shapes.join(cir.getReturnValue(), FULL_SHAPE, BooleanOp.OR));
            }
        }


    }

}
