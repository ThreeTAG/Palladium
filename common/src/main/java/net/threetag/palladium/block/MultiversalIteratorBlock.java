package net.threetag.palladium.block;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.threetag.palladium.menu.ExtendedMenuProvider;
import net.threetag.palladium.menu.MultiversalIteratorMenu;
import net.threetag.palladium.menu.MultiversalIteratorSuitStandMenu;
import net.threetag.palladium.menu.PalladiumMenuTypes;
import org.jetbrains.annotations.NotNull;

public class MultiversalIteratorBlock extends HorizontalDirectionalBlock {

    public static final Component CONTAINER_TITLE = Component.translatable("container.palladium.multiversal_iterator");

    public MultiversalIteratorBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            var entities = level.getEntities((Entity) null, new AABB(pos.getX(), pos.getY() + 1, pos.getZ(), pos.getX() + 1, pos.getY() + 2, pos.getZ() + 1), entity -> entity instanceof ArmorStand);
            var suitStand = entities.isEmpty() ? null : (ArmorStand) entities.get(0);


            if(suitStand == null) {
                player.openMenu(state.getMenuProvider(level, pos));
            } else {
                PalladiumMenuTypes.openExtendedMenu((ServerPlayer) player, new ExtendedMenuProvider() {
                    @Override
                    public void addAdditionalData(FriendlyByteBuf buf) {
                        buf.writeInt(suitStand.getId());
                    }

                    @Override
                    public Component getDisplayName() {
                        return CONTAINER_TITLE;
                    }

                    @Override
                    public @NotNull AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
                        return new MultiversalIteratorSuitStandMenu(i, inventory, suitStand, ContainerLevelAccess.create(level, pos));
                    }
                });
            }
            // TODO Stats?
//            player.awardStat(Stats.INTERACT_WITH_CRAFTING_TABLE);
            return InteractionResult.CONSUME;
        }
    }

    @Override
    public MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        return new SimpleMenuProvider((i, inventory, player) -> new MultiversalIteratorMenu(i, inventory, ContainerLevelAccess.create(level, pos)), CONTAINER_TITLE);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}
