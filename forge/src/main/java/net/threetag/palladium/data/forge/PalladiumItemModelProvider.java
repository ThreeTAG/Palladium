package net.threetag.palladium.data.forge;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.item.PalladiumItems;
import net.threetag.palladiumcore.registry.RegistrySupplier;

public class PalladiumItemModelProvider extends ItemModelProvider {

    public PalladiumItemModelProvider(PackOutput packOutput, ExistingFileHelper existingFileHelper) {
        super(packOutput, Palladium.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        this.defaultBlockItem(PalladiumItems.LEAD_ORE);
        this.defaultBlockItem(PalladiumItems.DEEPSLATE_LEAD_ORE);
        this.defaultBlockItem(PalladiumItems.TITANIUM_ORE);
        this.defaultBlockItem(PalladiumItems.VIBRANIUM_ORE);

        this.defaultBlockItem(PalladiumItems.REDSTONE_FLUX_CRYSTAL_GEODE);
        this.defaultBlockItem(PalladiumItems.DEEPSLATE_REDSTONE_FLUX_CRYSTAL_GEODE);
        this.defaultBlockItem2d(PalladiumItems.SMALL_REDSTONE_FLUX_CRYSTAL_BUD);
        this.defaultBlockItem2d(PalladiumItems.MEDIUM_REDSTONE_FLUX_CRYSTAL_BUD);
        this.defaultBlockItem2d(PalladiumItems.LARGE_REDSTONE_FLUX_CRYSTAL_BUD);
        this.defaultBlockItem2d(PalladiumItems.REDSTONE_FLUX_CRYSTAL_CLUSTER);

        this.defaultBlockItem(PalladiumItems.LEAD_BLOCK);
        this.defaultBlockItem(PalladiumItems.VIBRANIUM_BLOCK);

        this.defaultBlockItem(PalladiumItems.RAW_LEAD_BLOCK);
        this.defaultBlockItem(PalladiumItems.RAW_TITANIUM_BLOCK);
        this.defaultBlockItem(PalladiumItems.RAW_VIBRANIUM_BLOCK);

        this.singleTexture(PalladiumItems.HEART_SHAPED_HERB.getId().getPath(), new ResourceLocation("item/generated"), "layer0", new ResourceLocation(Palladium.MOD_ID, "block/heart_shaped_herb"));

        this.defaultItem(PalladiumItems.RAW_LEAD);
        this.defaultItem(PalladiumItems.LEAD_INGOT);
        this.defaultItem(PalladiumItems.RAW_TITANIUM);
        this.defaultItem(PalladiumItems.RAW_VIBRANIUM);
        this.defaultItem(PalladiumItems.VIBRANIUM_INGOT);
        this.defaultItem(PalladiumItems.REDSTONE_FLUX_CRYSTAL);

        this.defaultItem(PalladiumItems.SUIT_STAND);
        this.defaultItem(PalladiumItems.LEAD_CIRCUIT);
        this.defaultItem(PalladiumItems.QUARTZ_CIRCUIT);
        this.defaultItem(PalladiumItems.VIBRANIUM_CIRCUIT);
        this.fluxCapacitor(PalladiumItems.LEAD_FLUX_CAPACITOR);
        this.fluxCapacitor(PalladiumItems.QUARTZ_FLUX_CAPACITOR);
        this.fluxCapacitor(PalladiumItems.VIBRANIUM_FLUX_CAPACITOR);
        this.withExistingParent(PalladiumItems.VIBRANIUM_WEAVE_BOOTS.getId().getPath(), new ResourceLocation("item/generated")).texture("layer0", new ResourceLocation("item/leather_boots")).texture("layer1", new ResourceLocation(Palladium.MOD_ID, "item/vibranium_weave_boots_overlay"));
    }

    public void defaultItem(RegistrySupplier<Item> item) {
        this.defaultItem(item, "item/generated");
    }

    public void defaultItem(RegistrySupplier<Item> item, String parent) {
        this.singleTexture(item.getId().getPath(), new ResourceLocation(parent), "layer0", new ResourceLocation(item.getId().getNamespace(), "item/" + item.getId().getPath()));
    }

    public void defaultBlockItem(RegistrySupplier<Item> item) {
        this.withExistingParent(item.getId().getPath(), new ResourceLocation(item.getId().getNamespace(), "block/" + item.getId().getPath()));
    }

    public void defaultBlockItem2d(RegistrySupplier<Item> item) {
        this.singleTexture(item.getId().getPath(), new ResourceLocation("item/generated"), "layer0", new ResourceLocation(item.getId().getNamespace(), "block/" + item.getId().getPath()));
    }

    public void fluxCapacitor(RegistrySupplier<? extends Item> item) {
        var charged = this.singleTexture(item.getId().getPath() + "_charged", new ResourceLocation("item/generated"), "layer0", new ResourceLocation(item.getId().getNamespace(), "item/" + item.getId().getPath() + "_charged"));
        this.singleTexture(item.getId().getPath(), new ResourceLocation("item/generated"), "layer0", new ResourceLocation(item.getId().getNamespace(), "item/" + item.getId().getPath())).override().predicate(Palladium.id("charged"), 1F).model(charged).end();
    }

    @Override
    public String getName() {
        return "Palladium " + super.getName();
    }
}
