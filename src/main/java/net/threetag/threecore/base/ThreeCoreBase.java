package net.threetag.threecore.base;

import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.base.block.TCBaseBlocks;
import net.threetag.threecore.base.entity.TCBaseEntityTypes;
import net.threetag.threecore.base.inventory.TCBaseContainerTypes;
import net.threetag.threecore.base.item.TCBaseItems;
import net.threetag.threecore.base.network.OpenConstructionTableTabMessage;
import net.threetag.threecore.base.recipe.TCBaseRecipeSerializers;


public class ThreeCoreBase {

    public ThreeCoreBase() {
        FMLJavaModLoadingContext.get().getModEventBus().register(new TCBaseBlocks());
        FMLJavaModLoadingContext.get().getModEventBus().register(new TCBaseContainerTypes());
        FMLJavaModLoadingContext.get().getModEventBus().register(new TCBaseEntityTypes());
        FMLJavaModLoadingContext.get().getModEventBus().register(new TCBaseRecipeSerializers());

        TCBaseItems.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        ThreeCore.registerMessage(OpenConstructionTableTabMessage.class, OpenConstructionTableTabMessage::toBytes, OpenConstructionTableTabMessage::new, OpenConstructionTableTabMessage::handle);
    }

    public void setup(final FMLCommonSetupEvent event) {
        TCBaseItems.loadItemColors();
    }

}
