package net.threetag.palladium.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.threetag.palladium.Palladium;
import net.threetag.palladiumcore.registry.CreativeModeTabRegistry;

import java.util.ArrayList;
import java.util.List;

public class PalladiumCreativeModeTabs {

    public static final CreativeModeTab TECHNOLOGY = CreativeModeTabRegistry.create(new ResourceLocation(Palladium.MOD_ID, "technology"), () -> new ItemStack(PalladiumItems.LEAD_CIRCUIT.get()));

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

    public static List<ResourceLocation> getTabs() {
        List<ResourceLocation> tabs = new ArrayList<>();

        for (CreativeModeTab tab : CreativeModeTab.TABS) {
            if (tab.getRecipeFolderName().contains(".")) {
                String[] s = tab.getRecipeFolderName().split("\\.", 2);
                tabs.add(new ResourceLocation(s[0], s[1]));
            } else {
                tabs.add(new ResourceLocation(tab.getRecipeFolderName()));
            }
        }

        return tabs;
    }

}
