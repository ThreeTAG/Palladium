package net.threetag.palladium.core.event;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.concurrent.atomic.AtomicReference;

public interface PalladiumPlayerEvents {

    /**
     * Fired when an Entity is started to be "tracked" by this player, usually when an entity enters a player's view distance.
     */
    Event<Tracking> START_TRACKING = EventFactory.createLoop();

    /**
     * Fired when an Entity is stopped to be "tracked" by this player, usually the client was sent a packet to destroy the entity at this point
     */
    Event<Tracking> STOP_TRACKING = EventFactory.createLoop();

    /**
     * @see NameFormat#playerNameFormat(Player, Component, AtomicReference)
     */
    Event<NameFormat> NAME_FORMAT = EventFactory.createLoop();

    @FunctionalInterface
    interface Tracking {

        /**
         * @param tracker       Player that is tracking the given entity
         * @param trackedEntity Entity being tracked
         */
        void playerTracking(Player tracker, Entity trackedEntity);

    }

    @FunctionalInterface
    interface NameFormat {

        /**
         * Used for changing a player's display name. Use {@link net.threetag.palladium.core.entity.PlayerNameChange#refreshDisplayName(Player)} to refresh the player's name upon change
         *
         * @param player      The player whose name is changed
         * @param username    Username of the player
         * @param displayName Current display name of the player
         */
        void playerNameFormat(Player player, Component username, AtomicReference<Component> displayName);

    }

}
