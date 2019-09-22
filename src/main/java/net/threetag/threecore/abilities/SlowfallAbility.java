package net.threetag.threecore.abilities;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Items;
import net.threetag.threecore.util.icon.ItemIcon;

public class SlowfallAbility extends Ability {

    public SlowfallAbility() {
        super(AbilityType.SLOWFALL);
    }

    @Override
    public void registerData() {
        super.registerData();
        this.dataManager.register(Ability.ICON, new ItemIcon(Items.FEATHER));
    }

    @Override
    public void updateTick(LivingEntity entity) {
        if (!entity.onGround && entity.getMotion().y < 0.0D) {
            entity.setMotion(entity.getMotion().x, entity.getMotion().y * 0.6D, entity.getMotion().z);
            entity.fallDistance = 0F;
        }
    }
}
