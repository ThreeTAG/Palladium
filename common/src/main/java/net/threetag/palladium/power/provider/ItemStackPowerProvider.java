package net.threetag.palladium.power.provider;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.IPowerHolder;

import java.util.List;

public record ItemStackPowerProvider(
        List<EquipmentSlot> equipmentSlots) implements IPowerProvider {

    @Override
    public IPowerHolder createHolder(LivingEntity entity) {
        for (EquipmentSlot slot : this.equipmentSlots) {
            // TODO register power look ups
        }
        return null;
    }
}
