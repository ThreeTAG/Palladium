package net.threetag.palladium.item;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.DyeableArmorItem;

public class VibraniumWeaveArmorItem extends DyeableArmorItem implements ArmorWithRenderer {

    private Object renderer;

    public VibraniumWeaveArmorItem(ArmorMaterial armorMaterial, ArmorItem.Type type, Properties properties) {
        super(armorMaterial, type, properties);
    }

    @Override
    public void setCachedArmorRenderer(Object object) {
        this.renderer = object;
    }

    @Override
    public Object getCachedArmorRenderer() {
        return this.renderer;
    }
}
