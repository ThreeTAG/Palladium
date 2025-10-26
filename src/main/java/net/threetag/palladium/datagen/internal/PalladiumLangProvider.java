package net.threetag.palladium.datagen.internal;

import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.client.gui.screen.customization.PlayerCustomizationScreen;
import net.threetag.palladium.command.DataAttachmentCommand;
import net.threetag.palladium.command.SuperpowerCommand;
import net.threetag.palladium.compat.geckolib.GeckoLibCompat;
import net.threetag.palladium.config.PalladiumClientConfig;
import net.threetag.palladium.config.PalladiumServerConfig;
import net.threetag.palladium.customization.BuiltinCustomization;
import net.threetag.palladium.customization.Customization;
import net.threetag.palladium.customization.CustomizationCategories;
import net.threetag.palladium.customization.CustomizationCategory;
import net.threetag.palladium.item.PalladiumItems;
import net.threetag.palladium.power.ability.AbilitySerializer;
import net.threetag.palladium.power.ability.AbilitySerializers;
import net.threetag.palladium.registry.PalladiumRegistries;

import java.util.Objects;

public abstract class PalladiumLangProvider extends LanguageProvider {

    protected final String modid;

    public PalladiumLangProvider(PackOutput output, String modid, String locale) {
        super(output, modid, locale);
        this.modid = modid;
    }

    public void addAbility(Holder<? extends AbilitySerializer<?>> key, String name) {
        this.add(key.value(), name);
    }

    public void add(AbilitySerializer<?> key, String name) {
        ResourceLocation id = PalladiumRegistries.ABILITY_SERIALIZER.getKey(key);
        this.add("ability." + Objects.requireNonNull(id).getNamespace() + "." + id.getPath(), name);
    }

    public void addConfigEntry(ModConfigSpec.ConfigValue<?> configSpec, String name) {
        this.add(this.modid + ".configuration." + String.join(".", configSpec.getPath()), name);
    }

    public void addKeyMapping(String key, String name) {
        this.add("key.palladium." + key, name);
    }

    public void addCustomizationCategory(ResourceKey<CustomizationCategory> key, String name) {
        this.add(CustomizationCategory.makeDescriptionId(key), name);
    }

    public void addCustomization(ResourceKey<Customization> key, String name) {
        this.add(Customization.makeDescriptionId(key), name);
    }

    public void addBuiltinCustomization(BuiltinCustomization.Type type, String name) {
        this.add(Customization.makeDescriptionId(Palladium.id(type.name())), name);
    }

    public static class English extends PalladiumLangProvider {

        public English(PackOutput output) {
            super(output, Palladium.MOD_ID, "en_us");
        }

