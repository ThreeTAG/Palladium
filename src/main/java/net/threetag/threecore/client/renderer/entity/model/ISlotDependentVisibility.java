package net.threetag.threecore.client.renderer.entity.model;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public interface ISlotDependentVisibility {

    void setSlotVisibility(EquipmentSlotType slot);

}
