package net.threetag.palladium.forge.data;

import net.minecraft.data.DataGenerator;
import net.threetag.palladium.block.PalladiumBlocks;
import net.threetag.palladium.item.PalladiumItems;

public class PalladiumLootTableProvider extends BlockLootTableProvider {

    public PalladiumLootTableProvider(DataGenerator dataGeneratorIn) {
        super(dataGeneratorIn);
    }

    @Override
    public void addTables() {
        this.add(PalladiumBlocks.LEAD_ORE.get(), (block) -> createOreDrop(block, PalladiumItems.RAW_LEAD.get()));
        this.add(PalladiumBlocks.DEEPSLATE_LEAD_ORE.get(), (block) -> createOreDrop(block, PalladiumItems.RAW_LEAD.get()));
        this.add(PalladiumBlocks.SILVER_ORE.get(), (block) -> createOreDrop(block, PalladiumItems.RAW_SILVER.get()));
        this.add(PalladiumBlocks.DEEPSLATE_SILVER_ORE.get(), (block) -> createOreDrop(block, PalladiumItems.RAW_SILVER.get()));
        this.add(PalladiumBlocks.TITANIUM_ORE.get(), (block) -> createOreDrop(block, PalladiumItems.RAW_TITANIUM.get()));
        this.add(PalladiumBlocks.VIBRANIUM_ORE.get(), (block) -> createOreDrop(block, PalladiumItems.RAW_VIBRANIUM.get()));
        this.dropSelf(PalladiumBlocks.LEAD_BLOCK.get());
        this.dropSelf(PalladiumBlocks.SILVER_BLOCK.get());
        this.dropSelf(PalladiumBlocks.TITANIUM_BLOCK.get());
        this.dropSelf(PalladiumBlocks.VIBRANIUM_BLOCK.get());
        this.dropSelf(PalladiumBlocks.RAW_LEAD_BLOCK.get());
        this.dropSelf(PalladiumBlocks.RAW_SILVER_BLOCK.get());
        this.dropSelf(PalladiumBlocks.RAW_TITANIUM_BLOCK.get());
        this.dropSelf(PalladiumBlocks.RAW_VIBRANIUM_BLOCK.get());
        this.dropSelf(PalladiumBlocks.HEART_SHAPED_HERB.get());
        this.dropPottedContents(PalladiumBlocks.POTTED_HEART_SHAPED_HERB.get());
    }

    @Override
    public String getName() {
        return "Palladium " + super.getName();
    }
}
