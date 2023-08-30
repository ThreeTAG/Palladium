package net.threetag.palladium.compat.condensedcreative;

import io.wispforest.condensed_creative.registry.CondensedCreativeInitializer;
import io.wispforest.condensed_creative.registry.CondensedEntryRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.item.SuitSet;

import java.util.LinkedList;
import java.util.List;

@CondensedCreativeInitializer.InitializeCondensedEntries
public class CondensedCreativeCompat implements CondensedCreativeInitializer {

    @Override
    public void onInitializeCondensedEntries(boolean refreshed) {
        for (SuitSet suitSet : SuitSet.REGISTRY) {
            var id = SuitSet.REGISTRY.getKey(suitSet);
            var icon = getIcon(suitSet);

            if (id != null && icon != null) {
                var items = items(suitSet);
                CondensedEntryRegistry.fromItemStacks(Palladium.id("suitset/" + id.getNamespace() + "/" + id.getPath()),
                                icon,
                                items
                        )
                        .setTitle(Component.translatable(suitSet.getDescriptionId()))
                        // TODO set to tab of items
                        .addToItemGroup(CreativeModeTabs.COMBAT);
            }
        }
    }

    public static Item getIcon(SuitSet suitSet) {
        if (suitSet.getHelmet() != null) {
            return suitSet.getHelmet();
        }

        if (suitSet.getChestplate() != null) {
            return suitSet.getHelmet();
        }

        if (suitSet.getMainHand() != null) {
            return suitSet.getHelmet();
        }

        if (suitSet.getOffHand() != null) {
            return suitSet.getHelmet();
        }

        if (suitSet.getLeggings() != null) {
            return suitSet.getHelmet();
        }

        if (suitSet.getBoots() != null) {
            return suitSet.getHelmet();
        }

        return null;
    }

    public static List<ItemStack> items(SuitSet suitSet) {
        List<ItemStack> items = new LinkedList<>();

        if (suitSet.getHelmet() != null) {
            items.add(suitSet.getHelmet().getDefaultInstance());
        }

        if (suitSet.getChestplate() != null) {
            items.add(suitSet.getChestplate().getDefaultInstance());
        }

        if (suitSet.getLeggings() != null) {
            items.add(suitSet.getLeggings().getDefaultInstance());
        }

        if (suitSet.getBoots() != null) {
            items.add(suitSet.getBoots().getDefaultInstance());
        }

        if (suitSet.getMainHand() != null) {
            items.add(suitSet.getMainHand().getDefaultInstance());
        }

        if (suitSet.getOffHand() != null) {
            items.add(suitSet.getOffHand().getDefaultInstance());
        }

        return items;
    }

}
