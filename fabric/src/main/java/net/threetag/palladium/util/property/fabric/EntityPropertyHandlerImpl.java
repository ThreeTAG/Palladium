package net.threetag.palladium.util.property.fabric;

import net.minecraft.world.entity.Entity;
import net.threetag.palladium.components.fabric.PalladiumComponents;
import net.threetag.palladium.util.property.EntityPropertyHandler;

import java.util.Optional;

public class EntityPropertyHandlerImpl {

    public static Optional<EntityPropertyHandler> getHandler(Entity entity) {
        try {
            return Optional.of(PalladiumComponents.ENTITY_PROPERTIES.get(entity));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

}
