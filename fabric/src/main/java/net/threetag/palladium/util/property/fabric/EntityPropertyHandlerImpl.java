package net.threetag.palladium.util.property.fabric;

import net.minecraft.world.entity.Entity;
import net.threetag.palladium.components.fabric.PalladiumComponents;
import net.threetag.palladium.util.property.EntityPropertyHandler;

public class EntityPropertyHandlerImpl {

    public static EntityPropertyHandler getHandler(Entity entity) {
        return PalladiumComponents.ENTITY_PROPERTIES.get(entity);
    }

}
