package net.threetag.palladium.compat.accessories;

import io.wispforest.accessories.api.AccessoriesCapability;
import io.wispforest.accessories.data.SlotTypeLoader;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Collections;
import java.util.List;

public class AccessoriesCompatImpl extends AccessoriesCompat {

    public static void init() {
        AccessoriesCompat.INSTANCE = new AccessoriesCompatImpl();
    }

    @Override
    public List<ResourceLocation> getSlots(Level level) {
        return SlotTypeLoader.INSTANCE.getEntries(level).keySet().stream().toList();
    }

    @Override
    public List<ItemStack> getFromSlot(LivingEntity entity, String slot) {
        var cap = AccessoriesCapability.get(entity);

        if (cap != null) {
            var container = cap.getContainer(() -> slot);

            if (container != null) {
                return container.getAccessories().getItems();
            }
        }

        return Collections.emptyList();
    }

    @Override
    public ItemStack getFromSlot(LivingEntity entity, String slot, int index) {
        var cap = AccessoriesCapability.get(entity);

        if (cap != null) {
            var container = cap.getContainer(() -> slot);

            if (container != null) {
                return container.getAccessories().getItem(index);
            }
        }

        return ItemStack.EMPTY;
    }

    @Override
    public void setInSlot(LivingEntity entity, String slot, int index, ItemStack stack) {
        var cap = AccessoriesCapability.get(entity);

        if (cap != null) {
            var container = cap.getContainer(() -> slot);

            if (container != null) {
                container.getAccessories().setItem(index, stack);
            }
        }
    }

    @Override
    public int getSlotSize(LivingEntity entity, String slot) {
        var cap = AccessoriesCapability.get(entity);

        if (cap != null) {
            var container = cap.getContainer(() -> slot);

            if (container != null) {
                return container.getAccessories().getContainerSize();
            }
        }

        return 0;
    }

    @Override
    public void clearSlot(LivingEntity entity, String slot) {
        var cap = AccessoriesCapability.get(entity);

        if (cap != null) {
            var container = cap.getContainer(() -> slot);

            if (container != null) {
                container.getAccessories().clearContent();
            }
        }
    }
}
