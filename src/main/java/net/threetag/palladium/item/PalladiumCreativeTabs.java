package net.threetag.palladium.item;

import net.minecraft.util.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.threetag.palladium.Palladium;

import static net.threetag.palladium.item.PalladiumItems.*;

@EventBusSubscriber(modid = Palladium.MOD_ID)
public class PalladiumCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Palladium.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAIN = CREATIVE_TABS.register("main", () -> CreativeModeTab.builder()
            .title(Component.translatable(Util.makeDescriptionId("itemGroup", Palladium.id("main"))))
            .icon(PalladiumItems.SUIT_STAND::toStack)
            .displayItems(ITEMS.getEntries())
            .build()
    );

    @SubscribeEvent
    static void creativeTabPlacement(BuildCreativeModeTabContentsEvent e) {
        if (e.getTabKey().equals(CreativeModeTabs.REDSTONE_BLOCKS) || e.getTabKey().equals(CreativeModeTabs.FUNCTIONAL_BLOCKS)) {
            e.insertAfter(Items.ARMOR_STAND.getDefaultInstance(), SUIT_STAND.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }

        if (e.getTabKey().equals(CreativeModeTabs.FUNCTIONAL_BLOCKS)) {
            e.insertAfter(Items.LOOM.getDefaultInstance(), TAILORING_BENCH.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }

        if (e.getTabKey().equals(CreativeModeTabs.INGREDIENTS)) {
            e.insertAfter(Items.PINK_DYE.getDefaultInstance(), WHITE_FABRIC.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            e.insertAfter(WHITE_FABRIC.toStack(), LIGHT_GRAY_FABRIC.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            e.insertAfter(LIGHT_GRAY_FABRIC.toStack(), GRAY_FABRIC.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            e.insertAfter(GRAY_FABRIC.toStack(), BLACK_FABRIC.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            e.insertAfter(BLACK_FABRIC.toStack(), BROWN_FABRIC.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            e.insertAfter(BROWN_FABRIC.toStack(), RED_FABRIC.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            e.insertAfter(RED_FABRIC.toStack(), ORANGE_FABRIC.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            e.insertAfter(ORANGE_FABRIC.toStack(), YELLOW_FABRIC.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            e.insertAfter(YELLOW_FABRIC.toStack(), LIME_FABRIC.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            e.insertAfter(LIME_FABRIC.toStack(), GREEN_FABRIC.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            e.insertAfter(GREEN_FABRIC.toStack(), CYAN_FABRIC.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            e.insertAfter(CYAN_FABRIC.toStack(), LIGHT_BLUE_FABRIC.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            e.insertAfter(LIGHT_BLUE_FABRIC.toStack(), BLUE_FABRIC.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            e.insertAfter(BLUE_FABRIC.toStack(), PURPLE_FABRIC.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            e.insertAfter(PURPLE_FABRIC.toStack(), MAGENTA_FABRIC.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            e.insertAfter(MAGENTA_FABRIC.toStack(), PINK_FABRIC.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }
    }

}
