package com.threetag.threecore.abilities;

import com.threetag.threecore.abilities.data.AbilityData;
import com.threetag.threecore.abilities.data.AbilityDataFloat;
import com.threetag.threecore.abilities.data.AbilityDataInteger;
import com.threetag.threecore.abilities.data.EnumSync;
import net.minecraft.entity.EntityLivingBase;

public class AbilityHealing extends AbilityConstant {

    public static AbilityData<Integer> FREQUENCY = new AbilityDataInteger("frequency").disableSaving().setSyncType(EnumSync.NONE).enableSetting("frequency", "Sets the frequency of healing (in ticks)");
    public static AbilityData<Float> AMOUNT = new AbilityDataFloat("amount").disableSaving().setSyncType(EnumSync.NONE).enableSetting("amount", "Sets the amount of hearts for each healing");

    public AbilityHealing() {
        super(AbilityType.HEALING);
    }

    @Override
    public void registerData() {
        super.registerData();
        this.dataManager.register(FREQUENCY, 20);
        this.dataManager.register(AMOUNT, 0.5F);
    }

    @Override
    public void updateTick(EntityLivingBase entity) {
        int frequency = this.dataManager.get(FREQUENCY);
        if (frequency != 0 && ticks % frequency == 0) {
            entity.heal(this.dataManager.get(AMOUNT));
        }
    }

}
