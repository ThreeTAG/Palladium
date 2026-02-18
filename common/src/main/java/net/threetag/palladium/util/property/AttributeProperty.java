package net.threetag.palladium.util.property;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attribute;

public class AttributeProperty extends RegistryObjectProperty<Attribute> {

    public AttributeProperty(String key) {
        super(key, BuiltInRegistries.ATTRIBUTE);
    }

    @Override
    public String getPropertyType() {
        return "attribute";
    }
}
