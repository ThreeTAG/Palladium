package net.threetag.palladium.compat.jei.multiversalvariants;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.threetag.palladium.addonpack.log.AddonPackLog;
import net.threetag.palladium.item.MultiversalExtrapolatorItem;
import net.threetag.palladium.item.PalladiumItems;
import net.threetag.palladium.multiverse.MultiversalItemVariants;
import net.threetag.palladium.multiverse.MultiversalItemVariantsManager;
import net.threetag.palladium.multiverse.MultiverseManager;
import net.threetag.palladium.multiverse.Universe;

import java.util.ArrayList;
import java.util.List;

public class MultiversalVariantRecipe {

    private final List<Item> inputs;
    private final Component universeTitle;
    private final ItemStack extrapolator;
    private final ItemStack result;

    public MultiversalVariantRecipe(Level level, List<Item> inputs, ResourceLocation universeId, ItemStack result) {
        this.inputs = inputs;
        this.result = result;
        this.universeTitle = MultiverseManager.getInstance(level).get(universeId).getTitle();

        var stack = PalladiumItems.MULTIVERSAL_EXTRAPOLATOR.get().getDefaultInstance();
        var universe = MultiverseManager.getInstance(level).get(universeId);
        if (universe != null) {
            MultiversalExtrapolatorItem.setUniverse(stack, universe);
        }
        this.extrapolator = stack;
    }

    public List<Item> getInputs() {
        return inputs;
    }

    public ItemStack getExtrapolator() {
        return extrapolator;
    }

    public ItemStack getResult() {
        return result;
    }

    public Component getUniverseTitle() {
        return universeTitle;
    }

    public static List<MultiversalVariantRecipe> getRecipes(Level level) {
        List<MultiversalVariantRecipe> recipes = new ArrayList<>();
        var variantsManager = MultiversalItemVariantsManager.getInstance(level);
        var multiverse = MultiverseManager.getInstance(level);

        for (MultiversalItemVariants variants : MultiversalItemVariantsManager.getInstance(level).getEntries().values()) {
            for (ResourceLocation category : variants.getCategories()) {
                for (Item item : variants.getItemsOfCategory(category)) {
                    try {
                        List<Item> inputs = new ArrayList<>();
                        for (Universe universe : multiverse.getUniverses().values()) {
                            inputs.addAll(variantsManager.getVariantsOf(item, universe));
                        }

                        if (!inputs.isEmpty()) {
                            recipes.add(new MultiversalVariantRecipe(level, inputs, variantsManager.getFirstUniverseIdOfItem(item), item.getDefaultInstance()));
                        }
                    } catch (Exception e) {
                        AddonPackLog.error("Error creating JEI entry for multiversal variant %s: %s", BuiltInRegistries.ITEM.getKey(item), e.getMessage());
                    }
                }
            }
        }

        return recipes;
    }
}
