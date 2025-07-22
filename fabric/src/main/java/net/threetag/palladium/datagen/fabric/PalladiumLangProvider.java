package net.threetag.palladium.datagen.fabric;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.HolderLookup;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.PalladiumConfig;
import net.threetag.palladium.customization.CustomizationCategories;
import net.threetag.palladium.client.gui.component.UiAlignment;
import net.threetag.palladium.client.gui.screen.abilitybar.AbilityKeyBindDisplay;
import net.threetag.palladium.client.gui.screen.customization.PlayerCustomizationScreen;
import net.threetag.palladium.command.DataAttachmentCommand;
import net.threetag.palladium.command.SuperpowerCommand;
import net.threetag.palladium.compat.geckolib.GeckoLibCompat;
import net.threetag.palladium.item.PalladiumItems;
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
            // Items
            builder.add(PalladiumItems.SUIT_STAND.get(), "Suit Stand");

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
            this.addConfigCategory(builder, PalladiumConfig.CATEGORY_GAMEPLAY, "Gameplay");
            this.addConfigEntry(builder, "MAX_SUPERPOWER_SETS", "Max. amount of superpower sets");

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
            builder.add(SuperpowerCommand.ERROR_NO_LIVING_ENTITY, "%s is not a living entity");
            builder.add(SuperpowerCommand.QUERY_SUCCESS, "%s has the following superpowers: %s");
            builder.add(SuperpowerCommand.QUERY_NO_POWERS, "%s has no superpowers");
            builder.add(SuperpowerCommand.SET_SUCCESS_SINGLE, "%s has gained the superpower %s");
            builder.add(SuperpowerCommand.SET_SUCCESS_MULTIPLE, "%s entities have gained the superpower %s");
            builder.add(SuperpowerCommand.ADD_ERROR_ALREADY_HAS, "%s already has that superpower");
            builder.add(SuperpowerCommand.ADD_ERROR_NOT_POSSIBLE, "This superpower can not be added to %s anymore");
            builder.add(SuperpowerCommand.ADD_ERROR_NONE_ADDED, "No entity was able to gain the superpower %s");
            builder.add(SuperpowerCommand.ADD_SUCCESS_SINGLE, "%s has gained the superpower %s");
            builder.add(SuperpowerCommand.ADD_SUCCESS_MULTIPLE, "%s entities have gained the superpower %s");
            builder.add(SuperpowerCommand.REMOVE_ERROR_NO_MATCH, "No matching superpower was found");
            builder.add(SuperpowerCommand.REMOVE_SUCCESS_SINGLE, "The superpower(s) of %s were removed");
            builder.add(SuperpowerCommand.REMOVE_SUCCESS_MULTIPLE, "The superpower(s) of %s entities were removed");
            builder.add(SuperpowerCommand.REPLACE_SUCCESS_SINGLE, "Some of %s's superpowers have been replaced with %s");
            builder.add(SuperpowerCommand.REPLACE_SUCCESS_MULTIPLE, "Some of %s entities' superpowers have been replaced with %s");
            builder.add(SuperpowerCommand.REPLACE_ERROR_NOT_POSSIBLE, "This superpower can not be added to %s anymore");
            builder.add(SuperpowerCommand.REPLACE_ERROR_NONE_ADDED, "No entity was able to gain the superpower %s");

            builder.add(DataAttachmentCommand.TRANS_UNKNOWN_TYPE, "Unknown data attachment type: %s");
            builder.add(DataAttachmentCommand.TRANS_NO_DATA, "No data found for data attachment type %s");
            builder.add(DataAttachmentCommand.TRANS_GET_DATA, "Data attachment value %s for %s is %s");
            builder.add(DataAttachmentCommand.TRANS_PARSE_ERROR, "Error while parsing data attachment value");
            builder.add(DataAttachmentCommand.TRANS_SET_DATA_SINGLE, "Data attachment value %s for %s is now set to %s");
            builder.add(DataAttachmentCommand.TRANS_SET_DATA_MULTIPLE, "Data attachment value %s for %s entities is now set to %s");

            // GUI
            builder.add("gui.palladium.powers", "Powers");
            builder.add("gui.palladium.powers.buy_ability", "Do you want to unlock this ability?");
            builder.add("gui.palladium.powers.buy_ability.or", "or");
            builder.add("gui.palladium.powers.buy_ability.experience_level", "%sx experience level");
            builder.add("gui.palladium.powers.buy_ability.experience_level_plural", "%sx experience levels");
            builder.add(PlayerCustomizationScreen.TITLE_TRANSLATION_KEY, "Customization");

            this.addCustomizationCategory(builder, CustomizationCategories.HAT, "Hat");
            this.addCustomizationCategory(builder, CustomizationCategories.HEAD, "Head");
            this.addCustomizationCategory(builder, CustomizationCategories.CHEST, "Chest");
            this.addCustomizationCategory(builder, CustomizationCategories.BACK, "Back");
            this.addCustomizationCategory(builder, CustomizationCategories.ARMS, "Arms");
            this.addCustomizationCategory(builder, CustomizationCategories.LEGS, "Legs");
        }

    }

    public static class German extends PalladiumLangProvider {

        public German(FabricDataOutput packOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
            super(packOutput, "de_de", registryLookup);
        }

        @Override
        public void generateTranslations(HolderLookup.Provider provider, TranslationBuilder builder) {
            // Items
            builder.add(PalladiumItems.SUIT_STAND.get(), "Anzugsständer");

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
            this.addConfigCategory(builder, PalladiumConfig.CATEGORY_GAMEPLAY, "Gameplay");
            this.addConfigEntry(builder, "MAX_SUPERPOWER_SETS", "Max. Anzahl an Superkraft-Sets");

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

            // Commands
            builder.add(SuperpowerCommand.ERROR_NO_LIVING_ENTITY, "%s ist kein lebendes Wesen");
            builder.add(SuperpowerCommand.QUERY_SUCCESS, "%s hat folgende Superkräfte: %s");
            builder.add(SuperpowerCommand.QUERY_NO_POWERS, "%s hat keine Superkräfte");
            builder.add(SuperpowerCommand.SET_SUCCESS_SINGLE, "%s hat die Superkraft %s erhalten");
            builder.add(SuperpowerCommand.SET_SUCCESS_MULTIPLE, "%s Wesen haben die Superkraft %s erhalten");
            builder.add(SuperpowerCommand.ADD_ERROR_ALREADY_HAS, "%s besitzt diese Superkraft bereits");
            builder.add(SuperpowerCommand.ADD_ERROR_NOT_POSSIBLE, "Diese Superkraft kann %s nicht mehr hinzugefügt werden");
            builder.add(SuperpowerCommand.ADD_ERROR_NONE_ADDED, "Keinem Wesen konnte die Superkraft %s verliehen werden");
            builder.add(SuperpowerCommand.ADD_SUCCESS_SINGLE, "%s hat die Superkraft %s erhalten");
            builder.add(SuperpowerCommand.ADD_SUCCESS_MULTIPLE, "%s Wesen haben die Superkraft %s erhalten");
            builder.add(SuperpowerCommand.REMOVE_ERROR_NO_MATCH, "Keine passende Superkraft wurde gefunden");
            builder.add(SuperpowerCommand.REMOVE_SUCCESS_SINGLE, "Die Superkraft(e) von %s wurde(n) entfernt");
            builder.add(SuperpowerCommand.REMOVE_SUCCESS_MULTIPLE, "Die Superkraft(e) von %s Wesen wurden entfernt");
            builder.add(SuperpowerCommand.REPLACE_SUCCESS_SINGLE, "Einige von %ss Superkräften wurden durch %s ersetzt");
            builder.add(SuperpowerCommand.REPLACE_SUCCESS_MULTIPLE, "Einige der Superkräfte von %s Wesen wurden durch %s ersetzt");
            builder.add(SuperpowerCommand.REPLACE_ERROR_NOT_POSSIBLE, "Diese Superkraft kann %s nicht mehr hinzugefügt werden");
            builder.add(SuperpowerCommand.REPLACE_ERROR_NONE_ADDED, "Keinem Wesen konnte die Superkraft %s verliehen werden");

            builder.add(DataAttachmentCommand.TRANS_UNKNOWN_TYPE, "Unbekannter Datenanhang: %s");
            builder.add(DataAttachmentCommand.TRANS_NO_DATA, "Keine Daten für Datenanlagentyp %s gefunden");
            builder.add(DataAttachmentCommand.TRANS_GET_DATA, "Der Wert der Datenanlage %s für %s ist %s");
            builder.add(DataAttachmentCommand.TRANS_PARSE_ERROR, "Fehler beim Parsen von Datenanhangswerten");
            builder.add(DataAttachmentCommand.TRANS_SET_DATA_SINGLE, "Datenanhangswert %s für %s ist jetzt auf %s gesetzt");
            builder.add(DataAttachmentCommand.TRANS_SET_DATA_MULTIPLE, "Datenanhangswert %s für %s Entitäten ist jetzt auf %s gesetzt");

            // GUI
            builder.add("gui.palladium.powers", "Kräfte");
            builder.add("gui.palladium.powers.buy_ability", "Möchtest du diese Fähigkeit freischalten?");
            builder.add("gui.palladium.powers.buy_ability.or", "oder");
            builder.add("gui.palladium.powers.buy_ability.experience_level", "%sx Erfahrungsstufe");
            builder.add("gui.palladium.powers.buy_ability.experience_level_plural", "%sx Erfahrungsstufen");
            builder.add(PlayerCustomizationScreen.TITLE_TRANSLATION_KEY, "Anpassungen");

            this.addCustomizationCategory(builder, CustomizationCategories.HAT, "Hut");
            this.addCustomizationCategory(builder, CustomizationCategories.HEAD, "Kopf");
            this.addCustomizationCategory(builder, CustomizationCategories.CHEST, "Brust");
            this.addCustomizationCategory(builder, CustomizationCategories.BACK, "Rücken");
            this.addCustomizationCategory(builder, CustomizationCategories.ARMS, "Arme");
            this.addCustomizationCategory(builder, CustomizationCategories.LEGS, "Beine");
        }
    }

    public static class Saxon extends PalladiumLangProvider {

        public Saxon(FabricDataOutput packOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
            super(packOutput, "sxu", registryLookup);
        }

        @Override
        public void generateTranslations(HolderLookup.Provider provider, TranslationBuilder builder) {
            // Items
            builder.add(PalladiumItems.SUIT_STAND.get(), "Anzuchsschdändorr");

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
            this.addConfigCategory(builder, PalladiumConfig.CATEGORY_GAMEPLAY, "Gameplay");
            this.addConfigEntry(builder, "MAX_SUPERPOWER_SETS", "Max. Anzahl an Superkraft-Sets");

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

            // Commands
            builder.add(SuperpowerCommand.ERROR_NO_LIVING_ENTITY, "%s is keen lebdisches Viech");
            builder.add(SuperpowerCommand.QUERY_SUCCESS, "%s hat de folgende Superkrächte: %s");
            builder.add(SuperpowerCommand.QUERY_NO_POWERS, "%s hat keene Superkrächte");
            builder.add(SuperpowerCommand.SET_SUCCESS_SINGLE, "%s hat nu die Superkraft %s bekomm");
            builder.add(SuperpowerCommand.SET_SUCCESS_MULTIPLE, "%s Viecher ham nu die Superkraft %s bekomm");
            builder.add(SuperpowerCommand.ADD_ERROR_ALREADY_HAS, "%s hat die Superkraft doch schon");
            builder.add(SuperpowerCommand.ADD_ERROR_NOT_POSSIBLE, "Die Superkraft kann mer %s nich mehr geb'n");
            builder.add(SuperpowerCommand.ADD_ERROR_NONE_ADDED, "Keins von de Viecher konnt die Superkraft %s kriegn");
            builder.add(SuperpowerCommand.ADD_SUCCESS_SINGLE, "%s hat nu die Superkraft %s bekomm");
            builder.add(SuperpowerCommand.ADD_SUCCESS_MULTIPLE, "%s Viecher ham nu die Superkraft %s bekomm");
            builder.add(SuperpowerCommand.REMOVE_ERROR_NO_MATCH, "Keene passnde Superkraft gefundn");
            builder.add(SuperpowerCommand.REMOVE_SUCCESS_SINGLE, "Die Superkraft(e) von %s sin nu wech");
            builder.add(SuperpowerCommand.REMOVE_SUCCESS_MULTIPLE, "Die Superkraft(e) von %s Viechern sin nu wech");
            builder.add(SuperpowerCommand.REPLACE_SUCCESS_SINGLE, "E paar von %ss Superkrächte sin nu %s");
            builder.add(SuperpowerCommand.REPLACE_SUCCESS_MULTIPLE, "E paar Superkrächte von %s Viechern sin nu %s");
            builder.add(SuperpowerCommand.REPLACE_ERROR_NOT_POSSIBLE, "Die Superkraft kann mer %s nich mehr geb'n");
            builder.add(SuperpowerCommand.REPLACE_ERROR_NONE_ADDED, "Keins von de Viecher konnt die Superkraft %s kriegn");

            builder.add(DataAttachmentCommand.TRANS_UNKNOWN_TYPE, "Unbegannter Datenanhang: %s");
            builder.add(DataAttachmentCommand.TRANS_NO_DATA, "Kehne Daten für Datenanlagentyp %s jefunden");
            builder.add(DataAttachmentCommand.TRANS_GET_DATA, "Der Wert dor Datenanlage %s für %s is %s");
            builder.add(DataAttachmentCommand.TRANS_PARSE_ERROR, "Fehler beim Parsen von Datenanhangswerten");
            builder.add(DataAttachmentCommand.TRANS_SET_DATA_SINGLE, "Datenanhangswert %s für %s jetze uff %s jesetzt");
            builder.add(DataAttachmentCommand.TRANS_SET_DATA_MULTIPLE, "Datenanhangswert %s für %s Entitäten is jetze uff %s jesetzt");

            // GUI
            builder.add("gui.palladium.powers", "Kräfte");
            builder.add("gui.palladium.powers.buy_ability", "Möschtest'e disse Fähischgeht freischaltn?");
            builder.add("gui.palladium.powers.buy_ability.or", "oder");
            builder.add("gui.palladium.powers.buy_ability.experience_level", "%sx Erfahrungsschdufe");
            builder.add("gui.palladium.powers.buy_ability.experience_level_plural", "%sx Erfahrungsschdufen");
            builder.add(PlayerCustomizationScreen.TITLE_TRANSLATION_KEY, "Anpassungen");

            this.addCustomizationCategory(builder, CustomizationCategories.HAT, "Hood");
            this.addCustomizationCategory(builder, CustomizationCategories.HEAD, "Nüschl");
            this.addCustomizationCategory(builder, CustomizationCategories.CHEST, "Bruscht");
            this.addCustomizationCategory(builder, CustomizationCategories.BACK, "Rieggn");
            this.addCustomizationCategory(builder, CustomizationCategories.ARMS, "Arme");
            this.addCustomizationCategory(builder, CustomizationCategories.LEGS, "Behne");

        }
    }

}
