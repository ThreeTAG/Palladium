package net.threetag.palladium.data.forge;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.block.PalladiumBlocks;

import static net.minecraftforge.client.model.generators.ModelProvider.BLOCK_FOLDER;

public class PalladiumBlockStateProvider extends BlockStateProvider {

    public PalladiumBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, Palladium.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        this.simpleBlock(PalladiumBlocks.LEAD_ORE.get());
        this.simpleBlock(PalladiumBlocks.DEEPSLATE_LEAD_ORE.get());
        this.simpleBlock(PalladiumBlocks.SILVER_ORE.get());
        this.simpleBlock(PalladiumBlocks.DEEPSLATE_SILVER_ORE.get());
        this.simpleBlock(PalladiumBlocks.TITANIUM_ORE.get());
        this.simpleBlock(PalladiumBlocks.VIBRANIUM_ORE.get());
        this.simpleBlock(PalladiumBlocks.LEAD_BLOCK.get());
        this.simpleBlock(PalladiumBlocks.SILVER_BLOCK.get());
        this.simpleBlock(PalladiumBlocks.TITANIUM_BLOCK.get());
        this.simpleBlock(PalladiumBlocks.VIBRANIUM_BLOCK.get());
        this.simpleBlock(PalladiumBlocks.RAW_LEAD_BLOCK.get());
        this.simpleBlock(PalladiumBlocks.RAW_SILVER_BLOCK.get());
        this.simpleBlock(PalladiumBlocks.RAW_TITANIUM_BLOCK.get());
        this.simpleBlock(PalladiumBlocks.RAW_VIBRANIUM_BLOCK.get());
        this.simpleBlock(PalladiumBlocks.SOLAR_PANEL.get(), models()
                .withExistingParent("solar_panel", BLOCK_FOLDER + "/block")
                .texture("particle", new ResourceLocation(Palladium.MOD_ID, "block/solar_panel_top"))
                .texture("top", new ResourceLocation(Palladium.MOD_ID, "block/solar_panel_top"))
                .texture("side", new ResourceLocation(Palladium.MOD_ID, "block/solar_panel_side"))
                .texture("bottom", new ResourceLocation(Palladium.MOD_ID, "block/solar_panel_bottom"))
                .element().from(0, 0, 0).to(16, 3, 16).allFaces((direction, faceBuilder) -> {
                    switch (direction) {
                        case NORTH, EAST, WEST, SOUTH -> faceBuilder.texture("#side").end();
                        case UP -> faceBuilder.texture("#top").end();
                        case DOWN -> faceBuilder.texture("#bottom").end();
                    }
                }).end()
        );
        this.simpleBlock(PalladiumBlocks.HEART_SHAPED_HERB.get(), models().cross("heart_shaped_herb", new ResourceLocation(Palladium.MOD_ID, "block/heart_shaped_herb")));
        this.simpleBlock(PalladiumBlocks.POTTED_HEART_SHAPED_HERB.get(), models().withExistingParent("potted_heart_shaped_herb", BLOCK_FOLDER + "/flower_pot_cross").texture("plant", new ResourceLocation(Palladium.MOD_ID, "block/heart_shaped_herb")));
    }

    @Override
    public String getName() {
        return "Palladium " + super.getName();
    }
}
