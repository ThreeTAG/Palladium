package net.threetag.threecore.ability;

import net.threetag.threecore.util.threedata.ThreeData;
import net.threetag.threecore.util.threedata.FloatThreeData;
import net.threetag.threecore.util.threedata.IntegerThreeData;
import net.threetag.threecore.util.threedata.EnumSync;
import net.threetag.threecore.util.icon.TexturedIcon;
import net.minecraft.entity.LivingEntity;

public class HealingAbility extends Ability {

    public static ThreeData<Integer> FREQUENCY = new IntegerThreeData("frequency").setSyncType(EnumSync.NONE).enableSetting("frequency", "Sets the frequency of healing (in ticks)");
    public static ThreeData<Float> AMOUNT = new FloatThreeData("amount").setSyncType(EnumSync.NONE).enableSetting("amount", "Sets the amount of hearts for each healing");

    public HealingAbility() {
        super(AbilityType.HEALING);
    }

    @Override
    public void registerData() {
        super.registerData();
        this.dataManager.register(ICON, new TexturedIcon(TexturedIcon.ICONS_TEXTURE, 0, 0, 16, 16));
        this.dataManager.register(FREQUENCY, 20);
        this.dataManager.register(AMOUNT, 0.5F);
    }

    @Override
    public void action(LivingEntity entity) {
        int frequency = this.dataManager.get(FREQUENCY);
        if (frequency != 0 && ticks % frequency == 0) {
            entity.heal(this.dataManager.get(AMOUNT));
        }
    }

}
