package net.threetag.palladium.network;

import dev.architectury.networking.simple.MessageType;
import dev.architectury.networking.simple.SimpleNetworkManager;
import net.threetag.palladium.Palladium;

public class PalladiumNetwork {

    public static final SimpleNetworkManager NETWORK = SimpleNetworkManager.create(Palladium.MOD_ID);

    public static final MessageType SYNC_POWERS = NETWORK.registerS2C("sync_powers", SyncPowersMessage::new);
    public static final MessageType SET_POWER = NETWORK.registerS2C("set_power", SetPowerMessage::new);
    public static final MessageType SYNC_ABILITY_STATE = NETWORK.registerS2C("sync_ability_state", SyncAbilityStateMessage::new);
    public static final MessageType SYNC_PROPERTY = NETWORK.registerS2C("sync_property", SyncPropertyMessage::new);
    public static final MessageType ABILITY_KEY_PRESSED = NETWORK.registerC2S("ability_key_pressed", AbilityKeyPressedMessage::new);

    public static void init() {

    }
}
