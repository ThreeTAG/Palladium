package net.threetag.threecore.util.modellayer;

import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ModelLayerContext implements IModelLayerContext {

    private final LivingEntity entity;
    private final ItemStack stack;
    private final EquipmentSlotType slotType;

    public ModelLayerContext(LivingEntity entity) {
        this.entity = entity;
        this.stack = ItemStack.EMPTY;
        this.slotType = null;
    }

    public ModelLayerContext(LivingEntity entity, ItemStack stack) {
        this.entity = entity;
        this.stack = stack == null ? ItemStack.EMPTY : stack;
        this.slotType = null;
    }

    public ModelLayerContext(LivingEntity entity, ItemStack stack, EquipmentSlotType slotType) {
        this.entity = entity;
        this.stack = stack == null ? ItemStack.EMPTY : stack;
        this.slotType = slotType;
    }

    @Nullable
    @Override
    public ItemStack getAsItem() {
        return this.stack;
    }

    @Nullable
    @Override
    public EquipmentSlotType getSlot() {
        return this.slotType;
    }

    @Nonnull
    @Override
    public LivingEntity getAsEntity() {
        return this.entity;
    }
}
