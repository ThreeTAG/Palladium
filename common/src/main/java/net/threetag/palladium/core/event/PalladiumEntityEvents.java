package net.threetag.palladium.core.event;

import net.minecraft.world.entity.Entity;

public interface PalladiumEntityEvents {

    /**
     * @see Tick#entityTick(Entity)
     */
    PalladiumEvent<Tick> TICK_PRE = new PalladiumEvent<>(Tick.class, listeners -> (e) -> {
        for (Tick listener : listeners) {
            listener.entityTick(e);
        }
    });

    /**
     * @see Tick#entityTick(Entity)
     */
    PalladiumEvent<Tick> TICK_POST = new PalladiumEvent<>(Tick.class, listeners -> (e) -> {
        for (Tick listener : listeners) {
            listener.entityTick(e);
        }
    });

    @FunctionalInterface
    interface Tick {

        /**
         * Called during every tick of an entity, duh
         *
         * @param entity The entity.
         */
        void entityTick(Entity entity);

    }

}
