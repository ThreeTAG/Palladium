package net.threetag.palladium.data.forge;

import net.minecraft.data.DataGenerator;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.accessory.Accessories;
import net.threetag.palladium.block.PalladiumBlocks;
import net.threetag.palladium.client.PalladiumKeyMappings;
import net.threetag.palladium.entity.PalladiumAttributes;
import net.threetag.palladium.entity.PalladiumEntityTypes;
import net.threetag.palladium.item.PalladiumItems;
import net.threetag.palladium.power.ability.Abilities;
import net.threetag.palladium.sound.PalladiumSoundEvents;

public abstract class PalladiumLangProvider extends ExtendedLangProvider {

    public PalladiumLangProvider(DataGenerator gen, String modid, String locale) {
        super(gen, modid, locale);
    }

    public PalladiumLangProvider(DataGenerator gen, String locale) {
        super(gen, Palladium.MOD_ID, locale);
    }

    public static class English extends PalladiumLangProvider {

        public English(DataGenerator gen) {
            super(gen, "en_us");
        }

        @Override
        protected void addTranslations() {
            // Blocks
            this.addBlock(PalladiumBlocks.LEAD_ORE, "Lead Ore");
            this.addBlock(PalladiumBlocks.DEEPSLATE_LEAD_ORE, "Deepslate Lead Ore");
            this.addBlock(PalladiumBlocks.TITANIUM_ORE, "Titanium Ore");
            this.addBlock(PalladiumBlocks.VIBRANIUM_ORE, "Vibranium Ore");
            this.addBlock(PalladiumBlocks.REDSTONE_FLUX_CRYSTAL_GEODE, "Redstone Flux Crystal Geode");
            this.addBlock(PalladiumBlocks.DEEPSLATE_REDSTONE_FLUX_CRYSTAL_GEODE, "Deepslate Redstone Flux Crystal Geode");
            this.addBlock(PalladiumBlocks.SMALL_REDSTONE_FLUX_CRYSTAL_BUD, "Small Redstone Flux Crystal Bud");
            this.addBlock(PalladiumBlocks.MEDIUM_REDSTONE_FLUX_CRYSTAL_BUD, "Medium Redstone Flux Crystal Bud");
            this.addBlock(PalladiumBlocks.LARGE_REDSTONE_FLUX_CRYSTAL_BUD, "Large Redstone Flux Crystal Bud");
            this.addBlock(PalladiumBlocks.REDSTONE_FLUX_CRYSTAL_CLUSTER, "Redstone Flux Crystal Cluster");
            this.addBlock(PalladiumBlocks.LEAD_BLOCK, "Block of Lead");
            this.addBlock(PalladiumBlocks.VIBRANIUM_BLOCK, "Block of Vibranium");
            this.addBlock(PalladiumBlocks.RAW_LEAD_BLOCK, "Block of Raw Lead");
            this.addBlock(PalladiumBlocks.RAW_TITANIUM_BLOCK, "Block of Raw Titanium");
            this.addBlock(PalladiumBlocks.RAW_VIBRANIUM_BLOCK, "Block of Raw Vibranium");
            this.addBlock(PalladiumBlocks.SOLAR_PANEL, "Solar Panel");
            this.addBlock(PalladiumBlocks.HEART_SHAPED_HERB, "Heart-Shaped Herb");
            this.addBlock(PalladiumBlocks.POTTED_HEART_SHAPED_HERB, "Potted Heart-Shaped Herb");

            // Items
            this.addItem(PalladiumItems.RAW_LEAD, "Raw Lead");
            this.addItem(PalladiumItems.LEAD_INGOT, "Lead Ingot");
            this.addItem(PalladiumItems.RAW_TITANIUM, "Raw Titanium");
            this.addItem(PalladiumItems.RAW_VIBRANIUM, "Raw Vibranium");
            this.addItem(PalladiumItems.VIBRANIUM_INGOT, "Vibranium Ingot");
            this.addItem(PalladiumItems.REDSTONE_FLUX_CRYSTAL, "Redstone Flux Crystal");
            this.addItem(PalladiumItems.SUIT_STAND, "Suit Stand");
            this.addItem(PalladiumItems.HAMMER, "Hammer");
            this.addItem(PalladiumItems.LEAD_CIRCUIT, "Lead Circuit");
            this.addItem(PalladiumItems.QUARTZ_CIRCUIT, "Quartz Circuit");
            this.addItem(PalladiumItems.VIBRANIUM_CIRCUIT, "Vibranium Circuit");
            this.addItem(PalladiumItems.LEAD_FLUX_CAPACITOR, "Lead Flux Capacitor");
            this.addItem(PalladiumItems.QUARTZ_FLUX_CAPACITOR, "Quartz Flux Capacitor");
            this.addItem(PalladiumItems.VIBRANIUM_FLUX_CAPACITOR, "Vibranium Flux Capacitor");
            this.add("item.palladium.flux_capacitor.desc", "Energy: %s / %s RF");
            this.addItem(PalladiumItems.VIBRANIUM_WEAVE_BOOTS, "Vibranium Weave Boots");

            // Entities
            this.addEntityType(PalladiumEntityTypes.EFFECT, "Effect");
            this.addEntityType(PalladiumEntityTypes.CUSTOM_PROJECTILE, "Projectile");
            this.addEntityType(PalladiumEntityTypes.SUIT_STAND, "Suit Stand");

            // Attributes
            this.addAttribute(PalladiumAttributes.FLIGHT_SPEED, "Flight Speed");
            this.addAttribute(PalladiumAttributes.LEVITATION_SPEED, "Levitation Speed");
            this.addAttribute(PalladiumAttributes.JETPACK_FLIGHT_SPEED, "Jetpack Flight Speed");
            this.addAttribute(PalladiumAttributes.HOVERING, "Hovering");
            this.addAttribute(PalladiumAttributes.PUNCH_DAMAGE, "Punch Damage");
            this.addAttribute(PalladiumAttributes.JUMP_POWER, "Jump Power");

            // Abilities
            this.addAbility(Abilities.DUMMY, "Dummy");
            this.addAbility(Abilities.COMMAND, "Command");
            this.addAbility(Abilities.RENDER_LAYER, "Render Layer");
            this.addAbility(Abilities.ANIMATION_TIMER, "Animation Timer");
            this.addAbility(Abilities.SHRINK_BODY_OVERLAY, "Shrink Body Overlay");
            this.addAbility(Abilities.ATTRIBUTE_MODIFIER, "Attribute Modifier");
            this.addAbility(Abilities.HEALING, "Healing");
            this.addAbility(Abilities.SLOWFALL, "Slowfall");
            this.addAbility(Abilities.DAMAGE_IMMUNITY, "Damage Immunity");
            this.addAbility(Abilities.INVISIBILITY, "Invisibility");
            this.addAbility(Abilities.SIZE, "Size");
            this.addAbility(Abilities.PROJECTILE, "Projectile");
            this.addAbility(Abilities.SKIN_CHANGE, "Skin Change");
            this.addAbility(Abilities.AIM, "Aim");
            this.addAbility(Abilities.HIDE_BODY_PARTS, "Hide Body Parts");
            this.addAbility(Abilities.SHADER_EFFECT, "Shader Effect");
            this.addAbility(Abilities.GUI_OVERLAY, "Gui Overlay");
            this.addAbility(Abilities.SHOW_BOTH_ARMS, "Show Both Arms");
            this.addAbility(Abilities.PLAYER_ANIMATION, "Player Animation");
            this.addAbility(Abilities.WATER_WALK, "Water Walk");

            // Creative Tab
            this.add("itemGroup.palladium.technology", "Technology");

            // Key Mappings
            this.add(PalladiumKeyMappings.CATEGORY, "Abilities");
            this.add("key.palladium.switch_ability_list", "Switch Ability List");
            for (int i = 1; i <= PalladiumKeyMappings.ABILITY_KEYS.length; i++) {
                this.add("key.palladium.ability_" + i, "Ability " + i);
            }

            // Commands
            this.add("commands.superpower.error.powerNotFound", "No power was found by the name '%1$s'");
            this.add("commands.superpower.error.noLivingEntity", "This entity is not a living entity");
            this.add("commands.superpower.success.entity.single", "%s has gained the superpower %s");
            this.add("commands.superpower.success.entity.multiple", "%s entities have gained the superpower %s");
            this.add("commands.superpower.remove.success.entity.single", "%s's superpower was removed");
            this.add("commands.superpower.remove.success.entity.multiple", "The superpower of %s entities was removed");
            this.add("commands.superpower.error.alreadyHasSuperpower", "%s already has that superpower");
            this.add("commands.superpower.error.doesntHaveSuperpower", "%s does not have that superpower");

            // GUI
            this.add("gui.palladium.powers", "Powers");
            this.add("gui.palladium.powers.buy_ability", "Do you want to unlock this ability?");
            this.add("gui.palladium.powers.buy_ability.or", "or");
            this.add("gui.palladium.powers.buy_ability.experience_level", "Experience level");
            this.add("gui.palladium.powers.buy_ability.experience_level_plural", "Experience level");
            this.add("gui.palladium.accessories", "Accessories");
            this.add("gui.palladium.addon_pack_log", "Addon Pack Log");
            this.add("gui.palladium.addon_pack_log.search", "Search...");
            this.add("gui.palladium.addon_pack_log_entry", "Log Entry");
            this.add("gui.palladium.addon_pack_log_entry.copy_to_clipboard", "Copy to Clipboard");
            this.add("gui.palladium.addon_pack_log_entry.upload_to_mclogs", "Upload to mclo.gs");

            // Accessories
            this.addAccessory(Accessories.LUCRAFT_ARC_REACTOR, "Lucraft Arc Reactor");
            this.addAccessory(Accessories.HEROBRINE_EYES, "Herobrine Eyes");
            this.addAccessory(Accessories.FACE_MASK, "Face Mask");
            this.addAccessory(Accessories.GLASSES_3D, "3D Glasses");
            this.addAccessory(Accessories.SUN_GLASSES, "Sun Glasses");
            this.addAccessory(Accessories.HEART_GLASSES, "Heart Glasses");
            this.addAccessory(Accessories.OWCA_FEDORA, "OWCA Fedora");
            this.addAccessory(Accessories.ELTON_HAT, "Elton Hat");
            this.addAccessory(Accessories.STRAWHAT, "Strawhat");
            this.addAccessory(Accessories.FEZ, "Fez");
            this.addAccessory(Accessories.ANTENNA, "Antenna");
            this.addAccessory(Accessories.KRUSTY_KRAB_HAT, "Krusty Krab Hat");
            this.addAccessory(Accessories.SEA_PICKLE_HAT, "Sea Pickle Hat");
            this.addAccessory(Accessories.WINTER_SOLDIER_ARM, "Winter Soldier Arm");
            this.addAccessory(Accessories.HYPERION_ARM, "Hyperion Arm");
            this.addAccessory(Accessories.MECHANICAL_ARM, "Mechanical Arm");
            this.addAccessory(Accessories.HAMMOND_CANE, "Hammond Cane");
            this.addAccessory(Accessories.WOODEN_LEG, "Wooden Leg");

            this.add("accessory_slot.hat", "Hat");
            this.add("accessory_slot.head", "Head");
            this.add("accessory_slot.face", "Face");
            this.add("accessory_slot.chest", "Chest");
            this.add("accessory_slot.back", "Back");
            this.add("accessory_slot.main_arm", "Main Arm");
            this.add("accessory_slot.off_arm", "Off Arm");
            this.add("accessory_slot.main_hand", "Main Hand");
            this.add("accessory_slot.off_hand", "Off Hand");
            this.add("accessory_slot.right_leg", "Right Leg");
            this.add("accessory_slot.left_leg", "Left Leg");
            this.add("accessory_slot.special", "Special");

            // Subtitles
            this.add(PalladiumSoundDefinitionsProvider.subtitle(PalladiumSoundEvents.HEAT_VISION), "Heat Vision");
        }
    }

