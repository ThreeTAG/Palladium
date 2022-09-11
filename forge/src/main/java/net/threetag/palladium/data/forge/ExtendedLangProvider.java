package net.threetag.palladium.data.forge;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.LanguageProvider;
import net.threetag.palladium.accessory.Accessory;
import net.threetag.palladium.power.ability.Ability;

import java.util.Objects;
import java.util.function.Supplier;

public abstract class ExtendedLangProvider extends LanguageProvider {

    public ExtendedLangProvider(DataGenerator gen, String modid, String locale) {
        super(gen, modid, locale);
    }

    public void addAbility(Supplier<? extends Ability> key, String name) {
        add(key.get(), name);
    }

    public void add(Ability key, String name) {
        ResourceLocation id = Ability.REGISTRY.getKey(key);
        add("ability." + Objects.requireNonNull(id).getNamespace() + "." + id.getPath(), name);
    }

    protected void addAccessory(Supplier<? extends Accessory> key, String name) {
        add(key.get(), name);
    }

    public void add(Accessory key, String name) {
        ResourceLocation id = Accessory.REGISTRY.getKey(key);
        add("accessory." + Objects.requireNonNull(id).getNamespace() + "." + id.getPath(), name);
    }

}