        @Override
        protected void addTranslations() {
            // Items
            this.add(PalladiumItems.SUIT_STAND.get(), "Suit Stand");

            // Config
            this.addConfigEntry(PalladiumClientConfig.ABILITY_BAR_ALIGNMENT, "Ability Bar - Alignment");
            this.addConfigEntry(PalladiumClientConfig.ABILITY_BAR_KEY_BIND_DISPLAY, "Ability Bar - Key Bind Display");
            this.addConfigEntry(PalladiumClientConfig.HIDE_EXPERIMENTAL_WARNING, "Singleplayer - Hide Experimental Settings Warning");
            this.addConfigEntry(PalladiumServerConfig.MAX_SUPERPOWER_SETS, "Max. amount of superpower sets");

            // Key Mappings
            this.add("key.palladium.categories.powers", "Powers");
            this.addKeyMapping("open_equipment", "Open/Close Equipment");
            this.addKeyMapping("show_powers", "Powers");
            this.addKeyMapping("rotate_ability_list", "Rotate through ability bar");
            for (int i = 1; i <= 5; i++) {
                this.addKeyMapping("ability_" + i, "Ability #" + i);
            }

            // Abilities
            this.addAbility(AbilitySerializers.DUMMY, "Dummy");
            this.addAbility(AbilitySerializers.COMMAND, "Command");
//            this.addAbility(AbilitySerializers.RENDER_LAYER_BY_ACCESSORY_SLOT, "Render Layer");
            this.addAbility(AbilitySerializers.SHRINK_BODY_OVERLAY, "Shrink Body Overlay");
            this.addAbility(AbilitySerializers.ATTRIBUTE_MODIFIER, "Attribute Modifier");
            this.addAbility(AbilitySerializers.HEALING, "Healing");
            this.addAbility(AbilitySerializers.SLOWFALL, "Slowfall");
            this.addAbility(AbilitySerializers.DAMAGE_IMMUNITY, "Damage Immunity");
            this.addAbility(AbilitySerializers.INVISIBILITY, "Invisibility");
            this.addAbility(AbilitySerializers.BEAM, "Energy Beam");
            this.addAbility(AbilitySerializers.SIZE, "Size");
//            this.addAbility(AbilitySerializers.PROJECTILE, "Projectile");
            this.addAbility(AbilitySerializers.SKIN_CHANGE, "Skin Change");
            this.addAbility(AbilitySerializers.AIM, "Aim");
            this.addAbility(AbilitySerializers.HIDE_MODEL_PART, "Hide Body Part");
            this.addAbility(AbilitySerializers.SHADER_EFFECT, "Shader Effect");
            this.addAbility(AbilitySerializers.GUI_OVERLAY, "Gui Overlay");
            this.addAbility(AbilitySerializers.SHOW_BOTH_ARMS, "Show Both Arms");
//            this.addAbility(AbilitySerializers.PLAYER_ANIMATION, "Player Animation");
            this.addAbility(AbilitySerializers.FLUID_WALKING, "Fluid Walking");
            this.addAbility(AbilitySerializers.RESTRICT_SLOTS, "Restrict Slots");
            this.addAbility(AbilitySerializers.PLAY_SOUND, "Sound");
            this.addAbility(AbilitySerializers.VIBRATE, "Vibrate");
            this.addAbility(AbilitySerializers.INTANGIBILITY, "Intangibility");
            this.addAbility(AbilitySerializers.NAME_CHANGE, "Name Change");
            this.addAbility(AbilitySerializers.SCULK_IMMUNITY, "Sculk Immunity");
            this.addAbility(AbilitySerializers.FIRE_ASPECT, "Fire Aspect");
            this.addAbility(AbilitySerializers.PARTICLES, "Particles");
            this.addAbility(GeckoLibCompat.TRIGGER_LAYER_ANIMATION, "Geo Animation");

            // Commands
            this.add(SuperpowerCommand.ERROR_NO_LIVING_ENTITY, "%s is not a living entity");
            this.add(SuperpowerCommand.QUERY_SUCCESS, "%s has the following superpowers: %s");
            this.add(SuperpowerCommand.QUERY_NO_POWERS, "%s has no superpowers");
            this.add(SuperpowerCommand.SET_SUCCESS_SINGLE, "%s has gained the superpower %s");
            this.add(SuperpowerCommand.SET_SUCCESS_MULTIPLE, "%s entities have gained the superpower %s");
            this.add(SuperpowerCommand.ADD_ERROR_ALREADY_HAS, "%s already has that superpower");
            this.add(SuperpowerCommand.ADD_ERROR_NOT_POSSIBLE, "This superpower can not be added to %s anymore");
            this.add(SuperpowerCommand.ADD_ERROR_NONE_ADDED, "No entity was able to gain the superpower %s");
            this.add(SuperpowerCommand.ADD_SUCCESS_SINGLE, "%s has gained the superpower %s");
            this.add(SuperpowerCommand.ADD_SUCCESS_MULTIPLE, "%s entities have gained the superpower %s");
            this.add(SuperpowerCommand.REMOVE_ERROR_NO_MATCH, "No matching superpower was found");
            this.add(SuperpowerCommand.REMOVE_SUCCESS_SINGLE, "The superpower(s) of %s were removed");
            this.add(SuperpowerCommand.REMOVE_SUCCESS_MULTIPLE, "The superpower(s) of %s entities were removed");
            this.add(SuperpowerCommand.REPLACE_SUCCESS_SINGLE, "Some of %s's superpowers have been replaced with %s");
            this.add(SuperpowerCommand.REPLACE_SUCCESS_MULTIPLE, "Some of %s entities' superpowers have been replaced with %s");
            this.add(SuperpowerCommand.REPLACE_ERROR_NOT_POSSIBLE, "This superpower can not be added to %s anymore");
            this.add(SuperpowerCommand.REPLACE_ERROR_NONE_ADDED, "No entity was able to gain the superpower %s");

            this.add(DataAttachmentCommand.TRANS_UNKNOWN_TYPE, "Unknown data attachment type: %s");
            this.add(DataAttachmentCommand.TRANS_NO_DATA, "No data found for data attachment type %s");
            this.add(DataAttachmentCommand.TRANS_GET_DATA, "Data attachment value %s for %s is %s");
            this.add(DataAttachmentCommand.TRANS_PARSE_ERROR, "Error while parsing data attachment value");
            this.add(DataAttachmentCommand.TRANS_SET_DATA_SINGLE, "Data attachment value %s for %s is now set to %s");
            this.add(DataAttachmentCommand.TRANS_SET_DATA_MULTIPLE, "Data attachment value %s for %s entities is now set to %s");

            // GUI
            this.add("gui.palladium.powers", "Powers");
            this.add("gui.palladium.powers.buy_ability", "Do you want to unlock this ability?");
            this.add("gui.palladium.powers.buy_ability.or", "or");
            this.add("gui.palladium.powers.buy_ability.experience_level", "%sx experience level");
            this.add("gui.palladium.powers.buy_ability.experience_level_plural", "%sx experience levels");
            this.add(PlayerCustomizationScreen.TITLE_TRANSLATION_KEY, "Customization");

            this.addCustomizationCategory(CustomizationCategories.HAT, "Hat");
            this.addCustomizationCategory(CustomizationCategories.HEAD, "Head");
            this.addCustomizationCategory(CustomizationCategories.CHEST, "Chest");
            this.addCustomizationCategory(CustomizationCategories.BACK, "Back");
            this.addCustomizationCategory(CustomizationCategories.ARMS, "Arms");
            this.addCustomizationCategory(CustomizationCategories.LEGS, "Legs");
        }
    }

