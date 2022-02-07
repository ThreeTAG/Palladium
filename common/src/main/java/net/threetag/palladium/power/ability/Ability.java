package net.threetag.palladium.power.ability;

import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.Registries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.util.property.PalladiumProperty;

import java.util.HashMap;
import java.util.Map;

public class Ability {

    public static final ResourceKey<Registry<Ability>> RESOURCE_KEY = ResourceKey.createRegistryKey(new ResourceLocation(Palladium.MOD_ID, "abilities"));
    public static final Registrar<Ability> REGISTRY = Registries.get(Palladium.MOD_ID).builder(RESOURCE_KEY.location(), new Ability[0]).build();

    final Map<PalladiumProperty<?>, Object> defaultProperties = new HashMap<>();

    public void tick(LivingEntity entity, AbilityEntry entry, IPowerHolder holder, boolean enabled) {

    }

    public void firstTick(LivingEntity entity, AbilityEntry entry, IPowerHolder holder, boolean enabled) {

    }

    public void lastTick(LivingEntity entity, AbilityEntry entry, IPowerHolder holder, boolean enabled) {

    }

    public <T> Ability withProperty(PalladiumProperty<T> data, T value) {
        this.defaultProperties.put(data, value);
        return this;
    }

}
