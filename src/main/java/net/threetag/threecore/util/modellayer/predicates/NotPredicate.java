package net.threetag.threecore.util.modellayer.predicates;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.threetag.threecore.util.modellayer.ModelLayerManager;

import javax.annotation.Nullable;

public class NotPredicate implements ModelLayerManager.IArmorLayerPredicate {

    public final ModelLayerManager.IArmorLayerPredicate predicate;

    public NotPredicate(ModelLayerManager.IArmorLayerPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public boolean test(ItemStack stack, @Nullable LivingEntity entity) {
        return !this.predicate.test(stack, entity);
    }
}
