package net.threetag.threecore.util.block;

import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class BlockUtil {

    public static void dropInventoryItems(World world, BlockPos pos, IItemHandler itemHandler) {
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            ItemStack stack = itemHandler.getStackInSlot(i);

            if (!stack.isEmpty())
                InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), stack);
        }
    }

    public static int calcRedstone(TileEntity tileEntity) {
        if (tileEntity == null) {
            return 0;
        } else {
            AtomicInteger i = new AtomicInteger();
            AtomicReference<Float> f = new AtomicReference<>(0.0F);

            tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent((itemHandler) -> {
                for (int j = 0; j < itemHandler.getSlots(); ++j) {
                    ItemStack itemstack = itemHandler.getStackInSlot(j);
                    if (!itemstack.isEmpty()) {
                        int finalJ = j;
                        f.updateAndGet(v -> new Float((float) (v + (float) itemstack.getCount() / (float) Math.min(itemHandler.getSlotLimit(finalJ), itemstack.getMaxStackSize()))));
                        i.incrementAndGet();
                    }
                }

                f.set(f.get() / (float) itemHandler.getSlots());
            });


            return MathHelper.floor(f.get() * 14.0F) + (i.get() > 0 ? 1 : 0);
        }
    }

}
