package net.threetag.palladium.power;

import com.google.common.collect.ImmutableMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.network.UpdatePowersMessage;
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

            List<IPowerHolder> toRemove = new ArrayList<>();
            PowerCollector collector = new PowerCollector(this.entity, this, toRemove);

            // Find invalid
            for (IPowerHolder holder : this.powers.values()) {
                if (holder.isInvalid()) {
                    toRemove.add(holder);
                }
            }

            // Get new ones
            for (PowerProvider provider : PowerProvider.REGISTRY.getValues()) {
                provider.providePowers(this.entity, this, collector);
            }

            // Remove old ones
            for (IPowerHolder holder : toRemove) {
                this.removePowerHolder(holder.getPower());
            }

            // Add new ones
            for (DefaultPowerHolder holder : collector.getAdded()) {
                this.setPowerHolder(holder.getPower(), holder);
            }

            // Sync
            if (!toRemove.isEmpty() || !collector.getAdded().isEmpty()) {
                var msg = new UpdatePowersMessage(this.entity.getId(), toRemove.stream().map(p -> p.getPower().getId()).toList(), collector.getAdded().stream().map(p -> p.getPower().getId()).toList());
                if (this.entity instanceof ServerPlayer serverPlayer) {
                    msg.sendToTrackingAndSelf(serverPlayer);
                } else {
                    msg.sendToTracking(this.entity);
                }
            }

            // Tick
            for (IPowerHolder holder : this.powers.values()) {
                holder.tick();
            }
        } else {
            for (IPowerHolder holder : this.powers.values()) {
                holder.tick();
            }
        }
    }

    public void setPowerHolder(Power power, IPowerHolder holder) {
        if (this.hasPower(power)) {
            this.powers.put(power.getId(), holder);
        } else {
            this.removePowerHolder(power);
            this.powers.put(power.getId(), holder);
            holder.firstTick();
        }
    }

    public void removePowerHolder(Power power) {
        this.removePowerHolder(power.getId());
    }

    public void removePowerHolder(ResourceLocation powerId) {
        if (this.powers.containsKey(powerId)) {
            this.powers.get(powerId).lastTick();
            this.powers.remove(powerId);
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

    @Override
    public void removeAndAddPowers(List<Power> toRemove, List<Power> toAdd) {
        if (this.entity.level.isClientSide) {
            for (Power power : toRemove) {
                this.removePowerHolder(power);
            }

            for (Power power : toAdd) {
                this.setPowerHolder(power, new DefaultPowerHolder(this.entity, power, IPowerValidator.ALWAYS_ACTIVE));
            }
        }
    }
}
