package net.threetag.palladium.util.property;

import net.minecraft.core.Registry;
import net.minecraft.world.entity.ai.attributes.Attribute;

public class AttributeProperty extends RegistryObjectProperty<Attribute> {

    public AttributeProperty(String key) {
        super(key, Registry.ATTRIBUTE);
    }

    @Override
    public String getPropertyType() {
        return "attribute";
    }
}
