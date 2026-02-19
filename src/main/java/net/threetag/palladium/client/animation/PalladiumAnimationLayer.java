package net.threetag.palladium.client.animation;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

public enum PalladiumAnimationLayer implements StringRepresentable {

    IDLE("idle"),
    MOVEMENT("movement"),
    FLIGHT("flight"),
    SPECIAL("special");

    public static final Codec<PalladiumAnimationLayer> CODEC = StringRepresentable.fromEnum(PalladiumAnimationLayer::values);

    private final String name;

    PalladiumAnimationLayer(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
