package net.threetag.palladium.accessory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class DefaultAccessory extends Accessory {

    private final List<AccessorySlot> slots = new ArrayList<>();

    public DefaultAccessory slot(AccessorySlot... slots) {
        Collections.addAll(this.slots, slots);
        return this;
    }

    @Override
    public Collection<AccessorySlot> getPossibleSlots() {
        return this.slots;
    }
}
