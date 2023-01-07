package net.threetag.palladium.network;

import net.threetag.palladium.Palladium;
import net.threetag.palladiumcore.network.MessageType;
import net.threetag.palladiumcore.network.NetworkManager;

public class PalladiumNetwork {

    public static final NetworkManager NETWORK = NetworkManager.create(Palladium.id("main_channel"));

    public static final MessageType SYNC_POWERS = NETWORK.registerS2C("sync_powers", SyncPowersMessage::new);
    public static final MessageType UPDATE_POWERS = NETWORK.registerS2C("update_powers", UpdatePowersMessage::new);
    public static final MessageType SYNC_ABILITY_STATE = NETWORK.registerS2C("sync_ability_state", SyncAbilityStateMessage::new);
    public static final MessageType SYNC_PROPERTY = NETWORK.registerS2C("sync_property", SyncPropertyMessage::new);
    public static final MessageType SYNC_ABILITY_ENTRY_PROPERTY = NETWORK.registerS2C("sync_ability_entry_property", SyncAbilityEntryPropertyMessage::new);
    public static final MessageType ABILITY_KEY_PRESSED = NETWORK.registerC2S("ability_key_pressed", AbilityKeyPressedMessage::new);
    public static final MessageType NOTIFY_JUMP_KEY_LISTENER = NETWORK.registerC2S("notify_jump_key_listener", NotifyJumpKeyListenerMessage::new);
    public static final MessageType SYNC_ACCESSORIES = NETWORK.registerS2C("sync_accessories", SyncAccessoriesMessage::new);
    public static final MessageType TOGGLE_ACCESSORY = NETWORK.registerC2S("toggle_accessory", ToggleAccessoryMessage::new);
    public static final MessageType REQUEST_ABILITY_BUY_SCREEN = NETWORK.registerC2S("request_ability_buy_screen", RequestAbilityBuyScreenMessage::new);
    public static final MessageType OPEN_ABILITY_BUY_SCREEN = NETWORK.registerS2C("open_ability_buy_screen", OpenAbilityBuyScreenMessage::new);
    public static final MessageType BUY_ABILITY_UNLOCK = NETWORK.registerC2S("buy_ability_unlock", BuyAbilityUnlockMessage::new);

    public static void init() {

    }
}
