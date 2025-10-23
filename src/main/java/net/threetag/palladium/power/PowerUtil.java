package net.threetag.palladium.power;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.entity.data.PalladiumEntityData;
import net.threetag.palladium.entity.data.PalladiumEntityDataTypes;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

import java.util.Collection;
import java.util.Objects;

public class PowerUtil {

    /**
     * Returns the power handler for the given entity
     */
    public static EntityPowerHandler getPowerHandler(LivingEntity entity) {
        return PalladiumEntityData.get(entity, PalladiumEntityDataTypes.POWER_HANDLER.value());
    }

    /**
     * Returns the powers the entity currently has
     *
     * @param entity {@link LivingEntity} which has powers
     * @return {@link Collection} of powers of the entity
     */
    public static Collection<Power> getPowers(LivingEntity entity) {
        var powerHandler = getPowerHandler(entity);
        return powerHandler.getPowerHolders().values().stream().map(powerHolder -> powerHolder.getPower().value()).toList();
    }

    /**
     * Returns the IDs of the powers the entity currently has
     *
     * @param entity {@link LivingEntity} which has superpowers
     * @return {@link Collection} of IDs of powers of the entity
     */
    public static Collection<ResourceLocation> getPowerIds(LivingEntity entity) {
        var powerHandler = getPowerHandler(entity);
        return powerHandler.getPowerHolders().values().stream()
                .map(h -> entity.registryAccess().lookupOrThrow(PalladiumRegistryKeys.POWER).getKey(h.getPower().value()))
                .toList();
    }

    /**
     * Checks if the entity has the given power
     *
     * @param entity  Entity having abilities
     * @param powerId ID of the power that is being checked for
     * @return True if the entity has the power
     */
    public static boolean hasPower(LivingEntity entity, ResourceLocation powerId) {
        Power power = entity.registryAccess().lookupOrThrow(PalladiumRegistryKeys.POWER).getValue(powerId);

        if (power == null) {
            return false;
        }

        EntityPowerHandler handler = PowerUtil.getPowerHandler(entity);

        if (handler == null) {
            return false;
        }

        return handler.getPowerHolder(powerId) != null;
    }

    /**
     * Returns the powers the entity currently has, filtered by the namespace
     *
     * @param entity {@link LivingEntity} which has powers
     * @return {@link Collection} of powers of the entity
     */
    public static Collection<Power> getPowersForNamespace(LivingEntity entity, String namespace) {
        var powerHandler = getPowerHandler(entity);
        return powerHandler.getPowerHolders().values().stream()
                .map(powerHolder -> powerHolder.getPower().value())
                .filter(p -> entity.registryAccess().lookupOrThrow(PalladiumRegistryKeys.POWER).getKey(p).getNamespace().equals(namespace))
                .toList();
    }

    /**
     * Returns the IDs of the powers the entity currently has, filtered by the namespace
     *
     * @param entity {@link LivingEntity} which has powers
     * @return {@link Collection} of IDs of powers of the entity
     */
    public static Collection<ResourceLocation> getPowerIdsForNamespace(LivingEntity entity, String namespace) {
        var powerHandler = getPowerHandler(entity);
        return powerHandler.getPowerHolders().values().stream()
                .map(h -> entity.registryAccess().lookupOrThrow(PalladiumRegistryKeys.POWER).getKey(h.getPower().value()))
                .filter(id -> Objects.requireNonNull(id).getNamespace().equals(namespace))
                .toList();
    }

}
