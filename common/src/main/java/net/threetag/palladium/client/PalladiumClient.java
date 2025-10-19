package net.threetag.palladium.client;

import dev.architectury.event.events.client.ClientLifecycleEvent;
import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.platform.Platform;
import dev.architectury.registry.ReloadListenerRegistry;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import net.minecraft.server.packs.PackType;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.client.animation.PalladiumAnimationManager;
import net.threetag.palladium.client.beam.BeamManager;
import net.threetag.palladium.client.beam.BeamRendererSerializer;
import net.threetag.palladium.client.beam.BeamRendererSerializers;
import net.threetag.palladium.client.gui.screen.abilitybar.AbilityBar;
import net.threetag.palladium.client.gui.screen.customization.PlayerCustomizationScreen;
import net.threetag.palladium.client.gui.screen.hud.AbilityGuiLayer;
import net.threetag.palladium.client.gui.screen.power.PowersScreen;
import net.threetag.palladium.client.model.ModelLayerManager;
import net.threetag.palladium.client.model.PalladiumModelLayers;
import net.threetag.palladium.client.particleemitter.ParticleEmitterManager;
import net.threetag.palladium.client.renderer.WatcherRenderer;
import net.threetag.palladium.client.renderer.entity.EffectEntityRenderer;
import net.threetag.palladium.client.renderer.entity.SuitStandRenderer;
import net.threetag.palladium.client.renderer.entity.SwingAnchorRenderer;
import net.threetag.palladium.client.renderer.entity.effect.EntityEffectRenderer;
import net.threetag.palladium.client.renderer.entity.layer.PackRenderLayerManager;
import net.threetag.palladium.client.renderer.entity.layer.PackRenderLayerSerializer;
import net.threetag.palladium.client.renderer.entity.layer.PackRenderLayerSerializers;
import net.threetag.palladium.client.renderer.icon.IconRenderer;
import net.threetag.palladium.client.texture.DynamicTextureManager;
import net.threetag.palladium.client.texture.DynamicTextureSerializer;
import net.threetag.palladium.client.texture.DynamicTextureSerializers;
import net.threetag.palladium.client.texture.TextureReference;
import net.threetag.palladium.client.texture.transformer.TextureTransformerSerializer;
import net.threetag.palladium.client.texture.transformer.TextureTransformerSerializers;
import net.threetag.palladium.compat.geckolib.GeckoLibCompatClient;
import net.threetag.palladium.core.event.PalladiumLifecycleEvents;
import net.threetag.palladium.core.registry.GuiLayerRegistry;
import net.threetag.palladium.documentation.HTMLBuilder;
import net.threetag.palladium.entity.PalladiumEntityTypes;
import net.threetag.palladium.logic.value.ValueSerializer;
import net.threetag.palladium.proxy.PalladiumClientProxy;
import net.threetag.palladium.registry.PalladiumRegistries;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

import java.util.Arrays;

public class PalladiumClient {

    public static void init() {
        Palladium.PROXY = new PalladiumClientProxy();
        PalladiumKeyMappings.init();

        // Entity Renderers
        EntityRendererRegistry.register(PalladiumEntityTypes.SUIT_STAND, SuitStandRenderer::new);
        EntityRendererRegistry.register(PalladiumEntityTypes.EFFECT, EffectEntityRenderer::new);
        EntityRendererRegistry.register(PalladiumEntityTypes.SWING_ANCHOR, SwingAnchorRenderer::new);
        PalladiumModelLayers.init();

        // Overlay Renderer
        GuiLayerRegistry.register(Palladium.id("ability_bar"), AbilityBar.INSTANCE);
        GuiLayerRegistry.register(Palladium.id("gui_overlay_abilities"), AbilityGuiLayer.INSTANCE);
        ClientTickEvent.CLIENT_POST.register(AbilityBar.INSTANCE);

        // Screens
        PowersScreen.register();
        PlayerCustomizationScreen.register();

        // Render Layers
        ReloadListenerRegistry.register(PackType.CLIENT_RESOURCES, DynamicTextureManager.INSTANCE, DynamicTextureManager.ID);
        ReloadListenerRegistry.register(PackType.CLIENT_RESOURCES, PackRenderLayerManager.INSTANCE, PackRenderLayerManager.ID, Arrays.asList(ModelLayerManager.ID, DynamicTextureManager.ID));
        PackRenderLayerSerializers.init();

        // Misc
        ReloadListenerRegistry.register(PackType.CLIENT_RESOURCES, PalladiumAnimationManager.INSTANCE, PalladiumAnimationManager.ID);
        ReloadListenerRegistry.register(PackType.CLIENT_RESOURCES, ParticleEmitterManager.INSTANCE, Palladium.id("particle_emitters"));
        ReloadListenerRegistry.register(PackType.CLIENT_RESOURCES, BeamManager.INSTANCE, Palladium.id("energy_beams"));
        WatcherRenderer.init();
        BeamRendererSerializers.init();
        TextureTransformerSerializers.init();
        DynamicTextureSerializers.init();
        PalladiumLifecycleEvents.CLIENT_SETUP.register(() -> {
            IconRenderer.registerRenderers();
            EntityEffectRenderer.registerRenderers();
        });
        TextureReference.DYNAMIC_TEXTURE_RESOLVER = (path, context) -> {
            var dyn = DynamicTextureManager.INSTANCE.get(path);
            return dyn != null ? dyn.getTexture(context) : null;
        };

        // Documentation
        ClientLifecycleEvent.CLIENT_LEVEL_LOAD.register(clientLevel -> {
            HTMLBuilder.documentedPage(Palladium.id("path_variables"), ValueSerializer.getTypes(), "Path Variables", clientLevel.registryAccess()).save();
            HTMLBuilder.documentedPage(Palladium.id("texture_transformers"), TextureTransformerSerializer.getTypes(), "Texture Transformers", clientLevel.registryAccess()).save();
            HTMLBuilder.documentedPage(Palladium.id("dynamic_textures"), DynamicTextureSerializer.getTypes(), "Dynamic Textures", clientLevel.registryAccess()).save();
            HTMLBuilder.documentedPage(Palladium.id("render_layers"), PackRenderLayerSerializer.getTypes(), "Render Layers", clientLevel.registryAccess()).save();
            HTMLBuilder.documentedPage(Palladium.id("beams"), BeamRendererSerializer.getTypes(), "Beams", clientLevel.registryAccess()).save();
            HTMLBuilder.documentedPage(PalladiumRegistryKeys.ABILITY_SERIALIZER, PalladiumRegistries.ABILITY_SERIALIZER, "Abilities", clientLevel.registryAccess()).save();
            HTMLBuilder.documentedPage(PalladiumRegistryKeys.FLIGHT_TYPE_SERIALIZERS, PalladiumRegistries.FLIGHT_TYPE_SERIALIZERS, "Flight Types", clientLevel.registryAccess()).save();
        });

        // Compat
        if (Platform.isModLoaded("geckolib")) {
            GeckoLibCompatClient.init();
        }
    }

}
