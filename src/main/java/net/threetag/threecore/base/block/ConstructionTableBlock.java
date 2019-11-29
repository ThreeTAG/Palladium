package net.threetag.threecore.base.block;

import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.util.icon.IIcon;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class ConstructionTableBlock extends HorizontalBlock {

    private static Map<ResourceLocation, Tab> TABS = Maps.newLinkedHashMap();

    public static void registerTab(ResourceLocation id, Tab tab) {
        TABS.put(id, tab);
    }

    public static Map<ResourceLocation, Tab> getTabs() {
        return TABS;
    }

    public static INamedContainerProvider getContainerProvider(World world, BlockPos pos, Tab tab) {
        return new SimpleNamedContainerProvider((id, playerInventory, p) -> tab.provider.createMenu(id, playerInventory, p, world, pos), TITLE);
    }

    public static VoxelShape SHAPE;
    private static final ITextComponent TITLE =
            new TranslationTextComponent(Util.makeTranslationKey("container", new ResourceLocation(ThreeCore.MODID, "construction_table")));

    protected ConstructionTableBlock(Properties builder) {
        super(builder);
        this.setDefaultState(this.stateContainer.getBaseState().with(HORIZONTAL_FACING, Direction.NORTH));
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(HORIZONTAL_FACING, context.getPlacementHorizontalFacing().getOpposite());
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(HORIZONTAL_FACING);
    }

    @Override
    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        player.openContainer(state.getContainer(worldIn, pos));
        // TODO stats?
//        player.addStat(Stats.INTERACT_WITH_CRAFTING_TABLE);
        return true;
    }

    @Override
    public INamedContainerProvider getContainer(BlockState state, World worldIn, BlockPos pos) {
        return TABS.size() == 0 ? null : getContainerProvider(worldIn, pos, TABS.values().iterator().next());
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getRaytraceShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return VoxelShapes.fullCube();
    }

    static {
        SHAPE = Stream.of(
                Block.makeCuboidShape(0, 13, 0, 16, 15, 16),
                Block.makeCuboidShape(0, 2, 0, 16, 3, 16),
                Block.makeCuboidShape(14, 0, 1, 15, 13, 2),
                Block.makeCuboidShape(14, 0, 14, 15, 13, 15),
                Block.makeCuboidShape(1, 0, 14, 2, 13, 15),
                Block.makeCuboidShape(1, 0, 1, 2, 13, 2)
        ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();
    }

    public static class Tab {

        public final Supplier<ContainerType> containerType;
        public final IConstructionTableContainerProvider provider;
        public final IIcon icon;

        public Tab(Supplier<ContainerType> containerType, IConstructionTableContainerProvider provider, IIcon icon) {
            this.containerType = containerType;
            this.provider = provider;
            this.icon = icon;
        }
    }

    public interface IConstructionTableContainerProvider {

        @Nullable
        Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity player, World world, BlockPos pos);

    }

}
