package net.threetag.threecore.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.threetag.threecore.tileentity.GrinderTileEntity;
import net.threetag.threecore.util.TCDamageSources;
import net.threetag.threecore.sound.TCSounds;

import javax.annotation.Nullable;
import java.util.Random;

public class GrinderBlock extends MachineBlock {

    public GrinderBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new GrinderTileEntity();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void animateTick(BlockState state, World world, BlockPos pos, Random random) {
        if (state.get(MachineBlock.LIT)) {
            double x = (double) pos.getX() + 0.5D;
            double y = (double) pos.getY() + 0.5D;
            double z = (double) pos.getZ() + 0.5D;

            if (random.nextDouble() < 0.4D) {
                world.playSound(x, y, z, TCSounds.GRINDER.get(), SoundCategory.BLOCKS, 1.0F, 1.0F, false);
            }

            double x2 = random.nextDouble() * 0.8D - 0.4D;
            double y2 = random.nextDouble() * 0.8D;
            double z2 = random.nextDouble() * 0.8D - 0.4D;
            world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, Blocks.COBBLESTONE.getDefaultState()), x + x2, y + y2, z + z2, 0.0D, 0.0D, 0.0D);
            world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, Blocks.GRAVEL.getDefaultState()), x + x2, y + y2, z + z2, 0.0D, 0.0D, 0.0D);

            if (random.nextInt(40) == 0) {
                world.addParticle(ParticleTypes.FLAME, x + x2, y + y2, z + z2, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        float y = (float) pos.getY() + 0.5F;
        if (!world.isRemote && entity.getBoundingBox().minY <= (double) y) {
            if (state.get(MachineBlock.LIT)) {
                entity.attackEntityFrom(TCDamageSources.GRINDER, 2F);
            } else {
                double xSpeed = Math.abs(entity.getPosX() - entity.lastTickPosX);
                double zSpeed = Math.abs(entity.getPosZ() - entity.lastTickPosZ);
                if (xSpeed >= 0.003000000026077032D || zSpeed >= 0.003000000026077032D) {
                    entity.attackEntityFrom(TCDamageSources.GRINDER, 1.0F);
                }
            }
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext selectionContext) {
        return MachineBlock.SHAPE;
    }

    @Override
    public VoxelShape getRaytraceShape(BlockState state, IBlockReader blockReader, BlockPos pos) {
        return MachineBlock.INSIDE;
    }

}
