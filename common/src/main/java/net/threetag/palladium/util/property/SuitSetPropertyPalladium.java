package net.threetag.palladium.util.property;

import net.threetag.palladium.item.SuitSet;

public class SuitSetPropertyPalladium extends PalladiumRegistryObjectProperty<SuitSet> {

    public SuitSetPropertyPalladium(String key) {
        super(key, SuitSet.REGISTRY);
    }

    @Override
    public String getPropertyType() {
        return "suit_set";
    }

}
