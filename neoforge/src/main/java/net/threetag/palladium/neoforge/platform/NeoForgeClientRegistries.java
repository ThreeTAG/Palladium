package net.threetag.palladium.neoforge.platform;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.GuiLayer;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.core.registry.GuiLayerRegistry;
import net.threetag.palladium.platform.ClientRegistryService;

import java.util.HashMap;
import java.util.Map;

@EventBusSubscriber(modid = Palladium.MOD_ID, value = Dist.CLIENT)
public class NeoForgeClientRegistries implements ClientRegistryService {

    private static final Map<ResourceLocation, GuiLayerRegistry.GuiLayer> LAYERS = new HashMap<>();

    @Override
    public void registerGuiLayer(ResourceLocation id, GuiLayerRegistry.GuiLayer layer) {
        LAYERS.put(id, layer);
    }

    @SubscribeEvent
    public static void onRegisterGuiOverlays(RegisterGuiLayersEvent e) {
        LAYERS.forEach((id, layer) -> e.registerAboveAll(id, new Wrapper(layer)));
    }

    private record Wrapper(GuiLayerRegistry.GuiLayer layer) implements GuiLayer {

        @Override
        public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
            this.layer.render(guiGraphics, deltaTracker);
        }
    }

}
