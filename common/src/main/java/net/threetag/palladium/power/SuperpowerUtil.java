package net.threetag.palladium.power;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.util.property.PalladiumProperties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SuperpowerUtil {

    /**
     * Sets the entity's superpowers to just the given one
     *
     * @param entity  {@link LivingEntity} receiving the {@link Power}
     * @param powerId ID of the {@link Power} being given to the {@link LivingEntity}
     */
    public static void setSuperpower(LivingEntity entity, ResourceLocation powerId) {
        PalladiumProperties.SUPERPOWER_IDS.set(entity, Collections.singletonList(powerId));
    }

    /**
     * Sets the entity's superpowers to just the given one
     *
     * @param entity {@link LivingEntity} receiving the {@link Power}
     * @param power  The {@link Power} being given to the {@link LivingEntity}
     */
    public static void setSuperpower(LivingEntity entity, Power power) {
        setSuperpower(entity, power.getId());
    }

    /**
     * Sets the entity's superpowers to the given ones
     *
     * @param entity   {@link LivingEntity} receiving the {@link Power}s
     * @param powerIds {@link List} of IDs of the {@link Power}s being given to the {@link LivingEntity}
     */
    public static void setSuperpowerIds(LivingEntity entity, List<ResourceLocation> powerIds) {
        PalladiumProperties.SUPERPOWER_IDS.set(entity, powerIds);
    }

    /**
     * Sets the entity's superpowers to the given ones
     *
     * @param entity {@link LivingEntity} receiving the {@link Power}s
     * @param powers {@link List} of the {@link Power}s being given to the {@link LivingEntity}
     */
    public static void setSuperpowers(LivingEntity entity, List<Power> powers) {
        setSuperpowerIds(entity, powers.stream().map(Power::getId).collect(Collectors.toList()));
    }

    /**
     * Sets the entity's superpowers to the given ones
     *
     * @param entity   {@link LivingEntity} receiving the {@link Power}s
     * @param powerIds Array of IDs of the {@link Power}s being given to the {@link LivingEntity}
     */
    public static void setSuperpowerIds(LivingEntity entity, ResourceLocation... powerIds) {
        PalladiumProperties.SUPERPOWER_IDS.set(entity, Arrays.asList(powerIds));
    }

    /**
     * Sets the entity's superpowers to the given ones
     *
     * @param entity {@link LivingEntity} receiving the {@link Power}s
     * @param powers Array of the {@link Power}s being given to the {@link LivingEntity}
     */
    public static void setSuperpower(LivingEntity entity, Power... powers) {
        setSuperpowerIds(entity, Arrays.stream(powers).map(Power::getId).collect(Collectors.toList()));
    }

    /**
     * Tests if the entity has the given superpower
     *
     * @param entity  The {@link LivingEntity} being tested for the superpower
     * @param powerId ID of the {@link Power} that is being checked for
     * @return true if the entity has the superpower
     */
    public static boolean hasSuperpower(LivingEntity entity, ResourceLocation powerId) {
        return PalladiumProperties.SUPERPOWER_IDS.get(entity).contains(powerId);
    }

    /**
     * Tests if the entity has the given superpower
     *
     * @param entity The {@link LivingEntity} being tested for the superpower
     * @param power  {@link Power} that is being checked for
     * @return true if the entity has the superpower
     */
    public static boolean hasSuperpower(LivingEntity entity, Power power) {
        return hasSuperpower(entity, power.getId());
    }

    /**
     * Adds a superpower to the entity
     *
     * @param entity  The {@link LivingEntity} receiving the superpower
     * @param powerId ID of the {@link Power} being given to the {@link LivingEntity}
     * @return true if the {@link Power} exists and wasn't already given to the {@link LivingEntity}
     */
    public static boolean addSuperpower(LivingEntity entity, ResourceLocation powerId) {
        PowerManager powerManager = PowerManager.getInstance(entity.level);

        if (powerManager.getPower(powerId) == null || hasSuperpower(entity, powerId)) {
            return false;
        }

        List<ResourceLocation> powerIds = new ArrayList<>(PalladiumProperties.SUPERPOWER_IDS.get(entity));
        powerIds.add(powerId);
        PalladiumProperties.SUPERPOWER_IDS.set(entity, powerIds);
        return true;
    }

    /**
     * Adds a superpower to the entity
     *
     * @param entity The {@link LivingEntity} receiving the superpower
     * @param power  {@link Power} being given to the {@link LivingEntity}
     * @return true if the {@link Power} exists and wasn't already given to the {@link LivingEntity}
     */
    public static boolean addSuperpower(LivingEntity entity, Power power) {
        return addSuperpower(entity, power.getId());
    }

    /**
     * Removes a superpower to the entity
     *
     * @param entity  The {@link LivingEntity} having the superpower removed
     * @param powerId ID of the {@link Power} being removed from the {@link LivingEntity}
     * @return true if the {@link Power} exists and was already given to the {@link LivingEntity}
     */
    public static boolean removeSuperpower(LivingEntity entity, ResourceLocation powerId) {
        PowerManager powerManager = PowerManager.getInstance(entity.level);

        if (powerManager.getPower(powerId) == null || !hasSuperpower(entity, powerId)) {
            return false;
        }

        List<ResourceLocation> powerIds = new ArrayList<>(PalladiumProperties.SUPERPOWER_IDS.get(entity));
        powerIds.remove(powerId);
        PalladiumProperties.SUPERPOWER_IDS.set(entity, powerIds);
        return true;
    }

    /**
     * Removes a superpower to the entity
     *
     * @param entity The {@link LivingEntity} having the superpower removed
     * @param power  {@link Power} being removed from the {@link LivingEntity}
     * @return true if the {@link Power} exists and was already given to the {@link LivingEntity}
     */
    public static boolean removeSuperpower(LivingEntity entity, Power power) {
        return removeSuperpower(entity, power.getId());
    }

    /**
     * Removes all superpowers from an entity
     *
     * @param entity {@link LivingEntity} having all superpowers removed
     */
    public static void removeAllSuperpowers(LivingEntity entity) {
        PalladiumProperties.SUPERPOWER_IDS.set(entity, Collections.emptyList());
    }

}
