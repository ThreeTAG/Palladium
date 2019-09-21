package net.threetag.threecore.util.armorlayer;

import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;

public abstract class ArmorLayer {

    public abstract void render(ItemStack stack, LivingEntity entity, IEntityRenderer entityRenderer, EquipmentSlotType slot, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale);

    public abstract boolean isActive(ItemStack stack, LivingEntity entity);

    public abstract ArmorLayer addPredicate(ArmorLayerManager.IArmorLayerPredicate predicate);

}
