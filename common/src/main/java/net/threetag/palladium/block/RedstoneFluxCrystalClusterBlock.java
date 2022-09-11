package net.threetag.palladium.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;

public class RedstoneFluxCrystalClusterBlock extends AmethystClusterBlock {

    public RedstoneFluxCrystalClusterBlock(int i, int j, Properties properties) {
        super(i, j, properties);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        super.animateTick(state, level, pos, random);
        AABB shape = this.getShape(state, level, pos, CollisionContext.empty()).bounds().inflate(0.2F);

        for (int i = 0; i < 7; i++) {
            double x = pos.getX() + shape.minX + (random.nextDouble() * (shape.maxX - shape.minX));
            double y = pos.getY() + shape.minY + (random.nextDouble() * (shape.maxY - shape.minY));
            double z = pos.getZ() + shape.minZ + (random.nextDouble() * (shape.maxZ - shape.minZ));
            level.addParticle(DustParticleOptions.REDSTONE, x, y, z, 0.0D, 0.0D, 0.0D);
        }
    }

}
