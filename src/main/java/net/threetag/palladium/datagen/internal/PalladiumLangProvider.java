package net.threetag.palladium.datagen.internal;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Util;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.block.PalladiumBlocks;
import net.threetag.palladium.client.gui.screen.customization.CustomizationsGrid;
import net.threetag.palladium.client.gui.screen.customization.EyeSelectionScreen;
import net.threetag.palladium.client.gui.screen.customization.PlayerCustomizationScreen;
import net.threetag.palladium.client.gui.toast.CustomizationToast;
import net.threetag.palladium.client.gui.widget.PowerTreeWidget;
import net.threetag.palladium.command.CustomizationCommand;
import net.threetag.palladium.command.DataAttachmentCommand;
import net.threetag.palladium.command.ScreenCommand;
import net.threetag.palladium.command.SuperpowerCommand;
import net.threetag.palladium.compat.geckolib.GeckoLibCompat;
import net.threetag.palladium.config.PalladiumClientConfig;
import net.threetag.palladium.config.PalladiumServerConfig;
import net.threetag.palladium.customization.BuiltinCustomization;
import net.threetag.palladium.customization.Customization;
import net.threetag.palladium.customization.CustomizationCategories;
import net.threetag.palladium.customization.CustomizationCategory;
import net.threetag.palladium.item.PalladiumCreativeTabs;
import net.threetag.palladium.item.PalladiumItems;
import net.threetag.palladium.power.Power;
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

    public void addCreativeModeTab(Holder<? extends CreativeModeTab> key, String name) {
        this.add(key.value(), name);
    }

    public void add(CreativeModeTab key, String name) {
        Identifier id = BuiltInRegistries.CREATIVE_MODE_TAB.getKey(key);
        this.add(Util.makeDescriptionId("itemGroup", id), name);
    }

    public void addPower(ResourceKey<Power> power, String name) {
        this.add(Util.makeDescriptionId("power", power.identifier()), name);
    }

    public void addAbility(Holder<? extends AbilitySerializer<?>> key, String name) {
        this.add(key.value(), name);
    }

    public void add(AbilitySerializer<?> key, String name) {
        Identifier id = PalladiumRegistries.ABILITY_SERIALIZER.getKey(key);
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
            // Blocks
            this.addBlock(PalladiumBlocks.TAILORING_BENCH, "Tailoring Bench");
            this.addBlock(PalladiumBlocks.METEORITE_STONE, "Meteorite Stone");
            this.addBlock(PalladiumBlocks.METEORITE_BRICKS, "Meteorite Bricks");
            this.addBlock(PalladiumBlocks.METEORITE_COAL_ORE, "Meteorite Coal Ore");
            this.addBlock(PalladiumBlocks.METEORITE_IRON_ORE, "Meteorite Iron Ore");
            this.addBlock(PalladiumBlocks.METEORITE_COPPER_ORE, "Meteorite Copper Ore");
            this.addBlock(PalladiumBlocks.METEORITE_GOLD_ORE, "Meteorite Gold Ore");
            this.addBlock(PalladiumBlocks.METEORITE_REDSTONE_ORE, "Meteorite Redstone Ore");
            this.addBlock(PalladiumBlocks.METEORITE_EMERALD_ORE, "Meteorite Emerald Ore");
            this.addBlock(PalladiumBlocks.METEORITE_LAPIS_ORE, "Meteorite Lapis Lazuli Ore");
            this.addBlock(PalladiumBlocks.METEORITE_DIAMOND_ORE, "Meteorite Diamond Ore");
            this.addBlock(PalladiumBlocks.METEORITE_VIBRANIUM_ORE, "Meteorite Vibranium Ore");
            this.addBlock(PalladiumBlocks.METEORITE_VIBRANIUM_VEIN, "Meteorite Vibranium Vein");
            this.addBlock(PalladiumBlocks.VIBRANIUM_BLOCK, "Block of Vibranium");
            this.addBlock(PalladiumBlocks.RAW_VIBRANIUM_BLOCK, "Block of Raw Vibranium");

            // Items
            this.addCreativeModeTab(PalladiumCreativeTabs.MAIN, "Palladium");
            this.addItem(PalladiumItems.SUIT_STAND, "Suit Stand");
            this.addItem(PalladiumItems.WHITE_FABRIC, "White Fabric");
            this.addItem(PalladiumItems.ORANGE_FABRIC, "Orange Fabric");
            this.addItem(PalladiumItems.MAGENTA_FABRIC, "Magenta Fabric");
            this.addItem(PalladiumItems.LIGHT_BLUE_FABRIC, "Light Blue Fabric");
            this.addItem(PalladiumItems.YELLOW_FABRIC, "Yellow Fabric");
            this.addItem(PalladiumItems.LIME_FABRIC, "Lime Fabric");
            this.addItem(PalladiumItems.PINK_FABRIC, "Pink Fabric");
            this.addItem(PalladiumItems.GRAY_FABRIC, "Gray Fabric");
            this.addItem(PalladiumItems.LIGHT_GRAY_FABRIC, "Light Gray Fabric");
            this.addItem(PalladiumItems.CYAN_FABRIC, "Cyan Fabric");
            this.addItem(PalladiumItems.PURPLE_FABRIC, "Purple Fabric");
            this.addItem(PalladiumItems.BLUE_FABRIC, "Blue Fabric");
            this.addItem(PalladiumItems.BROWN_FABRIC, "Brown Fabric");
            this.addItem(PalladiumItems.GREEN_FABRIC, "Green Fabric");
            this.addItem(PalladiumItems.RED_FABRIC, "Red Fabric");
            this.addItem(PalladiumItems.BLACK_FABRIC, "Black Fabric");
            this.addItem(PalladiumItems.RAW_VIBRANIUM, "Raw Vibranium");
            this.addItem(PalladiumItems.VIBRANIUM_INGOT, "Vibranium Ingot");
            this.addItem(PalladiumItems.VIBRANIUM_NUGGET, "Vibranium Nugget");

            // Abilities
            this.addAbility(AbilitySerializers.DUMMY, "Dummy");
            this.addAbility(AbilitySerializers.COMMAND, "Command");
            this.addAbility(AbilitySerializers.SHRINK_PLAYER_OVERLAY, "Shrink Body Overlay");
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
            this.addAbility(AbilitySerializers.FLUID_WALKING, "Fluid Walking");
            this.addAbility(AbilitySerializers.RESTRICT_SLOTS, "Restrict Slots");
            this.addAbility(AbilitySerializers.PLAY_SOUND, "Sound");
            this.addAbility(AbilitySerializers.INTANGIBILITY, "Intangibility");
            this.addAbility(AbilitySerializers.NAME_CHANGE, "Name Change");
            this.addAbility(AbilitySerializers.SCULK_IMMUNITY, "Sculk Immunity");
            this.addAbility(AbilitySerializers.FIRE_ASPECT, "Fire Aspect");
            this.addAbility(AbilitySerializers.PARTICLES, "Particles");
            this.addAbility(AbilitySerializers.FLIGHT, "Flight");
            this.addAbility(AbilitySerializers.GLIDING, "Gliding");
            this.addAbility(AbilitySerializers.WALL_CLIMBING, "Wall Climbing");
            this.addAbility(GeckoLibCompat.TRIGGER_LAYER_ANIMATION, "Geo Animation");

            // Config
            this.addConfigEntry(PalladiumClientConfig.DEV_MODE, "Addon Developer Mode");
            this.addConfigEntry(PalladiumClientConfig.ABILITY_BAR_ALIGNMENT, "Ability Bar - Alignment");
            this.addConfigEntry(PalladiumClientConfig.ABILITY_BAR_KEY_BIND_DISPLAY, "Ability Bar - Key Bind Display");
            this.addConfigEntry(PalladiumClientConfig.HIDE_EXPERIMENTAL_WARNING, "Singleplayer - Hide Experimental Settings Warning");
            this.addConfigEntry(PalladiumClientConfig.SCALE_CAMERA_FIX, "Visuals - Fix Camera Scale");
            this.addConfigEntry(PalladiumServerConfig.MAX_SUPERPOWER_SETS, "Max. amount of superpower sets");

            // Key Mappings
            this.add("key.palladium.categories.powers", "Powers");
            this.addKeyMapping("open_equipment", "Open/Close Equipment");
            this.addKeyMapping("show_powers", "Powers");
            this.addKeyMapping("rotate_ability_list", "Rotate through ability bar");
            for (int i = 1; i <= 5; i++) {
                this.addKeyMapping("ability_" + i, "Ability #" + i);
            }

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

            this.add(CustomizationCommand.UNLOCK_SUCCESS, "Successfully unlocked %s for %s");
            this.add(CustomizationCommand.LOCK_SUCCESS, "Successfully locked %s for %s");
            this.add(CustomizationCommand.ERROR_NOT_UNLOCKABLE, "%s can not be unlocked by commands");
            this.add(CustomizationCommand.ERROR_ALREADY_UNLOCKED, "%s is already unlocked");
            this.add(CustomizationCommand.ERROR_ALREADY_LOCKED, "%s is already locked");
            this.add(CustomizationCommand.ERROR_CANT_HAVE_CUSTOMIZATIONS, "The given entity can not have customizations");

            this.add(ScreenCommand.TRANS_SHOWED_SCREEN_SINGLE, "Opened screen %s for %s");
            this.add(ScreenCommand.TRANS_SHOWED_SCREEN_MULTIPLE, "Opened screen %s for %s players");

            // Container
            this.add("container.palladium.tailoring", "Tailoring");
            this.add("container.palladium.tailoring.craft", "Create");

            // GUI
            this.add(PowerTreeWidget.TRANS_TITLE, "Powers");
            this.add(PowerTreeWidget.TRANS_CUSTOMIZE, "Customize");
            this.add(PowerTreeWidget.TRANS_KEY_UNLOCK, "Unlock");
            this.add(PowerTreeWidget.TRANS_KEY_NO_ABILITIES_LABEL, "There doesn't seem to be anything here...");
            this.add(PowerTreeWidget.TRANS_KEY_VERY_SAD_LABEL, ":(");
            this.add("gui.palladium.powers.buy_ability.experience_level", "%sx experience level");
            this.add("gui.palladium.powers.buy_ability.experience_level_plural", "%sx experience levels");
            this.add(PlayerCustomizationScreen.TITLE_TRANSLATION_KEY, "Customization");
            this.add(EyeSelectionScreen.TRANS_TITLE, "Select your eyes");
            this.add(EyeSelectionScreen.TRANS_RESET, "Reset");
            this.add(EyeSelectionScreen.TRANS_SAVE, "Save");
            this.add(CustomizationsGrid.NO_CUSTOMIZATIONS_LABEL, "There doesn't seem to be anything here...");
            this.add(CustomizationsGrid.VERY_SAD_LABEL, ":(");

            // Toasts
            this.add(CustomizationToast.TITLE_TEXT, "New Customization Unlocked!");

            // Customizations
            this.addCustomization(BuiltinCustomization.Type.LUCRAFT_ARC_REACTOR.getResourceKey(), "Lucraft Arc Reactor");
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
            // Blocks
            this.addBlock(PalladiumBlocks.TAILORING_BENCH, "Schneidertisch");
            this.addBlock(PalladiumBlocks.METEORITE_STONE, "Meteoritengestein");
            this.addBlock(PalladiumBlocks.METEORITE_BRICKS, "Meteoritenziegel");
            this.addBlock(PalladiumBlocks.METEORITE_COAL_ORE, "Meteoriten-Steinkohle");
            this.addBlock(PalladiumBlocks.METEORITE_IRON_ORE, "Meteoriten-Eisenerz");
            this.addBlock(PalladiumBlocks.METEORITE_COPPER_ORE, "Meteoriten-Kupferz");
            this.addBlock(PalladiumBlocks.METEORITE_GOLD_ORE, "Meteoriten-Golderz");
            this.addBlock(PalladiumBlocks.METEORITE_REDSTONE_ORE, "Meteoriten-Redstone-Erz");
            this.addBlock(PalladiumBlocks.METEORITE_EMERALD_ORE, "Meteoriten-Smaragderz");
            this.addBlock(PalladiumBlocks.METEORITE_LAPIS_ORE, "Meteoriten-Lapislazulierz");
            this.addBlock(PalladiumBlocks.METEORITE_DIAMOND_ORE, "Meteoriten-Diamanterz");
            this.addBlock(PalladiumBlocks.METEORITE_VIBRANIUM_ORE, "Meteoriten-Vibraniumerz");
            this.addBlock(PalladiumBlocks.METEORITE_VIBRANIUM_VEIN, "Meteoriten-Vibraniumvene");
            this.addBlock(PalladiumBlocks.VIBRANIUM_BLOCK, "Vibraniumblock");
            this.addBlock(PalladiumBlocks.RAW_VIBRANIUM_BLOCK, "Rohvibraniumblock");

            // Items
            this.addCreativeModeTab(PalladiumCreativeTabs.MAIN, "Palladium");
            this.addItem(PalladiumItems.SUIT_STAND, "Anzugsständer");
            this.addItem(PalladiumItems.WHITE_FABRIC, "Weißer Stoff");
            this.addItem(PalladiumItems.ORANGE_FABRIC, "Oranger Stoff");
            this.addItem(PalladiumItems.MAGENTA_FABRIC, "Magenta Stoff");
            this.addItem(PalladiumItems.LIGHT_BLUE_FABRIC, "Hellblauer Stoff");
            this.addItem(PalladiumItems.YELLOW_FABRIC, "Gelber Stoff");
            this.addItem(PalladiumItems.LIME_FABRIC, "Hellgrüner Stoff");
            this.addItem(PalladiumItems.PINK_FABRIC, "Rosa Stoff");
            this.addItem(PalladiumItems.GRAY_FABRIC, "Grauer Stoff");
            this.addItem(PalladiumItems.LIGHT_GRAY_FABRIC, "Hellgrauer Stoff");
            this.addItem(PalladiumItems.CYAN_FABRIC, "Türkiser Stoff");
            this.addItem(PalladiumItems.PURPLE_FABRIC, "Violetter Stoff");
            this.addItem(PalladiumItems.BLUE_FABRIC, "Blauer Stoff");
            this.addItem(PalladiumItems.BROWN_FABRIC, "Brauner Stoff");
            this.addItem(PalladiumItems.GREEN_FABRIC, "Grüner Stoff");
            this.addItem(PalladiumItems.RED_FABRIC, "Roter Stoff");
            this.addItem(PalladiumItems.BLACK_FABRIC, "Schwarzer Stoff");
            this.addItem(PalladiumItems.RAW_VIBRANIUM, "Rohvibranium");
            this.addItem(PalladiumItems.VIBRANIUM_INGOT, "Vibraniumbarren");
            this.addItem(PalladiumItems.VIBRANIUM_NUGGET, "Vibraniumklumpen");

            // Abilities
            this.addAbility(AbilitySerializers.DUMMY, "Dummy");
            this.addAbility(AbilitySerializers.COMMAND, "Befehl");
            this.addAbility(AbilitySerializers.SHRINK_PLAYER_OVERLAY, "Körperoverlay schrumpfen");
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
            this.addAbility(AbilitySerializers.FLUID_WALKING, "Auf Flüssigkeit Laufen");
            this.addAbility(AbilitySerializers.RESTRICT_SLOTS, "Slots beschränken");
            this.addAbility(AbilitySerializers.PLAY_SOUND, "Sound");
            this.addAbility(AbilitySerializers.INTANGIBILITY, "Ungreifbarkeit");
            this.addAbility(AbilitySerializers.NAME_CHANGE, "Namesänderung");
            this.addAbility(AbilitySerializers.SCULK_IMMUNITY, "Sculk-Immunität");
            this.addAbility(AbilitySerializers.FIRE_ASPECT, "Verbrennung");
            this.addAbility(AbilitySerializers.PARTICLES, "Partikel");
            this.addAbility(AbilitySerializers.FLIGHT, "Flug");
            this.addAbility(AbilitySerializers.GLIDING, "Gleiten");
            this.addAbility(AbilitySerializers.WALL_CLIMBING, "Wandklettern");
            this.addAbility(GeckoLibCompat.TRIGGER_LAYER_ANIMATION, "Geo Animation");

            // Config
            this.addConfigEntry(PalladiumClientConfig.ABILITY_BAR_ALIGNMENT, "Addon Entwickler Modus");
            this.addConfigEntry(PalladiumClientConfig.ABILITY_BAR_KEY_BIND_DISPLAY, "Ability Bar - Anzeige der Tastenbelegung");
            this.addConfigEntry(PalladiumClientConfig.HIDE_EXPERIMENTAL_WARNING, "Singleplayer - Experimental Settings Warnung ausblenden");
            this.addConfigEntry(PalladiumClientConfig.SCALE_CAMERA_FIX, "Visuell - Kameraskalierung fixen");
            this.addConfigEntry(PalladiumServerConfig.MAX_SUPERPOWER_SETS, "Max. Anzahl an Superkraft-Sets");

            // Key Mappings
            this.add("key.palladium.categories.powers", "Kräfte");
            this.addKeyMapping("open_equipment", "Ausrüstung öffnen/schließen");
            this.addKeyMapping("show_powers", "Kräfte");
            this.addKeyMapping("rotate_ability_list", "Durch Fähigkeitenleiste rotieren");
            for (int i = 1; i <= 5; i++) {
                this.addKeyMapping("ability_" + i, "Fähigkeit #" + i);
            }

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

            this.add(CustomizationCommand.UNLOCK_SUCCESS, "%s wurde erfolgreich für %s freigeschaltet");
            this.add(CustomizationCommand.LOCK_SUCCESS, "%s wurde erfolgreich für %s gesperrt");
            this.add(CustomizationCommand.ERROR_NOT_UNLOCKABLE, "%s kann nicht per Befehl freigeschaltet werden");
            this.add(CustomizationCommand.ERROR_ALREADY_UNLOCKED, "%s ist bereits freigeschaltet");
            this.add(CustomizationCommand.ERROR_ALREADY_LOCKED, "%s ist bereits gesperrt");
            this.add(CustomizationCommand.ERROR_CANT_HAVE_CUSTOMIZATIONS, "Die angegebene Entität kann keine Anpassungen besitzen");

            this.add(ScreenCommand.TRANS_SHOWED_SCREEN_SINGLE, "Fenster %s für %s geöffnet");
            this.add(ScreenCommand.TRANS_SHOWED_SCREEN_MULTIPLE, "Fenster %s für %s Spieler geöffnet");

            // Container
            this.add("container.palladium.tailoring", "Zuschneidern");
            this.add("container.palladium.tailoring.craft", "Herstellen");

            // GUI
            this.add(PowerTreeWidget.TRANS_TITLE, "Kräfte");
            this.add(PowerTreeWidget.TRANS_CUSTOMIZE, "Anpassen");
            this.add(PowerTreeWidget.TRANS_KEY_UNLOCK, "Freischalten");
            this.add(PowerTreeWidget.TRANS_KEY_NO_ABILITIES_LABEL, "Hier scheint es nichts zu geben...");
            this.add(PowerTreeWidget.TRANS_KEY_VERY_SAD_LABEL, ":(");
            this.add("gui.palladium.powers.buy_ability.experience_level", "%sx Erfahrungsstufe");
            this.add("gui.palladium.powers.buy_ability.experience_level_plural", "%sx Erfahrungsstufen");
            this.add(PlayerCustomizationScreen.TITLE_TRANSLATION_KEY, "Anpassungen");
            this.add(EyeSelectionScreen.TRANS_TITLE, "Wähle deine Augen aus");
            this.add(EyeSelectionScreen.TRANS_RESET, "Zurücksetzen");
            this.add(EyeSelectionScreen.TRANS_SAVE, "Speichern");
            this.add(CustomizationsGrid.NO_CUSTOMIZATIONS_LABEL, "Hier scheint es nichts zu geben...");
            this.add(CustomizationsGrid.VERY_SAD_LABEL, ":(");

            // Toasts
            this.add(CustomizationToast.TITLE_TEXT, "Neue Anpassung freigeschaltet!");

            // Customizations
            this.addCustomization(BuiltinCustomization.Type.LUCRAFT_ARC_REACTOR.getResourceKey(), "Lucraft Arc Reaktor");
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


            // Blocks
            this.addBlock(PalladiumBlocks.TAILORING_BENCH, "Schneidertisch’n");
            this.addBlock(PalladiumBlocks.METEORITE_STONE, "Meteoriitngestehn");
            this.addBlock(PalladiumBlocks.METEORITE_BRICKS, "Meteoriitnzieschl");
            this.addBlock(PalladiumBlocks.METEORITE_COAL_ORE, "Meteoriitn-Schdeengohle");
            this.addBlock(PalladiumBlocks.METEORITE_IRON_ORE, "Meteoriitn-Eisnärds");
            this.addBlock(PalladiumBlocks.METEORITE_COPPER_ORE, "Meteoriitn-Gupforrärds");
            this.addBlock(PalladiumBlocks.METEORITE_GOLD_ORE, "Meteoriitn-Goldärz");
            this.addBlock(PalladiumBlocks.METEORITE_REDSTONE_ORE, "Meteoriitn-Redstone-Ärds");
            this.addBlock(PalladiumBlocks.METEORITE_EMERALD_ORE, "Meteoriitn-Smaragdärz");
            this.addBlock(PalladiumBlocks.METEORITE_LAPIS_ORE, "Meteoriitn-Labisladsuliärds");
            this.addBlock(PalladiumBlocks.METEORITE_DIAMOND_ORE, "Meteoriitn-Diamantärds");
            this.addBlock(PalladiumBlocks.METEORITE_VIBRANIUM_ORE, "Meteoriitn-Vibraniumärds");
            this.addBlock(PalladiumBlocks.METEORITE_VIBRANIUM_VEIN, "Meteoriitn-Vibraniumvene");
            this.addBlock(PalladiumBlocks.VIBRANIUM_BLOCK, "Vibraniumblogg");
            this.addBlock(PalladiumBlocks.RAW_VIBRANIUM_BLOCK, "Rohvibraniumblogg");

            // Items
            this.addCreativeModeTab(PalladiumCreativeTabs.MAIN, "Palladium");
            this.addItem(PalladiumItems.SUIT_STAND, "Anzuchsschdändorr");
            this.addItem(PalladiumItems.WHITE_FABRIC, "Weeßer Schdoff");
            this.addItem(PalladiumItems.ORANGE_FABRIC, "Orangscher Schdoff");
            this.addItem(PalladiumItems.MAGENTA_FABRIC, "Magenda Schdoff");
            this.addItem(PalladiumItems.LIGHT_BLUE_FABRIC, "Hellblaaer Schdoff");
            this.addItem(PalladiumItems.YELLOW_FABRIC, "Gälber Schdoff");
            this.addItem(PalladiumItems.LIME_FABRIC, "Hellgriener Schdoff");
            this.addItem(PalladiumItems.PINK_FABRIC, "Roser Schdoff");
            this.addItem(PalladiumItems.GRAY_FABRIC, "Graer Schdoff");
            this.addItem(PalladiumItems.LIGHT_GRAY_FABRIC, "Hellgraer Schdoff");
            this.addItem(PalladiumItems.CYAN_FABRIC, "Türkiser Schdoff");
            this.addItem(PalladiumItems.PURPLE_FABRIC, "Violedder Schdoff");
            this.addItem(PalladiumItems.BLUE_FABRIC, "Blaer Schdoff");
            this.addItem(PalladiumItems.BROWN_FABRIC, "Braer Schdoff");
            this.addItem(PalladiumItems.GREEN_FABRIC, "Griener Schdoff");
            this.addItem(PalladiumItems.RED_FABRIC, "Roder Schdoff");
            this.addItem(PalladiumItems.BLACK_FABRIC, "Schwarzer Schdoff");

            // Abilities
            this.addAbility(AbilitySerializers.DUMMY, "Dummy");
            this.addAbility(AbilitySerializers.COMMAND, "Befehl");
            this.addAbility(AbilitySerializers.SHRINK_PLAYER_OVERLAY, "Körperoverlay schrumpfen");
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
            this.addAbility(AbilitySerializers.FLUID_WALKING, "Uff Flüssichkeht Lofen");
            this.addAbility(AbilitySerializers.RESTRICT_SLOTS, "Slots beschränken");
            this.addAbility(AbilitySerializers.PLAY_SOUND, "Sound");
            this.addAbility(AbilitySerializers.INTANGIBILITY, "Ungreifbarkeht");
            this.addAbility(AbilitySerializers.NAME_CHANGE, "Namesänderung");
            this.addAbility(AbilitySerializers.SCULK_IMMUNITY, "Sculk-Immunität");
            this.addAbility(AbilitySerializers.FIRE_ASPECT, "Vorbrennung");
            this.addAbility(AbilitySerializers.PARTICLES, "Partikel");
            this.addAbility(AbilitySerializers.FLIGHT, "Flug");
            this.addAbility(AbilitySerializers.GLIDING, "Gleiden");
            this.addAbility(AbilitySerializers.WALL_CLIMBING, "Wandkleddern");
            this.addAbility(GeckoLibCompat.TRIGGER_LAYER_ANIMATION, "Geo Animation");

            // Config
            this.addConfigEntry(PalladiumClientConfig.DEV_MODE, "Addon Entwickler Modus");
            this.addConfigEntry(PalladiumClientConfig.ABILITY_BAR_ALIGNMENT, "Ability Bar - Position");
            this.addConfigEntry(PalladiumClientConfig.ABILITY_BAR_KEY_BIND_DISPLAY, "Ability Bar - Anzeige der Tastenbelegung");
            this.addConfigEntry(PalladiumClientConfig.HIDE_EXPERIMENTAL_WARNING, "Singleplayer - Experimental Settings Warnung ausblenden");
            this.addConfigEntry(PalladiumClientConfig.SCALE_CAMERA_FIX, "Visuell - Kameraskalierung fixen");
            this.addConfigEntry(PalladiumServerConfig.MAX_SUPERPOWER_SETS, "Max. Anzahl an Superkraft-Sets");

            // Key Mappings
            this.add("key.palladium.categories.powers", "Gräfte");
            this.addKeyMapping("open_equipment", "Ohsrüstung öffnen/schließen");
            this.addKeyMapping("show_powers", "Gräfte");
            this.addKeyMapping("rotate_ability_list", "Durch Fähischgehtlehste rotieren");
            for (int i = 1; i <= 5; i++) {
                this.addKeyMapping("ability_" + i, "Fähischgeht #" + i);
            }

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

            this.add(CustomizationCommand.UNLOCK_SUCCESS, "%s is nu ordentlich für %s freigeschaltn");
            this.add(CustomizationCommand.LOCK_SUCCESS, "%s is nu ordentlich für %s gesperrt");
            this.add(CustomizationCommand.ERROR_NOT_UNLOCKABLE, "%s kannsch du mit Befehln nich freischaltn");
            this.add(CustomizationCommand.ERROR_ALREADY_UNLOCKED, "%s is scho freigeschaldet");
            this.add(CustomizationCommand.ERROR_ALREADY_LOCKED, "%s is scho gesperrt");
            this.add(CustomizationCommand.ERROR_CANT_HAVE_CUSTOMIZATIONS, "Die Geeschd kann kee Anpassungen habn");

            this.add(ScreenCommand.TRANS_SHOWED_SCREEN_SINGLE, "Fenster %s für %s uffgemacht");
            this.add(ScreenCommand.TRANS_SHOWED_SCREEN_MULTIPLE, "Fenster %s für %s Spielor uffgemacht");

            // Container
            this.add("container.palladium.tailoring", "Zuschneidern");
            this.add("container.palladium.tailoring.craft", "Herstellen");

            // GUI
            this.add(PowerTreeWidget.TRANS_TITLE, "Kräfte");
            this.add(PowerTreeWidget.TRANS_CUSTOMIZE, "Anpassn");
            this.add(PowerTreeWidget.TRANS_KEY_UNLOCK, "Freischaltn");
            this.add(PowerTreeWidget.TRANS_KEY_NO_ABILITIES_LABEL, "Hier scheind es nüschd zu gähm...");
            this.add(PowerTreeWidget.TRANS_KEY_VERY_SAD_LABEL, ":(");
            this.add("gui.palladium.powers.buy_ability.experience_level", "%sx Erfahrungsschdufe");
            this.add("gui.palladium.powers.buy_ability.experience_level_plural", "%sx Erfahrungsschdufen");
            this.add(PlayerCustomizationScreen.TITLE_TRANSLATION_KEY, "Anpassungen");
            this.add(EyeSelectionScreen.TRANS_TITLE, "Such dir dei Oogn aus");
            this.add(EyeSelectionScreen.TRANS_RESET, "Zrücksetzn");
            this.add(EyeSelectionScreen.TRANS_SAVE, "Speichern");
            this.add(CustomizationsGrid.NO_CUSTOMIZATIONS_LABEL, "Hier scheind es nüschd zu gähm...");
            this.add(CustomizationsGrid.VERY_SAD_LABEL, ":(");

            // Toasts
            this.add(CustomizationToast.TITLE_TEXT, "Neue Anpassung freigeschaldet!");

            // Customizations
            this.addCustomization(BuiltinCustomization.Type.LUCRAFT_ARC_REACTOR.getResourceKey(), "Lucraft Arc Reaktor");
            this.addCustomizationCategory(CustomizationCategories.HAT, "Hood");
            this.addCustomizationCategory(CustomizationCategories.HEAD, "Nüschl");
            this.addCustomizationCategory(CustomizationCategories.CHEST, "Bruscht");
            this.addCustomizationCategory(CustomizationCategories.BACK, "Rieggn");
            this.addCustomizationCategory(CustomizationCategories.ARMS, "Arme");
            this.addCustomizationCategory(CustomizationCategories.LEGS, "Behne");
        }
    }

}
