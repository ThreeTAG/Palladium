package net.threetag.palladium.network;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.accessory.Accessory;
import net.threetag.palladium.power.PowerManager;
import net.threetag.palladium.power.ability.AbilityEntry;
import net.threetag.palladium.power.ability.AbilityUtil;
import net.threetag.palladium.util.property.EntityPropertyHandler;
import net.threetag.palladiumcore.network.MessageType;
import net.threetag.palladiumcore.network.NetworkManager;
import net.threetag.palladiumcore.util.DataSyncUtil;

import java.util.Collections;

public class PalladiumNetwork {

    public static final NetworkManager NETWORK = NetworkManager.create(Palladium.id("main_channel"));

    public static final MessageType SYNC_POWERS = NETWORK.registerS2C("sync_powers", SyncPowersMessage::new);
    public static final MessageType UPDATE_POWERS = NETWORK.registerS2C("update_powers", UpdatePowersMessage::new);
    public static final MessageType SYNC_ABILITY_STATE = NETWORK.registerS2C("sync_ability_state", SyncAbilityStateMessage::new);
    public static final MessageType SYNC_PROPERTY = NETWORK.registerS2C("sync_property", SyncPropertyMessage::new);
    public static final MessageType SYNC_ABILITY_ENTRY_PROPERTY = NETWORK.registerS2C("sync_ability_entry_property", SyncAbilityEntryPropertyMessage::new);
    public static final MessageType ABILITY_KEY_PRESSED = NETWORK.registerC2S("ability_key_pressed", AbilityKeyPressedMessage::new);
    public static final MessageType NOTIFY_MOVEMENT_KEY_LISTENER = NETWORK.registerC2S("notify_jump_key_listener", NotifyMovementKeyListenerMessage::new);
    public static final MessageType SYNC_ACCESSORIES = NETWORK.registerS2C("sync_accessories", SyncAccessoriesMessage::new);
    public static final MessageType TOGGLE_ACCESSORY = NETWORK.registerC2S("toggle_accessory", ToggleAccessoryMessage::new);
    public static final MessageType REQUEST_ABILITY_BUY_SCREEN = NETWORK.registerC2S("request_ability_buy_screen", RequestAbilityBuyScreenMessage::new);
    public static final MessageType OPEN_ABILITY_BUY_SCREEN = NETWORK.registerS2C("open_ability_buy_screen", OpenAbilityBuyScreenMessage::new);
    public static final MessageType BUY_ABILITY_UNLOCK = NETWORK.registerC2S("buy_ability_unlock", BuyAbilityUnlockMessage::new);
    public static final MessageType SET_FLYING_STATE = NETWORK.registerC2S("set_flying_state", SetFlyingStateMessage::new);
    public static final MessageType TOGGLE_OPENABLE_EQUIPMENT = NETWORK.registerC2S("toggle_openable_equipment", ToggleOpenableEquipmentMessage::new);

    public static void init() {
        // Powers
        DataSyncUtil.registerEntitySync((entity, consumer) -> {
            if (entity instanceof LivingEntity livingEntity) {
                var opt = PowerManager.getPowerHandler(livingEntity);

                if (opt.isPresent()) {
                    var handler = opt.get();
                    consumer.accept(new UpdatePowersMessage(livingEntity.getId(), Collections.emptyList(), handler.getPowerHolders().values().stream().map(h -> h.getPower().getId()).toList()));

                    for (AbilityEntry entry : AbilityUtil.getEntries(livingEntity)) {
                        consumer.accept(entry.getSyncStateMessage(livingEntity));
                    }
                }
            }
        });

        // Accessories
        DataSyncUtil.registerEntitySync((entity, consumer) -> {
            if (entity instanceof ServerPlayer serverPlayer) {
                var opt = Accessory.getPlayerData(serverPlayer);
                opt.ifPresent(accessoryPlayerData -> consumer.accept(new SyncAccessoriesMessage(serverPlayer.getId(), accessoryPlayerData.accessories)));
            }
        });

        // Properties
        DataSyncUtil.registerEntitySync((entity, consumer) -> {
            EntityPropertyHandler.getHandler(entity).ifPresent(properties -> {
                properties.values().forEach((palladiumProperty, o) -> {
                    consumer.accept(new SyncPropertyMessage(entity.getId(), palladiumProperty, o));
                });
            });
        });
    }
}
