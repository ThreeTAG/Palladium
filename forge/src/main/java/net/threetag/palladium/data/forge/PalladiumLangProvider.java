package net.threetag.palladium.data.forge;

import net.minecraft.data.PackOutput;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.accessory.Accessories;
import net.threetag.palladium.accessory.AccessorySlot;
import net.threetag.palladium.block.PalladiumBlocks;
import net.threetag.palladium.client.PalladiumKeyMappings;
import net.threetag.palladium.entity.PalladiumAttributes;
import net.threetag.palladium.entity.PalladiumEntityTypes;
import net.threetag.palladium.item.PalladiumItems;
import net.threetag.palladium.power.ability.Abilities;
import net.threetag.palladium.sound.PalladiumSoundEvents;
import org.jetbrains.annotations.NotNull;

public abstract class PalladiumLangProvider extends ExtendedLangProvider {

    public PalladiumLangProvider(PackOutput output, String modid, String locale) {
        super(output, modid, locale);
    }

    public PalladiumLangProvider(PackOutput packOutput, String locale) {
        super(packOutput, Palladium.MOD_ID, locale);
    }

    public static class English extends PalladiumLangProvider {

        public English(PackOutput packOutput) {
            super(packOutput, "en_us");
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
            this.addAttribute(PalladiumAttributes.FLIGHT_FLEXIBILITY, "Flight Flexibility");
            this.addAttribute(PalladiumAttributes.HEROIC_FLIGHT_TYPE, "Heroic Flight");
            this.addAttribute(PalladiumAttributes.PUNCH_DAMAGE, "Punch Damage");
            this.addAttribute(PalladiumAttributes.JUMP_POWER, "Jump Power");
            this.addAttribute(PalladiumAttributes.DESTROY_SPEED, "Destroy Speed");
            this.addAttribute(PalladiumAttributes.FALL_RESISTANCE, "Fall Resistance");
            this.addAttribute(PalladiumAttributes.LEAPING, "Leaping");
            this.add("attribute.name.forge.swim_speed", "Swim Speed");

            // Abilities
            this.addAbility(Abilities.DUMMY, "Dummy");
            this.addAbility(Abilities.COMMAND, "Command");
            this.addAbility(Abilities.RENDER_LAYER, "Render Layer");
            this.addAbility(Abilities.RENDER_LAYER_BY_ACCESSORY_SLOT, "Render Layer");
            this.addAbility(Abilities.ANIMATION_TIMER, "Animation Timer");
            this.addAbility(Abilities.REPEATING_ANIMATION_TIMER, "Animation Timer");
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
            this.addAbility(Abilities.HIDE_BODY_PART, "Hide Body Part");
            this.addAbility(Abilities.REMOVE_BODY_PART, "Remove Body Part");
            this.addAbility(Abilities.SHADER_EFFECT, "Shader Effect");
            this.addAbility(Abilities.GUI_OVERLAY, "Gui Overlay");
            this.addAbility(Abilities.SHOW_BOTH_ARMS, "Show Both Arms");
            this.addAbility(Abilities.PLAYER_ANIMATION, "Player Animation");
            this.addAbility(Abilities.WATER_WALK, "Water Walk");
            this.addAbility(Abilities.FLUID_WALKING, "Fluid Walking");
            this.addAbility(Abilities.RESTRICT_SLOTS, "Restrict Slots");
            this.addAbility(Abilities.PLAY_SOUND, "Sound");
            this.addAbility(Abilities.VIBRATE, "Vibrate");
            this.addAbility(Abilities.INTANGIBILITY, "Intangibility");
            this.addAbility(Abilities.NAME_CHANGE, "Name Change");
            this.addAbility(Abilities.SCULK_IMMUNITY, "Sculk Immunity");
            this.addAbility(Abilities.FIRE_ASPECT, "Fire Aspect");
            this.add("ability.geckolib.render_layer_animation", "Gecko Render Layer Animation");
            this.add("ability.geckolib.armor_animation", "Gecko Armor Animation");

            // Creative Tab
            this.add("itemGroup.palladium.technology", "Technology");
            this.add("itemGroup.palladium.mods", "Palladium Mods");

            // Key Mappings
            this.add(PalladiumKeyMappings.CATEGORY, "Abilities");
            this.add("key.palladium.switch_ability_list", "Switch Ability List");
            this.add("key.palladium.open_equipment", "Open Equipment");
            for (int i = 1; i <= PalladiumKeyMappings.ABILITY_KEYS.length; i++) {
                this.add("key.palladium.ability_" + i, "Ability " + i);
            }

            // Commands
            this.add("commands.superpower.error.powerNotFound", "No power was found by the name '%1$s'");
            this.add("commands.superpower.error.noLivingEntity", "This entity is not a living entity");
            this.add("commands.superpower.error.noSuperpowers", "%s has no superpowers");
            this.add("commands.superpower.query.success", "%s has the following superpowers: %s");
            this.add("commands.superpower.success.entity.single", "%s has gained the superpower %s");
            this.add("commands.superpower.success.entity.multiple", "%s entities have gained the superpower %s");
            this.add("commands.superpower.remove.success.entity.single", "%s's superpower(s) was removed");
            this.add("commands.superpower.remove.success.entity.multiple", "The superpower(s) of %s entities was removed");
            this.add("commands.superpower.replace.success.entity.single", "%s's superpower(s) have been replaced");
            this.add("commands.superpower.replace.success.entity.multiple", "The superpower(s) of %s entities have been replaced");
            this.add("commands.superpower.error.alreadyHasSuperpower", "%s already has that superpower");
            this.add("commands.superpower.error.doesntHaveSuperpower", "%s does not have that superpower");

            this.add("commands.ability.error.notUnlockable", "The %s ability of the %s power is not unlockable");
            this.add("commands.ability.error.doesntHavePower", "%s does not have that power");
            this.add("commands.ability.locking.success", "The %s abilities of the %s power have been locked for %s entities");
            this.add("commands.ability.unlocking.success", "The %s abilities of the %s power have been unlocked for %s entities");

            this.add("commands.energybar.error.noLivingEntity", "This entity is not a living entity");
            this.add("commands.energybar.error.energyBarNotFound", "Energy bar does not exist");
            this.add("commands.energybar.value.get.success", "%s's energy bar %s#%s is at %s");
            this.add("commands.energybar.value.set.success", "%s's energy bar %s#%s was set to %s");
            this.add("commands.energybar.maxValue.get.success", "%s's energy bar %s#%s has a maximum of %s");
            this.add("commands.energybar.maxValue.set.success", "%s's energy bar %s#%s was set to have a maximum of %s");
            this.add("commands.energybar.maxValue.reset.success", "%s's energy bar %s#%s was reset to have a maximum of %s");

            this.add("argument.entity.options.palladium.power.description", "Required power");

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

            this.add(AccessorySlot.HAT, "Hat");
            this.add(AccessorySlot.HEAD, "Head");
            this.add(AccessorySlot.FACE, "Face");
            this.add(AccessorySlot.CHEST, "Chest");
            this.add(AccessorySlot.BACK, "Back");
            this.add(AccessorySlot.MAIN_ARM, "Main Arm");
            this.add(AccessorySlot.OFF_ARM, "Off Arm");
            this.add(AccessorySlot.MAIN_HAND, "Main Hand");
            this.add(AccessorySlot.OFF_HAND, "Off Hand");
            this.add(AccessorySlot.RIGHT_LEG, "Right Leg");
            this.add(AccessorySlot.LEFT_LEG, "Left Leg");
            this.add(AccessorySlot.SPECIAL, "Special");

            // Subtitles
            this.add(PalladiumSoundDefinitionsProvider.subtitle(PalladiumSoundEvents.HEAT_VISION), "Heat Vision");
        }
    }

