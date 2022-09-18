package net.threetag.palladium.event;

import net.threetag.palladium.documentation.HTMLBuilder;
import net.threetag.palladium.util.property.EntityPropertyHandler;
import net.threetag.palladiumcore.event.Event;

import java.util.function.Consumer;

public interface PalladiumEvents {

    Event<RegisterProperty> REGISTER_PROPERTY = new Event<>(RegisterProperty.class, listeners -> (h) -> {
        for (RegisterProperty listener : listeners) {
            listener.register(h);
        }
    });

    Event<GenerateDocumentation> GENERATE_DOCUMENTATION = new Event<>(GenerateDocumentation.class, listeners -> (c) -> {
        for (GenerateDocumentation listener : listeners) {
            listener.generate(c);
        }
    });

    interface RegisterProperty {

        void register(EntityPropertyHandler handler);

    }

    interface GenerateDocumentation {

        void generate(Consumer<HTMLBuilder> consumer);

    }

}
