package net.threetag.palladium.util.property;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;

public class EntityTypeProperty extends RegistryObjectProperty<EntityType<?>> {

    public EntityTypeProperty(String key) {
        super(key, BuiltInRegistries.ENTITY_TYPE);
    }

    @Override
    public String getPropertyType() {
        return "entity_type";
    }
}