    public static class German extends PalladiumLangProvider {

        public German(DataGenerator gen) {
            super(gen, "de_de");
        }

        @Override
        protected void addTranslations() {
            // Blocks
            this.addBlock(PalladiumBlocks.LEAD_ORE, "Bleierz");
            this.addBlock(PalladiumBlocks.DEEPSLATE_LEAD_ORE, "Tiefenschiefer-Bleierz");
            this.addBlock(PalladiumBlocks.TITANIUM_ORE, "Titaniumerz");
            this.addBlock(PalladiumBlocks.VIBRANIUM_ORE, "Vibraniumerz");
            this.addBlock(PalladiumBlocks.REDSTONE_FLUX_CRYSTAL_GEODE, "Redstone-Flux-Kristallgeode");
            this.addBlock(PalladiumBlocks.DEEPSLATE_REDSTONE_FLUX_CRYSTAL_GEODE, "Tiefenschiefer-Redstone-Flux-Kristallgeode");
            this.addBlock(PalladiumBlocks.SMALL_REDSTONE_FLUX_CRYSTAL_BUD, "Kleine Redstone-Flux-Kristallknospe");
            this.addBlock(PalladiumBlocks.MEDIUM_REDSTONE_FLUX_CRYSTAL_BUD, "Mittlere Redstone-Flux-Kristallknospe");
            this.addBlock(PalladiumBlocks.LARGE_REDSTONE_FLUX_CRYSTAL_BUD, "Gro\u00DFe Redstone-Flux-Kristallknospe");
            this.addBlock(PalladiumBlocks.REDSTONE_FLUX_CRYSTAL_CLUSTER, "Redstone-Flux-Kristallhaufen");
            this.addBlock(PalladiumBlocks.LEAD_BLOCK, "Bleiblock");
            this.addBlock(PalladiumBlocks.VIBRANIUM_BLOCK, "Vibraniumblock");
            this.addBlock(PalladiumBlocks.RAW_LEAD_BLOCK, "Rohbleiblock");
            this.addBlock(PalladiumBlocks.RAW_TITANIUM_BLOCK, "Rohtitaniumblock");
            this.addBlock(PalladiumBlocks.RAW_VIBRANIUM_BLOCK, "Rohvibraniumblock");
            this.addBlock(PalladiumBlocks.SOLAR_PANEL, "Solarpaneel");
            this.addBlock(PalladiumBlocks.HEART_SHAPED_HERB, "Herzf\u00F6rmiges Kraut");
            this.addBlock(PalladiumBlocks.POTTED_HEART_SHAPED_HERB, "Eingetopftes Herzf\u00F6rmige Kraut");

            // Items
            this.addItem(PalladiumItems.RAW_LEAD, "Rohblei");
            this.addItem(PalladiumItems.LEAD_INGOT, "Bleibarren");
            this.addItem(PalladiumItems.RAW_TITANIUM, "Rohtitanium");
            this.addItem(PalladiumItems.RAW_VIBRANIUM, "Rohvibranium");
            this.addItem(PalladiumItems.VIBRANIUM_INGOT, "Vibraniumbarren");
            this.addItem(PalladiumItems.REDSTONE_FLUX_CRYSTAL, "Redstone-Flux-Kristall");
            this.addItem(PalladiumItems.SUIT_STAND, "Anzugsständer");
            this.addItem(PalladiumItems.HAMMER, "Hammer");
            this.addItem(PalladiumItems.LEAD_CIRCUIT, "Bleischaltkreis");
            this.addItem(PalladiumItems.QUARTZ_CIRCUIT, "Quarzschaltkreis");
            this.addItem(PalladiumItems.VIBRANIUM_CIRCUIT, "Vibraniumschaltkreis");
            this.addItem(PalladiumItems.LEAD_FLUX_CAPACITOR, "Blei-Fluxkompensator");
            this.addItem(PalladiumItems.QUARTZ_FLUX_CAPACITOR, "Quarz-Fluxkompensator");
            this.addItem(PalladiumItems.VIBRANIUM_FLUX_CAPACITOR, "Vibranium-Fluxkompensator");
            this.add("item.palladium.flux_capacitor.desc", "Energie: %s / %s RF");
            this.addItem(PalladiumItems.VIBRANIUM_WEAVE_BOOTS, "Vibraniumgewebeschuhe");

            // Entities
            this.addEntityType(PalladiumEntityTypes.EFFECT, "Effekt");
            this.addEntityType(PalladiumEntityTypes.CUSTOM_PROJECTILE, "Projektil");
            this.addEntityType(PalladiumEntityTypes.SUIT_STAND, "Anzugsständer");

            // Attributes
            this.addAttribute(PalladiumAttributes.FLIGHT_SPEED, "Fluggeschwindigkeit");
            this.addAttribute(PalladiumAttributes.LEVITATION_SPEED, "Schwebegeschwindigkeit");
            this.addAttribute(PalladiumAttributes.JETPACK_FLIGHT_SPEED, "Jetpack-Fluggeschwindigkeit");
            this.addAttribute(PalladiumAttributes.HOVERING, "Schweben");
            this.addAttribute(PalladiumAttributes.PUNCH_DAMAGE, "Schlagkraft");
            this.addAttribute(PalladiumAttributes.JUMP_POWER, "Sprungkraft");

            // Abilities
            this.addAbility(Abilities.DUMMY, "Dummy");
            this.addAbility(Abilities.COMMAND, "Befehl");
            this.addAbility(Abilities.RENDER_LAYER, "Render Layer");
            this.addAbility(Abilities.ANIMATION_TIMER, "Animations-Timer");
            this.addAbility(Abilities.SHRINK_BODY_OVERLAY, "K\u00F6rperoverlay schrumpfen");
            this.addAbility(Abilities.ATTRIBUTE_MODIFIER, "Attributmodifikator");
            this.addAbility(Abilities.HEALING, "Heilung");
            this.addAbility(Abilities.SLOWFALL, "Langsamer Fall");
            this.addAbility(Abilities.DAMAGE_IMMUNITY, "Schadensimmunit\u00E4t");
            this.addAbility(Abilities.INVISIBILITY, "Unsichtbarkeit");
            this.addAbility(Abilities.SIZE, "Gr\u00F6\u00DFe");
            this.addAbility(Abilities.PROJECTILE, "Projektil");
            this.addAbility(Abilities.SKIN_CHANGE, "Skin \u00C4nderung");
            this.addAbility(Abilities.AIM, "Zielen");
            this.addAbility(Abilities.HIDE_BODY_PARTS, "K\u00F6rperteile verstecken");
            this.addAbility(Abilities.SHADER_EFFECT, "Shader Effekt");
            this.addAbility(Abilities.GUI_OVERLAY, "GUI-Overlay");
            this.addAbility(Abilities.SHOW_BOTH_ARMS, "Beide Arme zeigen");
            this.addAbility(Abilities.PLAYER_ANIMATION, "Spieler-Animation");
            this.addAbility(Abilities.WATER_WALK, "Auf Wasser Laufen");

            // Creative Tab
            this.add("itemGroup.palladium.technology", "Technologie");

            // Key Mappings
            this.add(PalladiumKeyMappings.CATEGORY, "F\u00E4higkeiten");
            this.add("key.palladium.switch_ability_list", "F\u00E4higkeitenliste wechseln");
            for (int i = 1; i <= PalladiumKeyMappings.ABILITY_KEYS.length; i++) {
                this.add("key.palladium.ability_" + i, "F\u00E4higkeit " + i);
            }

            // Commands
            this.add("commands.superpower.error.powerNotFound", "Es wurde keine Kraft mit dem Namen '%1$s' gefunden");
            this.add("commands.superpower.error.noLivingEntity", "Objekt ist nicht lebend");
            this.add("commands.superpower.success.entity.single", "%s hat die Superkraft %s erhalten");
            this.add("commands.superpower.success.entity.multiple", "%s Lebewesen haben die Superkraft %s erhalten");
            this.add("commands.superpower.remove.success.entity.single", "%s's Superkraft wurde entfernt");
            this.add("commands.superpower.remove.success.entity.multiple", "Die Superkraft von %s Lebewesen wurde entfernt");
            this.add("commands.superpower.error.alreadyHasSuperpower", "%s hat bereits diese Superkraft");
            this.add("commands.superpower.error.doesntHaveSuperpower", "%s hat diese Superkraft nicht");

            // GUI
            this.add("gui.palladium.powers", "Kr\u00E4fte");
            this.add("gui.palladium.powers.buy_ability", "Möchtest du diese Fähigkeit freischalten?");
            this.add("gui.palladium.powers.buy_ability.or", "oder");
            this.add("gui.palladium.powers.buy_ability.experience_level", "Erfahrungsstufe");
            this.add("gui.palladium.powers.buy_ability.experience_level_plural", "Erfahrungsstufen");
            this.add("gui.palladium.accessories", "Zubeh\u00F6r");
            this.add("gui.palladium.addon_pack_log", "Addon Pack Log");
            this.add("gui.palladium.addon_pack_log.search", "Suchen...");
            this.add("gui.palladium.addon_pack_log_entry", "Logeintrag");
            this.add("gui.palladium.addon_pack_log_entry.copy_to_clipboard", "Kopieren");
            this.add("gui.palladium.addon_pack_log_entry.upload_to_mclogs", "Auf mclo.gs hochladen");

            // Accessories
            this.addAccessory(Accessories.LUCRAFT_ARC_REACTOR, "Lucraft Arc Reactor");
            this.addAccessory(Accessories.HEROBRINE_EYES, "Herobrineaugen");
            this.addAccessory(Accessories.FACE_MASK, "Atemschutzmaske");
            this.addAccessory(Accessories.GLASSES_3D, "3D-Brille");
            this.addAccessory(Accessories.SUN_GLASSES, "Sonnenbrille");
            this.addAccessory(Accessories.HEART_GLASSES, "Herzbrille");
            this.addAccessory(Accessories.OWCA_FEDORA, "OWCA-Hut");
            this.addAccessory(Accessories.ELTON_HAT, "Elton-Hut");
            this.addAccessory(Accessories.STRAWHAT, "Strohhut");
            this.addAccessory(Accessories.FEZ, "Fez");
            this.addAccessory(Accessories.ANTENNA, "Antenne");
            this.addAccessory(Accessories.KRUSTY_KRAB_HAT, "Krosse-Krabbe-Hut");
            this.addAccessory(Accessories.SEA_PICKLE_HAT, "Seegurkenhut");
            this.addAccessory(Accessories.WINTER_SOLDIER_ARM, "Winter Soldier Arm");
            this.addAccessory(Accessories.HYPERION_ARM, "Hyperion Arm");
            this.addAccessory(Accessories.MECHANICAL_ARM, "Mechanischer Arm");
            this.addAccessory(Accessories.HAMMOND_CANE, "Hammonds Gehstock");
            this.addAccessory(Accessories.WOODEN_LEG, "Holzbein");

            this.add("accessory_slot.hat", "Hut");
            this.add("accessory_slot.head", "Kopf");
            this.add("accessory_slot.face", "Gesicht");
            this.add("accessory_slot.chest", "Brust");
            this.add("accessory_slot.back", "R\u00FCcken");
            this.add("accessory_slot.main_arm", "Hauptarm");
            this.add("accessory_slot.off_arm", "Zweitarm");
            this.add("accessory_slot.main_hand", "Haupthand");
            this.add("accessory_slot.off_hand", "Zweithand");
            this.add("accessory_slot.right_leg", "Rechtes Bein");
            this.add("accessory_slot.left_leg", "Linkes Bein");
            this.add("accessory_slot.special", "Spezial");

            // Subtitles
            this.add(PalladiumSoundDefinitionsProvider.subtitle(PalladiumSoundEvents.HEAT_VISION), "Hitzeblick");
        }
    }

