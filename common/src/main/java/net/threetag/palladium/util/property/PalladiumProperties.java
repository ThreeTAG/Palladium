package net.threetag.palladium.util.property;

import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.event.PalladiumEvents;

import java.util.ArrayList;
import java.util.List;

public class PalladiumProperties {

    public static final PalladiumProperty<List<ResourceLocation>> SUPERPOWER_IDS = new ResourceLocationListProperty("superpowers");
    public static final PalladiumProperty<Boolean> JUMP_KEY_DOWN = new BooleanProperty("jump_key_down").sync(SyncType.SELF);
    public static final PalladiumProperty<Boolean> LEFT_KEY_DOWN = new BooleanProperty("left_key_down").sync(SyncType.SELF);
    public static final PalladiumProperty<Boolean> RIGHT_KEY_DOWN = new BooleanProperty("right_key_down").sync(SyncType.SELF);
    public static final PalladiumProperty<Boolean> FORWARD_KEY_DOWN = new BooleanProperty("forward_key_down").sync(SyncType.SELF);
    public static final PalladiumProperty<Boolean> BACKWARDS_KEY_DOWN = new BooleanProperty("backwards_key_down").sync(SyncType.SELF);

    public static void init() {
        PalladiumEvents.REGISTER_PROPERTY.register(handler -> {
            handler.register(SUPERPOWER_IDS, new ArrayList<>());
            handler.register(JUMP_KEY_DOWN, false);
            handler.register(LEFT_KEY_DOWN, false);
            handler.register(RIGHT_KEY_DOWN, false);
            handler.register(FORWARD_KEY_DOWN, false);
            handler.register(BACKWARDS_KEY_DOWN, false);
        });
    }
}
