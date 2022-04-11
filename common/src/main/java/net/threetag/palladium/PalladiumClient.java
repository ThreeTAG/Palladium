package net.threetag.palladium;

import dev.architectury.registry.client.rendering.ColorHandlerRegistry;
import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DyeableLeatherItem;
import net.threetag.palladium.block.PalladiumBlocks;
import net.threetag.palladium.client.PalladiumKeyMappings;
import net.threetag.palladium.client.renderer.renderlayer.PackRenderLayerRenderer;
import net.threetag.palladium.client.renderer.renderlayer.RenderLayerRegistry;
import net.threetag.palladium.client.screen.AbilityBarRenderer;
import net.threetag.palladium.client.screen.OverlayRegistry;
import net.threetag.palladium.client.screen.power.PowersScreen;
import net.threetag.palladium.item.PalladiumItems;

public class PalladiumClient {

    public static void init() {
        blockRenderTypes();
        colorHandlers();
        OverlayRegistry.registerOverlay("Ability Bar", new AbilityBarRenderer());
        PalladiumKeyMappings.init();
        PowersScreen.register();
        RenderLayerRegistry.addToAll(renderLayerParent -> new PackRenderLayerRenderer((RenderLayerParent<LivingEntity, HumanoidModel<LivingEntity>>) renderLayerParent));
    }

    public static void blockRenderTypes() {
        RenderTypeRegistry.register(RenderType.cutout(),
                PalladiumBlocks.HEART_SHAPED_HERB.get(),
                PalladiumBlocks.POTTED_HEART_SHAPED_HERB.get(),
                PalladiumBlocks.SMALL_REDSTONE_FLUX_CRYSTAL_BUD.get(),
                PalladiumBlocks.MEDIUM_REDSTONE_FLUX_CRYSTAL_BUD.get(),
                PalladiumBlocks.LARGE_REDSTONE_FLUX_CRYSTAL_BUD.get(),
                PalladiumBlocks.REDSTONE_FLUX_CRYSTAL_CLUSTER.get());
    }

    public static void colorHandlers() {
        ColorHandlerRegistry.registerItemColors((itemStack, i) -> i > 0 ? -1 : ((DyeableLeatherItem) itemStack.getItem()).getColor(itemStack), PalladiumItems.VIBRANIUM_WEAVE_BOOTS.get());
    }

}
