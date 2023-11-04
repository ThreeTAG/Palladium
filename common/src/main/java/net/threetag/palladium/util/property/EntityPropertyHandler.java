package net.threetag.palladium.util.property;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.threetag.palladium.entity.PalladiumEntityExtension;
import net.threetag.palladium.event.PalladiumEvents;
import net.threetag.palladium.network.SyncPropertyMessage;

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

    public static Optional<EntityPropertyHandler> getHandler(Entity entity) {
        if (entity instanceof PalladiumEntityExtension ext) {
            return Optional.of(ext.palladium$getPropertyHandler());
        } else {
            return Optional.empty();
        }
    }

    @Override
    public <T> void onChanged(PalladiumProperty<T> property, T oldValue, T newValue) {
        if (!entity.level.isClientSide) {
            if(!(property instanceof BooleanProperty) || oldValue != newValue) {
                if (property.getSyncType() == SyncType.EVERYONE) {
                    new SyncPropertyMessage(this.entity.getId(), property, newValue).sendToDimension(this.entity.level);
                } else if (property.getSyncType() == SyncType.SELF && this.entity instanceof ServerPlayer serverPlayer) {
                    new SyncPropertyMessage(this.entity.getId(), property, newValue).send(serverPlayer);
                }
            }
        }
    }
}
