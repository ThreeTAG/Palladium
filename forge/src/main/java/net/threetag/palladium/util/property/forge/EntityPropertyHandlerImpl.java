package net.threetag.palladium.util.property.forge;

import net.minecraft.world.entity.Entity;
import net.threetag.palladium.forge.capability.PalladiumCapabilities;
import net.threetag.palladium.util.property.EntityPropertyHandler;

public class EntityPropertyHandlerImpl {

    public static EntityPropertyHandler getHandler(Entity entity) {
        return entity.getCapability(PalladiumCapabilities.ENTITY_PROPERTIES).orElseThrow(() -> new RuntimeException("Entity does not have property capability!"));
    }

}
