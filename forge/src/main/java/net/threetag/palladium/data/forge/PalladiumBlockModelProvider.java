package net.threetag.palladium.data.forge;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.threetag.palladium.Palladium;

import java.util.function.Supplier;

public class PalladiumBlockModelProvider extends BlockModelProvider {

    public PalladiumBlockModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Palladium.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
//        this.defaultBlock(TCBlocks.LEAD_ORE);
//        this.defaultBlock(TCBlocks.DEEPSLATE_LEAD_ORE);
//        this.defaultBlock(TCBlocks.SILVER_ORE);
//        this.defaultBlock(TCBlocks.DEEPSLATE_SILVER_ORE);
//        this.defaultBlock(TCBlocks.TITANIUM_ORE);
//        this.defaultBlock(TCBlocks.VIBRANIUM_ORE);
//
//        this.defaultBlock(TCBlocks.LEAD_BLOCK);
//        this.defaultBlock(TCBlocks.SILVER_BLOCK);
//        this.defaultBlock(TCBlocks.TITANIUM_BLOCK);
//        this.defaultBlock(TCBlocks.VIBRANIUM_BLOCK);
//
//        this.defaultBlock(TCBlocks.RAW_LEAD_BLOCK);
//        this.defaultBlock(TCBlocks.RAW_SILVER_BLOCK);
//        this.defaultBlock(TCBlocks.RAW_TITANIUM_BLOCK);
//        this.defaultBlock(TCBlocks.RAW_VIBRANIUM_BLOCK);
//
//        this.cross(TCBlocks.HEART_SHAPED_HERB.getId().getPath(), new ResourceLocation(ThreeCore.MOD_ID, "block/heart_shaped_herb"));
//        this.singleTexture(TCBlocks.POTTED_HEART_SHAPED_HERB.getId().getPath(), new ResourceLocation(BLOCK_FOLDER + "/flower_pot_cross"), "plant", new ResourceLocation(ThreeCore.MOD_ID, "block/heart_shaped_herb"));
    }

    public void defaultBlock(Supplier<Block> block) {
        this.cubeAll(block.get().getRegistryName().getPath(), new ResourceLocation(Palladium.MOD_ID, "block/" + block.get().getRegistryName().getPath()));
    }

    @Override
    public String getName() {
        return "Palladium " + super.getName();
    }
}
