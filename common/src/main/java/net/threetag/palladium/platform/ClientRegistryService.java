package net.threetag.palladium.platform;

import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.core.registry.GuiLayerRegistry;

public interface ClientRegistryService {

    void registerGuiLayer(ResourceLocation id, GuiLayerRegistry.GuiLayer layer);

}
