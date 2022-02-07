package net.threetag.palladium.forge.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.block.PalladiumBlocks;
import net.threetag.palladium.item.PalladiumItems;

public abstract class PalladiumLangProvider extends LanguageProvider {

    public PalladiumLangProvider(DataGenerator gen, String locale) {
        super(gen, Palladium.MOD_ID, locale);
    }

    public static class English extends PalladiumLangProvider {

        public English(DataGenerator gen) {
            super(gen, "en_us");
        }

        @Override
        protected void addTranslations() {
            this.addBlock(PalladiumBlocks.LEAD_ORE, "Lead Ore");
            this.addBlock(PalladiumBlocks.DEEPSLATE_LEAD_ORE, "Deepslate Lead Ore");
            this.addBlock(PalladiumBlocks.SILVER_ORE, "Silver Ore");
            this.addBlock(PalladiumBlocks.DEEPSLATE_SILVER_ORE, "Deepslate Silver Ore");
            this.addBlock(PalladiumBlocks.TITANIUM_ORE, "Titanium Ore");
            this.addBlock(PalladiumBlocks.VIBRANIUM_ORE, "Vibranium Ore");
            this.addBlock(PalladiumBlocks.LEAD_BLOCK, "Block of Lead");
            this.addBlock(PalladiumBlocks.SILVER_BLOCK, "Block of Silver");
            this.addBlock(PalladiumBlocks.TITANIUM_BLOCK, "Block of Titanium");
            this.addBlock(PalladiumBlocks.VIBRANIUM_BLOCK, "Block of Vibranium");
            this.addBlock(PalladiumBlocks.RAW_LEAD_BLOCK, "Block of Raw Lead");
            this.addBlock(PalladiumBlocks.RAW_SILVER_BLOCK, "Block of Raw Silver");
            this.addBlock(PalladiumBlocks.RAW_TITANIUM_BLOCK, "Block of Raw Titanium");
            this.addBlock(PalladiumBlocks.RAW_VIBRANIUM_BLOCK, "Block of Raw Vibranium");
            this.addBlock(PalladiumBlocks.HEART_SHAPED_HERB, "Heart-Shaped Herb");
            this.addBlock(PalladiumBlocks.POTTED_HEART_SHAPED_HERB, "Potted Heart-Shaped Herb");

            this.addItem(PalladiumItems.RAW_LEAD, "Raw Lead");
            this.addItem(PalladiumItems.LEAD_INGOT, "Lead Ingot");
            this.addItem(PalladiumItems.RAW_SILVER, "Raw Silver");
            this.addItem(PalladiumItems.SILVER_INGOT, "Silver Ingot");
            this.addItem(PalladiumItems.RAW_TITANIUM, "Raw Titanium");
            this.addItem(PalladiumItems.TITANIUM_INGOT, "Titanium Ingot");
            this.addItem(PalladiumItems.RAW_VIBRANIUM, "Raw Vibranium");
            this.addItem(PalladiumItems.VIBRANIUM_INGOT, "Vibranium Ingot");
            this.addItem(PalladiumItems.HAMMER, "Hammer");
            this.addItem(PalladiumItems.REDSTONE_CIRCUIT, "Redstone Circuit");
            this.addItem(PalladiumItems.QUARTZ_CIRCUIT, "Quartz Circuit");
            this.addItem(PalladiumItems.VIBRANIUM_CIRCUIT, "Vibranium Circuit");
        }
    }

    public static class German extends PalladiumLangProvider {

        public German(DataGenerator gen) {
            super(gen, "de_de");
        }

