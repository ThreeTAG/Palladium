package net.threetag.threecore.ability.condition;

import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.threetag.threecore.ability.Ability;
import net.threetag.threecore.util.threedata.FluidTagThreeData;
import net.threetag.threecore.util.threedata.ThreeData;

public class EyesInFluidCondition extends Condition {

    public static final ThreeData<Tag<Fluid>> FLUID_TAG = new FluidTagThreeData("fluid_tag").enableSetting("Determines the fluid the eyes are needed to be in");

    public EyesInFluidCondition(Ability ability) {
        super(ConditionType.EYES_IN_FLUID, ability);
    }

    @Override
    public void registerData() {
        super.registerData();
        this.register(FLUID_TAG, FluidTags.WATER);
    }

    @Override
    public ITextComponent createTitle() {
        return new TranslationTextComponent(Util.makeTranslationKey("ability.condition", this.type.getRegistryName()), this.get(FLUID_TAG));
    }

    @Override
    public boolean test(LivingEntity entity) {
        return entity.areEyesInFluid(this.get(FLUID_TAG));
    }
}