    public static class German extends PalladiumLangProvider {

        public German(PackOutput output) {
            super(output, Palladium.MOD_ID, "de_de");
        }

        @Override
        protected void addTranslations() {
            // Items
            this.add(PalladiumItems.SUIT_STAND.get(), "Anzugsständer");

            // Config
            this.addConfigEntry(PalladiumClientConfig.ABILITY_BAR_ALIGNMENT, "Ability Bar - Position");
            this.addConfigEntry(PalladiumClientConfig.ABILITY_BAR_KEY_BIND_DISPLAY, "Ability Bar - Anzeige der Tastenbelegung");
            this.addConfigEntry(PalladiumClientConfig.HIDE_EXPERIMENTAL_WARNING, "Singleplayer - Experimental Settings Warnung ausblenden");
            this.addConfigEntry(PalladiumServerConfig.MAX_SUPERPOWER_SETS, "Max. Anzahl an Superkraft-Sets");

            // Key Mappings
            this.add("key.palladium.categories.powers", "Kräfte");
            this.addKeyMapping("open_equipment", "Ausrüstung öffnen/schließen");
            this.addKeyMapping("show_powers", "Kräfte");
            this.addKeyMapping("rotate_ability_list", "Durch Fähigkeitenleiste rotieren");
            for (int i = 1; i <= 5; i++) {
                this.addKeyMapping("ability_" + i, "Fähigkeit #" + i);
            }

            // Abilities
            this.addAbility(AbilitySerializers.DUMMY, "Dummy");
            this.addAbility(AbilitySerializers.COMMAND, "Befehl");
//            this.addAbility(AbilitySerializers.RENDER_LAYER_BY_ACCESSORY_SLOT, "Render Layer");
            this.addAbility(AbilitySerializers.SHRINK_BODY_OVERLAY, "Körperoverlay schrumpfen");
            this.addAbility(AbilitySerializers.ATTRIBUTE_MODIFIER, "Attributmodifikator");
            this.addAbility(AbilitySerializers.HEALING, "Heilung");
            this.addAbility(AbilitySerializers.SLOWFALL, "Langsamer Fall");
            this.addAbility(AbilitySerializers.DAMAGE_IMMUNITY, "Schadensimmunität");
            this.addAbility(AbilitySerializers.INVISIBILITY, "Unsichtbarkeit");
            this.addAbility(AbilitySerializers.BEAM, "Energiestrahl");
            this.addAbility(AbilitySerializers.SIZE, "Größe");
//            this.addAbility(AbilitySerializers.PROJECTILE, "Projektil");
            this.addAbility(AbilitySerializers.SKIN_CHANGE, "Skin Änderung");
            this.addAbility(AbilitySerializers.AIM, "Zielen");
            this.addAbility(AbilitySerializers.HIDE_MODEL_PART, "Körperteile verstecken");
            this.addAbility(AbilitySerializers.SHADER_EFFECT, "Shader Effekt");
            this.addAbility(AbilitySerializers.GUI_OVERLAY, "GUI-Overlay");
            this.addAbility(AbilitySerializers.SHOW_BOTH_ARMS, "Beide Arme zeigen");
//            this.addAbility(AbilitySerializers.PLAYER_ANIMATION, "Spieler-Animation");
            this.addAbility(AbilitySerializers.FLUID_WALKING, "Auf Flüssigkeit Laufen");
            this.addAbility(AbilitySerializers.RESTRICT_SLOTS, "Slots beschränken");
            this.addAbility(AbilitySerializers.PLAY_SOUND, "Sound");
            this.addAbility(AbilitySerializers.VIBRATE, "Vibrieren");
            this.addAbility(AbilitySerializers.INTANGIBILITY, "Ungreifbarkeit");
            this.addAbility(AbilitySerializers.NAME_CHANGE, "Namesänderung");
            this.addAbility(AbilitySerializers.SCULK_IMMUNITY, "Sculk-Immunität");
            this.addAbility(AbilitySerializers.FIRE_ASPECT, "Verbrennung");
            this.addAbility(AbilitySerializers.PARTICLES, "Partikel");
            this.addAbility(GeckoLibCompat.TRIGGER_LAYER_ANIMATION, "Geo Animation");

            // Commands
            this.add(SuperpowerCommand.ERROR_NO_LIVING_ENTITY, "%s ist kein lebendes Wesen");
            this.add(SuperpowerCommand.QUERY_SUCCESS, "%s hat folgende Superkräfte: %s");
            this.add(SuperpowerCommand.QUERY_NO_POWERS, "%s hat keine Superkräfte");
            this.add(SuperpowerCommand.SET_SUCCESS_SINGLE, "%s hat die Superkraft %s erhalten");
            this.add(SuperpowerCommand.SET_SUCCESS_MULTIPLE, "%s Wesen haben die Superkraft %s erhalten");
            this.add(SuperpowerCommand.ADD_ERROR_ALREADY_HAS, "%s besitzt diese Superkraft bereits");
            this.add(SuperpowerCommand.ADD_ERROR_NOT_POSSIBLE, "Diese Superkraft kann %s nicht mehr hinzugefügt werden");
            this.add(SuperpowerCommand.ADD_ERROR_NONE_ADDED, "Keinem Wesen konnte die Superkraft %s verliehen werden");
            this.add(SuperpowerCommand.ADD_SUCCESS_SINGLE, "%s hat die Superkraft %s erhalten");
            this.add(SuperpowerCommand.ADD_SUCCESS_MULTIPLE, "%s Wesen haben die Superkraft %s erhalten");
            this.add(SuperpowerCommand.REMOVE_ERROR_NO_MATCH, "Keine passende Superkraft wurde gefunden");
            this.add(SuperpowerCommand.REMOVE_SUCCESS_SINGLE, "Die Superkraft(e) von %s wurde(n) entfernt");
            this.add(SuperpowerCommand.REMOVE_SUCCESS_MULTIPLE, "Die Superkraft(e) von %s Wesen wurden entfernt");
            this.add(SuperpowerCommand.REPLACE_SUCCESS_SINGLE, "Einige von %ss Superkräften wurden durch %s ersetzt");
            this.add(SuperpowerCommand.REPLACE_SUCCESS_MULTIPLE, "Einige der Superkräfte von %s Wesen wurden durch %s ersetzt");
            this.add(SuperpowerCommand.REPLACE_ERROR_NOT_POSSIBLE, "Diese Superkraft kann %s nicht mehr hinzugefügt werden");
            this.add(SuperpowerCommand.REPLACE_ERROR_NONE_ADDED, "Keinem Wesen konnte die Superkraft %s verliehen werden");

            this.add(DataAttachmentCommand.TRANS_UNKNOWN_TYPE, "Unbekannter Datenanhang: %s");
            this.add(DataAttachmentCommand.TRANS_NO_DATA, "Keine Daten für Datenanlagentyp %s gefunden");
            this.add(DataAttachmentCommand.TRANS_GET_DATA, "Der Wert der Datenanlage %s für %s ist %s");
            this.add(DataAttachmentCommand.TRANS_PARSE_ERROR, "Fehler beim Parsen von Datenanhangswerten");
            this.add(DataAttachmentCommand.TRANS_SET_DATA_SINGLE, "Datenanhangswert %s für %s ist jetzt auf %s gesetzt");
            this.add(DataAttachmentCommand.TRANS_SET_DATA_MULTIPLE, "Datenanhangswert %s für %s Entitäten ist jetzt auf %s gesetzt");

            // GUI
            this.add("gui.palladium.powers", "Kräfte");
            this.add("gui.palladium.powers.buy_ability", "Möchtest du diese Fähigkeit freischalten?");
            this.add("gui.palladium.powers.buy_ability.or", "oder");
            this.add("gui.palladium.powers.buy_ability.experience_level", "%sx Erfahrungsstufe");
            this.add("gui.palladium.powers.buy_ability.experience_level_plural", "%sx Erfahrungsstufen");
            this.add(PlayerCustomizationScreen.TITLE_TRANSLATION_KEY, "Anpassungen");

            this.addCustomizationCategory(CustomizationCategories.HAT, "Hut");
            this.addCustomizationCategory(CustomizationCategories.HEAD, "Kopf");
            this.addCustomizationCategory(CustomizationCategories.CHEST, "Brust");
            this.addCustomizationCategory(CustomizationCategories.BACK, "Rücken");
            this.addCustomizationCategory(CustomizationCategories.ARMS, "Arme");
            this.addCustomizationCategory(CustomizationCategories.LEGS, "Beine");
        }
    }
    
