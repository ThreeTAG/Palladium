package net.threetag.palladium.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.PlayerModelType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.client.animation.PalladiumAnimationManager;
import net.threetag.palladium.client.beam.BeamManager;
import net.threetag.palladium.client.beam.BeamRendererSerializer;
import net.threetag.palladium.client.beam.BeamRendererSerializers;
import net.threetag.palladium.client.gui.pip.GuiMultiEntityRenderState;
import net.threetag.palladium.client.gui.pip.GuiMultiEntityRenderer;
import net.threetag.palladium.client.gui.screen.abilitybar.AbilityBar;
import net.threetag.palladium.client.gui.screen.hud.AbilityGuiLayer;
import net.threetag.palladium.client.model.ModelLayerManager;
import net.threetag.palladium.client.particleemitter.ParticleEmitterManager;
import net.threetag.palladium.client.renderer.PalladiumRenderTypes;
import net.threetag.palladium.client.renderer.entity.EffectEntityRenderer;
import net.threetag.palladium.client.renderer.entity.SuitStandRenderer;
import net.threetag.palladium.client.renderer.entity.SwingAnchorRenderer;
import net.threetag.palladium.client.renderer.entity.effect.EntityEffectRenderer;
import net.threetag.palladium.client.renderer.entity.layer.PackRenderLayerManager;
import net.threetag.palladium.client.renderer.entity.layer.PackRenderLayerRenderer;
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
import net.threetag.palladium.datagen.internal.*;
import net.threetag.palladium.documentation.HTMLBuilder;
import net.threetag.palladium.entity.PalladiumEntityTypes;
import net.threetag.palladium.logic.value.ValueSerializer;
import net.threetag.palladium.proxy.PalladiumClientProxy;
import net.threetag.palladium.registry.PalladiumRegistries;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

import java.util.Objects;

@Mod(value = Palladium.MOD_ID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = Palladium.MOD_ID, value = Dist.CLIENT)
public class PalladiumClient {

    public PalladiumClient(ModContainer container) {
        Palladium.PROXY = new PalladiumClientProxy();
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);

        // Render Layers
        PackRenderLayerSerializers.init();

        // Misc
        BeamRendererSerializers.init();
        TextureTransformerSerializers.init();
        DynamicTextureSerializers.init();
        TextureReference.DYNAMIC_TEXTURE_RESOLVER = (path, context) -> {
            var dyn = DynamicTextureManager.INSTANCE.get(path);
            return dyn != null ? dyn.getTexture(context) : null;
        };

