package net.threetag.palladium.power;

import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.provider.IPowerProvider;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PowerHandler implements IPowerHandler {

    private final Map<IPowerProvider, IPowerHolder> powers = new HashMap<>();
    private final LivingEntity entity;

    public PowerHandler(LivingEntity entity) {
        this.entity = entity;
    }

    @Override
    public Collection<IPowerHolder> getPowerHolders() {
        return this.powers.values();
    }

    @Override
    public void tick() {
        if (!this.entity.level.isClientSide) {
            for (Map.Entry<ResourceLocation, Pair<Integer, IPowerProvider>> entry : PowerManager.PROVIDERS.entrySet()) {


                
                Pair<Integer, IPowerProvider> pair = entry.getValue();
                IPowerProvider provider = pair.getSecond();
                IPowerHolder newPowerHolder = pair.getSecond().createHolder(this.entity);

                if (newPowerHolder != null) {
                    if (this.powers.containsKey(provider)) {
                        IPowerHolder powerHolder = this.powers.get(provider);

                        if (powerHolder.getPower().getId().equals(newPowerHolder.getPower().getId())) {
                            // nothing
                        } else {
                            this.setPowerHolder(provider, newPowerHolder);
                        }
                    } else {
                        this.setPowerHolder(provider, newPowerHolder);
                    }
                } else if (this.powers.containsKey(provider)) {
                    this.removePowerHolder(provider);
                }
            }
        }

        for (Map.Entry<IPowerProvider, IPowerHolder> entry : this.powers.entrySet()) {
            IPowerHolder holder = entry.getValue();
            if (!this.entity.level.isClientSide) {
                IPowerProvider provider = entry.getKey();
                if (holder.isInvalid()) {
                    Power newPower = PowerManager.getInstance(this.entity.level).getPower(holder.getPower().getId());

                    if (newPower != null) {
                        this.setPowerHolder(provider, provider.createHolder(this.entity));
                    } else {
                        this.removePowerHolder(provider);
                    }
                }
            }

            holder.tick();
        }
    }

    public void setPowerHolder(IPowerProvider provider, IPowerHolder holder) {
        if (this.powers.containsKey(provider)) {
            this.powers.get(provider).lastTick();
        }
        this.powers.put(provider, holder);
        holder.firstTick();
    }

    public void removePowerHolder(IPowerProvider provider) {
        if (this.powers.containsKey(provider)) {
            this.powers.get(provider).lastTick();
        }
        this.powers.remove(provider);
    }

    @Override
    public IPowerHolder getPowerHolder(IPowerProvider provider) {
        return this.powers.get(provider);
    }
}
