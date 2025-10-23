package net.threetag.palladium.power.ability;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.EntityPowerHandler;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.PowerHolder;
import net.threetag.palladium.power.PowerUtil;
import net.threetag.palladium.registry.PalladiumRegistryKeys;
import net.threetag.palladium.util.Easing;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class AbilityUtil {

    /**
     * Returns all ability instances from the given entity
     *
     * @param entity Entity having abilities
     * @return List of all ability instances
     */
    @NotNull
    public static <T extends Ability> Collection<AbilityInstance<T>> getInstances(LivingEntity entity) {
        List<AbilityInstance<T>> instances = new ArrayList<>();
        var handler = PowerUtil.getPowerHandler(entity);
        handler.getPowerHolders().values().stream().map(holder -> holder.getAbilities().values()).forEach(abilityInstances -> abilityInstances.forEach(i -> instances.add((AbilityInstance<T>) i)));
        return instances;
    }

    /**
     * Returns all ability instances of the given ability type from the entity
     *
     * @param entity  Entity having abilities
     * @param ability The ability that is being looked for
     * @return List of all ability instances of the given ability type
     */
    @NotNull
    public static <T extends Ability> Collection<AbilityInstance<T>> getInstances(LivingEntity entity, AbilitySerializer<T> ability) {
        List<AbilityInstance<T>> instances = new ArrayList<>();
        var handler = PowerUtil.getPowerHandler(entity);
        handler.getPowerHolders().values().stream().map(holder -> holder.getAbilities().values().stream().filter(instance -> instance.getAbility().getSerializer() == ability).collect(Collectors.toList())).forEach(abilityInstances -> abilityInstances.forEach(i -> instances.add((AbilityInstance<T>) i)));
        return instances;
    }

    /**
     * Returns all enabled ability instances from the given entity
     *
     * @param entity Entity having abilities
     * @return List of all enabled ability instances
     */
    @NotNull
    public static Collection<AbilityInstance<?>> getEnabledInstances(LivingEntity entity) {
        List<AbilityInstance<?>> instances = new ArrayList<>();
        var handler = PowerUtil.getPowerHandler(entity);
        for (PowerHolder holder : handler.getPowerHolders().values()) {
            Collection<AbilityInstance<?>> values = holder.getAbilities().values();
            for (AbilityInstance<?> value : values) {
                if (value.isEnabled()) {
                    instances.add(value);
                }
            }
        }
        return instances;
    }

    /**
     * Returns all enabled ability instances of the given ability type from the entity
     *
     * @param entity  Entity having abilities
     * @param ability The ability that is being looked for
     * @return List of all enabled ability instances of the given ability type
     */
    @NotNull
    public static <T extends Ability> Collection<AbilityInstance<T>> getEnabledInstances(LivingEntity entity, AbilitySerializer<T> ability) {
        List<AbilityInstance<T>> instances = new ArrayList<>();
        var handler = PowerUtil.getPowerHandler(entity);
        handler.getPowerHolders().values().stream().map(holder -> holder.getAbilities().values().stream().filter(instance -> instance.isEnabled() && instance.getAbility().getSerializer() == ability).collect(Collectors.toList())).forEach(abilityInstances -> abilityInstances.forEach(i -> instances.add((AbilityInstance<T>) i)));
        return instances;
    }

    /**
     * Returns a specific ability instance from a specific power
     *
     * @param entity     Entity having abilities
     * @param powerId    ID of the power containing the specific ability
     * @param abilityKey The unique key being used in the power json for the ability
     * @return The specific {@link AbilityInstance}, or null
     */
    @Nullable
    public static <T extends Ability> AbilityInstance<T> getInstance(LivingEntity entity, ResourceLocation powerId, String abilityKey) {
        Power power = entity.registryAccess().lookupOrThrow(PalladiumRegistryKeys.POWER).getValue(powerId);

        if (power == null) {
            return null;
        }

        EntityPowerHandler handler = PowerUtil.getPowerHandler(entity);

        if (handler == null) {
            return null;
        }

        PowerHolder holder = handler.getPowerHolder(powerId);

        if (holder == null) {
            return null;
        }

        return (AbilityInstance<T>) holder.getAbilities().get(abilityKey);
    }

    /**
     * Checks if a specific ability instance is unlocked
     *
     * @param entity     Entity having abilities
     * @param powerId    ID of the power containing the specific ability
     * @param abilityKey The unique key being used in the power json for the ability
     * @return True if the ability is unlocked
     */
    public static boolean isUnlocked(LivingEntity entity, ResourceLocation powerId, String abilityKey) {
        var instance = getInstance(entity, powerId, abilityKey);
        return instance != null && instance.isUnlocked();
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
        var instance = getInstance(entity, powerId, abilityKey);
        return instance != null && instance.isEnabled();
    }

    /**
     * Checks if a specific ability instance of a certain type is unlocked
     *
     * @param entity  Entity having abilities
     * @param ability Type of the ability that must be unlocked
     * @return True if any ability of the type is unlocked
     */
    public static boolean isTypeUnlocked(LivingEntity entity, AbilitySerializer<?> ability) {
        return getInstances(entity, ability).stream().anyMatch(AbilityInstance::isUnlocked);
    }

    /**
     * Checks if a specific ability instance of a certain type is enabled
     *
     * @param entity  Entity having abilities
     * @param ability Type of the ability that must be enabled
     * @return True if any ability of the type is enabled
     */
    public static boolean isTypeEnabled(LivingEntity entity, AbilitySerializer<?> ability) {
        return getInstances(entity, ability).stream().anyMatch(AbilityInstance::isEnabled);
    }

    public static float getHighestAnimationTimerValue(LivingEntity entity, AbilitySerializer<?> ability, float partialTick) {
        return getHighestAnimationTimerValue(entity, ability, partialTick, Easing.LINEAR);
    }

    public static float getHighestAnimationTimerValue(LivingEntity entity, AbilitySerializer<?> ability, float partialTick, Easing easing) {
        float f = 0F;

        for (AbilityInstance<?> instance : AbilityUtil.getInstances(entity, ability)) {
            var timer = instance.getAnimationTimer();

            if (timer != null) {
                f = Math.max(f, timer.interpolated(partialTick));
            }
        }

        return easing.apply(f);
    }

    public static float getHighestAnimationTimerProgress(LivingEntity entity, AbilitySerializer<?> ability, float partialTick) {
        return getHighestAnimationTimerProgress(entity, ability, partialTick, Easing.LINEAR);
    }

    public static float getHighestAnimationTimerProgress(LivingEntity entity, AbilitySerializer<?> ability, float partialTick, Easing easing) {
        float f = 0F;

        for (AbilityInstance<?> instance : AbilityUtil.getInstances(entity, ability)) {
            var timer = instance.getAnimationTimer();

            if (timer != null) {
                f = Math.max(f, timer.progress(partialTick));
            }
        }

        return easing.apply(f);
    }
}
