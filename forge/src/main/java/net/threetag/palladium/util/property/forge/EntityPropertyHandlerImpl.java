package net.threetag.palladium.util.property.forge;

import net.minecraft.world.entity.Entity;
import net.threetag.palladium.capability.forge.PalladiumCapabilities;
import net.threetag.palladium.util.property.EntityPropertyHandler;

import java.util.Optional;

public class EntityPropertyHandlerImpl {

    public static Optional<EntityPropertyHandler> getHandler(Entity entity) {
        return entity.getCapability(PalladiumCapabilities.ENTITY_PROPERTIES).resolve();
    }

}
