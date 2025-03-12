package net.threetag.palladium.datagen.fabric;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.HolderLookup;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.PalladiumConfig;
import net.threetag.palladium.client.gui.component.UiAlignment;
import net.threetag.palladium.client.gui.screen.abilitybar.AbilityKeyBindDisplay;
import net.threetag.palladium.command.SuperpowerCommand;
import net.threetag.palladium.compat.geckolib.GeckoLibCompat;
import net.threetag.palladium.power.ability.AbilitySerializers;

import java.util.concurrent.CompletableFuture;

public abstract class PalladiumLangProvider extends ExtendedLangProvider {

    public PalladiumLangProvider(FabricDataOutput output, String locale, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(output, Palladium.MOD_ID, locale, registryLookup);
    }

    public static class English extends PalladiumLangProvider {

        public English(FabricDataOutput packOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
            super(packOutput, "en_us", registryLookup);
        }

        @Override
        public void generateTranslations(HolderLookup.Provider provider, TranslationBuilder builder) {
            // Config
            this.addConfigTitle(builder, "Palladium");
            this.addConfigCategory(builder, PalladiumConfig.CATEGORY_CLIENT, "Client");
            this.addConfigEntry(builder, "ABILITY_BAR_ALIGNMENT", "Ability Bar - Alignment");
            this.addConfigEnum(builder, UiAlignment.TOP_LEFT, "Top Left");
            this.addConfigEnum(builder, UiAlignment.TOP_RIGHT, "Top Right");
            this.addConfigEnum(builder, UiAlignment.BOTTOM_LEFT, "Bottom Left");
            this.addConfigEnum(builder, UiAlignment.BOTTOM_RIGHT, "Bottom Right");
            this.addConfigEntry(builder, "ABILITY_BAR_KEY_BIND_DISPLAY", "Ability Bar - Key Bind Display");
            this.addConfigEnum(builder, AbilityKeyBindDisplay.INSIDE, "Inside");
            this.addConfigEnum(builder, AbilityKeyBindDisplay.OUTSIDE, "Outside");
            this.addConfigEntry(builder, "HIDE_EXPERIMENTAL_WARNING", "Singleplayer - Hide Experimental Settings Warning");

            // Key Mappings
            builder.add("key.palladium.categories.powers", "Powers");
            this.addKeyMapping(builder, "open_equipment", "Open/Close Equipment");
            this.addKeyMapping(builder, "show_powers", "Powers");
            this.addKeyMapping(builder, "rotate_ability_list", "Rotate through ability bar");
            for (int i = 1; i <= 5; i++) {
                this.addKeyMapping(builder, "ability_" + i, "Ability #" + i);
            }

            // Abilities
            this.addAbility(builder, AbilitySerializers.DUMMY, "Dummy");
            this.addAbility(builder, AbilitySerializers.COMMAND, "Command");
//            this.addAbility(builder, AbilitySerializers.RENDER_LAYER_BY_ACCESSORY_SLOT, "Render Layer");
            this.addAbility(builder, AbilitySerializers.SHRINK_BODY_OVERLAY, "Shrink Body Overlay");
            this.addAbility(builder, AbilitySerializers.ATTRIBUTE_MODIFIER, "Attribute Modifier");
            this.addAbility(builder, AbilitySerializers.HEALING, "Healing");
            this.addAbility(builder, AbilitySerializers.SLOWFALL, "Slowfall");
            this.addAbility(builder, AbilitySerializers.DAMAGE_IMMUNITY, "Damage Immunity");
            this.addAbility(builder, AbilitySerializers.INVISIBILITY, "Invisibility");
            this.addAbility(builder, AbilitySerializers.ENERGY_BEAM, "Energy Beam");
            this.addAbility(builder, AbilitySerializers.SIZE, "Size");
//            this.addAbility(builder, AbilitySerializers.PROJECTILE, "Projectile");
            this.addAbility(builder, AbilitySerializers.SKIN_CHANGE, "Skin Change");
            this.addAbility(builder, AbilitySerializers.AIM, "Aim");
            this.addAbility(builder, AbilitySerializers.HIDE_BODY_PART, "Hide Body Part");
            this.addAbility(builder, AbilitySerializers.REMOVE_BODY_PART, "Remove Body Part");
            this.addAbility(builder, AbilitySerializers.SHADER_EFFECT, "Shader Effect");
            this.addAbility(builder, AbilitySerializers.GUI_OVERLAY, "Gui Overlay");
            this.addAbility(builder, AbilitySerializers.SHOW_BOTH_ARMS, "Show Both Arms");
//            this.addAbility(builder, AbilitySerializers.PLAYER_ANIMATION, "Player Animation");
            this.addAbility(builder, AbilitySerializers.WATER_WALK, "Water Walk");
            this.addAbility(builder, AbilitySerializers.FLUID_WALKING, "Fluid Walking");
            this.addAbility(builder, AbilitySerializers.RESTRICT_SLOTS, "Restrict Slots");
            this.addAbility(builder, AbilitySerializers.PLAY_SOUND, "Sound");
            this.addAbility(builder, AbilitySerializers.VIBRATE, "Vibrate");
            this.addAbility(builder, AbilitySerializers.INTANGIBILITY, "Intangibility");
            this.addAbility(builder, AbilitySerializers.NAME_CHANGE, "Name Change");
            this.addAbility(builder, AbilitySerializers.SCULK_IMMUNITY, "Sculk Immunity");
            this.addAbility(builder, AbilitySerializers.FIRE_ASPECT, "Fire Aspect");
            this.addAbility(builder, AbilitySerializers.PARTICLES, "Particles");
            this.addAbility(builder, GeckoLibCompat.TRIGGER_LAYER_ANIMATION, "Geo Animation");

            // Commands
            builder.add(SuperpowerCommand.QUERY_SUCCESS, "%s has the following superpowers: %s");

            // GUI
            builder.add("gui.palladium.powers", "Powers");
            builder.add("gui.palladium.powers.buy_ability", "Do you want to unlock this ability?");
            builder.add("gui.palladium.powers.buy_ability.or", "or");
            builder.add("gui.palladium.powers.buy_ability.experience_level", "%sx experience level");
            builder.add("gui.palladium.powers.buy_ability.experience_level_plural", "%sx experience levels");
        }

    }

