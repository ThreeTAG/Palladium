package net.threetag.palladium.core.event;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.jetbrains.annotations.Nullable;

public interface PalladiumLifecycleEvents {

    /**
     * Called once during startup.
     * Invoked during FMLCommonSetupEvent on NeoForge and client/server entrypoint on Fabric
     */
    PalladiumEvent<Runnable> SETUP = new PalladiumEvent<>(Runnable.class, listeners -> () -> {
        for (Runnable listener : listeners) {
            listener.run();
        }
    });

    /**
     * Called once during client startup.
     * Invoked during FMLClientSetupEvent on NeoForge and client entrypoint on Fabric
     */
    PalladiumEvent<Runnable> CLIENT_SETUP = new PalladiumEvent<>(Runnable.class, listeners -> () -> {
        for (Runnable listener : listeners) {
            listener.run();
        }
    });

    /**
     * @see DataPackSync#onDataPackSync(PlayerList, ServerPlayer)
     */
    PalladiumEvent<DataPackSync> DATA_PACK_SYNC = new PalladiumEvent<>(DataPackSync.class, listeners -> (l, p) -> {
        for (DataPackSync listener : listeners) {
            listener.onDataPackSync(l, p);
        }
    });

    @FunctionalInterface
    interface DataPackSync {

        /**
         * Fires when a player joins the server or when the reload command is ran, before tags and crafting recipes are sent to the client. Send datapack data to clients when this event fires.
         *
         * @param playerList List of players
         * @param player     Player that the data is synced to. Null if reload was run
         */
        void onDataPackSync(PlayerList playerList, @Nullable ServerPlayer player);

    }

}
