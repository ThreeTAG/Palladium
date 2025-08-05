package net.threetag.palladium.core.registry.fabric;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.fabricmc.fabric.impl.client.rendering.hud.HudElementRegistryImpl;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.core.registry.GuiLayerRegistry;

@Environment(EnvType.CLIENT)
public class GuiLayerRegistryImpl {

    @SuppressWarnings("UnstableApiUsage")
    public static void register(ResourceLocation id, GuiLayerRegistry.GuiLayer layer) {
        HudElementRegistryImpl.addLast(id, new Wrapper(layer));
    }

    private record Wrapper(GuiLayerRegistry.GuiLayer layer) implements HudElement {

        @Override
        public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
            this.layer.render(guiGraphics, deltaTracker);
        }
    }

}
