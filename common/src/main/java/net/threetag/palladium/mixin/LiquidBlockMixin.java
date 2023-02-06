package net.threetag.palladium.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.threetag.palladium.power.ability.Abilities;
import net.threetag.palladium.power.ability.AbilityUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LiquidBlock.class)
public class LiquidBlockMixin {

    @Unique
    private final VoxelShape fullShape = Block.box(0.0, 0.0, 0.0, 16.0, 15.0, 16.0);

    @Inject(method = "getCollisionShape", at = @At("HEAD"), cancellable = true)
    private void getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context, CallbackInfoReturnable<VoxelShape> cir) {
        if (state.getValue(LiquidBlock.LEVEL) == 0 && context instanceof EntityCollisionContext entityCollisionContext && entityCollisionContext.getEntity() instanceof LivingEntity living && !AbilityUtil.getEnabledEntries(living, Abilities.WATER_WALK.get()).isEmpty()) {
            cir.setReturnValue(fullShape);
        }
    }

}
