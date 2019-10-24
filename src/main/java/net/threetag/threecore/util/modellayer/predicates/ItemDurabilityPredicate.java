package net.threetag.threecore.util.modellayer.predicates;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.threetag.threecore.util.modellayer.ModelLayerManager;

import javax.annotation.Nullable;

public class ItemDurabilityPredicate implements ModelLayerManager.IModelLayerPredicate {

    public final float min;
    public final float max;

    public ItemDurabilityPredicate(float min, float max) {
        this.min = MathHelper.clamp(min, 0F, 1F);
        this.max = MathHelper.clamp(max, 0F, 1F);

        if (min > max)
            throw new IllegalStateException("Min damage value cant be bigger than max damage value!");
    }

    @Override
    public boolean test(ItemStack stack, @Nullable LivingEntity entity) {
        float dmg = 1F - (float) stack.getDamage() / (float) stack.getMaxDamage();
        return dmg >= min && dmg <= max;
    }

}
