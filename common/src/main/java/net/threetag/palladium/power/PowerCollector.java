package net.threetag.palladium.power;

import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class PowerCollector {

    private final LivingEntity entity;
    private final EntityPowerHandler handler;
    private final List<PowerHolder> toRemove;
    private final List<PowerHolderCache> powerHolders = new ArrayList<>();

    public PowerCollector(LivingEntity entity, EntityPowerHandler handler, List<PowerHolder> toRemove) {
        this.entity = entity;
        this.handler = handler;
        this.toRemove = toRemove;
    }

    public void addPower(Holder<Power> power, Supplier<PowerValidator> validatorSupplier) {
        if (power == null) {
            return;
        }

        PowerHolder found = null;
        for (PowerHolder holder : this.toRemove) {
            if (holder.getPower().is(power)) {
                found = holder;
                break;
            }
        }

        if (found != null) {
            found.switchValidator(validatorSupplier.get());
            this.toRemove.remove(found);
            return;
        }

        if (!this.handler.hasPower(power.unwrapKey().orElseThrow().location())) {
            this.powerHolders.add(new PowerHolderCache(power, validatorSupplier.get()));
        }
    }

    public List<PowerHolderCache> getAdded() {
        return this.powerHolders;
    }

    public record PowerHolderCache(Holder<Power> power, PowerValidator validator) {

        public PowerHolder make(LivingEntity entity, CompoundTag compoundTag) {
            return new PowerHolder(entity, this.power, this.validator, compoundTag.getCompoundOrEmpty(this.power.unwrapKey().orElseThrow().location().toString()));
        }

    }
}
