package net.threetag.palladium.power;

import net.minecraft.world.entity.LivingEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class PowerCollector {

    private final LivingEntity entity;
    private final IPowerHandler handler;
    private final List<IPowerHolder> toRemove;
    private final List<DefaultPowerHolder> powerHolders = new ArrayList<>();

    public PowerCollector(LivingEntity entity, IPowerHandler handler, List<IPowerHolder> toRemove) {
        this.entity = entity;
        this.handler = handler;
        this.toRemove = toRemove;
    }

    public void addPower(Power power, Supplier<IPowerValidator> validatorSupplier) {
        if (power == null) {
            return;
        }

        IPowerHolder found = null;
        for (IPowerHolder holder : this.toRemove) {
            if (holder.getPower() == power) {
                found = holder;
                break;
            }
        }

        if (found != null) {
            found.switchValidator(validatorSupplier.get());
            this.toRemove.remove(found);
            return;
        }

        if (!this.handler.hasPower(power)) {
            this.powerHolders.add(new DefaultPowerHolder(this.entity, power, validatorSupplier.get()));
        }
    }

    public List<DefaultPowerHolder> getAdded() {
        return this.powerHolders;
    }
}