        // Compat
        if (ModList.get().isLoaded("geckolib")) {
            GeckoLibCompatClient.init();
        }
    }

    @SubscribeEvent
    static void clientSetup(FMLClientSetupEvent event) {
        IconRenderer.registerRenderers();
        EntityEffectRenderer.registerRenderers();
    }

    @SubscribeEvent
    static void entityRenderers(EntityRenderersEvent.RegisterRenderers e) {
        e.registerEntityRenderer(PalladiumEntityTypes.SUIT_STAND.get(), SuitStandRenderer::new);
        e.registerEntityRenderer(PalladiumEntityTypes.EFFECT.get(), EffectEntityRenderer::new);
        e.registerEntityRenderer(PalladiumEntityTypes.SWING_ANCHOR.get(), SwingAnchorRenderer::new);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @SubscribeEvent
    static void entityRenderLayers(EntityRenderersEvent.AddLayers e) {
        for (EntityType<?> entityType : BuiltInRegistries.ENTITY_TYPE) {
            if (e.getRenderer(entityType) instanceof LivingEntityRenderer renderer) {
                renderer.addLayer(new PackRenderLayerRenderer<>(renderer));
            }
        }
        for (PlayerModelType skin : e.getSkins()) {
            var playerRenderer = e.getPlayerRenderer(skin);
            Objects.requireNonNull(playerRenderer).addLayer(new PackRenderLayerRenderer<>(playerRenderer));
            var mannequinRenderer = e.getMannequinRenderer(skin);
            Objects.requireNonNull(mannequinRenderer).addLayer(new PackRenderLayerRenderer<>(mannequinRenderer));
        }
    }

    @SubscribeEvent
    static void guiOverlays(RegisterGuiLayersEvent e) {
        e.registerAboveAll(Palladium.id("ability_bar"), AbilityBar.INSTANCE);
        e.registerAboveAll(Palladium.id("gui_overlay_abilities"), AbilityGuiLayer.INSTANCE);
    }

    @SubscribeEvent
    static void reloadListeners(AddClientReloadListenersEvent e) {
        e.addListener(ModelLayerManager.ID, ModelLayerManager.INSTANCE);
        e.addListener(DynamicTextureManager.ID, DynamicTextureManager.INSTANCE);
        e.addListener(PackRenderLayerManager.ID, PackRenderLayerManager.INSTANCE);
        e.addListener(PalladiumAnimationManager.ID, PalladiumAnimationManager.INSTANCE);
        e.addListener(ParticleEmitterManager.ID, ParticleEmitterManager.INSTANCE);
        e.addListener(BeamManager.ID, BeamManager.INSTANCE);

        e.addDependency(ModelLayerManager.ID, DynamicTextureManager.ID);
        e.addDependency(DynamicTextureManager.ID, PackRenderLayerManager.ID);
        e.addDependency(e.getNameLookup().apply(Minecraft.getInstance().getModelManager()), ModelLayerManager.ID);
        e.addDependency(ModelLayerManager.ID, e.getNameLookup().apply(Minecraft.getInstance().getEntityRenderDispatcher().equipmentAssets));
    }

    @SubscribeEvent
    static void gatherData(GatherDataEvent.Client e) {
        // Client
        e.createProvider(PalladiumLangProvider.English::new);
        e.createProvider(PalladiumLangProvider.German::new);
        e.createProvider(PalladiumLangProvider.Saxon::new);
        e.createProvider(PalladiumItemModelProvider::new);
        e.createProvider(PalladiumRenderLayerProvider::new);
        e.createProvider(PalladiumBeamProvider::new);

        // Server
        e.createProvider(PalladiumDocumentationGenerator::new);
        e.createProvider(PalladiumBlockTagProvider::new);
        e.createProvider(PalladiumCustomizationProvider::new);
        e.createProvider(PalladiumCustomizationCategoryProvider::new);
        e.createProvider(PalladiumFlightTypeProvider::new);
        e.createProvider(PalladiumRecipeProvider.Runner::new);
    }

    @SubscribeEvent
    static void documentation(LevelEvent.Load e) {
        var clientLevel = e.getLevel();
        HTMLBuilder.documentedPage(Palladium.id("path_variables"), ValueSerializer.getTypes(), "Path Variables", clientLevel.registryAccess()).save();
        HTMLBuilder.documentedPage(Palladium.id("texture_transformers"), TextureTransformerSerializer.getTypes(), "Texture Transformers", clientLevel.registryAccess()).save();
        HTMLBuilder.documentedPage(Palladium.id("dynamic_textures"), DynamicTextureSerializer.getTypes(), "Dynamic Textures", clientLevel.registryAccess()).save();
        HTMLBuilder.documentedPage(Palladium.id("render_layers"), PackRenderLayerSerializer.getTypes(), "Render Layers", clientLevel.registryAccess()).save();
        HTMLBuilder.documentedPage(Palladium.id("beams"), BeamRendererSerializer.getTypes(), "Beams", clientLevel.registryAccess()).save();
        HTMLBuilder.documentedPage(PalladiumRegistryKeys.ABILITY_SERIALIZER, PalladiumRegistries.ABILITY_SERIALIZER, "Abilities", clientLevel.registryAccess()).save();
        HTMLBuilder.documentedPage(PalladiumRegistryKeys.FLIGHT_TYPE_SERIALIZERS, PalladiumRegistries.FLIGHT_TYPE_SERIALIZERS, "Flight Types", clientLevel.registryAccess()).save();
    }

    @SubscribeEvent
    static void registerPiP(RegisterPictureInPictureRenderersEvent e) {
        e.register(GuiMultiEntityRenderState.class, bufferSource -> new GuiMultiEntityRenderer(bufferSource, Minecraft.getInstance().getEntityRenderDispatcher()));
    }

    @SubscribeEvent
    static void registerPipelines(RegisterRenderPipelinesEvent e) {
        e.registerPipeline(PalladiumRenderTypes.Pipelines.ADD);
    }

}
