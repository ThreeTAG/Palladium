package net.threetag.threecore.client.renderer.entity.modellayer.predicates;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.threetag.threecore.client.renderer.entity.modellayer.IModelLayerContext;

public class ItemDurabilityPredicate implements IModelLayerPredicate {

    public final float min;
    public final float max;

    public ItemDurabilityPredicate(float min, float max) {
        this.min = MathHelper.clamp(min, 0F, 1F);
        this.max = MathHelper.clamp(max, 0F, 1F);

        if (min > max)
            throw new IllegalStateException("Min damage value cant be bigger than max damage value!");
    }

    @Override
    public boolean test(IModelLayerContext context) {
        ItemStack stack = context.getAsItem();
        if (stack == null || stack.isEmpty())
            return false;
        float dmg = 1F - (float) stack.getDamage() / (float) stack.getMaxDamage();
        return dmg >= min && dmg <= max;
    }

}
