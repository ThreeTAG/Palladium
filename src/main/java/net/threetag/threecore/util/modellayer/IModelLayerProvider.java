package net.threetag.threecore.util.modellayer;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public interface IModelLayerProvider {

    @OnlyIn(Dist.CLIENT)
    List<ModelLayer> getArmorLayers(ItemStack stack, LivingEntity entity);

}
