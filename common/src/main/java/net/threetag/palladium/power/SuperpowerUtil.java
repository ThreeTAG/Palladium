package net.threetag.palladium.power;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.entity.data.PalladiumEntityData;
import net.threetag.palladium.entity.data.PalladiumEntityDataTypes;
import net.threetag.palladium.power.superpower.EntitySuperpowerHandler;

import java.util.Collection;
import java.util.function.Predicate;

public class SuperpowerUtil {

    public static EntitySuperpowerHandler getHandler(LivingEntity entity) {
        return PalladiumEntityData.get(entity, PalladiumEntityDataTypes.SUPERPOWER_HANDLER.get());
    }

    /**
     * Returns the superpowers the entity currently has
     *
     * @param entity {@link LivingEntity} which has superpowers
     * @return {@link Collection} of superpowers of the entity
     */
    public static Collection<Holder<Power>> getSuperpowers(LivingEntity entity) {
        return getHandler(entity).getSuperpowers();
    }

    /**
     * Sets the entity's superpowers to just the given one
     *
     * @param entity {@link LivingEntity} receiving the {@link Power}
     * @param power  The {@link Power} being given to the {@link LivingEntity}
     */
    public static void setSuperpower(LivingEntity entity, Holder<Power> power) {
        var handler = getHandler(entity);
        getHandler(entity).removeAll();
        handler.add(power);
    }

    /**
     * Tests if the entity has the given superpower
     *
     * @param entity The {@link LivingEntity} being tested for the superpower
     * @param power  {@link Power} that is being checked for
     * @return true if the entity has the superpower
     */
    public static boolean hasSuperpower(LivingEntity entity, Holder<Power> power) {
        return getHandler(entity).has(power);
    }

    /**
     * Tests if the superpower can be added to the entity
     *
     * @param entity The {@link LivingEntity} being tested for the superpower
     * @param power  {@link Power} that is being checked for
     * @return true if the superpower can be added
     */
    public static boolean canSuperpowerBeAdded(LivingEntity entity, Holder<Power> power) {
        return getHandler(entity).canBeAdded(power);
    }

    /**
     * Adds a superpower to the entity
     *
     * @param entity The {@link LivingEntity} receiving the superpower
     * @param power  {@link Power} being given to the {@link LivingEntity}
     * @return true if the {@link Power} exists and wasn't already given to the {@link LivingEntity}
     */
    public static boolean addSuperpower(LivingEntity entity, Holder<Power> power) {
        return getHandler(entity).add(power);
    }

    /**
     * Removes a superpower to the entity
     *
     * @param entity The {@link LivingEntity} having the superpower removed
     * @param power  {@link Power} being removed from the {@link LivingEntity}
     * @return true if the {@link Power} exists and was already given to the {@link LivingEntity}
     */
    public static boolean removeSuperpower(LivingEntity entity, Holder<Power> power) {
        return getHandler(entity).remove(power);
    }

    /**
     * Removes a superpower to the entity
     *
     * @param entity The {@link LivingEntity} having the superpower removed
     * @param predicate  The {@link Predicate} to test the {@link Power} against
     * @return true if any power was able to be removed
     */
    public static boolean removeSuperpower(LivingEntity entity, Predicate<Holder<Power>> predicate) {
        return getHandler(entity).remove(predicate);
    }

    /**
     * Removes all superpowers from an entity
     *
     * @param entity {@link LivingEntity} having all superpowers removed
     */
    public static void removeAllSuperpowers(LivingEntity entity) {
        getHandler(entity).removeAll();
    }

}
