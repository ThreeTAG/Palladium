package net.threetag.threecore.block;

import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.threetag.threecore.ThreeCoreServerConfig;
import net.threetag.threecore.tileentity.CapacitorBlockTileEntity;
import net.threetag.threecore.util.BlockUtil;
import net.threetag.threecore.util.energy.IEnergyConfig;

import javax.annotation.Nullable;

public class CapacitorBlock extends ContainerBlock {

    public static final IntegerProperty LEVEL_0_10 = IntegerProperty.create("level", 0, 10);
    private final Type type;

    public CapacitorBlock(Properties properties, Type type) {
        super(properties);
        this.type = type;
        this.setDefaultState(this.stateContainer.getBaseState().with(LEVEL_0_10, Integer.valueOf(0)));
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new CapacitorBlockTileEntity(this.type);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public int getLightValue(BlockState state) {
        return state.get(LEVEL_0_10);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
        if (world.isRemote) {
            return ActionResultType.SUCCESS;
        } else {
            NetworkHooks.openGui((ServerPlayerEntity) player, getContainer(state, world, pos), pos);
            // TODO Stats ?
            return ActionResultType.SUCCESS;
        }
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity entity, ItemStack stack) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof CapacitorBlockTileEntity) {
            if (stack.hasDisplayName())
                ((LockableTileEntity) tileEntity).setCustomName(stack.getDisplayName());
            ((CapacitorBlockTileEntity) tileEntity).energyStorage.setEnergyStored(stack.getOrCreateTag().getInt("Energy"));
        }
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            if (tileEntity != null) {
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
    public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos) {
        return blockState.get(LEVEL_0_10);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(LEVEL_0_10);
    }

    public enum Type implements IStringSerializable {

        NORMAL("normal", ThreeCoreServerConfig.ENERGY.CAPACITOR), ADVANCED("advanced", ThreeCoreServerConfig.ENERGY.ADVANCED_CAPACITOR);

        private final String name;
        private final IEnergyConfig energyConfig;

        Type(String name, IEnergyConfig energyConfig) {
            this.name = name;
            this.energyConfig = energyConfig;
        }

        @Override
        public String getName() {
            return this.name;
        }

        public IEnergyConfig getEnergyConfig() {
            return energyConfig;
        }

        public static Type getByName(String name) {
            for (Type type : values()) {
                if (type.getName().equalsIgnoreCase(name)) {
                    return type;
                }
            }
            return values()[0];
        }
    }
}
