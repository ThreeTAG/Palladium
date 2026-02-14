package net.threetag.palladium.power;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryFixedCodec;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.client.texture.TextureReference;
import net.threetag.palladium.icon.Icon;
import net.threetag.palladium.power.ability.Ability;
import net.threetag.palladium.power.energybar.EnergyBarConfiguration;
import net.threetag.palladium.registry.PalladiumRegistryKeys;
import net.threetag.palladium.util.PalladiumCodecs;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

public class Power {

    public static final Identifier DEFAULT_POWER_SCREEN = Palladium.id("power/default");

    public static final Codec<Power> CODEC = RecordCodecBuilder.create((instance) -> instance
            .group(
                    Identifier.CODEC.optionalFieldOf("parent").forGetter(p -> Optional.ofNullable(p.parentId)),
                    ComponentSerialization.CODEC.fieldOf("name").forGetter(Power::getName),
                    Icon.CODEC.fieldOf("icon").forGetter(Power::getIcon),
                    Identifier.CODEC.optionalFieldOf("screen", DEFAULT_POWER_SCREEN).forGetter(p -> p.screen),
                    TextureReference.CODEC.optionalFieldOf("ability_bar_texture").forGetter(p -> Optional.ofNullable(p.abilityBar)),
                    PalladiumCodecs.COLOR_CODEC.optionalFieldOf("primary_color", new Color(210, 112, 49)).forGetter(Power::getPrimaryColor),
                    PalladiumCodecs.COLOR_CODEC.optionalFieldOf("secondary_color", new Color(126, 97, 86)).forGetter(Power::getSecondaryColor),
                    Codec.BOOL.optionalFieldOf("persistent_data", false).forGetter(Power::hasPersistentData),
                    Codec.BOOL.optionalFieldOf("hidden", false).forGetter(Power::isHidden),
                    Codec.unboundedMap(Codec.STRING, Ability.CODEC).optionalFieldOf("abilities", Collections.emptyMap()).forGetter(Power::getAbilities),
                    Codec.unboundedMap(Codec.STRING, EnergyBarConfiguration.CODEC).optionalFieldOf("energy_bars", Collections.emptyMap()).forGetter(Power::getEnergyBars)
            )
            .apply(instance, (parent, name, icon, screen, barTexture, primColor, secondColor, persistentData, hidden, abilities, energyBars) ->
                    new Power(parent.orElse(null), name, icon, screen, barTexture.orElse(null), primColor, secondColor, persistentData, hidden, abilities, energyBars)));

    public static final Codec<Holder<Power>> HOLDER_CODEC = RegistryFixedCodec.create(PalladiumRegistryKeys.POWER);

    @Nullable
    private final Identifier parentId;
    private final Component name;
    private final Icon icon;
    private final Map<String, Ability> abilities;
    private final Map<String, EnergyBarConfiguration> energyBars;
    private final Identifier screen;
    private final TextureReference abilityBar;
    private final Color primaryColor, secondaryColor;
    private final boolean persistentData;
    private final boolean hidden;

    public Power(@Nullable Identifier parentId, Component name, Icon icon, Identifier screen, TextureReference abilityBar, Color primaryColor, Color secondaryColor, boolean persistentData, boolean hidden, Map<String, Ability> abilities, Map<String, EnergyBarConfiguration> energyBars) {
        this.parentId = parentId;
        this.name = name;
        this.icon = icon;
        this.screen = screen;
        this.abilityBar = abilityBar;
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
        this.persistentData = persistentData;
        this.hidden = hidden;
        this.abilities = abilities;
        this.energyBars = energyBars;

        for (Map.Entry<String, Ability> e : this.abilities.entrySet()) {
            e.getValue().setKey(e.getKey());
        }

        for (Map.Entry<String, EnergyBarConfiguration> e : this.energyBars.entrySet()) {
            e.getValue().setKey(e.getKey());
        }
    }

    public @Nullable Identifier getParentId() {
        return parentId;
    }

    public Component getName() {
        return this.name;
    }

    public Icon getIcon() {
        return this.icon;
    }

    public Map<String, Ability> getAbilities() {
        return this.abilities;
    }

    public Map<String, EnergyBarConfiguration> getEnergyBars() {
        return this.energyBars;
    }

    public Identifier getScreenId() {
        return this.screen;
    }

    public TextureReference getAbilityBarTexture() {
        return this.abilityBar;
    }

    public Color getPrimaryColor() {
        return this.primaryColor;
    }

    public Color getSecondaryColor() {
        return this.secondaryColor;
    }

    public boolean hasPersistentData() {
        return this.persistentData;
    }

    public boolean isHidden() {
        return this.hidden;
    }

}