    public static class German extends PalladiumLangProvider {

        public German(PackOutput packOutput) {
            super(packOutput, "de_de");
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
            this.addBlock(PalladiumBlocks.LARGE_REDSTONE_FLUX_CRYSTAL_BUD, "Große Redstone-Flux-Kristallknospe");
            this.addBlock(PalladiumBlocks.REDSTONE_FLUX_CRYSTAL_CLUSTER, "Redstone-Flux-Kristallhaufen");
            this.addBlock(PalladiumBlocks.LEAD_BLOCK, "Bleiblock");
            this.addBlock(PalladiumBlocks.VIBRANIUM_BLOCK, "Vibraniumblock");
            this.addBlock(PalladiumBlocks.RAW_LEAD_BLOCK, "Rohbleiblock");
            this.addBlock(PalladiumBlocks.RAW_TITANIUM_BLOCK, "Rohtitaniumblock");
            this.addBlock(PalladiumBlocks.RAW_VIBRANIUM_BLOCK, "Rohvibraniumblock");
            this.addBlock(PalladiumBlocks.HEART_SHAPED_HERB, "Herzförmiges Kraut");
            this.addBlock(PalladiumBlocks.POTTED_HEART_SHAPED_HERB, "Eingetopftes Herzförmige Kraut");

            // Items
            this.addItem(PalladiumItems.RAW_LEAD, "Rohblei");
            this.addItem(PalladiumItems.LEAD_INGOT, "Bleibarren");
            this.addItem(PalladiumItems.RAW_TITANIUM, "Rohtitanium");
            this.addItem(PalladiumItems.RAW_VIBRANIUM, "Rohvibranium");
            this.addItem(PalladiumItems.VIBRANIUM_INGOT, "Vibraniumbarren");
            this.addItem(PalladiumItems.REDSTONE_FLUX_CRYSTAL, "Redstone-Flux-Kristall");
            this.addItem(PalladiumItems.SUIT_STAND, "Anzugsständer");
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
            this.addAttribute(PalladiumAttributes.FLIGHT_FLEXIBILITY, "Flugflexibilität");
            this.addAttribute(PalladiumAttributes.HEROIC_FLIGHT_TYPE, "Heroischer Flug");
            this.addAttribute(PalladiumAttributes.PUNCH_DAMAGE, "Schlagkraft");
            this.addAttribute(PalladiumAttributes.JUMP_POWER, "Sprungkraft");
            this.addAttribute(PalladiumAttributes.DESTROY_SPEED, "Abbaugeschwindigkeit");
            this.addAttribute(PalladiumAttributes.FALL_RESISTANCE, "Fallwiderstand");
            this.addAttribute(PalladiumAttributes.LEAPING, "Weitsprung");
            this.add("attribute.name.forge.swim_speed", "Schwimmgeschwindigkeit");

            // Abilities
            this.addAbility(Abilities.DUMMY, "Dummy");
            this.addAbility(Abilities.COMMAND, "Befehl");
            this.addAbility(Abilities.RENDER_LAYER, "Render Layer");
            this.addAbility(Abilities.RENDER_LAYER_BY_ACCESSORY_SLOT, "Render Layer");
            this.addAbility(Abilities.ANIMATION_TIMER, "Animations-Timer");
            this.addAbility(Abilities.REPEATING_ANIMATION_TIMER, "Animations-Timer");
            this.addAbility(Abilities.SHRINK_BODY_OVERLAY, "Körperoverlay schrumpfen");
            this.addAbility(Abilities.ATTRIBUTE_MODIFIER, "Attributmodifikator");
            this.addAbility(Abilities.HEALING, "Heilung");
            this.addAbility(Abilities.SLOWFALL, "Langsamer Fall");
            this.addAbility(Abilities.DAMAGE_IMMUNITY, "Schadensimmunität");
            this.addAbility(Abilities.INVISIBILITY, "Unsichtbarkeit");
            this.addAbility(Abilities.SIZE, "Größe");
            this.addAbility(Abilities.PROJECTILE, "Projektil");
            this.addAbility(Abilities.SKIN_CHANGE, "Skin Änderung");
            this.addAbility(Abilities.AIM, "Zielen");
            this.addAbility(Abilities.HIDE_BODY_PART, "Körperteile verstecken");
            this.addAbility(Abilities.REMOVE_BODY_PART, "Körperteile entfernen");
            this.addAbility(Abilities.SHADER_EFFECT, "Shader Effekt");
            this.addAbility(Abilities.GUI_OVERLAY, "GUI-Overlay");
            this.addAbility(Abilities.SHOW_BOTH_ARMS, "Beide Arme zeigen");
            this.addAbility(Abilities.PLAYER_ANIMATION, "Spieler-Animation");
            this.addAbility(Abilities.WATER_WALK, "Auf Wasser Laufen");
            this.addAbility(Abilities.FLUID_WALKING, "Auf Flüssigkeit Laufen");
            this.addAbility(Abilities.RESTRICT_SLOTS, "Slots beschränken");
            this.addAbility(Abilities.PLAY_SOUND, "Sound");
            this.addAbility(Abilities.VIBRATE, "Vibrieren");
            this.addAbility(Abilities.INTANGIBILITY, "Ungreifbarkeit");
            this.addAbility(Abilities.NAME_CHANGE, "Namesänderung");
            this.addAbility(Abilities.SCULK_IMMUNITY, "Sculk-Immunität");
            this.add("ability.geckolib.render_layer_animation", "Gecko Render Layer Animation");
            this.add("ability.geckolib.armor_animation", "Gecko Armor Animation");

            // Creative Tab
            this.add("itemGroup.palladium.technology", "Technologie");
            this.add("itemGroup.palladium.mods", "Palladium Mods");

            // Key Mappings
            this.add(PalladiumKeyMappings.CATEGORY, "Fähigkeiten");
            this.add("key.palladium.switch_ability_list", "Fähigkeitenliste wechseln");
            this.add("key.palladium.open_equipment", "Equipment öffnen");
            for (int i = 1; i <= PalladiumKeyMappings.ABILITY_KEYS.length; i++) {
                this.add("key.palladium.ability_" + i, "Fähigkeit " + i);
            }

            // Commands
            this.add("commands.superpower.error.powerNotFound", "Es wurde keine Kraft mit dem Namen '%1$s' gefunden");
            this.add("commands.superpower.error.noLivingEntity", "Objekt ist nicht lebend");
            this.add("commands.superpower.error.noSuperpowers", "%s hat keine Superkräfte");
            this.add("commands.superpower.query.success", "%s hat die folgenden Superkräfte: %s");
            this.add("commands.superpower.success.entity.single", "%s hat die Superkraft %s erhalten");
            this.add("commands.superpower.success.entity.multiple", "%s Lebewesen haben die Superkraft %s erhalten");
            this.add("commands.superpower.remove.success.entity.single", "%s's Superkraft/-kräfte wurde entfernt");
            this.add("commands.superpower.remove.success.entity.multiple", "Die Superkraft/-kräfte von %s Lebewesen wurde entfernt");
            this.add("commands.superpower.replace.success.entity.single", "%s's Superkraft/-kräfte wurden ausgetauscht");
            this.add("commands.superpower.replace.success.entity.multiple", "Die Superkraft/-kräfte von %s Lebewesen wurden ausgetauscht");
            this.add("commands.superpower.error.alreadyHasSuperpower", "%s hat bereits diese Superkraft");
            this.add("commands.superpower.error.doesntHaveSuperpower", "%s hat diese Superkraft nicht");

            this.add("commands.ability.error.notUnlockable", "Die %s Fähigkeit von der %s Kraft kann nicht freigeschaltet werden");
            this.add("commands.ability.error.doesntHavePower", "%s hat diese Kraft nicht");
            this.add("commands.ability.locking.success", "Die %s Fähigkeiten von der %s Kraft wurden für %s Lebewesen gesperrt");
            this.add("commands.ability.unlocking.success", "Die %s Fähigkeiten von der %s Kraft wurden für %s Lebewesen freigeschaltet");

            this.add("commands.energybar.error.noLivingEntity", "Objekt ist nicht lebend");
            this.add("commands.energybar.error.energyBarNotFound", "Energy-Bar existiert nicht");
            this.add("commands.energybar.value.get.success", "%s's Energy-Bar %s#%s ist bei %s");
            this.add("commands.energybar.value.set.success", "%s's Energy-Bar %s#%s wurde auf %s gesetzt");
            this.add("commands.energybar.maxValue.get.success", "%s's Energy-Bar %s#%s hat ein Maximum von %s");
            this.add("commands.energybar.maxValue.set.success", "%s's Energy-Bar %s#%s wurde auf ein Maximum von %s gesetzt");
            this.add("commands.energybar.maxValue.reset.success", "%s's Energy-Bar %s#%s wurde auf ein Maximum von %s zurückgesetzt");

            this.add("argument.entity.options.palladium.power.description", "Benötigte Kraft");

            // GUI
            this.add("gui.palladium.powers", "Kräfte");
            this.add("gui.palladium.powers.buy_ability", "Möchtest du diese Fähigkeit freischalten?");
            this.add("gui.palladium.powers.buy_ability.or", "oder");
            this.add("gui.palladium.powers.buy_ability.experience_level", "Erfahrungsstufe");
            this.add("gui.palladium.powers.buy_ability.experience_level_plural", "Erfahrungsstufen");
            this.add("gui.palladium.accessories", "Zubehör");
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

            this.add(AccessorySlot.HAT, "Hut");
            this.add(AccessorySlot.HEAD, "Kopf");
            this.add(AccessorySlot.FACE, "Gesicht");
            this.add(AccessorySlot.CHEST, "Brust");
            this.add(AccessorySlot.BACK, "Rücken");
            this.add(AccessorySlot.MAIN_ARM, "Hauptarm");
            this.add(AccessorySlot.OFF_ARM, "Zweitarm");
            this.add(AccessorySlot.MAIN_HAND, "Haupthand");
            this.add(AccessorySlot.OFF_HAND, "Zweithand");
            this.add(AccessorySlot.RIGHT_LEG, "Rechtes Bein");
            this.add(AccessorySlot.LEFT_LEG, "Linkes Bein");
            this.add(AccessorySlot.SPECIAL, "Spezial");

            // Subtitles
            this.add(PalladiumSoundDefinitionsProvider.subtitle(PalladiumSoundEvents.HEAT_VISION), "Hitzeblick");
        }
    }

