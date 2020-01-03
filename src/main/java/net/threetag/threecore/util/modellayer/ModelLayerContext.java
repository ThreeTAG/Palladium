package net.threetag.threecore.util.modellayer;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ModelLayerContext implements IModelLayerContext {

    private final Entity entity;
    private final ItemStack stack;
    private final EquipmentSlotType slotType;

    public ModelLayerContext(Entity entity) {
        this.entity = entity;
        this.stack = ItemStack.EMPTY;
        this.slotType = null;
    }

    public ModelLayerContext(Entity entity, ItemStack stack) {
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
    public Entity getAsEntity() {
        return this.entity;
    }
}
