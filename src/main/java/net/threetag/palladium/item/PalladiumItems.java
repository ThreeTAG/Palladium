package net.threetag.palladium.item;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.threetag.palladium.Palladium;

@EventBusSubscriber(modid = Palladium.MOD_ID)
public class PalladiumItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Palladium.MOD_ID);

    public static final DeferredItem<Item> SUIT_STAND = ITEMS.registerItem("suit_stand", SuitStandItem::new);

    @SubscribeEvent
    static void creativeTabPlacement(BuildCreativeModeTabContentsEvent e) {
        if (e.getTabKey().equals(CreativeModeTabs.REDSTONE_BLOCKS) || e.getTabKey().equals(CreativeModeTabs.FUNCTIONAL_BLOCKS)) {
            e.insertAfter(Items.ARMOR_STAND.getDefaultInstance(), SUIT_STAND.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }
    }

}
