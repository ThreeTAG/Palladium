package net.threetag.palladium.power;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.network.SetPowerMessage;
import net.threetag.palladium.power.provider.PowerProvider;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class PowerHandler implements IPowerHandler {

    private final Map<PowerProvider, IPowerHolder> powers = new HashMap<>();
    private final LivingEntity entity;

    public PowerHandler(LivingEntity entity) {
        this.entity = entity;
    }

    @Override
    public Map<PowerProvider, IPowerHolder> getPowerHolders() {
        return ImmutableMap.copyOf(this.powers);
    }

    @Override
    public void tick() {
        for (PowerProvider provider : PowerManager.PROVIDER_REGISTRY) {
            IPowerHolder holder = this.powers.get(provider);

            if (!this.entity.level.isClientSide) {
                if (holder != null) {
                    if (holder.isInvalid()) {
                        this.setPowerHolder(provider, provider.createHolder(this.entity, null));
                    }
                } else {
                    holder = provider.createHolder(this.entity, null);

                    if (holder != null) {
                        this.setPowerHolder(provider, holder);
                    }
                }
            }

            if (holder != null) {
                holder.tick();
            }
        }
    }

    @Override
    public void setPowerHolder(PowerProvider provider, @Nullable IPowerHolder holder) {
        if (this.powers.containsKey(provider)) {
            this.powers.get(provider).lastTick();
        }

        if (holder != null) {
            this.powers.put(provider, holder);
            holder.firstTick();
        } else {
            this.powers.remove(provider);
        }

        if (!this.entity.level.isClientSide) {
            new SetPowerMessage(this.entity.getId(), provider, holder != null ? holder.getPower().getId() : null).sendToLevel((ServerLevel) this.entity.level);
        }
    }

    public void removePowerHolder(PowerProvider provider) {
        this.setPowerHolder(provider, null);
    }

    @Override
    public IPowerHolder getPowerHolder(PowerProvider provider) {
        return this.powers.get(provider);
    }
}
