package net.threetag.palladium.power.provider;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.threetag.palladium.power.*;

import java.util.List;

public class EquipmentSlotPowerProvider extends PowerProvider {

    @Override
    public void providePowers(LivingEntity entity, IPowerHandler handler, PowerCollector collector) {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            List<Power> powers = ItemPowerManager.getInstance().getPowerForItem(slot.getName(), entity.getItemBySlot(slot).getItem());

            if (powers != null) {
                for (Power power : powers) {
                    collector.addPower(power, () -> new Validator(entity.getItemBySlot(slot), slot));
                }
            }
        }
    }

    public record Validator(ItemStack stack, EquipmentSlot slot) implements IPowerValidator {

        @Override
        public boolean stillValid(LivingEntity entity, Power power) {
            return this.stack == entity.getItemBySlot(this.slot);
        }
    }

}
