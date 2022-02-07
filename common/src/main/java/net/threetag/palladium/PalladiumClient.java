package net.threetag.palladium;

import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import net.minecraft.client.renderer.RenderType;
import net.threetag.palladium.block.PalladiumBlocks;

public class PalladiumClient {

    public static void init() {
        blockRenderTypes();
    }

    public static void blockRenderTypes() {
        RenderTypeRegistry.register(RenderType.cutout(), PalladiumBlocks.HEART_SHAPED_HERB.get(), PalladiumBlocks.POTTED_HEART_SHAPED_HERB.get());
    }

}
