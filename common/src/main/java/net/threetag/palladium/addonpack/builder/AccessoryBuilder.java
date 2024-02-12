package net.threetag.palladium.addonpack.builder;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.accessory.Accessory;
import net.threetag.palladium.accessory.AccessorySlot;
import net.threetag.palladium.accessory.DefaultAccessory;
import net.threetag.palladium.addonpack.parser.AccessoryParser;

import java.util.ArrayList;
import java.util.List;

public class AccessoryBuilder extends AddonBuilder<Accessory, AccessoryBuilder> {

    private final JsonObject json;
    private AccessoryParser.TypeSerializer typeSerializer = null;
    private List<AccessorySlot> slots;

    public AccessoryBuilder(ResourceLocation id, JsonObject json) {
        super(id);
        this.json = json;
    }

    @Override
    protected Accessory create() {
        var accessory = this.typeSerializer != null ? this.typeSerializer.parse(this.json) : new DefaultAccessory();

        accessory.slot(this.getValue(b -> b.slots, new ArrayList<AccessorySlot>()).toArray(new AccessorySlot[0]));

        return accessory;
    }

    public AccessoryBuilder type(AccessoryParser.TypeSerializer serializer) {
        this.typeSerializer = serializer;
        return this;
    }

    public AccessoryBuilder addSlot(AccessorySlot slot) {
        if (this.slots == null) {
            this.slots = new ArrayList<>();
        }

        if (slot != null && !this.slots.contains(slot)) {
            this.slots.add(slot);
        }
        return this;
    }
}
