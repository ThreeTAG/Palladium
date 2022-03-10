package net.threetag.palladium;

import dev.architectury.registry.client.rendering.ColorHandlerRegistry;
import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.DyeableLeatherItem;
import net.threetag.palladium.block.PalladiumBlocks;
import net.threetag.palladium.client.PalladiumKeyMappings;
import net.threetag.palladium.client.screen.AbilityBarRenderer;
import net.threetag.palladium.client.screen.OverlayRegistry;
import net.threetag.palladium.item.PalladiumItems;

public class PalladiumClient {

    public static void init() {
        blockRenderTypes();
        colorHandlers();
        OverlayRegistry.registerOverlay("Ability Bar", new AbilityBarRenderer());
        PalladiumKeyMappings.init();
    }

    public static void blockRenderTypes() {
        RenderTypeRegistry.register(RenderType.cutout(), PalladiumBlocks.HEART_SHAPED_HERB.get(), PalladiumBlocks.POTTED_HEART_SHAPED_HERB.get());
    }

    @Environment(EnvType.CLIENT)
    public static void colorHandlers() {
        ColorHandlerRegistry.registerItemColors((itemStack, i) -> i > 0 ? -1 : ((DyeableLeatherItem) itemStack.getItem()).getColor(itemStack), PalladiumItems.VIBRANIUM_WEAVE_BOOTS.get());
    }

}
