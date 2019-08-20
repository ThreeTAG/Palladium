package com.threetag.threecore.abilities.capability;

import com.threetag.threecore.ThreeCore;
import com.threetag.threecore.abilities.AbilityHelper;
import com.threetag.threecore.abilities.AbilityMap;
import com.threetag.threecore.abilities.IAbilityContainer;
import com.threetag.threecore.abilities.IAbilityProvider;
import com.threetag.threecore.abilities.data.EnumSync;
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
    public boolean init = false;

    public ItemAbilityContainer(ItemStack stack) {
        this.stack = stack;
        this.map = new AbilityMap();
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

        if (!entity.world.isRemote && !this.init) {
            if (stack.getOrCreateTag().contains("Abilities")) {
                AbilityHelper.loadFromNBT(stack.getOrCreateTag().getCompound("Abilities"), this.map);
                dirty.set(true);
            } else if (stack.getItem() instanceof IAbilityProvider) {
                this.addAbilities(null, (IAbilityProvider) stack.getItem());
            }
        }

        getAbilityMap().forEach((s, a) -> {
            a.container = this;
            a.tick(entity);
            if (a.sync != EnumSync.NONE || !init) {
                onUpdated(entity, a, a.sync);
                a.sync = EnumSync.NONE;
            }
            if (a.dirty) {
                dirty.set(true);
                a.dirty = false;
            }
        });

        if (entity.world.isRemote && dirty.get()) {
            this.stack.getOrCreateTag().put("Abilities", AbilityHelper.saveToNBT(this.map));
        }

        this.init = true;
    }

    @Override
    public AbilityMap getAbilityMap() {
        return this.map;
    }

    @Override
    public ResourceLocation getId() {
        return new ResourceLocation(ThreeCore.MODID, "item_" + this.slot.toString().toLowerCase());
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
