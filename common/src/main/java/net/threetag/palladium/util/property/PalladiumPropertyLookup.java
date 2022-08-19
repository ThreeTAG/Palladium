package net.threetag.palladium.util.property;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class PalladiumPropertyLookup {

    private static final Map<String, Function<String, PalladiumProperty<?>>> PROPERTIES = new HashMap<>();

    static {
        register("integer", IntegerProperty::new);
        register("float", FloatProperty::new);
        register("double", DoubleProperty::new);
        register("boolean", BooleanProperty::new);
        register("string", StringProperty::new);
        register("resource_location", ResourceLocationProperty::new);
        register("color", ColorProperty::new);
        register("attribute", AttributeProperty::new);
        register("component", ComponentProperty::new);
        register("compound_tag", CompoundTagProperty::new);
        register("enchantment", EnchantmentProperty::new);
        register("entity_type", EntityTypeProperty::new);
        register("icon", IconProperty::new);
        register("item", ItemProperty::new);
        register("mob_effect", MobEffectProperty::new);
        register("pose", PoseProperty::new);
        register("sound_event", SoundEventProperty::new);
        register("string_array", StringArrayProperty::new);
        register("suit_set", SuitSetProperty::new);
        register("uuid", UUIDProperty::new);
    }

    public static void register(String typeName, Function<String, PalladiumProperty<?>> function) {
        PROPERTIES.put(typeName, function);
    }

    public static PalladiumProperty<?> get(String type, String key) {
        return PROPERTIES.containsKey(type) ? PROPERTIES.get(type).apply(key) : null;
    }

}
