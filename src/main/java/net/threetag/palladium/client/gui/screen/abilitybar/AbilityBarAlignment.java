package net.threetag.palladium.client.gui.screen.abilitybar;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

public enum AbilityBarAlignment implements StringRepresentable {

    TOP_LEFT("top_left", true, true),
    TOP_RIGHT("top_right", false, true),
    BOTTOM_LEFT("bottom_left", true, false),
    BOTTOM_RIGHT("bottom_right", false, false);

    public static final Codec<AbilityBarAlignment> CODEC = StringRepresentable.fromEnum(AbilityBarAlignment::values);

    private final String name;
    private final boolean left, top;

    AbilityBarAlignment(String name, boolean left, boolean top) {
        this.name = name;
        this.left = left;
        this.top = top;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }

    public boolean isLeft() {
        return left;
    }

    public boolean isRight() {
        return !left;
    }

    public boolean isTop() {
        return top;
    }

    public boolean isBottom() {
        return !top;
    }
}
