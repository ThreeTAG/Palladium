package net.threetag.palladium.util.property;

import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.threetag.palladium.event.PalladiumEvents;
import net.threetag.palladium.network.messages.SyncPropertyMessage;

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
    public static EntityPropertyHandler getHandler(Entity entity) {
        throw new AssertionError();
    }

    @Override
    public <T> void onChanged(PalladiumProperty<T> property, T oldValue, T newValue) {
        if (!entity.level.isClientSide) {
            if (property.getSyncType() == SyncType.EVERYONE) {
                new SyncPropertyMessage(this.entity.getId(), property, newValue).sendToLevel((ServerLevel) this.entity.level);
            } else if (property.getSyncType() == SyncType.SELF && this.entity instanceof ServerPlayer serverPlayer) {
                new SyncPropertyMessage(this.entity.getId(), property, newValue).sendTo(serverPlayer);
            }
        }
    }

    public static void init() {
        PlayerEvent.PLAYER_JOIN.register(player -> {
            getHandler(player).values().forEach((palladiumProperty, o) -> {
                new SyncPropertyMessage(player.getId(), palladiumProperty, o).sendToLevel((ServerLevel) player.level);
            });
        });

        PalladiumEvents.START_TRACKING.register((tracker, target) -> {
            if (tracker instanceof ServerPlayer serverPlayer) {
                getHandler(target).values().forEach((palladiumProperty, o) -> new SyncPropertyMessage(target.getId(), palladiumProperty, o).sendTo(serverPlayer));
            }
        });
    }
}
