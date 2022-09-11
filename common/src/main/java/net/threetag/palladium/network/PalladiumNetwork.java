package net.threetag.palladium.network;

import net.threetag.palladium.Palladium;
import net.threetag.palladium.network.messages.*;

public class PalladiumNetwork {

    public static final NetworkManager NETWORK = NetworkManager.create(Palladium.id("main_channel"));

    public static final MessageType SYNC_POWERS = NETWORK.registerS2C("sync_powers", SyncPowersMessage::new);
    public static final MessageType ADD_POWER = NETWORK.registerS2C("add_power", AddPowerMessage::new);
    public static final MessageType REMOVE_POWER = NETWORK.registerS2C("remove_power", RemovePowerMessage::new);
    public static final MessageType SYNC_ABILITY_STATE = NETWORK.registerS2C("sync_ability_state", SyncAbilityStateMessage::new);
    public static final MessageType SYNC_PROPERTY = NETWORK.registerS2C("sync_property", SyncPropertyMessage::new);
    public static final MessageType SYNC_ABILITY_ENTRY_PROPERTY = NETWORK.registerS2C("sync_ability_entry_property", SyncAbilityEntryPropertyMessage::new);
    public static final MessageType ABILITY_KEY_PRESSED = NETWORK.registerC2S("ability_key_pressed", AbilityKeyPressedMessage::new);
    public static final MessageType NOTIFY_JUMP_KEY_LISTENER = NETWORK.registerC2S("notify_jump_key_listener", NotifyJumpKeyListenerMessage::new);
    public static final MessageType SYNC_ACCESSORIES = NETWORK.registerS2C("sync_accessories", SyncAccessoriesMessage::new);
    public static final MessageType TOGGLE_ACCESSORY = NETWORK.registerC2S("toggle_accessory", ToggleAccessoryMessage::new);
    public static void init() {

    }
}
