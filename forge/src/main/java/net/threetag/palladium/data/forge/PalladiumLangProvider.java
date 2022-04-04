package net.threetag.palladium.data.forge;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.LanguageProvider;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.block.PalladiumBlocks;
import net.threetag.palladium.client.PalladiumKeyMappings;
import net.threetag.palladium.item.PalladiumItems;
import net.threetag.palladium.power.ability.Abilities;
import net.threetag.palladium.power.ability.Ability;

import java.util.Objects;
import java.util.function.Supplier;

public abstract class PalladiumLangProvider extends LanguageProvider {

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
            this.addBlock(PalladiumBlocks.SILVER_ORE, "Silver Ore");
            this.addBlock(PalladiumBlocks.DEEPSLATE_SILVER_ORE, "Deepslate Silver Ore");
            this.addBlock(PalladiumBlocks.TITANIUM_ORE, "Titanium Ore");
            this.addBlock(PalladiumBlocks.VIBRANIUM_ORE, "Vibranium Ore");
            this.addBlock(PalladiumBlocks.REDSTONE_FLUX_CRYSTAL_GEODE, "Redstone Flux Crystal Geode");
            this.addBlock(PalladiumBlocks.DEEPSLATE_REDSTONE_FLUX_CRYSTAL_GEODE, "Deepslate Redstone Flux Crystal Geode");
            this.addBlock(PalladiumBlocks.SMALL_REDSTONE_FLUX_CRYSTAL_BUD, "Small Redstone Flux Crystal Bud");
            this.addBlock(PalladiumBlocks.MEDIUM_REDSTONE_FLUX_CRYSTAL_BUD, "Medium Redstone Flux Crystal Bud");
            this.addBlock(PalladiumBlocks.LARGE_REDSTONE_FLUX_CRYSTAL_BUD, "Large Redstone Flux Crystal Bud");
            this.addBlock(PalladiumBlocks.REDSTONE_FLUX_CRYSTAL_CLUSTER, "Redstone Flux Crystal Cluster");
            this.addBlock(PalladiumBlocks.LEAD_BLOCK, "Block of Lead");
            this.addBlock(PalladiumBlocks.SILVER_BLOCK, "Block of Silver");
            this.addBlock(PalladiumBlocks.TITANIUM_BLOCK, "Block of Titanium");
            this.addBlock(PalladiumBlocks.VIBRANIUM_BLOCK, "Block of Vibranium");
            this.addBlock(PalladiumBlocks.RAW_LEAD_BLOCK, "Block of Raw Lead");
            this.addBlock(PalladiumBlocks.RAW_SILVER_BLOCK, "Block of Raw Silver");
            this.addBlock(PalladiumBlocks.RAW_TITANIUM_BLOCK, "Block of Raw Titanium");
            this.addBlock(PalladiumBlocks.RAW_VIBRANIUM_BLOCK, "Block of Raw Vibranium");
            this.addBlock(PalladiumBlocks.SOLAR_PANEL, "Solar Panel");
            this.addBlock(PalladiumBlocks.HEART_SHAPED_HERB, "Heart-Shaped Herb");
            this.addBlock(PalladiumBlocks.POTTED_HEART_SHAPED_HERB, "Potted Heart-Shaped Herb");

            // Items
            this.addItem(PalladiumItems.RAW_LEAD, "Raw Lead");
            this.addItem(PalladiumItems.LEAD_INGOT, "Lead Ingot");
            this.addItem(PalladiumItems.RAW_SILVER, "Raw Silver");
            this.addItem(PalladiumItems.SILVER_INGOT, "Silver Ingot");
            this.addItem(PalladiumItems.RAW_TITANIUM, "Raw Titanium");
            this.addItem(PalladiumItems.TITANIUM_INGOT, "Titanium Ingot");
            this.addItem(PalladiumItems.RAW_VIBRANIUM, "Raw Vibranium");
            this.addItem(PalladiumItems.VIBRANIUM_INGOT, "Vibranium Ingot");
            this.addItem(PalladiumItems.REDSTONE_FLUX_CRYSTAL, "Redstone Flux Crystal");
            this.addItem(PalladiumItems.HAMMER, "Hammer");
            this.addItem(PalladiumItems.REDSTONE_CIRCUIT, "Redstone Circuit");
            this.addItem(PalladiumItems.QUARTZ_CIRCUIT, "Quartz Circuit");
            this.addItem(PalladiumItems.VIBRANIUM_CIRCUIT, "Vibranium Circuit");
            this.addItem(PalladiumItems.VIBRANIUM_WEAVE_BOOTS, "Vibranium Weave Boots");

