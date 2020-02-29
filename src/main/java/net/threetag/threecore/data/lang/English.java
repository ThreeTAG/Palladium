package net.threetag.threecore.data.lang;

import net.minecraft.data.DataGenerator;
import net.threetag.threecore.ability.AbilityType;
import net.threetag.threecore.block.TCBlocks;
import net.threetag.threecore.container.TCContainerTypes;
import net.threetag.threecore.entity.TCEntityTypes;
import net.threetag.threecore.item.TCItems;
import net.threetag.threecore.potion.TCEffects;

public class English extends ThreeCoreLanguageProvider {

    public English(DataGenerator gen) {
        super(gen, "en_us");
    }

    @Override
    protected void addTranslations() {
        // Item Groups
        this.add("itemGroup.technology", "Technology");
        this.add("itemGroup.suits_and_armor", "Suits & Armor");


        // Blocks
        this.addBlock(TCBlocks.CONSTRUCTION_TABLE, "Construction Table");
        this.addBlock(TCBlocks.GRINDER, "Grinder");
        this.addBlock(TCBlocks.HYDRAULIC_PRESS, "Hydraulic Press");
        this.addBlock(TCBlocks.FLUID_COMPOSER, "Fluid Composer");
        this.addBlock(TCBlocks.CAPACITOR_BLOCK, "Capacitor Block");
        this.addBlock(TCBlocks.ADVANCED_CAPACITOR_BLOCK, "Advanced Capacitor Block");
        this.addBlock(TCBlocks.STIRLING_GENERATOR, "Stirling Generator");
        this.addBlock(TCBlocks.SOLAR_PANEL, "Solar Panel");
        this.addBlock(TCBlocks.GOLD_CONDUIT, "Gold Energy Conduit");
        this.addBlock(TCBlocks.COPPER_CONDUIT, "Copper Energy Conduit");
        this.addBlock(TCBlocks.SILVER_CONDUIT, "Silver Energy Conduit");
        this.add("block.threecore.conduit_tooltip", "Transfers %s %s/tick");

        this.addBlock(TCBlocks.COPPER_BLOCK, "Block of Copper");
        this.addBlock(TCBlocks.TIN_BLOCK, "Block of Tin");
        this.addBlock(TCBlocks.LEAD_BLOCK, "Block of Lead");
        this.addBlock(TCBlocks.SILVER_BLOCK, "Block of Silver");
        this.addBlock(TCBlocks.PALLADIUM_BLOCK, "Block of Palladium");
        this.addBlock(TCBlocks.VIBRANIUM_BLOCK, "Block of Vibranium");
        this.addBlock(TCBlocks.OSMIUM_BLOCK, "Block of Osmium");
        this.addBlock(TCBlocks.URANIUM_BLOCK, "Block of Uranium");
        this.addBlock(TCBlocks.TITANIUM_BLOCK, "Block of Titanium");
        this.addBlock(TCBlocks.IRIDIUM_BLOCK, "Block of Iridium");
        this.addBlock(TCBlocks.URU_BLOCK, "Block of Uru");
        this.addBlock(TCBlocks.BRONZE_BLOCK, "Block of Bronze");
        this.addBlock(TCBlocks.INTERTIUM_BLOCK, "Block of Intertium");
        this.addBlock(TCBlocks.STEEL_BLOCK, "Block of Steel");
        this.addBlock(TCBlocks.GOLD_TITANIUM_ALLOY_BLOCK, "Block of Gold-Titanium-Alloy");
        this.addBlock(TCBlocks.ADAMANTIUM_BLOCK, "Block of Adamantium");

        this.addBlock(TCBlocks.COPPER_ORE, "Copper Ore");
        this.addBlock(TCBlocks.TIN_ORE, "Tin Ore");
        this.addBlock(TCBlocks.LEAD_ORE, "Lead Ore");
        this.addBlock(TCBlocks.SILVER_ORE, "Silver Ore");
        this.addBlock(TCBlocks.PALLADIUM_ORE, "Palladium Ore");
        this.addBlock(TCBlocks.VIBRANIUM_ORE, "Vibranium Ore");
        this.addBlock(TCBlocks.OSMIUM_ORE, "Osmium Ore");
        this.addBlock(TCBlocks.URANIUM_ORE, "Uranium Ore");
        this.addBlock(TCBlocks.TITANIUM_ORE, "Titanium Ore");
        this.addBlock(TCBlocks.IRIDIUM_ORE, "Iridium Ore");
        this.addBlock(TCBlocks.URU_ORE, "Uru Ore");

        this.addBlock(TCBlocks.WHITE_CONCRETE_SLAB, "White Concrete Slab");
        this.addBlock(TCBlocks.ORANGE_CONCRETE_SLAB, "Orange Concrete Slab");
        this.addBlock(TCBlocks.MAGENTA_CONCRETE_SLAB, "Magenta Concrete Slab");
        this.addBlock(TCBlocks.LIGHT_BLUE_CONCRETE_SLAB, "Light Blue Concrete Slab");
        this.addBlock(TCBlocks.YELLOW_CONCRETE_SLAB, "Yellow Concrete Slab");
        this.addBlock(TCBlocks.LIME_CONCRETE_SLAB, "Lime Concrete Slab");
        this.addBlock(TCBlocks.PINK_CONCRETE_SLAB, "Pink Concrete Slab");
        this.addBlock(TCBlocks.GRAY_CONCRETE_SLAB, "Gray Concrete Slab");
        this.addBlock(TCBlocks.LIGHT_GRAY_CONCRETE_SLAB, "Light Gray Concrete Slab");
        this.addBlock(TCBlocks.CYAN_CONCRETE_SLAB, "Cyan Concrete Slab");
        this.addBlock(TCBlocks.PURPLE_CONCRETE_SLAB, "Purple Concrete Slab");
        this.addBlock(TCBlocks.BLUE_CONCRETE_SLAB, "Blue Concrete Slab");
        this.addBlock(TCBlocks.BROWN_CONCRETE_SLAB, "Brown Concrete Slab");
        this.addBlock(TCBlocks.GREEN_CONCRETE_SLAB, "Green Concrete Slab");
        this.addBlock(TCBlocks.RED_CONCRETE_SLAB, "Red Concrete Slab");
        this.addBlock(TCBlocks.BLACK_CONCRETE_SLAB, "Black Concrete Slab");

        this.addBlock(TCBlocks.WHITE_CONCRETE_STAIRS, "White Concrete Stairs");
        this.addBlock(TCBlocks.ORANGE_CONCRETE_STAIRS, "Orange Concrete Stairs");
        this.addBlock(TCBlocks.MAGENTA_CONCRETE_STAIRS, "Magenta Concrete Stairs");
        this.addBlock(TCBlocks.LIGHT_BLUE_CONCRETE_STAIRS, "Light Blue Concrete Stairs");
        this.addBlock(TCBlocks.YELLOW_CONCRETE_STAIRS, "Yellow Concrete Stairs");
        this.addBlock(TCBlocks.LIME_CONCRETE_STAIRS, "Lime Concrete Stairs");
        this.addBlock(TCBlocks.PINK_CONCRETE_STAIRS, "Pink Concrete Stairs");
        this.addBlock(TCBlocks.GRAY_CONCRETE_STAIRS, "Gray Concrete Stairs");
        this.addBlock(TCBlocks.LIGHT_GRAY_CONCRETE_STAIRS, "Light Gray Concrete Stairs");
        this.addBlock(TCBlocks.CYAN_CONCRETE_STAIRS, "Cyan Concrete Stairs");
        this.addBlock(TCBlocks.PURPLE_CONCRETE_STAIRS, "Purple Concrete Stairs");
        this.addBlock(TCBlocks.BLUE_CONCRETE_STAIRS, "Blue Concrete Stairs");
        this.addBlock(TCBlocks.BROWN_CONCRETE_STAIRS, "Brown Concrete Stairs");
        this.addBlock(TCBlocks.GREEN_CONCRETE_STAIRS, "Green Concrete Stairs");
        this.addBlock(TCBlocks.RED_CONCRETE_STAIRS, "Red Concrete Stairs");
        this.addBlock(TCBlocks.BLACK_CONCRETE_STAIRS, "Black Concrete Stairs");


        // Items
        this.addItem(TCItems.HAMMER, "Hammer");
        this.addItem(TCItems.PLATE_CAST, "Plate Cast");
        this.addItem(TCItems.CAPACITOR, "Capacitor");
        this.addItem(TCItems.ADVANCED_CAPACITOR, "Advanced Capacitor");
        this.addItem(TCItems.CIRCUIT, "Circuit");
        this.addItem(TCItems.ADVANCED_CIRCUIT, "Advanced Circuit");
        this.addItem(TCItems.VIAL, "Vial");
        this.addItem(TCItems.SUIT_STAND, "Suit Stand");

        this.addItem(TCItems.COPPER_INGOT, "Copper Ingot");
        this.addItem(TCItems.TIN_INGOT, "Tin Ingot");
        this.addItem(TCItems.LEAD_INGOT, "Lead Ingot");
        this.addItem(TCItems.SILVER_INGOT, "Silver Ingot");
        this.addItem(TCItems.PALLADIUM_INGOT, "Palladium Ingot");
        this.addItem(TCItems.VIBRANIUM_INGOT, "Vibranium Ingot");
        this.addItem(TCItems.OSMIUM_INGOT, "Osmium Ingot");
        this.addItem(TCItems.URANIUM_INGOT, "Uranium Ingot");
        this.addItem(TCItems.TITANIUM_INGOT, "Titanium Ingot");
        this.addItem(TCItems.IRIDIUM_INGOT, "Iridium Ingot");
        this.addItem(TCItems.URU_INGOT, "Uru Ingot");
        this.addItem(TCItems.BRONZE_INGOT, "Bronze Ingot");
        this.addItem(TCItems.INTERTIUM_INGOT, "Intertium Ingot");
        this.addItem(TCItems.STEEL_INGOT, "Steel Ingot");
        this.addItem(TCItems.GOLD_TITANIUM_ALLOY_INGOT, "Gold-Titanium-Alloy Ingot");
        this.addItem(TCItems.ADAMANTIUM_INGOT, "Adamantium Ingot");

        this.addItem(TCItems.COPPER_NUGGET, "Copper Nugget");
        this.addItem(TCItems.TIN_NUGGET, "Tin Nugget");
        this.addItem(TCItems.LEAD_NUGGET, "Lead Nugget");
        this.addItem(TCItems.SILVER_NUGGET, "Silver Nugget");
        this.addItem(TCItems.PALLADIUM_NUGGET, "Palladium Nugget");
        this.addItem(TCItems.VIBRANIUM_NUGGET, "Vibranium Nugget");
        this.addItem(TCItems.OSMIUM_NUGGET, "Osmium Nugget");
        this.addItem(TCItems.URANIUM_NUGGET, "Uranium Nugget");
        this.addItem(TCItems.TITANIUM_NUGGET, "Titanium Nugget");
        this.addItem(TCItems.IRIDIUM_NUGGET, "Iridium Nugget");
        this.addItem(TCItems.URU_NUGGET, "Uru Nugget");
        this.addItem(TCItems.BRONZE_NUGGET, "Bronze Nugget");
        this.addItem(TCItems.INTERTIUM_NUGGET, "Intertium Nugget");
        this.addItem(TCItems.STEEL_NUGGET, "Steel Nugget");
        this.addItem(TCItems.GOLD_TITANIUM_ALLOY_NUGGET, "Gold-Titanium-Alloy Nugget");
        this.addItem(TCItems.ADAMANTIUM_NUGGET, "Adamantium Nugget");

        this.addItem(TCItems.COAL_DUST, "Coal Dust");
        this.addItem(TCItems.CHARCOAL_DUST, "Charcoal Dust");
        this.addItem(TCItems.IRON_DUST, "Iron Dust");
        this.addItem(TCItems.GOLD_DUST, "Gold Dust");
        this.addItem(TCItems.COPPER_DUST, "Copper Dust");
        this.addItem(TCItems.TIN_DUST, "Tin Dust");
        this.addItem(TCItems.LEAD_DUST, "Lead Dust");
        this.addItem(TCItems.SILVER_DUST, "Silver Dust");
        this.addItem(TCItems.PALLADIUM_DUST, "Palladium Dust");
        this.addItem(TCItems.VIBRANIUM_DUST, "Vibranium Dust");
        this.addItem(TCItems.OSMIUM_DUST, "Osmium Dust");
        this.addItem(TCItems.URANIUM_DUST, "Uranium Dust");
        this.addItem(TCItems.TITANIUM_DUST, "Titanium Dust");
        this.addItem(TCItems.IRIDIUM_DUST, "Iridium Dust");
        this.addItem(TCItems.URU_DUST, "Uru Dust");
        this.addItem(TCItems.BRONZE_DUST, "Bronze Dust");
        this.addItem(TCItems.INTERTIUM_DUST, "Intertium Dust");
        this.addItem(TCItems.STEEL_DUST, "Steel Dust");
        this.addItem(TCItems.GOLD_TITANIUM_ALLOY_DUST, "Gold-Titanium-Alloy Dust");
        this.addItem(TCItems.ADAMANTIUM_DUST, "Adamantium Dust");

        this.addItem(TCItems.IRON_PLATE, "Iron Plate");
        this.addItem(TCItems.GOLD_PLATE, "Gold Plate");
        this.addItem(TCItems.COPPER_PLATE, "Copper Plate");
        this.addItem(TCItems.TIN_PLATE, "Tin Plate");
        this.addItem(TCItems.LEAD_PLATE, "Lead Plate");
        this.addItem(TCItems.SILVER_PLATE, "Silver Plate");
        this.addItem(TCItems.PALLADIUM_PLATE, "Palladium Plate");
        this.addItem(TCItems.VIBRANIUM_PLATE, "Vibranium Plate");
        this.addItem(TCItems.OSMIUM_PLATE, "Osmium Plate");
        this.addItem(TCItems.URANIUM_PLATE, "Uranium Plate");
        this.addItem(TCItems.TITANIUM_PLATE, "Titanium Plate");
        this.addItem(TCItems.IRIDIUM_PLATE, "Iridium Plate");
        this.addItem(TCItems.URU_PLATE, "Uru Plate");
        this.addItem(TCItems.BRONZE_PLATE, "Bronze Plate");
        this.addItem(TCItems.INTERTIUM_PLATE, "Intertium Plate");
        this.addItem(TCItems.STEEL_PLATE, "Steel Plate");
        this.addItem(TCItems.GOLD_TITANIUM_ALLOY_PLATE, "Gold-Titanium-Alloy Plate");
        this.addItem(TCItems.ADAMANTIUM_PLATE, "Adamantium Plate");

        this.addItem(TCItems.WHITE_FABRIC, "White Fabric");
        this.addItem(TCItems.ORANGE_FABRIC, "Orange Fabric");
        this.addItem(TCItems.MAGENTA_FABRIC, "Magenta Fabric");
        this.addItem(TCItems.LIGHT_BLUE_FABRIC, "Light Blue Fabric");
        this.addItem(TCItems.YELLOW_FABRIC, "Yellow Fabric");
        this.addItem(TCItems.LIME_FABRIC, "Lime Fabric");
        this.addItem(TCItems.PINK_FABRIC, "Pink Fabric");
        this.addItem(TCItems.GRAY_FABRIC, "Gray Fabric");
        this.addItem(TCItems.LIGHT_GRAY_FABRIC, "Light Gray Fabric");
        this.addItem(TCItems.CYAN_FABRIC, "Cyan Fabric");
        this.addItem(TCItems.PURPLE_FABRIC, "Purple Fabric");
        this.addItem(TCItems.BLUE_FABRIC, "Blue Fabric");
        this.addItem(TCItems.BROWN_FABRIC, "Brown Fabric");
        this.addItem(TCItems.GREEN_FABRIC, "Green Fabric");
        this.addItem(TCItems.RED_FABRIC, "Red Fabric");
        this.addItem(TCItems.BLACK_FABRIC, "Black Fabric");


        // Entities
        this.addEntityType(TCEntityTypes.SUIT_STAND, "Suit Stand");
        this.addEntityType(TCEntityTypes.PROJECTILE, "Projectile");
        this.addEntityType(TCEntityTypes.SOLID_ITEM_ENTITY, "Solid Item");
        this.addEntityType(TCEntityTypes.EFFECT, "Effect");


        // Commands
        this.add("commands.karma.success.player.single", "%s karma has been set to %s");
        this.add("commands.karma.success.player.multiple", "Karma of %s players has been set to %s");
        this.add("commands.karma.players_karma", "%s's karma is at %s. (%s)");

        this.add("commands.sizechange.success.entity.single", "%s was scaled to %s using '%s'");
        this.add("commands.sizechange.success.entity.multiple", "%s entities were scaled to %s using '%s'");
        this.add("commands.sizechange.error.sizeChangeTypeNotFound", "No size change type was found by the name '%1$s'");

        this.add("commands.superpower.error.superpowerNotFound", "No superpower was found by the name '%1$s'");
        this.add("commands.superpower.error.noLivingEntity", "This entity is not a living entity");
        this.add("commands.superpower.success.entity.single", "%s has gained the superpower %s");
        this.add("commands.superpower.success.entity.multiple", "%s entities have gained the superpower %s");
        this.add("commands.superpower.remove.success.entity.single", "%s's superpower was removed");
        this.add("commands.superpower.remove.success.entity.multiple", "The superpower of %s entities was removed");

        this.add("commands.armorstandpose.reloaded", "Loaded %s armor stand poses!");
        this.add("commands.armorstandpose.pose_not_found", "The pose '%s' does not exist!");
        this.add("commands.armorstandpose.no_armor_stand", "You are not looking at an armor stand!");


        // Karma
        this.add("karma.class.tyrant", "Tyrant");
        this.add("karma.class.villain", "Villain");
        this.add("karma.class.thug", "Thug");
        this.add("karma.class.neutral", "Neutral");
        this.add("karma.class.vigilante", "Vigilante");
        this.add("karma.class.hero", "Hero");
        this.add("karma.class.legend", "Legend");
        this.add("karma.toast.title", "Your karma has changed!");


        // Abilities
        this.add(AbilityType.DUMMY, "Dummy");
        this.add(AbilityType.COMMAND, "Command");
        this.add(AbilityType.HEALING, "Healing");
        this.add(AbilityType.FLIGHT, "Flight");
        this.add(AbilityType.ACCELERATING_FLIGHT, "Accelerating Flight");
        this.add(AbilityType.TELEPORT, "Teleport");
        this.add(AbilityType.ATTRIBUTE_MODIFIER, "Attribute Modifier");
        this.add(AbilityType.INVISIBILITY, "Invisibility");
        this.add(AbilityType.SLOWFALL, "Slowfall");
        this.add(AbilityType.WATER_BREATHING, "Water Breathing");
        this.add(AbilityType.SIZE_CHANGE, "Size Change");
        this.add(AbilityType.CUSTOM_HOTBAR, "Custom Hotbar");
        this.add(AbilityType.OPENING_NBT_TIMER, "Open/Close");
        this.add(AbilityType.MODEL_LAYER, "Model Layer");
        this.add(AbilityType.PROJECTILE, "Projectile");

        this.add("ability.condition.threecore.action", "Key must be pressed once to active this ability");
        this.add("ability.condition.threecore.action.not", "Key must be pressed once to deactivate this ability");
        this.add("ability.condition.threecore.held", "Key must be held to activate this ability");
        this.add("ability.condition.threecore.held.not", "Key must be held to deactivate this ability");
        this.add("ability.condition.threecore.toggle", "Ability must be toggled with the key");
        this.add("ability.condition.threecore.toggle.not", "Ability must be toggled with the key");
        this.add("ability.condition.threecore.ability_unlocked", "%s ability must be unlocked");
        this.add("ability.condition.threecore.ability_unlocked.not", "%s ability must be locked");
        this.add("ability.condition.threecore.ability_enabled", "%s ability must be enabled");
        this.add("ability.condition.threecore.ability_enabled.not", "%s ability must be disabled");
        this.add("ability.condition.threecore.cooldown", "Ability must be cooled down");
        this.add("ability.condition.threecore.cooldown.not", "Ability must be cooling down");
        this.add("ability.condition.threecore.karma", "Karma must be between %s and %s");
        this.add("ability.condition.threecore.karma.not", "Karma must be outside of %s and %s");
        this.add("ability.condition.threecore.karma_at", "Karma must be at %s");
        this.add("ability.condition.threecore.karma_at.not", "Karma must something else than %s");
        this.add("ability.condition.threecore.karma_min", "Karma must be at least %s");
        this.add("ability.condition.threecore.karma_min.not", "Karma must be lower than %s");
        this.add("ability.condition.threecore.xp_buy", "Experience must be spent");
        this.add("ability.condition.threecore.xp_buy.not", "Experience must be spent");
        this.add("ability.condition.threecore.xp_buy.levels", "%s experience levels must be spent");
        this.add("ability.condition.threecore.xp_buy.levels.not", "%s experience levels must be spent");
        this.add("ability.condition.threecore.xp_buy.points", "%s experience points must be spent");
        this.add("ability.condition.threecore.xp_buy.points.not", "%s experience points must be spent");
        this.add("ability.condition.threecore.xp_buy.info.levels", "%s experience levels");
        this.add("ability.condition.threecore.xp_buy.info.points", "%s experience points");
        this.add("ability.condition.threecore.item_buy", "An item must be spent");
        this.add("ability.condition.threecore.item_buy.not", "An item must be spent");
        this.add("ability.condition.threecore.item_buy.info", "%sx %s");
        this.add("ability.condition.threecore.equipment_slot", "Item needs to be in the %s slot");
        this.add("ability.condition.threecore.equipment_slot.not", "Item needs to be outside of the %s slot");
        this.add("ability.condition.threecore.equipment_slot.mainhand", "main hand");
        this.add("ability.condition.threecore.equipment_slot.offhand", "off hand");
        this.add("ability.condition.threecore.equipment_slot.feet", "feet");
        this.add("ability.condition.threecore.equipment_slot.legs", "legs");
        this.add("ability.condition.threecore.equipment_slot.chest", "chest");
        this.add("ability.condition.threecore.equipment_slot.head", "head");
        this.add("ability.condition.threecore.xp", "Karma must be between %s and %s");
        this.add("ability.condition.threecore.xp.not", "Karma must be outside of %s and %s");
        this.add("ability.condition.threecore.xp_at", "Karma must be at %s");
        this.add("ability.condition.threecore.xp_at.not", "Karma must something else than %s");
        this.add("ability.condition.threecore.xp_min", "Karma must be at least %s");
        this.add("ability.condition.threecore.xp_min.not", "Karma must be lower than %s");
        this.add("ability.condition.threecore.wearing_item", "Entity must wear specific items");
        this.add("ability.condition.threecore.wearing_item_tag", "Entity must wear items within the %s tag");


        // Container
        this.addContainerType(TCContainerTypes.HELMET_CRAFTING, "Helmet Crafting");
        this.addContainerType(TCContainerTypes.CHESTPLATE_CRAFTING, "Chestplate Crafting");
        this.addContainerType(TCContainerTypes.LEGGINGS_CRAFTING, "Leggings Crafting");
        this.addContainerType(TCContainerTypes.BOOTS_CRAFTING, "Boots Crafting");
        this.addContainerType(TCContainerTypes.GRINDER, "Grinder");
        this.addContainerType(TCContainerTypes.HYDRAULIC_PRESS, "Hydraulic Press");
        this.addContainerType(TCContainerTypes.FLUID_COMPOSER, "Fluid Composer");
        this.addContainerType(TCContainerTypes.STIRLING_GENERATOR, "Stirling Generator");
        this.addContainerType(TCContainerTypes.CAPACITOR_BLOCK, "Capacitor Block");


        // Gui
        this.add("superpower.toast.title", "New superpower!");

        this.add("gui.threecore.abilities", "Abilities");
        this.add("gui.threecore.abilities.fulfill_condition", "Do you want to fulfill this condition?");
        this.add("gui.threecore.abilities.keybind", "Keybind, ");
        this.add("gui.jei.category.threecore.helmet_crafting", "Helmet Crafting");
        this.add("gui.jei.category.threecore.chestplate_crafting", "Chestplate Crafting");
        this.add("gui.jei.category.threecore.leggings_crafting", "Leggings Crafting");
        this.add("gui.jei.category.threecore.boots_crafting", "Boots Crafting");
        this.add("gui.jei.category.threecore.grinding", "Grinding");
        this.add("gui.jei.category.threecore.grinding.experience", "%s XP");
        this.add("gui.jei.category.threecore.pressing", "Pressing");
        this.add("gui.jei.category.threecore.pressing.experience", "%s XP");
        this.add("gui.jei.category.threecore.fluid_composing", "Fluid Composing");

        this.add("ability_container.threecore.player", "Player Abilities");


        // Death
        this.add("death.attack.grinder", "%1$s was grinded to death by a grinder");
        this.add("death.attack.grinder.player", "%1$s was grinded to death by a grinder whilst trying to escape %2$s");


        // Keys
        this.add("key.threecore.scroll_up", "Scroll Up");
        this.add("key.threecore.scroll_down", "Scroll Down");
        this.add("key.threecore.ability_1", "Ability 1");
        this.add("key.threecore.ability_2", "Ability 2");
        this.add("key.threecore.ability_3", "Ability 3");
        this.add("key.threecore.ability_4", "Ability 4");
        this.add("key.threecore.ability_5", "Ability 5");


        // Effects
        this.addEffect(TCEffects.UNCONSCIOUS, "Unconscious");


        // Util
        this.add("threecore.util.energy_display", "%,d %s");
        this.add("threecore.util.energy_storage_display", "%,d / %,d %s");
        this.add("threecore.util.fluid_display", "%,d %s");
        this.add("threecore.util.fluid_tank_display", "%,d / %,d %s");
    }
}
