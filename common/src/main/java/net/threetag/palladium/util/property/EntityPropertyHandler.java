package net.threetag.palladium.util.property;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.threetag.palladium.event.PalladiumEvents;
import net.threetag.palladium.network.SyncPropertyMessage;
import net.threetag.palladiumcore.event.PlayerEvents;

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
                new SyncPropertyMessage(this.entity.getId(), property, newValue).sendToDimension(this.entity.level);
            } else if (property.getSyncType() == SyncType.SELF && this.entity instanceof ServerPlayer serverPlayer) {
                new SyncPropertyMessage(this.entity.getId(), property, newValue).send(serverPlayer);
            }
        }
    }

    public static void init() {
        PlayerEvents.JOIN.register(player -> {
            getHandler(player).values().forEach((palladiumProperty, o) -> {
                new SyncPropertyMessage(player.getId(), palladiumProperty, o).sendToDimension(player.level);
            });
        });

        PlayerEvents.START_TRACKING.register((tracker, target) -> {
            if (tracker instanceof ServerPlayer serverPlayer) {
                getHandler(target).values().forEach((palladiumProperty, o) -> new SyncPropertyMessage(target.getId(), palladiumProperty, o).send(serverPlayer));
            }
        });
    }
}
