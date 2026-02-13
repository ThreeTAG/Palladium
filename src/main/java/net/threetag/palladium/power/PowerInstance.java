package net.threetag.palladium.power;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.component.PalladiumDataComponents;
import net.threetag.palladium.power.ability.Ability;
import net.threetag.palladium.power.ability.AbilityInstance;
import net.threetag.palladium.power.dampening.PowerDampeningSource;
import net.threetag.palladium.power.energybar.EnergyBarConfiguration;
import net.threetag.palladium.power.energybar.EnergyBarInstance;
import net.threetag.palladium.power.energybar.EnergyBarReference;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class PowerInstance {

    public final LivingEntity entity;
    private final Holder<Power> power;
    private final Identifier powerId;
    private final Map<String, AbilityInstance<?>> entryMap = new HashMap<>();
    private final Map<String, EnergyBarInstance> energyBars = new LinkedHashMap<>();
    private PowerValidator validator;
    private int priority;

    public PowerInstance(LivingEntity entity, Holder<Power> power, PowerValidator validator, int priority, CompoundTag componentTag) {
        this.entity = entity;
        this.power = power;
        this.powerId = power.unwrapKey().orElseThrow().identifier();
        this.validator = validator;
        this.priority = priority;

        var subTag = componentTag.getCompoundOrEmpty("abilities");
        for (Map.Entry<String, Ability> e : this.getPower().value().getAbilities().entrySet()) {
            AbilityInstance<?> entry = new AbilityInstance<>(e.getValue(), this, subTag.getCompoundOrEmpty(e.getKey()));
            this.entryMap.put(e.getKey(), entry);
        }

        for (Map.Entry<String, EnergyBarConfiguration> e : this.getPower().value().getEnergyBars().entrySet()) {
            this.energyBars.put(e.getKey(), new EnergyBarInstance(e.getValue(), this, new EnergyBarReference(power.unwrapKey().orElseThrow().identifier(), e.getKey())));
        }
    }

    public Holder<Power> getPower() {
        return this.power;
    }

    public Identifier getPowerId() {
        return this.powerId;
    }

    public LivingEntity getEntity() {
        return this.entity;
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();

        CompoundTag abilities = new CompoundTag();
        for (Map.Entry<String, AbilityInstance<?>> entry : this.entryMap.entrySet()) {
            Tag abData = entry.getValue().save();
            abilities.put(entry.getKey(), abData);
        }
        tag.put("abilities", abilities);

        CompoundTag energies = new CompoundTag();
        for (Map.Entry<String, EnergyBarInstance> entry : this.energyBars.entrySet()) {
            energies.put(entry.getKey(), entry.getValue().save());
        }
        tag.put("energy_bars", energies);

        return tag;
    }

    public Map<String, AbilityInstance<?>> getAbilities() {
        return ImmutableMap.copyOf(this.entryMap);
    }

    public Map<String, EnergyBarInstance> getEnergyBars() {
        return ImmutableMap.copyOf(this.energyBars);
    }

    public void tick() {
        boolean dampened = PowerDampeningSource.isDampened(this.power, this.entity);

        this.entryMap.forEach((id, entry) -> entry.tick(entity, dampened));

        if (!dampened && !this.getEntity().level().isClientSide()) {
            this.energyBars.forEach((id, bar) -> bar.tick(entity));
        }
    }

    public void firstTick() {
        this.entryMap.forEach((id, instance) -> {
            System.out.println("first tick!");
            if (instance.isEnabled()) {
                instance.getAbility().firstTick(entity, instance);
            }
        });
    }

    public void lastTick() {
        this.entryMap.forEach((id, instance) -> {
            if (instance.isEnabled()) {
                instance.set(PalladiumDataComponents.Abilities.ENABLED.get(), false);
                instance.getAbility().lastTick(entity, instance);
            }
        });
    }

    public boolean isInvalid() {
        return !this.validator.stillValid(this.entity, this.power);
    }

    public void switchValidator(PowerValidator validator) {
        this.validator = validator;
    }

    public int getPriority() {
        return this.priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