    public static class Saxon extends PalladiumLangProvider {

        public Saxon(PackOutput packOutput) {
            super(packOutput, "sxu");
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
            this.addBlock(PalladiumBlocks.LARGE_REDSTONE_FLUX_CRYSTAL_BUD, "Große Redstone-Flux-Kristallgnosbe");
            this.addBlock(PalladiumBlocks.REDSTONE_FLUX_CRYSTAL_CLUSTER, "Redstone-Flux-Kristallhaufn");
            this.addBlock(PalladiumBlocks.LEAD_BLOCK, "Bleblogg");
            this.addBlock(PalladiumBlocks.VIBRANIUM_BLOCK, "Vibraniumblogg");
            this.addBlock(PalladiumBlocks.RAW_LEAD_BLOCK, "Rohbleblogg");
            this.addBlock(PalladiumBlocks.RAW_TITANIUM_BLOCK, "Rohtitaniumblogg");
            this.addBlock(PalladiumBlocks.RAW_VIBRANIUM_BLOCK, "Rohvibraniumblogg");
            this.addBlock(PalladiumBlocks.HEART_SHAPED_HERB, "Herzförmijes Graud");
            this.addBlock(PalladiumBlocks.POTTED_HEART_SHAPED_HERB, "Eingedobbdes Herzförmije Graud");

            // Items
            this.addItem(PalladiumItems.RAW_LEAD, "Rohble");
            this.addItem(PalladiumItems.LEAD_INGOT, "Blebarrn");
            this.addItem(PalladiumItems.RAW_TITANIUM, "Rohtitanium");
            this.addItem(PalladiumItems.RAW_VIBRANIUM, "Rohvibranium");
            this.addItem(PalladiumItems.VIBRANIUM_INGOT, "Vibraniumbarrn");
            this.addItem(PalladiumItems.REDSTONE_FLUX_CRYSTAL, "Redstone-Flux-Kristall");
            this.addItem(PalladiumItems.SUIT_STAND, "Anzuchsschdändorr");
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
            this.addAttribute(PalladiumAttributes.FLIGHT_FLEXIBILITY, "Flugflexibilität");
            this.addAttribute(PalladiumAttributes.HEROIC_FLIGHT_TYPE, "Heroischer Flug");
            this.addAttribute(PalladiumAttributes.PUNCH_DAMAGE, "Schlachkraft");
            this.addAttribute(PalladiumAttributes.JUMP_POWER, "Sprungkraft");
            this.addAttribute(PalladiumAttributes.DESTROY_SPEED, "Abbohjeschwindichket");
            this.addAttribute(PalladiumAttributes.FALL_RESISTANCE, "Fallwidorstand");
            this.addAttribute(PalladiumAttributes.LEAPING, "Wehtsprung");
            this.add("attribute.name.forge.swim_speed", "Schwimmjeschwindigkeit");

            // Abilities
            this.addAbility(Abilities.DUMMY, "Dummy");
            this.addAbility(Abilities.COMMAND, "Befehl");
            this.addAbility(Abilities.RENDER_LAYER, "Render Layer");
            this.addAbility(Abilities.RENDER_LAYER_BY_ACCESSORY_SLOT, "Render Layer");
            this.addAbility(Abilities.ANIMATION_TIMER, "Animations-Timer");
            this.addAbility(Abilities.REPEATING_ANIMATION_TIMER, "Animations-Timer");
            this.addAbility(Abilities.SHRINK_BODY_OVERLAY, "Körperoverlay schrumpfen");
            this.addAbility(Abilities.ATTRIBUTE_MODIFIER, "Ättribütmodifikator");
            this.addAbility(Abilities.HEALING, "Helung");
            this.addAbility(Abilities.SLOWFALL, "Langsamer Fall");
            this.addAbility(Abilities.DAMAGE_IMMUNITY, "Schadensimmunität");
            this.addAbility(Abilities.INVISIBILITY, "Unsischtbarkeet");
            this.addAbility(Abilities.SIZE, "Größe");
            this.addAbility(Abilities.PROJECTILE, "Projektil");
            this.addAbility(Abilities.SKIN_CHANGE, "Skin Änderung");
            this.addAbility(Abilities.AIM, "Zielen");
            this.addAbility(Abilities.HIDE_BODY_PART, "Görperdeile versteggen");
            this.addAbility(Abilities.REMOVE_BODY_PART, "Görperdeile entfernen");
            this.addAbility(Abilities.SHADER_EFFECT, "Shader Effekt");
            this.addAbility(Abilities.GUI_OVERLAY, "GUI-Overlay");
            this.addAbility(Abilities.SHOW_BOTH_ARMS, "Beide Arme zeijen");
            this.addAbility(Abilities.PLAYER_ANIMATION, "Spieler-Animation");
            this.addAbility(Abilities.WATER_WALK, "Uff Wasser Lofen");
            this.addAbility(Abilities.FLUID_WALKING, "Uff Flüssichkeht Lofen");
            this.addAbility(Abilities.RESTRICT_SLOTS, "Slots beschränken");
            this.addAbility(Abilities.PLAY_SOUND, "Sound");
            this.addAbility(Abilities.VIBRATE, "Vibrieren");
            this.addAbility(Abilities.INTANGIBILITY, "Ungreifbarkeht");
            this.addAbility(Abilities.NAME_CHANGE, "Namesänderung");
            this.addAbility(Abilities.SCULK_IMMUNITY, "Sculk-Immunität");
            this.add("ability.geckolib.render_layer_animation", "Gecko Render Layer Animation");
            this.add("ability.geckolib.armor_animation", "Gecko Armor Animation");

            // Creative Tab
            this.add("itemGroup.palladium.technology", "Technolojie");
            this.add("itemGroup.palladium.mods", "Palladium Mods");

            // Key Mappings
            this.add(PalladiumKeyMappings.CATEGORY, "Fähischgehden");
            this.add("key.palladium.switch_ability_list", "Fähischgehden wechseln");
            this.add("key.palladium.open_equipment", "Equipmend öffnen");
            for (int i = 1; i <= PalladiumKeyMappings.ABILITY_KEYS.length; i++) {
                this.add("key.palladium.ability_" + i, "Fähischgehd " + i);
            }

            // Commands
            this.add("commands.superpower.error.powerNotFound", "Et wurd' kene Kraft mit'm Namen '%1$s' jefunden");
            this.add("commands.superpower.error.noLivingEntity", "Objekt ist nicht lebend");
            this.add("commands.superpower.error.noSuperpowers", "%s hat kene Supperkräfte");
            this.add("commands.superpower.query.success", "%s hat de foljend'n Supperkräfte: %s");
            this.add("commands.superpower.success.entity.single", "%s hat de Supperkraft %s erhalten");
            this.add("commands.superpower.success.entity.multiple", "%s Lebewesen haben de Supperkraft %s erhalten");
            this.add("commands.superpower.remove.success.entity.single", "%s's Supperkraft/-kräfte wurd' entfernt");
            this.add("commands.superpower.remove.success.entity.multiple", "De Supperkraft/-kräfte von %s Lebewesen wurd' entfernt");
            this.add("commands.superpower.replace.success.entity.single", "%s's Supperkraft/-kräfte wurd'n ausjetoscht");
            this.add("commands.superpower.replace.success.entity.multiple", "De Supperkraft/-kräfte von %s Lebewesen wurd'n ausjetoscht");
            this.add("commands.superpower.error.alreadyHasSuperpower", "%s had berehts dise Supperkraft");
            this.add("commands.superpower.error.doesntHaveSuperpower", "%s had dese Supperkraft nich");

            this.add("commands.ability.error.notUnlockable", "De %s Fähischgehd von der %s Kraft kann nüsch freigeschaltet werd'n");
            this.add("commands.ability.error.doesntHavePower", "%s hat diese Kraft nüsch");
            this.add("commands.ability.locking.success", "De %s Fähischgehden von der %s Kraft wurd'n für %s Lebewesen jesperrt");
            this.add("commands.ability.unlocking.success", "De %s Fähischgehden von der %s Kraft wurd'n für %s Lebewesen freijeschaltet");

            this.add("commands.energybar.error.noLivingEntity", "Objekt ist nicht lebend");
            this.add("commands.energybar.error.energyBarNotFound", "Energy-Bar existiert nüscht");
            this.add("commands.energybar.value.get.success", "%s's Energy-Bar %s#%s is bei %s");
            this.add("commands.energybar.value.set.success", "%s's Energy-Bar %s#%s wurd' uff %s jesetzt");
            this.add("commands.energybar.maxValue.get.success", "%s's Energy-Bar %s#%s hat ehn Maximum von %s");
            this.add("commands.energybar.maxValue.set.success", "%s's Energy-Bar %s#%s wurd' uff ehn Maximum von %s jesetzt");
            this.add("commands.energybar.maxValue.reset.success", "%s's Energy-Bar %s#%s wurd' uff ehn Maximum von %s zurückjesetzt");

            this.add("argument.entity.options.palladium.power.description", "Benötichte Kraft");

            // GUI
            this.add("gui.palladium.powers", "Kräfte");
            this.add("gui.palladium.powers.buy_ability", "Möschtest'e disse Fähischgeht freischaltn?");
            this.add("gui.palladium.powers.buy_ability.or", "oder");
            this.add("gui.palladium.powers.buy_ability.experience_level", "Erfahrungsschdufe");
            this.add("gui.palladium.powers.buy_ability.experience_level_plural", "Erfahrungsschdufen");
            this.add("gui.palladium.accessories", "Zubehör");
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

            this.add(AccessorySlot.HAT, "Hut");
            this.add(AccessorySlot.HEAD, "Nischel");
            this.add(AccessorySlot.FACE, "Gusche");
            this.add(AccessorySlot.CHEST, "Brust");
            this.add(AccessorySlot.BACK, "Rüggen");
            this.add(AccessorySlot.MAIN_ARM, "Haupdarm");
            this.add(AccessorySlot.OFF_ARM, "Zwehtarm");
            this.add(AccessorySlot.MAIN_HAND, "Haupthand");
            this.add(AccessorySlot.OFF_HAND, "Zwehthand");
            this.add(AccessorySlot.RIGHT_LEG, "Reschdes Behn");
            this.add(AccessorySlot.LEFT_LEG, "Linges Behn");
            this.add(AccessorySlot.SPECIAL, "Spezial");

            // Subtitles
            this.add(PalladiumSoundDefinitionsProvider.subtitle(PalladiumSoundEvents.HEAT_VISION), "Hitzeblick");
        }
    }

    @Override
    public @NotNull String getName() {
        return "Palladium " + super.getName();
    }
}
