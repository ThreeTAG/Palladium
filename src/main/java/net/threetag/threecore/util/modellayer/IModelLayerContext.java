package net.threetag.threecore.util.modellayer;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IModelLayerContext {

    @Nullable
    ItemStack getAsItem();

    @Nonnull
    LivingEntity getAsEntity();

}
