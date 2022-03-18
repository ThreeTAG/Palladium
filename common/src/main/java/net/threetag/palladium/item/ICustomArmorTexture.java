package net.threetag.palladium.item;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

public interface ICustomArmorTexture {

    default ModelLayerLocation getArmorModelLayer(ItemStack stack, Entity entity, EquipmentSlot slot) {
        return null;
    }

    default ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return null;
    }

}