    public static class Saxon extends PalladiumLangProvider {

        public Saxon(DataGenerator gen) {
            super(gen, "sxu");
        }

        @Override
        protected void addTranslations() {
            // Blocks
            this.addBlock(PalladiumBlocks.LEAD_ORE, "Bleärds");
            this.addBlock(PalladiumBlocks.DEEPSLATE_LEAD_ORE, "Diefnschieforr-Bleärds");
            this.addBlock(PalladiumBlocks.TITANIUM_ORE, "Titaniumärds");
            this.addBlock(PalladiumBlocks.VIBRANIUM_ORE, "Vibraniumärds");
            this.addBlock(PalladiumBlocks.REDSTONE_FLUX_CRYSTAL_GEODE, "Redstone-Flux-Kristallgeode");
            this.addBlock(PalladiumBlocks.DEEPSLATE_REDSTONE_FLUX_CRYSTAL_GEODE, "Diefnschieforr-Redstone-Flux-Kristallgeode");
            this.addBlock(PalladiumBlocks.SMALL_REDSTONE_FLUX_CRYSTAL_BUD, "Gleene Redstone-Flux-Kristallgnosbe");
            this.addBlock(PalladiumBlocks.MEDIUM_REDSTONE_FLUX_CRYSTAL_BUD, "Mittlorre Redstone-Flux-Kristallgnosbe");
            this.addBlock(PalladiumBlocks.LARGE_REDSTONE_FLUX_CRYSTAL_BUD, "Gro\u00DFe Redstone-Flux-Kristallgnosbe");
            this.addBlock(PalladiumBlocks.REDSTONE_FLUX_CRYSTAL_CLUSTER, "Redstone-Flux-Kristallhaufn");
            this.addBlock(PalladiumBlocks.LEAD_BLOCK, "Bleblogg");
            this.addBlock(PalladiumBlocks.VIBRANIUM_BLOCK, "Vibraniumblogg");
            this.addBlock(PalladiumBlocks.RAW_LEAD_BLOCK, "Rohbleblogg");
            this.addBlock(PalladiumBlocks.RAW_TITANIUM_BLOCK, "Rohtitaniumblogg");
            this.addBlock(PalladiumBlocks.RAW_VIBRANIUM_BLOCK, "Rohvibraniumblogg");
            this.addBlock(PalladiumBlocks.SOLAR_PANEL, "Solarpaneel");
            this.addBlock(PalladiumBlocks.HEART_SHAPED_HERB, "Herzf\u00F6rmijes Graud");
            this.addBlock(PalladiumBlocks.POTTED_HEART_SHAPED_HERB, "Eingedobbdes Herzf\u00F6rmije Graud");

            // Items
            this.addItem(PalladiumItems.RAW_LEAD, "Rohble");
            this.addItem(PalladiumItems.LEAD_INGOT, "Blebarrn");
            this.addItem(PalladiumItems.RAW_TITANIUM, "Rohtitanium");
            this.addItem(PalladiumItems.RAW_VIBRANIUM, "Rohvibranium");
            this.addItem(PalladiumItems.VIBRANIUM_INGOT, "Vibraniumbarrn");
            this.addItem(PalladiumItems.REDSTONE_FLUX_CRYSTAL, "Redstone-Flux-Kristall");
            this.addItem(PalladiumItems.SUIT_STAND, "Anzuchsschdändorr");
            this.addItem(PalladiumItems.HAMMER, "Hammer");
            this.addItem(PalladiumItems.LEAD_CIRCUIT, "Redstoneschaldkres");
            this.addItem(PalladiumItems.QUARTZ_CIRCUIT, "Quarzschaldkres");
            this.addItem(PalladiumItems.VIBRANIUM_CIRCUIT, "Vibraniumschaldkres");
            this.addItem(PalladiumItems.LEAD_FLUX_CAPACITOR, "Ble-Fluxgompensator");
            this.addItem(PalladiumItems.QUARTZ_FLUX_CAPACITOR, "Quarz-Fluxgompensator");
            this.addItem(PalladiumItems.VIBRANIUM_FLUX_CAPACITOR, "Vibranium-Fluxgompensator");
            this.add("item.palladium.flux_capacitor.desc", "Enerschie: %s / %s RF");
            this.addItem(PalladiumItems.VIBRANIUM_WEAVE_BOOTS, "Vibraniumjewebelaadschn");

            // Entities
            this.addEntityType(PalladiumEntityTypes.EFFECT, "Effekt");
            this.addEntityType(PalladiumEntityTypes.CUSTOM_PROJECTILE, "Projektil");
            this.addEntityType(PalladiumEntityTypes.SUIT_STAND, "Anzuchsschdändorr");

            // Attributes
            this.addAttribute(PalladiumAttributes.FLIGHT_SPEED, "Flugjeschwindichket");
            this.addAttribute(PalladiumAttributes.LEVITATION_SPEED, "Schwebejeschwindichket");
            this.addAttribute(PalladiumAttributes.JETPACK_FLIGHT_SPEED, "Jetpack-Flugjeschwindichket");
            this.addAttribute(PalladiumAttributes.HOVERING, "Schweben");
            this.addAttribute(PalladiumAttributes.PUNCH_DAMAGE, "Schlachkraft");
            this.addAttribute(PalladiumAttributes.JUMP_POWER, "Sprungkraft");

            // Abilities
            this.addAbility(Abilities.DUMMY, "Dummy");
            this.addAbility(Abilities.COMMAND, "Befehl");
            this.addAbility(Abilities.RENDER_LAYER, "Render Layer");
            this.addAbility(Abilities.ANIMATION_TIMER, "Animations-Timer");
            this.addAbility(Abilities.SHRINK_BODY_OVERLAY, "K\u00F6rperoverlay schrumpfen");
            this.addAbility(Abilities.ATTRIBUTE_MODIFIER, "\u00C4ttrib\u00FCtmodifikator");
            this.addAbility(Abilities.HEALING, "Helung");
            this.addAbility(Abilities.SLOWFALL, "Langsamer Fall");
            this.addAbility(Abilities.DAMAGE_IMMUNITY, "Schadensimmunit\u00E4t");
            this.addAbility(Abilities.INVISIBILITY, "Unsischtbarkeet");
            this.addAbility(Abilities.SIZE, "Gr\u00F6\u00DFe");
            this.addAbility(Abilities.PROJECTILE, "Projektil");
            this.addAbility(Abilities.SKIN_CHANGE, "Skin \u00C4nderung");
            this.addAbility(Abilities.AIM, "Zielen");
            this.addAbility(Abilities.HIDE_BODY_PARTS, "G\u00F6rperdeile versteggen");
            this.addAbility(Abilities.SHADER_EFFECT, "Shader Effekt");
            this.addAbility(Abilities.GUI_OVERLAY, "GUI-Overlay");
            this.addAbility(Abilities.SHOW_BOTH_ARMS, "Beide Arme zeijen");
            this.addAbility(Abilities.PLAYER_ANIMATION, "Spieler-Animation");
            this.addAbility(Abilities.WATER_WALK, "Uff Wasser Lofen");

            // Creative Tab
            this.add("itemGroup.palladium.technology", "Technolojie");

            // Key Mappings
            this.add(PalladiumKeyMappings.CATEGORY, "F\u00E4hischgehden");
            this.add("key.palladium.switch_ability_list", "F\u00E4hischgehden wechseln");
            for (int i = 1; i <= PalladiumKeyMappings.ABILITY_KEYS.length; i++) {
                this.add("key.palladium.ability_" + i, "F\u00E4hischgehd " + i);
            }

            // Commands
            this.add("commands.superpower.error.powerNotFound", "Et wurd' kene Kraft mit'm Namen '%1$s' jefunden");
            this.add("commands.superpower.error.noLivingEntity", "Objekt ist nicht lebend");
            this.add("commands.superpower.success.entity.single", "%s hat de Supperkraft %s erhalten");
            this.add("commands.superpower.success.entity.multiple", "%s Lebewesen haben de Supperkraft %s erhalten");
            this.add("commands.superpower.remove.success.entity.single", "%s's Supperkraft wurd' entfernt");
            this.add("commands.superpower.remove.success.entity.multiple", "De Supperkraft von %s Lebewesen wurd' entfernt");
            this.add("commands.superpower.error.alreadyHasSuperpower", "%s had berehts dise Supperkraft");
            this.add("commands.superpower.error.doesntHaveSuperpower", "%s had dese Supperkraft nich");

            // GUI
            this.add("gui.palladium.powers", "Kr\u00E4fte");
            this.add("gui.palladium.powers.buy_ability", "Möschtest'e disse Fähischgeht freischaltn?");
            this.add("gui.palladium.powers.buy_ability.or", "oder");
            this.add("gui.palladium.powers.buy_ability.experience_level", "Erfahrungsschdufe");
            this.add("gui.palladium.powers.buy_ability.experience_level_plural", "Erfahrungsschdufen");
            this.add("gui.palladium.accessories", "Zubeh\u00F6r");
            this.add("gui.palladium.addon_pack_log", "Addon Pack Log");
            this.add("gui.palladium.addon_pack_log.search", "Suchen...");
            this.add("gui.palladium.addon_pack_log_entry", "Logeintrag");
            this.add("gui.palladium.addon_pack_log_entry.copy_to_clipboard", "Kopieren");
            this.add("gui.palladium.addon_pack_log_entry.upload_to_mclogs", "Uff mclo.gs hochladen");

            // Accessories
            this.addAccessory(Accessories.LUCRAFT_ARC_REACTOR, "Lucraft Arc Reactor");
            this.addAccessory(Accessories.HEROBRINE_EYES, "Herobrineaugen");
            this.addAccessory(Accessories.FACE_MASK, "Atemschutzmaske");
            this.addAccessory(Accessories.GLASSES_3D, "3D-Brille");
            this.addAccessory(Accessories.SUN_GLASSES, "Sonnenbrille");
            this.addAccessory(Accessories.HEART_GLASSES, "Herzbrille");
            this.addAccessory(Accessories.OWCA_FEDORA, "OWCA-Hut");
            this.addAccessory(Accessories.ELTON_HAT, "Elton-Hut");
            this.addAccessory(Accessories.STRAWHAT, "Strohhut");
            this.addAccessory(Accessories.FEZ, "Fez");
            this.addAccessory(Accessories.ANTENNA, "Antenne");
            this.addAccessory(Accessories.KRUSTY_KRAB_HAT, "Krosse-Krabbe-Hut");
            this.addAccessory(Accessories.SEA_PICKLE_HAT, "Seegurkenhut");
            this.addAccessory(Accessories.WINTER_SOLDIER_ARM, "Winter Soldier Arm");
            this.addAccessory(Accessories.HYPERION_ARM, "Hyperion Arm");
            this.addAccessory(Accessories.MECHANICAL_ARM, "Mechanischer Arm");
            this.addAccessory(Accessories.HAMMOND_CANE, "Hammonds Gehstock");
            this.addAccessory(Accessories.WOODEN_LEG, "Holzbehn");

            this.add("accessory_slot.hat", "Hut");
            this.add("accessory_slot.head", "Nischel");
            this.add("accessory_slot.face", "Jesichd");
            this.add("accessory_slot.chest", "Brust");
            this.add("accessory_slot.back", "R\u00FCggen");
            this.add("accessory_slot.main_arm", "Haupdarm");
            this.add("accessory_slot.off_arm", "Zweitarm");
            this.add("accessory_slot.main_hand", "Haupthand");
            this.add("accessory_slot.off_hand", "Nebenhand");
            this.add("accessory_slot.right_leg", "Reschdes Bein");
            this.add("accessory_slot.left_leg", "Linges Bein");
            this.add("accessory_slot.special", "Spezial");

            // Subtitles
            this.add(PalladiumSoundDefinitionsProvider.subtitle(PalladiumSoundEvents.HEAT_VISION), "Hitzeblick");
        }
    }

    @Override
    public String getName() {
        return "Palladium " + super.getName();
    }
}
