package net.threetag.palladium.compat.kubejs;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.latvian.mods.kubejs.event.EventJS;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.threetag.palladiumcore.registry.client.OverlayRegistry;

import java.util.HashMap;
import java.util.Map;

public class RegisterGuiOverlaysEventJS extends EventJS {

    public static final Map<String, OverlayRegistry.IngameOverlay> OVERLAYS = new HashMap<>();

    public RegisterGuiOverlaysEventJS() {
        OVERLAYS.clear();
    }

    public void register(String id, OverlayRegistry.IngameOverlay overlay) {
        OVERLAYS.put(id, overlay);
    }

    public static class Overlay implements OverlayRegistry.IngameOverlay {

        @Override
        public void render(Minecraft minecraft, Gui gui, GuiGraphics guiGraphics, float partialTicks, int width, int height) {
            RenderSystem.enableBlend();
            for (OverlayRegistry.IngameOverlay overlay : OVERLAYS.values()) {
                overlay.render(minecraft, gui, guiGraphics, partialTicks, width, height);
            }
            RenderSystem.disableBlend();
        }
    }

}