            // Abilities
            this.addAbility(Abilities.COMMAND, "Command");
            this.addAbility(Abilities.RENDER_LAYER, "Render Layer");
            this.addAbility(Abilities.ATTRIBUTE_MODIFIER, "Attribute Modifier");
            this.addAbility(Abilities.HEALING, "Healing");
            this.addAbility(Abilities.SLOWFALL, "Slowfall");
            this.addAbility(Abilities.DAMAGE_IMMUNITY, "Damage Immunity");
            this.addAbility(Abilities.INVISIBILITY, "Invisibility");

            // Creative Tab
            this.add("itemGroup.palladium.technology", "Technology");

            // Key Mappings
            this.add(PalladiumKeyMappings.CATEGORY, "Abilities");
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
            this.addBlock(PalladiumBlocks.SILVER_ORE, "Silbererz");
            this.addBlock(PalladiumBlocks.DEEPSLATE_SILVER_ORE, "Tiefenschiefer-Silbererz");
            this.addBlock(PalladiumBlocks.TITANIUM_ORE, "Titaniumerz");
            this.addBlock(PalladiumBlocks.VIBRANIUM_ORE, "Vibraniumerz");
            this.addBlock(PalladiumBlocks.REDSTONE_FLUX_CRYSTAL_GEODE, "Redstone-Flux-Kristallgeode");
            this.addBlock(PalladiumBlocks.DEEPSLATE_REDSTONE_FLUX_CRYSTAL_GEODE, "Tiefenschiefer-Redstone-Flux-Kristallgeode");
            this.addBlock(PalladiumBlocks.SMALL_REDSTONE_FLUX_CRYSTAL_BUD, "Kleine Redstone-Flux-Kristallknospe");
            this.addBlock(PalladiumBlocks.MEDIUM_REDSTONE_FLUX_CRYSTAL_BUD, "Mittlere Redstone-Flux-Kristallknospe");
            this.addBlock(PalladiumBlocks.LARGE_REDSTONE_FLUX_CRYSTAL_BUD, "Gro\u00DFe Redstone-Flux-Kristallknospe");
            this.addBlock(PalladiumBlocks.REDSTONE_FLUX_CRYSTAL_CLUSTER, "Redstone-Flux-Kristallhaufen");
            this.addBlock(PalladiumBlocks.LEAD_BLOCK, "Bleiblock");
            this.addBlock(PalladiumBlocks.SILVER_BLOCK, "Silberblock");
            this.addBlock(PalladiumBlocks.TITANIUM_BLOCK, "Titaniumblock");
            this.addBlock(PalladiumBlocks.VIBRANIUM_BLOCK, "Vibraniumblock");
            this.addBlock(PalladiumBlocks.RAW_LEAD_BLOCK, "Rohbleiblock");
            this.addBlock(PalladiumBlocks.RAW_SILVER_BLOCK, "Rohsilberblock");
            this.addBlock(PalladiumBlocks.RAW_TITANIUM_BLOCK, "Rohtitaniumblock");
            this.addBlock(PalladiumBlocks.RAW_VIBRANIUM_BLOCK, "Rohvibraniumblock");
            this.addBlock(PalladiumBlocks.SOLAR_PANEL, "Solarpaneel");
            this.addBlock(PalladiumBlocks.HEART_SHAPED_HERB, "Herzf\u00F6rmiges Kraut");
            this.addBlock(PalladiumBlocks.POTTED_HEART_SHAPED_HERB, "Eingetopftes Herzf\u00F6rmige Kraut");

