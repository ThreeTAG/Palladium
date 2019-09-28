package net.threetag.threecore.base.block;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.threetag.threecore.base.tileentity.FluidComposerTileEntity;
import net.threetag.threecore.util.fluid.TCFluidUtil;

import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicReference;

public class FluidComposerBlock extends MachineBlock {

    public FluidComposerBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new FluidComposerTileEntity();
    }

    @Override
    public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
        if (world.isRemote) {
            return true;
        } else {
            ItemStack stack = player.getHeldItem(hand);
            AtomicReference<FluidActionResult> result = new AtomicReference<>();
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity != null && FluidUtil.getFluidHandler(stack).isPresent()) {
                tileEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, rayTraceResult.getFace()).ifPresent(fluidHandler -> {
                    player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(itemHandler -> {
                        result.set(TCFluidUtil.interactWithFluidHandler(stack, fluidHandler, itemHandler, player));
                    });
                });
            }

            if (result.get() == null || !result.get().isSuccess())
                NetworkHooks.openGui((ServerPlayerEntity) player, getContainer(state, world, pos), pos);
            else
                player.setHeldItem(hand, result.get().getResult());
            // TODO Stats ?
            return true;
        }
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        float y = (float) pos.getY() + 0.5F;
        if (!world.isRemote && entity.getBoundingBox().minY <= (double) y) {
            TileEntity tileEntity = world.getTileEntity(pos);

            if (tileEntity instanceof FluidComposerTileEntity) {
                FluidStack fluidStack = ((FluidComposerTileEntity) tileEntity).outputFluidTank.getFluid();

                if (!fluidStack.isEmpty() && fluidStack.getFluid().getAttributes().getTemperature(fluidStack) >= 1000) {
                    entity.setFire(10);
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
