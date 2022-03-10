package net.threetag.palladium.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.DyeableArmorItem;
import net.minecraft.world.item.ItemStack;
import net.threetag.palladium.Palladium;

public class VibraniumWeaveArmorItem extends DyeableArmorItem implements ICustomArmorTexture {

    public VibraniumWeaveArmorItem(ArmorMaterial armorMaterial, EquipmentSlot equipmentSlot, Properties properties) {
        super(armorMaterial, equipmentSlot, properties);
    }

    @Override
    public ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        String path = "textures/models/armor/vibranium_weave_layer_" + (slot == EquipmentSlot.LEGS ? 2 : 1);
        if (type != null) {
            path += "_" + type;
        }
        return new ResourceLocation(Palladium.MOD_ID, path + ".png");
    }
}
