package net.threetag.threecore.util.armorlayer;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public interface IArmorLayerProvider {

    @OnlyIn(Dist.CLIENT)
    List<ArmorLayer> getArmorLayers(ItemStack stack, LivingEntity entity);

}
