package com.threetag.threecore.karma;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public enum KarmaClass {

    TYRANT(Integer.MIN_VALUE, -1000, false, "tyrant"),
    VILLAIN(-999, -500, false, "villain"),
    THUG(-499, -100, false, "thug"),
    NEUTRAL(-99, 99, true, "neutral"),
    VIGILANTE(100, 499, true, "vigilante"),
    HERO(500, 999, true, "hero"),
    LEGEND(1000, Integer.MAX_VALUE, true, "legend");

    protected int minimum;
    protected int maximum;
    protected boolean good;
    protected String name;
    public static final KarmaClass[] VALUES = values();

    KarmaClass(int minimum, int maximum, boolean good, String name) {
        this.minimum = minimum;
        this.maximum = maximum;
        this.good = good;
        this.name = name;
    }

    public int getMaximum() {
        return maximum;
    }

    public int getMinimum() {
        return minimum;
    }

    public boolean isGood() {
        return good;
    }

    public String getName() {
        return name;
    }

    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("karma.class." + this.getName());
    }

    public static KarmaClass fromKarma(int karma) {
        for (KarmaClass classes : VALUES) {
            if (karma >= classes.minimum && karma <= classes.maximum) {
                return classes;
            }
        }
        return NEUTRAL;
    }

}
