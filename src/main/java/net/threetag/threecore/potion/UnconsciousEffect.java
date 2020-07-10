package net.threetag.threecore.potion;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

import java.util.Collections;
import java.util.List;

public class UnconsciousEffect extends Effect {

    public UnconsciousEffect(EffectType typeIn, int liquidColorIn) {
        super(typeIn, liquidColorIn);
        this.addAttributesModifier(Attributes.field_233821_d_, "81e019ea-39b9-4d68-b2c6-dea41c0f50ec", -5, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    @Override
    public List<ItemStack> getCurativeItems() {
        return Collections.emptyList();
    }

}
