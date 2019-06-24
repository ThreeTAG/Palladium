package com.threetag.threecore.base.block;

import com.threetag.threecore.base.tileentity.GrinderTileEntity;
import com.threetag.threecore.util.TCDamageSources;
import com.threetag.threecore.util.block.BlockUtil;
import com.threetag.threecore.util.sounds.ThreeCoreSounds;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import sun.security.provider.SHA;

import javax.annotation.Nullable;
import java.util.Random;

public class GrinderBlock extends ContainerBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    protected static final VoxelShape INSIDE = makeCuboidShape(2.0D, 8.0D, 2.0D, 14.0D, 16.0D, 14.0D);
    protected static final VoxelShape SHAPE = VoxelShapes.combineAndSimplify(VoxelShapes.fullCube(), INSIDE, IBooleanFunction.ONLY_FIRST);

    public GrinderBlock(Block.Properties properties) {
        super(properties);
        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH).with(LIT, false));
    }

    @Override
    public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
        if (world.isRemote) {
            return true;
        } else {
            NetworkHooks.openGui((ServerPlayerEntity) player, getContainer(state, world, pos), pos);
            // TODO Stats ?
            return true;
        }
    }

    @Override
    public int getLightValue(BlockState state) {
        return state.get(LIT) ? super.getLightValue(state) : 0;
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity entity, ItemStack stack) {
        if (stack.hasDisplayName()) {
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity instanceof GrinderTileEntity) {
                ((GrinderTileEntity) tileEntity).setCustomName(stack.getDisplayName());
            }
        }
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            if (tileEntity instanceof GrinderTileEntity) {
                tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent((i) -> BlockUtil.dropInventoryItems(worldIn, pos, i));
                worldIn.updateComparatorOutputLevel(pos, this);
            }

            super.onReplaced(state, worldIn, pos, newState, isMoving);
        }
    }

    @Override
    public boolean hasComparatorInputOverride(BlockState state) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(BlockState state, World world, BlockPos pos) {
        return BlockUtil.calcRedstone(world.getTileEntity(pos));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void animateTick(BlockState state, World world, BlockPos pos, Random random) {
        if (state.get(LIT)) {
            double x = (double) pos.getX() + 0.5D;
            double y = (double) pos.getY() + 0.5D;
            double z = (double) pos.getZ() + 0.5D;

            if (random.nextDouble() < 0.4D) {
                world.playSound(x, y, z, ThreeCoreSounds.GRINDER, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
            }

            double x2 = random.nextDouble() * 0.8D - 0.4D;
            double y2 = random.nextDouble() * 0.8D;
            double z2 = random.nextDouble() * 0.8D - 0.4D;
            world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, Blocks.COBBLESTONE.getDefaultState()), x + x2, y + y2, z + z2, 0.0D, 0.0D, 0.0D);
            world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, Blocks.GRAVEL.getDefaultState()), x + x2, y + y2, z + z2, 0.0D, 0.0D, 0.0D);

            if(random.nextInt(40) == 0) {
                world.addParticle(ParticleTypes.FLAME, x + x2, y + y2, z + z2, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        float y = (float)pos.getY() + 0.5F;
        if (!world.isRemote && entity.getBoundingBox().minY <= (double)y) {
            if(state.get(LIT)) {
                entity.attackEntityFrom(TCDamageSources.GRINDER, 2F);
            } else {
                double xSpeed = Math.abs(entity.posX - entity.lastTickPosX);
                double zSpeed = Math.abs(entity.posZ - entity.lastTickPosZ);
                if (xSpeed >= 0.003000000026077032D || zSpeed >= 0.003000000026077032D) {
                    entity.attackEntityFrom(TCDamageSources.GRINDER, 1.0F);
                }
            }
        }
    }

    @Override
    public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
        return SHAPE;
    }

    @Override
    public VoxelShape getRaytraceShape(BlockState p_199600_1_, IBlockReader p_199600_2_, BlockPos p_199600_3_) {
        return INSIDE;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.toRotation(state.get(FACING)));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING, LIT);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new GrinderTileEntity();
    }

}
