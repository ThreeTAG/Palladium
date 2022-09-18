package net.threetag.palladium.event;

import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.client.model.animation.Animation;
import net.threetag.palladiumcore.event.Event;

import java.util.function.BiConsumer;

public interface PalladiumClientEvents {

    Event<RegisterAnimations> REGISTER_ANIMATIONS = new Event<>(RegisterAnimations.class, listeners -> (r) -> {
        for (RegisterAnimations listener : listeners) {
            listener.register(r);
        }
    });

    interface RegisterAnimations {

        void register(BiConsumer<ResourceLocation, Animation> registry);

    }

}
