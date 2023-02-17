package net.threetag.palladium.util;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.threetag.palladium.compat.curiostinkets.CuriosTrinketsUtil;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class PlayerSlot {

    private static final Map<EquipmentSlot, PlayerSlot> EQUIPMENT_SLOTS = new HashMap<>();

    @Nullable
    public static PlayerSlot get(String name) {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getName().equalsIgnoreCase(name)) {
                return EQUIPMENT_SLOTS.computeIfAbsent(slot, EquipmentPlayerSlot::new);
            }
        }

        if (name.startsWith("curios:")) {
            return new CurioTrinketSlot(true, name.substring("curios:".length()));
        }

        if (name.startsWith("trinkets:")) {
            return new CurioTrinketSlot(false, name.substring("trinkets:".length()));
        }

        return null;
    }

    public abstract List<ItemStack> getItems(LivingEntity entity);

    @Nullable
    public EquipmentSlot getEquipmentSlot() {
        return null;
    }

    private static class EquipmentPlayerSlot extends PlayerSlot {

        private final EquipmentSlot slot;

        public EquipmentPlayerSlot(EquipmentSlot slot) {
            this.slot = slot;
        }

        @Override
        public List<ItemStack> getItems(LivingEntity entity) {
            return Collections.singletonList(entity.getItemBySlot(this.slot));
        }

        @Override
        public @Nullable EquipmentSlot getEquipmentSlot() {
            return this.slot;
        }

        @Override
        public String toString() {
            return this.slot.getName();
        }
    }

    private static class CurioTrinketSlot extends PlayerSlot {

        private final boolean curios;
        private final String slot;

        public CurioTrinketSlot(boolean curios, String slot) {
            this.curios = curios;
            this.slot = slot;
        }

        @Override
        public List<ItemStack> getItems(LivingEntity entity) {
            return CuriosTrinketsUtil.getInstance().getItemsInSlot(entity, this.slot);
        }

        @Override
        public String toString() {
            return (this.curios ? "curios:" : "trinkets:") + this.slot;
        }
    }

}
