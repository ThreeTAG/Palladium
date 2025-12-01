package net.threetag.palladium.power.dampening;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.component.PalladiumDataComponents;
import net.threetag.palladium.component.SlotDependentHolderSetComponent;
import net.threetag.palladium.entity.PlayerSlot;
import net.threetag.palladium.power.Power;

public class ItemPowerDampening extends PowerDampeningSource {

    @Override
    public boolean isDampened(LivingEntity entity, Holder<Power> powerHolder) {
        for (PlayerSlot slot : PlayerSlot.values(entity.level())) {
            for (int i = 0; i < slot.getSize(entity); i++) {
                var stack = slot.getItem(entity, i);

                if (stack.has(PalladiumDataComponents.Items.POWER_DAMPENING.get())) {
                    SlotDependentHolderSetComponent<Power> dampening = stack.get(PalladiumDataComponents.Items.POWER_DAMPENING.get());

                    if (dampening != null && dampening.contains(slot, powerHolder)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
