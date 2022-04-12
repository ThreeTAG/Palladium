package net.threetag.palladium.power.provider;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.threetag.palladium.power.DefaultPowerHolder;
import net.threetag.palladium.power.IPowerHandler;
import net.threetag.palladium.power.ItemPowerManager;
import net.threetag.palladium.power.Power;

import java.util.List;

public class EquipmentSlotPowerProvider extends PowerProvider {

    @Override
    public void providePowers(LivingEntity entity, IPowerHandler handler) {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            List<Power> powers = ItemPowerManager.getInstance().getPowerForItem(slot.getName(), entity.getItemBySlot(slot).getItem());

            if (powers != null) {
                for (Power power : powers) {
                    if (power != null && !handler.hasPower(power)) {
                        handler.setPowerHolder(power, new PowerHolder(entity, power, entity.getItemBySlot(slot), slot));
                    }
                }
            }
        }
    }

    public static class PowerHolder extends DefaultPowerHolder {

        public final ItemStack stack;
        public final EquipmentSlot slot;

        public PowerHolder(LivingEntity entity, Power power, ItemStack stack, EquipmentSlot slot) {
            super(entity, power, null);
            this.stack = stack;
            this.slot = slot;
        }

        @Override
        public boolean isInvalid() {
            return this.getPower().isInvalid() || this.stack != this.entity.getItemBySlot(this.slot);
        }
    }

}
