package net.threetag.palladium.item;

import dev.architectury.registry.CreativeTabRegistry;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.core.registry.DeferredRegister;
import net.threetag.palladium.core.registry.RegistryHolder;

public class PalladiumItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Palladium.MOD_ID);

    public static final RegistryHolder<Item> SUIT_STAND = ITEMS.registerItem("suit_stand", SuitStandItem::new);

    @SuppressWarnings("UnstableApiUsage")
    public static void init() {
        CreativeTabRegistry.modify(CreativeTabRegistry.defer(CreativeModeTabs.BUILDING_BLOCKS), (flags, output, canUseGameMasterBlocks) -> {
            output.acceptAfter(Items.ARMOR_STAND, SUIT_STAND.get());
        });
    }

}
