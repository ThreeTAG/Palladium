package net.threetag.palladium.power;

import com.google.common.collect.ImmutableMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.network.SetPowerMessage;
import net.threetag.palladium.power.provider.IPowerProvider;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
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
        for (IPowerProvider provider : PowerManager.getInstance(this.entity.level).getProviders()) {
            IPowerHolder holder = this.powers.get(provider.getKey());

            if (holder != null) {
                if (holder.isInvalid()) {
                    this.setPowerHolder(provider.getKey(), provider.createPower(this.entity, null));
                }
            } else {
                holder = provider.createPower(this.entity, null);

                if (holder != null) {
                    this.setPowerHolder(provider.getKey(), holder);
                }
            }

            if (holder != null) {
                holder.tick();
            }
        }
    }

    @Override
    public void setPowerHolder(ResourceLocation provider, @Nullable IPowerHolder holder) {
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

    public void removePowerHolder(ResourceLocation provider) {
        this.setPowerHolder(provider, null);
    }

    @Override
    public IPowerHolder getPowerHolder(ResourceLocation provider) {
        return this.powers.get(provider);
    }
}
