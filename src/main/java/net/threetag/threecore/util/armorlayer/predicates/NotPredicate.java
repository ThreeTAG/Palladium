package net.threetag.threecore.util.armorlayer.predicates;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.threetag.threecore.util.armorlayer.ArmorLayerManager;

import javax.annotation.Nullable;

public class NotPredicate implements ArmorLayerManager.IArmorLayerPredicate {

    public final ArmorLayerManager.IArmorLayerPredicate predicate;

    public NotPredicate(ArmorLayerManager.IArmorLayerPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public boolean test(ItemStack stack, @Nullable LivingEntity entity) {
        return !this.predicate.test(stack, entity);
    }
}
