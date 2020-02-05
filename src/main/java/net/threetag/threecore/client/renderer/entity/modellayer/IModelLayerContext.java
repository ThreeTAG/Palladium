package net.threetag.threecore.client.renderer.entity.modellayer;

import net.minecraft.entity.Entity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IModelLayerContext {

    @Nullable
    ItemStack getAsItem();

    @Nullable
    EquipmentSlotType getSlot();

    @Nonnull
    Entity getAsEntity();

}
