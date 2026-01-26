package net.threetag.palladium.feature;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.addonpack.log.AddonPackLog;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class PalladiumFeatureFlags {

    private static final Set<Type> ENABLED_FLAGS = new HashSet<>();
    public static final ResourceLocation DATA_CONDITION_ID = Palladium.id("feature_flag_enabled");

    public static void enable(String name) {
        if (name.equalsIgnoreCase("*")) {
            ENABLED_FLAGS.addAll(Arrays.stream(Type.values()).toList());
        }

        for (Type type : Type.values()) {
            if (type.getSerializedName().equalsIgnoreCase(name)) {
                enable(type);
            }
        }
    }

    public static void enable(Type type) {
        if (!ENABLED_FLAGS.contains(type)) {
            ENABLED_FLAGS.add(type);
            AddonPackLog.info("Enabled Feature Flag for " + type.getSerializedName());
        }
    }

    public static boolean isEnabled(Type type) {
        return ENABLED_FLAGS.contains(type);
    }

    @Nullable
    public static Type getFeatureFlag(String name) {
        for (Type type : Type.values()) {
            if (type.getSerializedName().equalsIgnoreCase(name))
                return type;
        }
        return null;
    }

    public enum Type implements StringRepresentable {

        RUINED_MULTIVERSE_PORTALS("worldgen/ruined_multiverse_portals");

        private final String name;

        Type(String name) {
            this.name = name;
        }

        @Override
        public @NotNull String getSerializedName() {
            return this.name;
        }
    }

}
