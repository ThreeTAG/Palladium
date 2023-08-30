package net.threetag.palladium.item;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public interface ArmorWithRenderer {

    void setCachedArmorRenderer(Object object);

    Object getCachedArmorRenderer();

    default ResourceLocation getArmorRendererFile() {
        return BuiltInRegistries.ITEM.getKey((Item) this);
    }

}
