package net.threetag.palladium.multiverse;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.*;

public class MultiversalItemVariants {

    private final ResourceLocation universeId;
    private final Map<ResourceLocation, List<Item>> variants;

    public MultiversalItemVariants(ResourceLocation universeId) {
        this.universeId = universeId;
        this.variants = new HashMap<>();
    }

    public void addVariant(ResourceLocation category, Item item) {
        this.variants.computeIfAbsent(category, resourceLocation -> new ArrayList<>()).add(item);
    }

    public List<ResourceLocation> getCategoriesOfItem(Item item) {
        List<ResourceLocation> categories = new ArrayList<>();
        for (ResourceLocation resourceLocation : this.variants.keySet()) {
            if (this.variants.get(resourceLocation).contains(item)) {
                categories.add(resourceLocation);
            }
        }
        return categories;
    }

    public List<Item> getVariants(ResourceLocation category) {
        return this.variants.getOrDefault(category, Collections.emptyList());
    }

}
