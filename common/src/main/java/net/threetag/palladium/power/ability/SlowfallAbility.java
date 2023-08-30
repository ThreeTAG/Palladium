package net.threetag.palladium.power.ability;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Items;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.util.icon.ItemIcon;

public class SlowfallAbility extends Ability {

    public SlowfallAbility() {
        this.withProperty(ICON, new ItemIcon(Items.FEATHER));
    }

    @Override
    public void tick(LivingEntity entity, AbilityEntry entry, IPowerHolder holder, boolean enabled) {
        if (enabled && !entity.onGround() && entity.getDeltaMovement().y() < 0D) {
            entity.setDeltaMovement(entity.getDeltaMovement().x, entity.getDeltaMovement().y * 0.6D, entity.getDeltaMovement().z);
            entity.fallDistance = 0F;
        }
    }

    @Override
    public String getDocumentationDescription() {
        return "Makes the entity fall slower.";
    }
}
