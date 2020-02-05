package net.threetag.threecore.ability;

import net.minecraft.entity.LivingEntity;
import net.threetag.threecore.util.icon.TexturedIcon;

public class WaterBreathingAbility extends Ability {

    public WaterBreathingAbility() {
        super(AbilityType.WATER_BREATHING);
    }

    @Override
    public void registerData() {
        super.registerData();
        this.dataManager.register(ICON, new TexturedIcon(TexturedIcon.ICONS_TEXTURE, 16, 16, 16, 16));
    }

    @Override
    public void action(LivingEntity entity) {
        entity.setAir(entity.getMaxAir());
    }
}
