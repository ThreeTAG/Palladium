package net.threetag.palladium.util.property;

import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.event.PalladiumEvents;

import java.util.ArrayList;
import java.util.List;

public class PalladiumProperties {

    public static final PalladiumProperty<List<ResourceLocation>> SUPERPOWER_IDS = new ResourceLocationListProperty("superpowers");
    public static final PalladiumProperty<Boolean> JUMP_KEY_DOWN = new BooleanProperty("jump_key_down").sync(SyncType.SELF);

    public static void init() {
        PalladiumEvents.REGISTER_PROPERTY.register(handler -> {
            handler.register(SUPERPOWER_IDS, new ArrayList<>());
            handler.register(JUMP_KEY_DOWN, false);
        });
    }
}
