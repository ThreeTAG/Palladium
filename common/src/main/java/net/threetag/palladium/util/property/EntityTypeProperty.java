package net.threetag.palladium.util.property;

import net.minecraft.core.Registry;
import net.minecraft.world.entity.EntityType;

public class EntityTypeProperty extends RegistryObjectProperty<EntityType<?>> {

    public EntityTypeProperty(String key) {
        super(key, Registry.ENTITY_TYPE);
    }

    @Override
    public String getPropertyType() {
        return "entity_type";
    }
}
