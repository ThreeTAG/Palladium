package net.threetag.palladium.accessory;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.addonpack.parser.AccessoryParser;
import net.threetag.palladium.documentation.JsonDocumentationBuilder;

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

    public static class Serializer implements AccessoryParser.TypeSerializer {

        @Override
        public DefaultAccessory parse(JsonObject json) {
            return new DefaultAccessory();
        }

        @Override
        public void generateDocumentation(JsonDocumentationBuilder builder) {
            builder.setTitle("Dummy");
            builder.setDescription("Does nothing.");
            AccessoryParser.addSlotDocumentation(builder);
        }

        @Override
        public ResourceLocation getId() {
            return Palladium.id("dummy");
        }
    }
}