        @Override
        protected void addTranslations() {
            this.addBlock(PalladiumBlocks.LEAD_ORE, "Bleierz");
            this.addBlock(PalladiumBlocks.DEEPSLATE_LEAD_ORE, "Tiefenschiefer-Bleierz");
            this.addBlock(PalladiumBlocks.SILVER_ORE, "Silbererz");
            this.addBlock(PalladiumBlocks.DEEPSLATE_SILVER_ORE, "Tiefenschiefer-Silbererz");
            this.addBlock(PalladiumBlocks.TITANIUM_ORE, "Titaniumerz");
            this.addBlock(PalladiumBlocks.VIBRANIUM_ORE, "Vibraniumerz");
            this.addBlock(PalladiumBlocks.LEAD_BLOCK, "Bleiblock");
            this.addBlock(PalladiumBlocks.SILVER_BLOCK, "Silberblock");
            this.addBlock(PalladiumBlocks.TITANIUM_BLOCK, "Titaniumblock");
            this.addBlock(PalladiumBlocks.VIBRANIUM_BLOCK, "Vibraniumblock");
            this.addBlock(PalladiumBlocks.RAW_LEAD_BLOCK, "Rohbleiblock");
            this.addBlock(PalladiumBlocks.RAW_SILVER_BLOCK, "Rohsilberblock");
            this.addBlock(PalladiumBlocks.RAW_TITANIUM_BLOCK, "Rohtitaniumblock");
            this.addBlock(PalladiumBlocks.RAW_VIBRANIUM_BLOCK, "Rohvibraniumblock");
            this.addBlock(PalladiumBlocks.HEART_SHAPED_HERB, "Herzf\u00F6rmiges Kraut");
            this.addBlock(PalladiumBlocks.POTTED_HEART_SHAPED_HERB, "Eingetopftes Herzf\u00F6rmige Kraut");

            this.addItem(PalladiumItems.RAW_LEAD, "Rohblei");
            this.addItem(PalladiumItems.LEAD_INGOT, "Bleibarren");
            this.addItem(PalladiumItems.RAW_SILVER, "Rohsilber");
            this.addItem(PalladiumItems.SILVER_INGOT, "Silberbarren");
            this.addItem(PalladiumItems.RAW_TITANIUM, "Rohtitanium");
            this.addItem(PalladiumItems.TITANIUM_INGOT, "Titaniumbarren");
            this.addItem(PalladiumItems.RAW_VIBRANIUM, "Rohvibranium");
            this.addItem(PalladiumItems.VIBRANIUM_INGOT, "Vibraniumbarren");
            this.addItem(PalladiumItems.HAMMER, "Hammer");
            this.addItem(PalladiumItems.REDSTONE_CIRCUIT, "Redstoneschaltkreis");
            this.addItem(PalladiumItems.QUARTZ_CIRCUIT, "Quarzschaltkreis");
            this.addItem(PalladiumItems.VIBRANIUM_CIRCUIT, "Vibraniumschaltkreis");
        }
    }

    public static class Saxon extends PalladiumLangProvider {

        public Saxon(DataGenerator gen) {
            super(gen, "sxu");
        }

        @Override
        protected void addTranslations() {
            this.addBlock(PalladiumBlocks.LEAD_ORE, "Bleärds");
            this.addBlock(PalladiumBlocks.DEEPSLATE_LEAD_ORE, "Diefnschieforr-Bleärds");
            this.addBlock(PalladiumBlocks.SILVER_ORE, "Silberärds");
            this.addBlock(PalladiumBlocks.DEEPSLATE_SILVER_ORE, "Diefnschieforr-Silberärds");
            this.addBlock(PalladiumBlocks.TITANIUM_ORE, "Titaniumärds");
            this.addBlock(PalladiumBlocks.VIBRANIUM_ORE, "Vibraniumärds");
            this.addBlock(PalladiumBlocks.LEAD_BLOCK, "Bleblogg");
            this.addBlock(PalladiumBlocks.SILVER_BLOCK, "Silberblogg");
            this.addBlock(PalladiumBlocks.TITANIUM_BLOCK, "Titaniumblogg");
            this.addBlock(PalladiumBlocks.VIBRANIUM_BLOCK, "Vibraniumblogg");
            this.addBlock(PalladiumBlocks.RAW_LEAD_BLOCK, "Rohbleblogg");
            this.addBlock(PalladiumBlocks.RAW_SILVER_BLOCK, "Rohsilberblogg");
            this.addBlock(PalladiumBlocks.RAW_TITANIUM_BLOCK, "Rohtitaniumblogg");
            this.addBlock(PalladiumBlocks.RAW_VIBRANIUM_BLOCK, "Rohvibraniumblogg");
            this.addBlock(PalladiumBlocks.HEART_SHAPED_HERB, "Herzf\u00F6rmijes Graud");
            this.addBlock(PalladiumBlocks.POTTED_HEART_SHAPED_HERB, "Eingedobbdes Herzf\u00F6rmije Graud");

            this.addItem(PalladiumItems.RAW_LEAD, "Rohble");
            this.addItem(PalladiumItems.LEAD_INGOT, "Blebarrn");
            this.addItem(PalladiumItems.RAW_SILVER, "Rohsilber");
            this.addItem(PalladiumItems.SILVER_INGOT, "Silberbarrn");
            this.addItem(PalladiumItems.RAW_TITANIUM, "Rohtitanium");
            this.addItem(PalladiumItems.TITANIUM_INGOT, "Titaniumbarrn");
            this.addItem(PalladiumItems.RAW_VIBRANIUM, "Rohvibranium");
            this.addItem(PalladiumItems.VIBRANIUM_INGOT, "Vibraniumbarrn");
            this.addItem(PalladiumItems.HAMMER, "Hammer");
            this.addItem(PalladiumItems.REDSTONE_CIRCUIT, "Redstoneschaldkres");
            this.addItem(PalladiumItems.QUARTZ_CIRCUIT, "Quarzschaldkres");
            this.addItem(PalladiumItems.VIBRANIUM_CIRCUIT, "Vibraniumschaldkres");
        }
    }

    @Override
    public String getName() {
        return "Palladium " + super.getName();
    }
}
