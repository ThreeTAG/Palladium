package net.threetag.palladium.data.forge;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.item.PalladiumItems;

import java.util.function.Supplier;

public class PalladiumItemModelProvider extends ItemModelProvider {

    public PalladiumItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Palladium.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        this.defaultBlockItem(PalladiumItems.LEAD_ORE);
        this.defaultBlockItem(PalladiumItems.DEEPSLATE_LEAD_ORE);
        this.defaultBlockItem(PalladiumItems.SILVER_ORE);
        this.defaultBlockItem(PalladiumItems.DEEPSLATE_SILVER_ORE);
        this.defaultBlockItem(PalladiumItems.TITANIUM_ORE);
        this.defaultBlockItem(PalladiumItems.VIBRANIUM_ORE);

        this.defaultBlockItem(PalladiumItems.LEAD_BLOCK);
        this.defaultBlockItem(PalladiumItems.SILVER_BLOCK);
        this.defaultBlockItem(PalladiumItems.TITANIUM_BLOCK);
        this.defaultBlockItem(PalladiumItems.VIBRANIUM_BLOCK);

        this.defaultBlockItem(PalladiumItems.RAW_LEAD_BLOCK);
        this.defaultBlockItem(PalladiumItems.RAW_SILVER_BLOCK);
        this.defaultBlockItem(PalladiumItems.RAW_TITANIUM_BLOCK);
        this.defaultBlockItem(PalladiumItems.RAW_VIBRANIUM_BLOCK);

        this.singleTexture(PalladiumItems.HEART_SHAPED_HERB.getId().getPath(), new ResourceLocation("item/generated"), "layer0", new ResourceLocation(Palladium.MOD_ID, "block/heart_shaped_herb"));

        this.defaultItem(PalladiumItems.RAW_LEAD);
        this.defaultItem(PalladiumItems.LEAD_INGOT);
        this.defaultItem(PalladiumItems.RAW_SILVER);
        this.defaultItem(PalladiumItems.SILVER_INGOT);
        this.defaultItem(PalladiumItems.RAW_TITANIUM);
        this.defaultItem(PalladiumItems.TITANIUM_INGOT);
        this.defaultItem(PalladiumItems.RAW_VIBRANIUM);
        this.defaultItem(PalladiumItems.VIBRANIUM_INGOT);

        this.defaultItem(PalladiumItems.HAMMER, "item/handheld");
        this.defaultItem(PalladiumItems.REDSTONE_CIRCUIT);
        this.defaultItem(PalladiumItems.QUARTZ_CIRCUIT);
        this.defaultItem(PalladiumItems.VIBRANIUM_CIRCUIT);
    }

    public void defaultItem(Supplier<Item> item) {
        this.defaultItem(item, "item/generated");
    }

    public void defaultItem(Supplier<Item> item, String parent) {
        this.singleTexture(item.get().getRegistryName().getPath(), new ResourceLocation(parent), "layer0", new ResourceLocation(Palladium.MOD_ID, "item/" + item.get().getRegistryName().getPath()));
    }

    public void defaultBlockItem(Supplier<Item> item) {
        this.withExistingParent(item.get().getRegistryName().getPath(), new ResourceLocation(item.get().getRegistryName().getNamespace(), "block/" + item.get().getRegistryName().getPath()));
    }

    @Override
    public String getName() {
        return "Palladium " + super.getName();
    }
}