            // Items
            this.addItem(PalladiumItems.RAW_LEAD, "Rohblei");
            this.addItem(PalladiumItems.LEAD_INGOT, "Bleibarren");
            this.addItem(PalladiumItems.RAW_SILVER, "Rohsilber");
            this.addItem(PalladiumItems.SILVER_INGOT, "Silberbarren");
            this.addItem(PalladiumItems.RAW_TITANIUM, "Rohtitanium");
            this.addItem(PalladiumItems.TITANIUM_INGOT, "Titaniumbarren");
            this.addItem(PalladiumItems.RAW_VIBRANIUM, "Rohvibranium");
            this.addItem(PalladiumItems.VIBRANIUM_INGOT, "Vibraniumbarren");
            this.addItem(PalladiumItems.REDSTONE_FLUX_CRYSTAL, "Redstone-Flux-Kristall");
            this.addItem(PalladiumItems.HAMMER, "Hammer");
            this.addItem(PalladiumItems.REDSTONE_CIRCUIT, "Redstoneschaltkreis");
            this.addItem(PalladiumItems.QUARTZ_CIRCUIT, "Quarzschaltkreis");
            this.addItem(PalladiumItems.VIBRANIUM_CIRCUIT, "Vibraniumschaltkreis");
            this.addItem(PalladiumItems.VIBRANIUM_WEAVE_BOOTS, "Vibraniumgewebeschuhe");

            // Abilities
            this.addAbility(Abilities.COMMAND, "Befehl");
            this.addAbility(Abilities.RENDER_LAYER, "Render Layer");
            this.addAbility(Abilities.ATTRIBUTE_MODIFIER, "Attributmodifikator");
            this.addAbility(Abilities.HEALING, "Heilung");
            this.addAbility(Abilities.SLOWFALL, "Langsamer Fall");
            this.addAbility(Abilities.DAMAGE_IMMUNITY, "Schadensimmunit\u00E4t");
            this.addAbility(Abilities.INVISIBILITY, "Unsichtbarkeit");

            // Creative Tab
            this.add("itemGroup.palladium.technology", "Technologie");

            // Key Mappings
            this.add(PalladiumKeyMappings.CATEGORY, "F\u00E4higkeiten");
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
            this.addBlock(PalladiumBlocks.SILVER_ORE, "Silberärds");
            this.addBlock(PalladiumBlocks.DEEPSLATE_SILVER_ORE, "Diefnschieforr-Silberärds");
            this.addBlock(PalladiumBlocks.TITANIUM_ORE, "Titaniumärds");
            this.addBlock(PalladiumBlocks.VIBRANIUM_ORE, "Vibraniumärds");
            this.addBlock(PalladiumBlocks.REDSTONE_FLUX_CRYSTAL_GEODE, "Redstone-Flux-Kristallgeode");
            this.addBlock(PalladiumBlocks.DEEPSLATE_REDSTONE_FLUX_CRYSTAL_GEODE, "Diefnschieforr-Redstone-Flux-Kristallgeode");
            this.addBlock(PalladiumBlocks.SMALL_REDSTONE_FLUX_CRYSTAL_BUD, "Gleene Redstone-Flux-Kristallgnosbe");
            this.addBlock(PalladiumBlocks.MEDIUM_REDSTONE_FLUX_CRYSTAL_BUD, "Mittlorre Redstone-Flux-Kristallgnosbe");
            this.addBlock(PalladiumBlocks.LARGE_REDSTONE_FLUX_CRYSTAL_BUD, "Gro\u00DFe Redstone-Flux-Kristallgnosbe");
            this.addBlock(PalladiumBlocks.REDSTONE_FLUX_CRYSTAL_CLUSTER, "Redstone-Flux-Kristallhaufn");
            this.addBlock(PalladiumBlocks.LEAD_BLOCK, "Bleblogg");
            this.addBlock(PalladiumBlocks.SILVER_BLOCK, "Silberblogg");
            this.addBlock(PalladiumBlocks.TITANIUM_BLOCK, "Titaniumblogg");
            this.addBlock(PalladiumBlocks.VIBRANIUM_BLOCK, "Vibraniumblogg");
            this.addBlock(PalladiumBlocks.RAW_LEAD_BLOCK, "Rohbleblogg");
            this.addBlock(PalladiumBlocks.RAW_SILVER_BLOCK, "Rohsilberblogg");
            this.addBlock(PalladiumBlocks.RAW_TITANIUM_BLOCK, "Rohtitaniumblogg");
            this.addBlock(PalladiumBlocks.RAW_VIBRANIUM_BLOCK, "Rohvibraniumblogg");
            this.addBlock(PalladiumBlocks.SOLAR_PANEL, "Solarpaneel");
            this.addBlock(PalladiumBlocks.HEART_SHAPED_HERB, "Herzf\u00F6rmijes Graud");
            this.addBlock(PalladiumBlocks.POTTED_HEART_SHAPED_HERB, "Eingedobbdes Herzf\u00F6rmije Graud");

