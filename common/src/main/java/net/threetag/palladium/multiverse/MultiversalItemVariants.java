package net.threetag.palladium.multiverse;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.*;

public class MultiversalItemVariants {

    private final Map<ResourceLocation, List<Item>> variants = new HashMap<>();

    public Set<ResourceLocation> getCategories() {
        return this.variants.keySet();
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

    public List<Item> getItemsOfCategory(ResourceLocation category) {
        return this.variants.getOrDefault(category, new ArrayList<>());
    }

    public List<Item> getVariants(ResourceLocation category) {
        return this.variants.getOrDefault(category, Collections.emptyList());
    }

    public void toNetwork(FriendlyByteBuf buf) {
        buf.writeMap(this.variants,
                FriendlyByteBuf::writeResourceLocation,
                (friendlyByteBuf, items) ->
                        friendlyByteBuf.writeCollection(items, (friendlyByteBuf1, item) -> buf.writeResourceLocation(BuiltInRegistries.ITEM.getKey(item))));
    }

    public static MultiversalItemVariants fromNetwork(FriendlyByteBuf buf) {
        var variants = new MultiversalItemVariants();

        variants.variants.putAll(buf.readMap(FriendlyByteBuf::readResourceLocation,
                friendlyByteBuf ->
                        friendlyByteBuf.readCollection(value -> new ArrayList<>(),
                                friendlyByteBuf1 -> BuiltInRegistries.ITEM.get(friendlyByteBuf1.readResourceLocation()))));

        return variants;
    }

}
