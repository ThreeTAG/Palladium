package net.threetag.palladium.core.registry;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.platform.PlatformHelper;

public class GuiLayerRegistry {

    public static void register(ResourceLocation id, GuiLayer layer) {
        PlatformHelper.PLATFORM.getClientRegistries().registerGuiLayer(id, layer);
    }

    public interface GuiLayer {

        void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker);

    }

}