    public static class Saxon extends PalladiumLangProvider {

        public Saxon(PackOutput output) {
            super(output, Palladium.MOD_ID, "sxu");
        }

        @Override
        protected void addTranslations() {
            // Items
            this.add(PalladiumItems.SUIT_STAND.get(), "Anzuchsschdändorr");

            // Config
            this.addConfigEntry(PalladiumClientConfig.ABILITY_BAR_ALIGNMENT, "Ability Bar - Position");
            this.addConfigEntry(PalladiumClientConfig.ABILITY_BAR_KEY_BIND_DISPLAY, "Ability Bar - Anzeige der Tastenbelegung");
            this.addConfigEntry(PalladiumClientConfig.HIDE_EXPERIMENTAL_WARNING, "Singleplayer - Experimental Settings Warnung ausblenden");
            this.addConfigEntry(PalladiumServerConfig.MAX_SUPERPOWER_SETS, "Max. Anzahl an Superkraft-Sets");

            // Key Mappings
            this.add("key.palladium.categories.powers", "Gräfte");
            this.addKeyMapping("open_equipment", "Ohsrüstung öffnen/schließen");
            this.addKeyMapping("show_powers", "Gräfte");
            this.addKeyMapping("rotate_ability_list", "Durch Fähischgehtlehste rotieren");
            for (int i = 1; i <= 5; i++) {
                this.addKeyMapping("ability_" + i, "Fähischgeht #" + i);
            }

            // Abilities
            this.addAbility(AbilitySerializers.DUMMY, "Dummy");
            this.addAbility(AbilitySerializers.COMMAND, "Befehl");
//            this.addAbility(AbilitySerializers.RENDER_LAYER_BY_ACCESSORY_SLOT, "Render Layer");
            this.addAbility(AbilitySerializers.SHRINK_BODY_OVERLAY, "Körperoverlay schrumpfen");
            this.addAbility(AbilitySerializers.ATTRIBUTE_MODIFIER, "Ättribütmodifikator");
            this.addAbility(AbilitySerializers.HEALING, "Helung");
            this.addAbility(AbilitySerializers.SLOWFALL, "Langsamer Fall");
            this.addAbility(AbilitySerializers.DAMAGE_IMMUNITY, "Schadensimmunität");
            this.addAbility(AbilitySerializers.INVISIBILITY, "Unsischtbarkeet");
            this.addAbility(AbilitySerializers.BEAM, "Energiestrahl");
            this.addAbility(AbilitySerializers.SIZE, "Größe");
//            this.addAbility(AbilitySerializers.PROJECTILE, "Projektil");
            this.addAbility(AbilitySerializers.SKIN_CHANGE, "Skin Änderung");
            this.addAbility(AbilitySerializers.AIM, "Zielen");
            this.addAbility(AbilitySerializers.HIDE_MODEL_PART, "Görperdeile versteggen");
            this.addAbility(AbilitySerializers.SHADER_EFFECT, "Shader Effekt");
            this.addAbility(AbilitySerializers.GUI_OVERLAY, "GUI-Overlay");
            this.addAbility(AbilitySerializers.SHOW_BOTH_ARMS, "Beide Arme zeijen");
//            this.addAbility(AbilitySerializers.PLAYER_ANIMATION, "Spieler-Animation");
            this.addAbility(AbilitySerializers.FLUID_WALKING, "Uff Flüssichkeht Lofen");
            this.addAbility(AbilitySerializers.RESTRICT_SLOTS, "Slots beschränken");
            this.addAbility(AbilitySerializers.PLAY_SOUND, "Sound");
            this.addAbility(AbilitySerializers.VIBRATE, "Vibrieren");
            this.addAbility(AbilitySerializers.INTANGIBILITY, "Ungreifbarkeht");
            this.addAbility(AbilitySerializers.NAME_CHANGE, "Namesänderung");
            this.addAbility(AbilitySerializers.SCULK_IMMUNITY, "Sculk-Immunität");
            this.addAbility(AbilitySerializers.FIRE_ASPECT, "Vorbrennung");
            this.addAbility(AbilitySerializers.PARTICLES, "Partikel");
            this.addAbility(GeckoLibCompat.TRIGGER_LAYER_ANIMATION, "Geo Animation");

            // Commands
            this.add(SuperpowerCommand.ERROR_NO_LIVING_ENTITY, "%s is keen lebdisches Viech");
            this.add(SuperpowerCommand.QUERY_SUCCESS, "%s hat de folgende Superkrächte: %s");
            this.add(SuperpowerCommand.QUERY_NO_POWERS, "%s hat keene Superkrächte");
            this.add(SuperpowerCommand.SET_SUCCESS_SINGLE, "%s hat nu die Superkraft %s bekomm");
            this.add(SuperpowerCommand.SET_SUCCESS_MULTIPLE, "%s Viecher ham nu die Superkraft %s bekomm");
            this.add(SuperpowerCommand.ADD_ERROR_ALREADY_HAS, "%s hat die Superkraft doch schon");
            this.add(SuperpowerCommand.ADD_ERROR_NOT_POSSIBLE, "Die Superkraft kann mer %s nich mehr geb'n");
            this.add(SuperpowerCommand.ADD_ERROR_NONE_ADDED, "Keins von de Viecher konnt die Superkraft %s kriegn");
            this.add(SuperpowerCommand.ADD_SUCCESS_SINGLE, "%s hat nu die Superkraft %s bekomm");
            this.add(SuperpowerCommand.ADD_SUCCESS_MULTIPLE, "%s Viecher ham nu die Superkraft %s bekomm");
            this.add(SuperpowerCommand.REMOVE_ERROR_NO_MATCH, "Keene passnde Superkraft gefundn");
            this.add(SuperpowerCommand.REMOVE_SUCCESS_SINGLE, "Die Superkraft(e) von %s sin nu wech");
            this.add(SuperpowerCommand.REMOVE_SUCCESS_MULTIPLE, "Die Superkraft(e) von %s Viechern sin nu wech");
            this.add(SuperpowerCommand.REPLACE_SUCCESS_SINGLE, "E paar von %ss Superkrächte sin nu %s");
            this.add(SuperpowerCommand.REPLACE_SUCCESS_MULTIPLE, "E paar Superkrächte von %s Viechern sin nu %s");
            this.add(SuperpowerCommand.REPLACE_ERROR_NOT_POSSIBLE, "Die Superkraft kann mer %s nich mehr geb'n");
            this.add(SuperpowerCommand.REPLACE_ERROR_NONE_ADDED, "Keins von de Viecher konnt die Superkraft %s kriegn");

            this.add(DataAttachmentCommand.TRANS_UNKNOWN_TYPE, "Unbegannter Datenanhang: %s");
            this.add(DataAttachmentCommand.TRANS_NO_DATA, "Kehne Daten für Datenanlagentyp %s jefunden");
            this.add(DataAttachmentCommand.TRANS_GET_DATA, "Der Wert dor Datenanlage %s für %s is %s");
            this.add(DataAttachmentCommand.TRANS_PARSE_ERROR, "Fehler beim Parsen von Datenanhangswerten");
            this.add(DataAttachmentCommand.TRANS_SET_DATA_SINGLE, "Datenanhangswert %s für %s jetze uff %s jesetzt");
            this.add(DataAttachmentCommand.TRANS_SET_DATA_MULTIPLE, "Datenanhangswert %s für %s Entitäten is jetze uff %s jesetzt");

            // GUI
            this.add("gui.palladium.powers", "Kräfte");
            this.add("gui.palladium.powers.buy_ability", "Möschtest'e disse Fähischgeht freischaltn?");
            this.add("gui.palladium.powers.buy_ability.or", "oder");
            this.add("gui.palladium.powers.buy_ability.experience_level", "%sx Erfahrungsschdufe");
            this.add("gui.palladium.powers.buy_ability.experience_level_plural", "%sx Erfahrungsschdufen");
            this.add(PlayerCustomizationScreen.TITLE_TRANSLATION_KEY, "Anpassungen");

            this.addCustomizationCategory(CustomizationCategories.HAT, "Hood");
            this.addCustomizationCategory(CustomizationCategories.HEAD, "Nüschl");
            this.addCustomizationCategory(CustomizationCategories.CHEST, "Bruscht");
            this.addCustomizationCategory(CustomizationCategories.BACK, "Rieggn");
            this.addCustomizationCategory(CustomizationCategories.ARMS, "Arme");
            this.addCustomizationCategory(CustomizationCategories.LEGS, "Behne");
        }
    }
    
}
