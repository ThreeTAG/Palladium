package net.threetag.palladium.network;

import dev.architectury.networking.simple.MessageType;
import dev.architectury.networking.simple.SimpleNetworkManager;
import net.threetag.palladium.Palladium;

public class PalladiumNetwork {

    public static final SimpleNetworkManager NETWORK = SimpleNetworkManager.create(Palladium.MOD_ID);

    public static final MessageType SYNC_ABILITY_STATE = NETWORK.registerS2C("sync_ability_state", SyncAbilityStateMessage::new);

    public static void init() {

    }
}
