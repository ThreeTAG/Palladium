package net.threetag.threecore.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.Util;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.ForgeRegistries;
import net.threetag.threecore.ability.AbilityType;
import net.threetag.threecore.accessoires.Accessoire;

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

    protected void addFluid(Supplier<? extends Fluid> key, String name) {
        add(key.get(), name);
    }

    protected void add(Fluid key, String name) {
        add(Util.makeTranslationKey("fluid", ForgeRegistries.FLUIDS.getKey(key)), name);
    }

    protected void addContainerType(Supplier<? extends ContainerType> key, String name) {
        add(key.get(), name);
    }

    protected void add(ContainerType key, String name) {
        add(Util.makeTranslationKey("container", ForgeRegistries.CONTAINERS.getKey(key)), name);
    }

    protected void addAccessoire(Supplier<? extends Accessoire> key, String name) {
        add(key.get(), name);
    }

    protected void add(Accessoire key, String name) {
        add(Util.makeTranslationKey("accessoire", Accessoire.REGISTRY.getKey(key)), name);
    }

}