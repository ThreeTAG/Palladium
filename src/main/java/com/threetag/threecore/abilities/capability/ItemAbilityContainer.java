package com.threetag.threecore.abilities.capability;

import com.threetag.threecore.abilities.AbilityMap;
import com.threetag.threecore.abilities.IAbilityProvider;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

import java.util.concurrent.atomic.AtomicBoolean;

public class ItemAbilityContainer implements IAbilityContainer {

    public final ItemStack stack;
    protected final AbilityMap map;

    public ItemAbilityContainer(ItemStack stack) {
        this.stack = stack;
        this.map = new AbilityMap();

        if (stack.getItem() instanceof IAbilityProvider) {
            this.setAbilities((IAbilityProvider) stack.getItem());
        }
    }

    @Override
    public void tick(EntityLivingBase entity) {
        AtomicBoolean dirty = new AtomicBoolean(false);
        getAbilityMap().forEach((s, a) -> {
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
}
