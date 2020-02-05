package net.threetag.threecore.data.lang;

import net.minecraft.data.DataGenerator;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.data.ExtendedLanguageProvider;

public abstract class ThreeCoreLanguageProvider extends ExtendedLanguageProvider {

    public ThreeCoreLanguageProvider(DataGenerator gen, String locale) {
        super(gen, ThreeCore.MODID, locale);
    }

    @Override
    public String getName() {
        return "ThreeCore " + super.getName();
    }
}