    public static class German extends PalladiumLangProvider {

        public German(FabricDataOutput packOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
            super(packOutput, "de_de", registryLookup);
        }

        @Override
        public void generateTranslations(HolderLookup.Provider provider, TranslationBuilder builder) {
            // Config
            this.addConfigTitle(builder, "Palladium");
            this.addConfigCategory(builder, PalladiumConfig.CATEGORY_CLIENT, "Client");
            this.addConfigEntry(builder, "ABILITY_BAR_ALIGNMENT", "Ability Bar - Position");
            this.addConfigEnum(builder, UiAlignment.TOP_LEFT, "Oben links");
            this.addConfigEnum(builder, UiAlignment.TOP_RIGHT, "Oben rechts");
            this.addConfigEnum(builder, UiAlignment.BOTTOM_LEFT, "Unten links");
            this.addConfigEnum(builder, UiAlignment.BOTTOM_RIGHT, "Unten rechts");
            this.addConfigEntry(builder, "ABILITY_BAR_KEY_BIND_DISPLAY", "Ability Bar - Anzeige der Tastenbelegung");
            this.addConfigEnum(builder, AbilityKeyBindDisplay.INSIDE, "Innen");
            this.addConfigEnum(builder, AbilityKeyBindDisplay.OUTSIDE, "Außen");
            this.addConfigEntry(builder, "HIDE_EXPERIMENTAL_WARNING", "Singleplayer - Experimental Settings Warnung ausblenden");

            // Key Mappings
            builder.add("key.palladium.categories.powers", "Kräfte");
            this.addKeyMapping(builder, "open_equipment", "Ausrüstung öffnen/schließen");
            this.addKeyMapping(builder, "show_powers", "Kräfte");
            this.addKeyMapping(builder, "rotate_ability_list", "Durch Fähigkeitenleiste rotieren");
            for (int i = 1; i <= 5; i++) {
                this.addKeyMapping(builder, "ability_" + i, "Fähigkeit #" + i);
            }

            // Abilities
            this.addAbility(builder, AbilitySerializers.DUMMY, "Dummy");
            this.addAbility(builder, AbilitySerializers.COMMAND, "Befehl");
//            this.addAbility(builder, AbilitySerializers.RENDER_LAYER_BY_ACCESSORY_SLOT, "Render Layer");
            this.addAbility(builder, AbilitySerializers.SHRINK_BODY_OVERLAY, "Körperoverlay schrumpfen");
            this.addAbility(builder, AbilitySerializers.ATTRIBUTE_MODIFIER, "Attributmodifikator");
            this.addAbility(builder, AbilitySerializers.HEALING, "Heilung");
            this.addAbility(builder, AbilitySerializers.SLOWFALL, "Langsamer Fall");
            this.addAbility(builder, AbilitySerializers.DAMAGE_IMMUNITY, "Schadensimmunität");
            this.addAbility(builder, AbilitySerializers.INVISIBILITY, "Unsichtbarkeit");
            this.addAbility(builder, AbilitySerializers.ENERGY_BEAM, "Energiestrahl");
            this.addAbility(builder, AbilitySerializers.SIZE, "Größe");
//            this.addAbility(builder, AbilitySerializers.PROJECTILE, "Projektil");
            this.addAbility(builder, AbilitySerializers.SKIN_CHANGE, "Skin Änderung");
            this.addAbility(builder, AbilitySerializers.AIM, "Zielen");
            this.addAbility(builder, AbilitySerializers.HIDE_BODY_PART, "Körperteile verstecken");
            this.addAbility(builder, AbilitySerializers.REMOVE_BODY_PART, "Körperteile entfernen");
            this.addAbility(builder, AbilitySerializers.SHADER_EFFECT, "Shader Effekt");
            this.addAbility(builder, AbilitySerializers.GUI_OVERLAY, "GUI-Overlay");
            this.addAbility(builder, AbilitySerializers.SHOW_BOTH_ARMS, "Beide Arme zeigen");
//            this.addAbility(builder, AbilitySerializers.PLAYER_ANIMATION, "Spieler-Animation");
            this.addAbility(builder, AbilitySerializers.WATER_WALK, "Auf Wasser Laufen");
            this.addAbility(builder, AbilitySerializers.FLUID_WALKING, "Auf Flüssigkeit Laufen");
            this.addAbility(builder, AbilitySerializers.RESTRICT_SLOTS, "Slots beschränken");
            this.addAbility(builder, AbilitySerializers.PLAY_SOUND, "Sound");
            this.addAbility(builder, AbilitySerializers.VIBRATE, "Vibrieren");
            this.addAbility(builder, AbilitySerializers.INTANGIBILITY, "Ungreifbarkeit");
            this.addAbility(builder, AbilitySerializers.NAME_CHANGE, "Namesänderung");
            this.addAbility(builder, AbilitySerializers.SCULK_IMMUNITY, "Sculk-Immunität");
            this.addAbility(builder, AbilitySerializers.FIRE_ASPECT, "Verbrennung");
            this.addAbility(builder, AbilitySerializers.PARTICLES, "Partikel");
            this.addAbility(builder, GeckoLibCompat.TRIGGER_LAYER_ANIMATION, "Geo Animation");

            // GUI
            builder.add("gui.palladium.powers", "Kräfte");
            builder.add("gui.palladium.powers.buy_ability", "Möchtest du diese Fähigkeit freischalten?");
            builder.add("gui.palladium.powers.buy_ability.or", "oder");
            builder.add("gui.palladium.powers.buy_ability.experience_level", "%sx Erfahrungsstufe");
            builder.add("gui.palladium.powers.buy_ability.experience_level_plural", "%sx Erfahrungsstufen");
        }
    }

