package com.threetag.threecore.abilities.capability;

import com.threetag.threecore.ThreeCore;
import com.threetag.threecore.abilities.AbilityMap;
import com.threetag.threecore.abilities.IAbilityContainer;
import com.threetag.threecore.abilities.IAbilityProvider;
import com.threetag.threecore.util.render.IIcon;
import com.threetag.threecore.util.render.ItemIcon;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.util.concurrent.atomic.AtomicBoolean;

public class ItemAbilityContainer implements IAbilityContainer {

    public final ItemStack stack;
    public EquipmentSlotType slot;
    protected final AbilityMap map;

    public ItemAbilityContainer(ItemStack stack) {
        this.stack = stack;
        this.slot = slot;
        this.map = new AbilityMap();

        if (stack.getItem() instanceof IAbilityProvider) {
            this.addAbilities(null, (IAbilityProvider) stack.getItem());
        }
    }

    @Override
    public void tick(LivingEntity entity) {
        for (EquipmentSlotType slots : EquipmentSlotType.values()) {
            if (entity.getItemStackFromSlot(slots) == this.stack) {
                this.slot = slots;
                break;
            }
        }

        AtomicBoolean dirty = new AtomicBoolean(false);
        getAbilityMap().forEach((s, a) -> {
            a.container = this;
            a.tick(entity);
            if (a.dirty) {
                dirty.set(true);
                a.dirty = false;
            }
        });

        if (dirty.get()) {
            // TODO Save into item nbt tag
        }
    }

    @Override
    public AbilityMap getAbilityMap() {
        return this.map;
    }

    @Override
    public ResourceLocation getId() {
        return new ResourceLocation(ThreeCore.MODID, "item_" + this.slot);
    }

    @Override
    public ITextComponent getTitle() {
        return this.stack.getDisplayName();
    }

    @Override
    public IIcon getIcon() {
        return new ItemIcon(this.stack);
    }
}