            // Items
            this.addItem(PalladiumItems.RAW_LEAD, "Rohble");
            this.addItem(PalladiumItems.LEAD_INGOT, "Blebarrn");
            this.addItem(PalladiumItems.RAW_SILVER, "Rohsilber");
            this.addItem(PalladiumItems.SILVER_INGOT, "Silberbarrn");
            this.addItem(PalladiumItems.RAW_TITANIUM, "Rohtitanium");
            this.addItem(PalladiumItems.TITANIUM_INGOT, "Titaniumbarrn");
            this.addItem(PalladiumItems.RAW_VIBRANIUM, "Rohvibranium");
            this.addItem(PalladiumItems.VIBRANIUM_INGOT, "Vibraniumbarrn");
            this.addItem(PalladiumItems.REDSTONE_FLUX_CRYSTAL, "Redstone-Flux-Kristall");
            this.addItem(PalladiumItems.HAMMER, "Hammer");
            this.addItem(PalladiumItems.REDSTONE_CIRCUIT, "Redstoneschaldkres");
            this.addItem(PalladiumItems.QUARTZ_CIRCUIT, "Quarzschaldkres");
            this.addItem(PalladiumItems.VIBRANIUM_CIRCUIT, "Vibraniumschaldkres");
            this.addItem(PalladiumItems.VIBRANIUM_WEAVE_BOOTS, "Vibraniumjewebelaadschn");

            // Abilities
            this.addAbility(Abilities.COMMAND, "Befehl");
            this.addAbility(Abilities.RENDER_LAYER, "Render Layer");
            this.addAbility(Abilities.ATTRIBUTE_MODIFIER, "\u00C4ttrib\u00FCtmodifikator");
            this.addAbility(Abilities.HEALING, "Helung");
            this.addAbility(Abilities.SLOWFALL, "Langsamer Fall");
            this.addAbility(Abilities.DAMAGE_IMMUNITY, "Schadensimmunit\u00E4t");
            this.addAbility(Abilities.INVISIBILITY, "Unsischtbarkeet");

            // Creative Tab
            this.add("itemGroup.palladium.technology", "Technolojie");

            // Key Mappings
            this.add(PalladiumKeyMappings.CATEGORY, "F\u00E4hischgehden");
            for (int i = 1; i <= PalladiumKeyMappings.ABILITY_KEYS.length; i++) {
                this.add("key.palladium.ability_" + i, "F\u00E4hischgehd " + i);
            }

            // Commands
            this.add("commands.superpower.error.powerNotFound", "Et wurd' kene Kraft mit'm Namen '%1$s' jefunden");
            this.add("commands.superpower.error.noLivingEntity", "Objekt ist nicht lebend");
            this.add("commands.superpower.success.entity.single", "%s hat de Superkraft %s erhalten");
            this.add("commands.superpower.success.entity.multiple", "%s Lebewesen haben de Superkraft %s erhalten");
            this.add("commands.superpower.remove.success.entity.single", "%s's Superkraft wurd' entfernt");
            this.add("commands.superpower.remove.success.entity.multiple", "De Superkraft von %s Lebewesen wurd' entfernt");
        }
    }

    public void addAbility(Supplier<? extends Ability> key, String name) {
        add(key.get(), name);
    }

    public void add(Ability key, String name) {
        ResourceLocation id = Ability.REGISTRY.getId(key);
        add("ability." + Objects.requireNonNull(id).getNamespace() + "." + id.getPath(), name);
    }

    @Override
    public String getName() {
        return "Palladium " + super.getName();
    }
}
