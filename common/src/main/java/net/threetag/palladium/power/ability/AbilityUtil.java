package net.threetag.palladium.power.ability;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.IPowerHandler;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.PowerManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class AbilityUtil {

    /**
     * Returns all ability entries from the given entity
     *
     * @param entity Entity having abilities
     * @return List of all ability entries
     */
    @NotNull
    public static Collection<AbilityEntry> getEntries(LivingEntity entity) {
        List<AbilityEntry> entries = new ArrayList<>();
        PowerManager.getPowerHandler(entity).ifPresent(handler -> handler.getPowerHolders().values().stream().map(holder -> holder.getAbilities().values()).forEach(entries::addAll));
        return entries;
    }

    /**
     * Returns all ability entries of the given ability type from the entity
     *
     * @param entity    Entity having abilities
     * @param abilityId ID of the ability that is being looked for
     * @return List of all ability entries of the given ability type
     */
    @NotNull
    public static Collection<AbilityEntry> getEntries(LivingEntity entity, ResourceLocation abilityId) {
        if (!Ability.REGISTRY.containsKey(abilityId)) {
            return Collections.emptyList();
        }

        return getEntries(entity, Ability.REGISTRY.get(abilityId));
    }

    /**
     * Returns all ability entries of the given ability type from the entity
     *
     * @param entity  Entity having abilities
     * @param ability The ability that is being looked for
     * @return List of all ability entries of the given ability type
     */
    @NotNull
    public static Collection<AbilityEntry> getEntries(LivingEntity entity, Ability ability) {
        List<AbilityEntry> entries = new ArrayList<>();
        PowerManager.getPowerHandler(entity).ifPresent(handler -> handler.getPowerHolders().values().stream().map(holder -> holder.getAbilities().values().stream().filter(entry -> entry.getConfiguration().getAbility() == ability).collect(Collectors.toList())).forEach(entries::addAll));
        return entries;
    }

    /**
     * Returns all enabled ability entries of the given ability type from the entity
     *
     * @param entity    Entity having abilities
     * @param abilityId ID of the ability that is being looked for
     * @return List of all enabled ability entries of the given ability type
     */
    @NotNull
    public static Collection<AbilityEntry> getEnabledEntries(LivingEntity entity, ResourceLocation abilityId) {
        if (!Ability.REGISTRY.containsKey(abilityId)) {
            return Collections.emptyList();
        }

        return getEnabledEntries(entity, Ability.REGISTRY.get(abilityId));
    }

    /**
     * Returns all enabled ability entries of the given ability type from the entity
     *
     * @param entity  Entity having abilities
     * @param ability The ability that is being looked for
     * @return List of all enabled ability entries of the given ability type
     */
    @NotNull
    public static Collection<AbilityEntry> getEnabledEntries(LivingEntity entity, Ability ability) {
        List<AbilityEntry> entries = new ArrayList<>();
        PowerManager.getPowerHandler(entity).ifPresent(handler -> handler.getPowerHolders().values().stream().map(holder -> holder.getAbilities().values().stream().filter(entry -> entry.isEnabled() && entry.getConfiguration().getAbility() == ability).collect(Collectors.toList())).forEach(entries::addAll));
        return entries;
    }

    /**
     * Returns a specific ability entry from a specific power
     *
     * @param entity     Entity having abilities
     * @param powerId    ID of the power containing the specific ability
     * @param abilityKey The unique key being used in the power json for the ability
     * @return The specific {@link AbilityEntry}, or null
     */
    @Nullable
    public static AbilityEntry getEntry(LivingEntity entity, ResourceLocation powerId, String abilityKey) {
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

        return holder.getAbilities().get(abilityKey);
    }

    /**
     * Checks if a specific ability entry is unlocked
     *
     * @param entity     Entity having abilities
     * @param powerId    ID of the power containing the specific ability
     * @param abilityKey The unique key being used in the power json for the ability
     * @return True if the ability is unlocked
     */
    public static boolean isUnlocked(LivingEntity entity, ResourceLocation powerId, String abilityKey) {
        var entry = getEntry(entity, powerId, abilityKey);
        return entry != null && entry.isUnlocked();
    }

    /**
     * Checks if a specific ability entry is enabled
     *
     * @param entity     Entity having abilities
     * @param powerId    ID of the power containing the specific ability
     * @param abilityKey The unique key being used in the power json for the ability
     * @return True if the ability is enabled
     */
    public static boolean isEnabled(LivingEntity entity, ResourceLocation powerId, String abilityKey) {
        var entry = getEntry(entity, powerId, abilityKey);
        return entry != null && entry.isEnabled();
    }

    /**
     * Checks if a specific ability entry of a certain type is unlocked
     *
     * @param entity  Entity having abilities
     * @param ability Type of the ability that must be unlocked
     * @return True if any ability of the type is unlocked
     */
    public static boolean isTypeUnlocked(LivingEntity entity, Ability ability) {
        return getEntries(entity, ability).stream().anyMatch(AbilityEntry::isUnlocked);
    }

    /**
     * Checks if a specific ability entry of a certain type is unlocked
     *
     * @param entity    Entity having abilities
     * @param abilityId ID of the ability type that must be unlocked
     * @return True if any ability of the type is unlocked
     */
    public static boolean isTypeUnlocked(LivingEntity entity, ResourceLocation abilityId) {
        if (!Ability.REGISTRY.containsKey(abilityId)) {
            return false;
        }
        return isTypeUnlocked(entity, Ability.REGISTRY.get(abilityId));
    }

    /**
     * Checks if a specific ability entry of a certain type is enabled
     *
     * @param entity  Entity having abilities
     * @param ability Type of the ability that must be enabled
     * @return True if any ability of the type is enabled
     */
    public static boolean isTypeEnabled(LivingEntity entity, Ability ability) {
        return getEntries(entity, ability).stream().anyMatch(AbilityEntry::isEnabled);
    }

    /**
     * Checks if a specific ability entry of a certain type is enabled
     *
     * @param entity    Entity having abilities
     * @param abilityId ID of the ability type that must be enabled
     * @return True if any ability of the type is enabled
     */
    public static boolean isTypeEnabled(LivingEntity entity, ResourceLocation abilityId) {
        if (!Ability.REGISTRY.containsKey(abilityId)) {
            return false;
        }
        return isTypeEnabled(entity, Ability.REGISTRY.get(abilityId));
    }

    /**
     * Checks if the entity has the given power
     *
     * @param entity  Entity having abilities
     * @param powerId ID of the power that is being checked for
     * @return True if the entity has the power
     */
    public static boolean hasPower(LivingEntity entity, ResourceLocation powerId) {
        Power power = PowerManager.getInstance(entity.level).getPower(powerId);

        if (power == null) {
            return false;
        }

        IPowerHandler handler = PowerManager.getPowerHandler(entity).orElse(null);

        if (handler == null) {
            return false;
        }

        return handler.getPowerHolder(power) != null;
    }

}
