package net.threetag.palladium.util.property;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.threetag.palladium.event.PalladiumEvents;
import net.threetag.palladium.network.SyncPropertyMessage;

public class EntityPropertyHandler extends PropertyManager implements PropertyManager.Listener {

    final Entity entity;

    public EntityPropertyHandler(Entity entity) {
        this.entity = entity;
        this.setListener(this);
        PalladiumEvents.REGISTER_PROPERTY.invoker().register(this);
    }

    @ExpectPlatform
    public static EntityPropertyHandler getHandler(Entity entity) {
        throw new AssertionError();
    }

    @Override
    public <T> void onChanged(PalladiumProperty<T> property, T oldValue, T newValue) {
        if (!entity.level.isClientSide) {
            new SyncPropertyMessage(this.entity.getId(), property, newValue).sendToLevel((ServerLevel) this.entity.level);
        }
    }
}
