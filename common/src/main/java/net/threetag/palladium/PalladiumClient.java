package net.threetag.palladium;

import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import net.minecraft.client.renderer.RenderType;
import net.threetag.palladium.block.PalladiumBlocks;
import net.threetag.palladium.client.PalladiumKeyMappings;
import net.threetag.palladium.client.screen.AbilityBarRenderer;
import net.threetag.palladium.client.screen.OverlayRegistry;

public class PalladiumClient {

    public static void init() {
        blockRenderTypes();
        OverlayRegistry.registerOverlay("Ability Bar", new AbilityBarRenderer());
        PalladiumKeyMappings.init();
    }

    public static void blockRenderTypes() {
        RenderTypeRegistry.register(RenderType.cutout(), PalladiumBlocks.HEART_SHAPED_HERB.get(), PalladiumBlocks.POTTED_HEART_SHAPED_HERB.get());
    }

}
