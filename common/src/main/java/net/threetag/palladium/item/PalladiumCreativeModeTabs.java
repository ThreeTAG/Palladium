package net.threetag.palladium.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.threetag.palladium.Palladium;
import net.threetag.palladiumcore.registry.CreativeModeTabRegistry;

import java.util.ArrayList;
import java.util.List;

public class PalladiumCreativeModeTabs {

    private static CreativeModeTab TECHNOLOGY = null;

    public static CreativeModeTab getTab(ResourceLocation name) {
        if (name.toString().equalsIgnoreCase("palladium:technology")) {
            return technologyTab();
        }

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

        if (!tabs.contains(Palladium.id("technology"))) {
            tabs.add(Palladium.id("technology"));
        }

        return tabs;
    }

    public static CreativeModeTab technologyTab() {
        if (TECHNOLOGY == null) {
            TECHNOLOGY = CreativeModeTabRegistry.create(new ResourceLocation(Palladium.MOD_ID, "technology"), () -> new ItemStack(PalladiumItems.LEAD_CIRCUIT.get()));
        }
        return TECHNOLOGY;
    }

}
