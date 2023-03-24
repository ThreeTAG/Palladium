package net.threetag.palladium.util.property;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.threetag.palladium.event.PalladiumEvents;
import net.threetag.palladium.network.SyncPropertyMessage;
import net.threetag.palladiumcore.event.PlayerEvents;

import java.util.Optional;

public class EntityPropertyHandler extends PropertyManager implements PropertyManager.Listener {

    final Entity entity;

    public EntityPropertyHandler(Entity entity) {
        this.entity = entity;
        this.setListener(this);
        PalladiumEvents.REGISTER_PROPERTY.invoker().register(this);
    }

    public Entity getEntity() {
        return entity;
    }

    @ExpectPlatform
    public static Optional<EntityPropertyHandler> getHandler(Entity entity) {
        throw new AssertionError();
    }

    @Override
    public <T> void onChanged(PalladiumProperty<T> property, T oldValue, T newValue) {
        if (!entity.level.isClientSide) {
            if (property.getSyncType() == SyncType.EVERYONE) {
                new SyncPropertyMessage(this.entity.getId(), property, newValue).sendToDimension(this.entity.level);
            } else if (property.getSyncType() == SyncType.SELF && this.entity instanceof ServerPlayer serverPlayer) {
                new SyncPropertyMessage(this.entity.getId(), property, newValue).send(serverPlayer);
            }
        }
    }

    public static void init() {
        // TODO make use of DataSyncUtil
        PlayerEvents.JOIN.register(player -> {
            if (player instanceof ServerPlayer serverPlayer) {
                getHandler(player).ifPresent(handler -> {
                    handler.values().forEach((palladiumProperty, o) -> {
                        new SyncPropertyMessage(player.getId(), palladiumProperty, o).send(serverPlayer);
                    });
                });
            }
        });

        PlayerEvents.START_TRACKING.register((tracker, target) -> {
            if (tracker instanceof ServerPlayer serverPlayer) {
                getHandler(target).ifPresent(handler -> {
                    handler.values().forEach((palladiumProperty, o) -> new SyncPropertyMessage(target.getId(), palladiumProperty, o).send(serverPlayer));
                });
            }
        });

        PlayerEvents.RESPAWN.register((player, endConquered) -> {
            if (player instanceof ServerPlayer serverPlayer) {
                getHandler(player).ifPresent(handler -> {
                    handler.values().forEach((palladiumProperty, o) -> new SyncPropertyMessage(player.getId(), palladiumProperty, o).sendToTrackingAndSelf(serverPlayer));
                });
            }
        });

        PlayerEvents.CHANGED_DIMENSION.register((player, destination) -> {
            if (player instanceof ServerPlayer serverPlayer) {
                getHandler(player).ifPresent(handler -> {
                    handler.values().forEach((palladiumProperty, o) -> new SyncPropertyMessage(player.getId(), palladiumProperty, o).sendToTrackingAndSelf(serverPlayer));
                });
            }
        });
    }
}
