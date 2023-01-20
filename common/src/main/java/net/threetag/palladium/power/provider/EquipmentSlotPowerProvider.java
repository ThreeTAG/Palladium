package net.threetag.palladium.power.provider;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.threetag.palladium.power.*;

import java.util.List;

public class EquipmentSlotPowerProvider extends PowerProvider {

    @Override
    public void providePowers(LivingEntity entity, IPowerHandler handler, PowerCollector collector) {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            List<Power> powers = ItemPowerManager.getInstance().getPowerForItem(slot.getName(), entity.getItemBySlot(slot).getItem());

            if (powers != null) {
                for (Power power : powers) {
                    collector.addPower(power, () -> new Validator(entity.getItemBySlot(slot).getItem(), slot));
                }
            }
        }
    }

    public record Validator(Item item, EquipmentSlot slot) implements IPowerValidator {

        @Override
        public boolean stillValid(LivingEntity entity, Power power) {
            return entity.getItemBySlot(this.slot).is(this.item);
        }
    }

}
