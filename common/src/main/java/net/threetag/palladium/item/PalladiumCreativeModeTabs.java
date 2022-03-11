package net.threetag.palladium.item;

import dev.architectury.registry.CreativeTabRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.threetag.palladium.Palladium;

public class PalladiumCreativeModeTabs {

    public static final CreativeModeTab TECHNOLOGY = CreativeTabRegistry.create(new ResourceLocation(Palladium.MOD_ID, "technology"), () -> new ItemStack(PalladiumItems.VIBRANIUM_INGOT.get()));

    public static CreativeModeTab getTab(ResourceLocation name) {
        for (CreativeModeTab tab : CreativeModeTab.TABS) {
            if (tab.getRecipeFolderName().contains(".") && tab.getRecipeFolderName().equalsIgnoreCase(String.format("%s.%s", name.getNamespace(), name.getPath()))) {
                return tab;
            } else if (tab.getRecipeFolderName().equalsIgnoreCase(name.getPath())) {
                return tab;
            }
        }

        return null;
    }

}
