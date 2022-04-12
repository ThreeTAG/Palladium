package net.threetag.palladium.power;

import com.google.common.collect.ImmutableMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.network.AddPowerMessage;
import net.threetag.palladium.network.RemovePowerMessage;
import net.threetag.palladium.power.provider.PowerProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PowerHandler implements IPowerHandler {

    private final Map<ResourceLocation, IPowerHolder> powers = new HashMap<>();
    private final LivingEntity entity;

    public PowerHandler(LivingEntity entity) {
        this.entity = entity;
    }

    @Override
    public Map<ResourceLocation, IPowerHolder> getPowerHolders() {
        return ImmutableMap.copyOf(this.powers);
    }

    @Override
    public void tick() {
        if (!this.entity.level.isClientSide) {
            for (PowerProvider provider : PowerProvider.REGISTRY) {
                provider.providePowers(this.entity, this);
            }

            List<Power> toRemove = new ArrayList<>();
            for (IPowerHolder holder : this.powers.values()) {
                holder.tick();

                if (holder.isInvalid()) {
                    toRemove.add(holder.getPower());
                }
            }

            for (Power power : toRemove) {
                this.removePowerHolder(power);
            }
        } else {
            for (IPowerHolder holder : this.powers.values()) {
                holder.tick();
            }
        }
    }

    @Override
    public void setPowerHolder(Power power, IPowerHolder holder) {
        if (this.hasPower(power)) {
            this.powers.put(power.getId(), holder);
        } else {
            this.removePowerHolder(power);
            this.powers.put(power.getId(), holder);
            holder.firstTick();

            if (!this.entity.level.isClientSide) {
                new AddPowerMessage(this.entity.getId(), holder.getPower().getId()).sendToLevel((ServerLevel) this.entity.level);
            }
        }
    }

    @Override
    public void addPower(Power power) {
        if (!hasPower(power)) {
            this.setPowerHolder(power, new DefaultPowerHolder(this.entity, power, defaultPowerHolder -> false));
        }
    }

    @Override
    public void removePowerHolder(Power power) {
        this.removePowerHolder(power.getId());
    }

    @Override
    public void removePowerHolder(ResourceLocation powerId) {
        if (this.powers.containsKey(powerId)) {
            this.powers.get(powerId).lastTick();
            this.powers.remove(powerId);

            if (!this.entity.level.isClientSide) {
                new RemovePowerMessage(this.entity.getId(), powerId).sendToLevel((ServerLevel) this.entity.level);
            }
        }
    }

    @Override
    public IPowerHolder getPowerHolder(Power power) {
        return this.powers.get(power.getId());
    }

    @Override
    public boolean hasPower(Power power) {
        return this.powers.containsKey(power.getId());
    }
}
