package net.threetag.palladium.power.holderfactory;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.power.DefaultPowerHolder;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.ItemPowerManager;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.provider.IPowerProvider;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class EquipmentSlotPowerFactory extends PowerProviderFactory {

    @Override
    public void create(Consumer<IPowerProvider> consumer) {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            consumer.accept(new PowerProvider(new ResourceLocation(Palladium.MOD_ID, "equipment_slot_" + slot.getName()), slot));
        }
    }

    public record PowerProvider(ResourceLocation key, EquipmentSlot slot) implements IPowerProvider {

        @Override
        public ResourceLocation getKey() {
            return this.key;
        }

        @Override
        public IPowerHolder createPower(LivingEntity entity, @Nullable Power power) {
            power = power == null ? ItemPowerManager.getInstance().getPowerForItem(this.slot.getName(), entity.getItemBySlot(this.slot).getItem()) : power;
            return power != null ? new PowerHolder(entity, power, this.getKey(), entity.getItemBySlot(this.slot), this.slot) : null;
        }
    }

    public static class PowerHolder extends DefaultPowerHolder {

        public final ItemStack stack;
        public final EquipmentSlot slot;

        public PowerHolder(LivingEntity entity, Power power, ResourceLocation provider, ItemStack stack, EquipmentSlot slot) {
            super(entity, power, provider, null);
            this.stack = stack;
            this.slot = slot;
        }

        @Override
        public boolean isInvalid() {
            return this.getPower().isInvalid() || this.stack != this.entity.getItemBySlot(this.slot);
        }
    }

}
