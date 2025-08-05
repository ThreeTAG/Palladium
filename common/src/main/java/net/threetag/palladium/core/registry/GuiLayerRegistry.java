package net.threetag.palladium.core.registry;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class GuiLayerRegistry {

    @ExpectPlatform
    public static void register(ResourceLocation id, GuiLayer layer) {
        throw new AssertionError();
    }

    public interface GuiLayer {

        void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker);

    }

}
