package net.threetag.palladium.util.property;

import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.event.PalladiumEvents;

public class PalladiumProperties {

    public static final PalladiumProperty<ResourceLocation> SUPERPOWER_ID = new ResourceLocationProperty("superpower");
    public static final PalladiumProperty<Boolean> JUMP_KEY_DOWN = new BooleanProperty("jump_key_down").sync(SyncType.SELF);

    public static void init() {
        PalladiumEvents.REGISTER_PROPERTY.register(handler -> {
            handler.register(SUPERPOWER_ID, null);
            handler.register(JUMP_KEY_DOWN, false);
        });
    }
}
