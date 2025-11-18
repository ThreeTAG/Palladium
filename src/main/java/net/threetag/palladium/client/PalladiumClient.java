package net.threetag.palladium.client;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.*;
import net.minecraft.data.worldgen.biome.BiomeData;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.gametest.framework.GameTestEnvironments;
import net.minecraft.gametest.framework.GameTestInstances;
import net.minecraft.network.chat.ChatType;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.dialog.Dialogs;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.CatVariants;
import net.minecraft.world.entity.animal.ChickenVariants;
import net.minecraft.world.entity.animal.CowVariants;
import net.minecraft.world.entity.animal.PigVariants;
import net.minecraft.world.entity.animal.frog.FrogVariants;
import net.minecraft.world.entity.animal.wolf.WolfSoundVariants;
import net.minecraft.world.entity.animal.wolf.WolfVariants;
import net.minecraft.world.entity.decoration.PaintingVariants;
import net.minecraft.world.entity.player.PlayerModelType;
import net.minecraft.world.item.Instruments;
import net.minecraft.world.item.JukeboxSongs;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.providers.VanillaEnchantmentProviders;
import net.minecraft.world.item.equipment.trim.TrimMaterials;
import net.minecraft.world.item.equipment.trim.TrimPatterns;
import net.minecraft.world.level.biome.MultiNoiseBiomeSourceParameterLists;
import net.minecraft.world.level.block.entity.BannerPatterns;
import net.minecraft.world.level.block.entity.trialspawner.TrialSpawnerConfigs;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseRouterData;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorPresets;
import net.minecraft.world.level.levelgen.presets.WorldPresets;
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
import net.threetag.palladium.client.renderer.entity.layer.pack.PackRenderLayerManager;
import net.threetag.palladium.client.renderer.entity.layer.pack.PackRenderLayerRenderer;
import net.threetag.palladium.client.renderer.entity.layer.pack.PackRenderLayerSerializer;
import net.threetag.palladium.client.renderer.entity.layer.pack.PackRenderLayerSerializers;
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

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Mod(value = Palladium.MOD_ID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = Palladium.MOD_ID, value = Dist.CLIENT)
public class PalladiumClient {

    private static final RegistrySetBuilder BUILDER = (new RegistrySetBuilder()).add(PalladiumRegistryKeys.POWER, bootstrapContext -> {}).add(Registries.DIMENSION_TYPE, DimensionTypes::bootstrap).add(Registries.CONFIGURED_CARVER, Carvers::bootstrap).add(Registries.CONFIGURED_FEATURE, FeatureUtils::bootstrap).add(Registries.PLACED_FEATURE, PlacementUtils::bootstrap).add(Registries.STRUCTURE, Structures::bootstrap).add(Registries.STRUCTURE_SET, StructureSets::bootstrap).add(Registries.PROCESSOR_LIST, ProcessorLists::bootstrap).add(Registries.TEMPLATE_POOL, Pools::bootstrap).add(Registries.BIOME, BiomeData::bootstrap).add(Registries.MULTI_NOISE_BIOME_SOURCE_PARAMETER_LIST, MultiNoiseBiomeSourceParameterLists::bootstrap).add(Registries.NOISE, NoiseData::bootstrap).add(Registries.DENSITY_FUNCTION, NoiseRouterData::bootstrap).add(Registries.NOISE_SETTINGS, NoiseGeneratorSettings::bootstrap).add(Registries.WORLD_PRESET, WorldPresets::bootstrap).add(Registries.FLAT_LEVEL_GENERATOR_PRESET, FlatLevelGeneratorPresets::bootstrap).add(Registries.CHAT_TYPE, ChatType::bootstrap).add(Registries.TRIM_PATTERN, TrimPatterns::bootstrap).add(Registries.TRIM_MATERIAL, TrimMaterials::bootstrap).add(Registries.TRIAL_SPAWNER_CONFIG, TrialSpawnerConfigs::bootstrap).add(Registries.WOLF_VARIANT, WolfVariants::bootstrap).add(Registries.WOLF_SOUND_VARIANT, WolfSoundVariants::bootstrap).add(Registries.PAINTING_VARIANT, PaintingVariants::bootstrap).add(Registries.DAMAGE_TYPE, DamageTypes::bootstrap).add(Registries.BANNER_PATTERN, BannerPatterns::bootstrap).add(Registries.ENCHANTMENT, Enchantments::bootstrap).add(Registries.ENCHANTMENT_PROVIDER, VanillaEnchantmentProviders::bootstrap).add(Registries.JUKEBOX_SONG, JukeboxSongs::bootstrap).add(Registries.INSTRUMENT, Instruments::bootstrap).add(Registries.PIG_VARIANT, PigVariants::bootstrap).add(Registries.COW_VARIANT, CowVariants::bootstrap).add(Registries.CHICKEN_VARIANT, ChickenVariants::bootstrap).add(Registries.TEST_ENVIRONMENT, GameTestEnvironments::bootstrap).add(Registries.TEST_INSTANCE, GameTestInstances::bootstrap).add(Registries.FROG_VARIANT, FrogVariants::bootstrap).add(Registries.CAT_VARIANT, CatVariants::bootstrap).add(Registries.DIALOG, Dialogs::bootstrap);
    public static final List<? extends ResourceKey<? extends Registry<?>>> DATAPACK_REGISTRY_KEYS = BUILDER.getEntryKeys();

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
        var completableFuture = CompletableFuture.supplyAsync(PalladiumClient::createLookup, Util.backgroundExecutor());

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
        e.createProvider((output, lookupProvider) -> new PalladiumPowerTagProvider(output, completableFuture));
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

    public static HolderLookup.Provider createLookup() {
        RegistryAccess.Frozen registryaccess$frozen = RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);
        return BUILDER.build(registryaccess$frozen);
    }

}
