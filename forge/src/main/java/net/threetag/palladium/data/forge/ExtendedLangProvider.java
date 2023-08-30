package net.threetag.palladium.data.forge;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraftforge.common.data.LanguageProvider;
import net.threetag.palladium.accessory.Accessory;
import net.threetag.palladium.accessory.AccessorySlot;
import net.threetag.palladium.power.ability.Ability;

import java.util.Objects;
import java.util.function.Supplier;

public abstract class ExtendedLangProvider extends LanguageProvider {

    public ExtendedLangProvider(PackOutput packOutput, String modid, String locale) {
        super(packOutput, modid, locale);
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

    protected void add(AccessorySlot slot, String name) {
        add(slot.getTranslationKey(), name);
    }

    public void addBannerPatternDesc(Supplier<BannerPattern> bannerPattern, DyeColor color, String name) {
        this.addBannerPatternDesc(bannerPattern.get(), color, name);
    }

    public void addBannerPatternDesc(BannerPattern bannerPattern, DyeColor color, String name) {
        ResourceLocation id = BuiltInRegistries.BANNER_PATTERN.getKey(bannerPattern);
        this.add("block.minecraft.banner." + Objects.requireNonNull(id).getNamespace() + "." + id.getPath() + "." + color.getName(), name);
    }

    public void add(Attribute key, String name) {
        add(key.getDescriptionId(), name);
    }

    public void addAttribute(Supplier<? extends Attribute> key, String name) {
        this.add(key.get(), name);
    }

}
