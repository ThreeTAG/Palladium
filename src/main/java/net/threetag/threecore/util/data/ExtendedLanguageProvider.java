package net.threetag.threecore.util.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.util.Util;
import net.minecraftforge.common.data.LanguageProvider;
import net.threetag.threecore.abilities.AbilityType;

import java.util.function.Supplier;

public abstract class ExtendedLanguageProvider extends LanguageProvider {

    private final String modid;

    public ExtendedLanguageProvider(DataGenerator gen, String modid, String locale) {
        super(gen, modid, locale);
        this.modid = modid;
    }

    protected String getModId() {
        return this.modid;
    }

    protected void addSubtitle(String key, String name) {
        this.add("subtitles." + this.getModId() + "." + key, name);
    }

    protected void addAbilityType(Supplier<? extends AbilityType> key, String name) {
        add(key.get(), name);
    }

    protected void add(AbilityType key, String name) {
        add(Util.makeTranslationKey("ability", AbilityType.REGISTRY.getKey(key)), name);
    }

}