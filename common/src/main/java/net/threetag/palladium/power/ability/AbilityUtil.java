package net.threetag.palladium.power.ability;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.IPowerHandler;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.PowerManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class AbilityUtil {

    public static Collection<AbilityEntry> getEntries(LivingEntity entity) {
        List<AbilityEntry> entries = new ArrayList<>();
        PowerManager.getPowerHandler(entity).ifPresent(handler -> handler.getPowerHolders().values().stream().map(holder -> holder.getAbilities().values()).forEach(entries::addAll));
        return entries;
    }

    public static Collection<AbilityEntry> getEntries(LivingEntity entity, Ability ability) {
        List<AbilityEntry> entries = new ArrayList<>();
        PowerManager.getPowerHandler(entity).ifPresent(handler -> handler.getPowerHolders().values().stream().map(holder -> holder.getAbilities().values().stream().filter(entry -> entry.getConfiguration().getAbility() == ability).collect(Collectors.toList())).forEach(entries::addAll));
        return entries;
    }

    public static Collection<AbilityEntry> getEnabledEntries(LivingEntity entity, Ability ability) {
        List<AbilityEntry> entries = new ArrayList<>();
        PowerManager.getPowerHandler(entity).ifPresent(handler -> handler.getPowerHolders().values().stream().map(holder -> holder.getAbilities().values().stream().filter(entry -> entry.isEnabled() && entry.getConfiguration().getAbility() == ability).collect(Collectors.toList())).forEach(entries::addAll));
        return entries;
    }

    public static AbilityEntry getEntry(LivingEntity entity, ResourceLocation powerId, String abilityId) {
        Power power = PowerManager.getInstance(entity.level).getPower(powerId);

        if (power == null) {
            return null;
        }

        IPowerHandler handler = PowerManager.getPowerHandler(entity).orElse(null);

        if (handler == null) {
            return null;
        }

        IPowerHolder holder = handler.getPowerHolder(power);

        if (holder == null) {
            return null;
        }

        return holder.getAbilities().get(abilityId);
    }

    public static boolean isUnlocked(LivingEntity entity, ResourceLocation powerId, String abilityId) {
        var entry = getEntry(entity, powerId, abilityId);
        return entry != null && entry.isUnlocked();
    }

    public static boolean isEnabled(LivingEntity entity, ResourceLocation powerId, String abilityId) {
        var entry = getEntry(entity, powerId, abilityId);
        return entry != null && entry.isEnabled();
    }

}