    public static class Saxon extends PalladiumLangProvider {

        public Saxon(FabricDataOutput packOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
            super(packOutput, "sxu", registryLookup);
        }

        @Override
        public void generateTranslations(HolderLookup.Provider provider, TranslationBuilder builder) {
            // Config
            this.addConfigTitle(builder, "Palladium");
            this.addConfigCategory(builder, PalladiumConfig.CATEGORY_CLIENT, "Client");
            this.addConfigEntry(builder, "ABILITY_BAR_ALIGNMENT", "Ability Bar - Position");
            this.addConfigEnum(builder, UiAlignment.TOP_LEFT, "Oben links");
            this.addConfigEnum(builder, UiAlignment.TOP_RIGHT, "Oben rechts");
            this.addConfigEnum(builder, UiAlignment.BOTTOM_LEFT, "Unten links");
            this.addConfigEnum(builder, UiAlignment.BOTTOM_RIGHT, "Unten rechts");
            this.addConfigEntry(builder, "ABILITY_BAR_KEY_BIND_DISPLAY", "Ability Bar - Anzeige der Tastenbelegung");
            this.addConfigEnum(builder, AbilityKeyBindDisplay.INSIDE, "Innen");
            this.addConfigEnum(builder, AbilityKeyBindDisplay.OUTSIDE, "Außen");
            this.addConfigEntry(builder, "HIDE_EXPERIMENTAL_WARNING", "Singleplayer - Experimental Settings Warnung ausblenden");

            // Key Mappings
            builder.add("key.palladium.categories.powers", "Gräfte");
            this.addKeyMapping(builder, "open_equipment", "Ohsrüstung öffnen/schließen");
            this.addKeyMapping(builder, "show_powers", "Gräfte");
            this.addKeyMapping(builder, "rotate_ability_list", "Durch Fähischgehtlehste rotieren");
            for (int i = 1; i <= 5; i++) {
                this.addKeyMapping(builder, "ability_" + i, "Fähischgeht #" + i);
            }

            // Abilities
            this.addAbility(builder, AbilitySerializers.DUMMY, "Dummy");
            this.addAbility(builder, AbilitySerializers.COMMAND, "Befehl");
//            this.addAbility(builder, AbilitySerializers.RENDER_LAYER_BY_ACCESSORY_SLOT, "Render Layer");
            this.addAbility(builder, AbilitySerializers.SHRINK_BODY_OVERLAY, "Körperoverlay schrumpfen");
            this.addAbility(builder, AbilitySerializers.ATTRIBUTE_MODIFIER, "Ättribütmodifikator");
            this.addAbility(builder, AbilitySerializers.HEALING, "Helung");
            this.addAbility(builder, AbilitySerializers.SLOWFALL, "Langsamer Fall");
            this.addAbility(builder, AbilitySerializers.DAMAGE_IMMUNITY, "Schadensimmunität");
            this.addAbility(builder, AbilitySerializers.INVISIBILITY, "Unsischtbarkeet");
            this.addAbility(builder, AbilitySerializers.ENERGY_BEAM, "Energiestrahl");
            this.addAbility(builder, AbilitySerializers.SIZE, "Größe");
//            this.addAbility(builder, AbilitySerializers.PROJECTILE, "Projektil");
            this.addAbility(builder, AbilitySerializers.SKIN_CHANGE, "Skin Änderung");
            this.addAbility(builder, AbilitySerializers.AIM, "Zielen");
            this.addAbility(builder, AbilitySerializers.HIDE_BODY_PART, "Görperdeile versteggen");
            this.addAbility(builder, AbilitySerializers.REMOVE_BODY_PART, "Görperdeile entfernen");
            this.addAbility(builder, AbilitySerializers.SHADER_EFFECT, "Shader Effekt");
            this.addAbility(builder, AbilitySerializers.GUI_OVERLAY, "GUI-Overlay");
            this.addAbility(builder, AbilitySerializers.SHOW_BOTH_ARMS, "Beide Arme zeijen");
//            this.addAbility(builder, AbilitySerializers.PLAYER_ANIMATION, "Spieler-Animation");
            this.addAbility(builder, AbilitySerializers.WATER_WALK, "Uff Wasser Lofen");
            this.addAbility(builder, AbilitySerializers.FLUID_WALKING, "Uff Flüssichkeht Lofen");
            this.addAbility(builder, AbilitySerializers.RESTRICT_SLOTS, "Slots beschränken");
            this.addAbility(builder, AbilitySerializers.PLAY_SOUND, "Sound");
            this.addAbility(builder, AbilitySerializers.VIBRATE, "Vibrieren");
            this.addAbility(builder, AbilitySerializers.INTANGIBILITY, "Ungreifbarkeht");
            this.addAbility(builder, AbilitySerializers.NAME_CHANGE, "Namesänderung");
            this.addAbility(builder, AbilitySerializers.SCULK_IMMUNITY, "Sculk-Immunität");
            this.addAbility(builder, AbilitySerializers.FIRE_ASPECT, "Vorbrennung");
            this.addAbility(builder, AbilitySerializers.PARTICLES, "Partikel");
            this.addAbility(builder, GeckoLibCompat.TRIGGER_LAYER_ANIMATION, "Geo Animation");

            // GUI
            builder.add("gui.palladium.powers", "Kräfte");
            builder.add("gui.palladium.powers.buy_ability", "Möschtest'e disse Fähischgeht freischaltn?");
            builder.add("gui.palladium.powers.buy_ability.or", "oder");
            builder.add("gui.palladium.powers.buy_ability.experience_level", "%sx Erfahrungsschdufe");
            builder.add("gui.palladium.powers.buy_ability.experience_level_plural", "%sx Erfahrungsschdufen");
        }
    }

}
