package net.threetag.palladium.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.threetag.palladium.Palladium;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class CreativeModeTabRegistry {

    private static final Map<ResourceLocation, CreativeModeTab> TABS = new HashMap<>();

    public static final CreativeModeTab TECHNOLOGY = addTab(new ResourceLocation(Palladium.MOD_ID, "technology"), new ItemStack(PalladiumItems.VIBRANIUM_INGOT.get()));

    public static CreativeModeTab getTab(ResourceLocation name) {
        if (name.getNamespace().equals("minecraft")) {
            if (name.getPath().equalsIgnoreCase("blocks"))
                return CreativeModeTab.TAB_BUILDING_BLOCKS;
            if (name.getPath().equalsIgnoreCase("decoration"))
                return CreativeModeTab.TAB_DECORATIONS;
            if (name.getPath().equalsIgnoreCase("redstone"))
                return CreativeModeTab.TAB_REDSTONE;
            if (name.getPath().equalsIgnoreCase("transportation"))
                return CreativeModeTab.TAB_TRANSPORTATION;
            if (name.getPath().equalsIgnoreCase("misc"))
                return CreativeModeTab.TAB_MISC;
            if (name.getPath().equalsIgnoreCase("food"))
                return CreativeModeTab.TAB_FOOD;
            if (name.getPath().equalsIgnoreCase("tools"))
                return CreativeModeTab.TAB_TOOLS;
            if (name.getPath().equalsIgnoreCase("combat"))
                return CreativeModeTab.TAB_COMBAT;
            if (name.getPath().equalsIgnoreCase("brewing"))
                return CreativeModeTab.TAB_BREWING;
        }

        return TABS.get(name);
    }

    public static CreativeModeTab addTab(ResourceLocation name, ItemStack stack) {
        return addTab(name, () -> stack);
    }

    public static CreativeModeTab addTab(ResourceLocation name, Supplier<ItemStack> stackSupplier) {
        return addTab(name, dev.architectury.registry.CreativeTabRegistry.create(name, stackSupplier));
    }

    public static CreativeModeTab addTab(ResourceLocation name, CreativeModeTab itemGroup) {
        TABS.put(name, itemGroup);
        return itemGroup;
    }

    public static CreativeModeTab getOrCreateItemGroup(ResourceLocation name, ItemStack stack) {
        return getOrCreateItemGroup(name, () -> stack);
    }

    public static CreativeModeTab getOrCreateItemGroup(ResourceLocation name, Supplier<ItemStack> stackSupplier) {
        CreativeModeTab itemGroup = getTab(name);

        if (itemGroup != null)
            return itemGroup;
        else {
            return addTab(name, stackSupplier);
        }
    }

}
