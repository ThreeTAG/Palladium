package net.threetag.palladium;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.threetag.palladium.accessory.Accessory;
import net.threetag.palladium.addonpack.log.AddonPackLog;
import net.threetag.palladium.block.IAddonBlock;
import net.threetag.palladium.block.PalladiumBlocks;
import net.threetag.palladium.client.PalladiumKeyMappings;
import net.threetag.palladium.client.model.ArmorModelManager;
import net.threetag.palladium.client.model.SuitStandBasePlateModel;
import net.threetag.palladium.client.model.SuitStandModel;
import net.threetag.palladium.client.model.animation.AimAnimation;
import net.threetag.palladium.client.model.animation.FlightAnimation;
import net.threetag.palladium.client.model.animation.PalladiumAnimationRegistry;
import net.threetag.palladium.client.renderer.entity.CustomProjectileRenderer;
import net.threetag.palladium.client.renderer.entity.EffectEntityRenderer;
import net.threetag.palladium.client.renderer.entity.SuitStandRenderer;
import net.threetag.palladium.client.renderer.renderlayer.AbilityEffectsRenderLayer;
import net.threetag.palladium.client.renderer.renderlayer.AccessoryRenderLayer;
import net.threetag.palladium.client.renderer.renderlayer.PackRenderLayerManager;
import net.threetag.palladium.client.renderer.renderlayer.PackRenderLayerRenderer;
import net.threetag.palladium.client.screen.AbilityBarRenderer;
import net.threetag.palladium.client.screen.AccessoryScreen;
import net.threetag.palladium.client.screen.AddonPackLogScreen;
import net.threetag.palladium.client.screen.components.IconButton;
import net.threetag.palladium.client.screen.power.PowersScreen;
import net.threetag.palladium.energy.EnergyHelper;
import net.threetag.palladium.entity.PalladiumEntityTypes;
import net.threetag.palladium.event.PalladiumClientEvents;
import net.threetag.palladium.item.EnergyItem;
import net.threetag.palladium.item.PalladiumItems;
import net.threetag.palladium.power.ability.GuiOverlayAbility;
import net.threetag.palladium.util.SupporterHandler;
import net.threetag.palladium.util.icon.IIcon;
import net.threetag.palladium.util.icon.TexturedIcon;
import net.threetag.palladiumcore.event.LifecycleEvents;
import net.threetag.palladiumcore.event.ScreenEvents;
import net.threetag.palladiumcore.registry.ReloadListenerRegistry;
import net.threetag.palladiumcore.registry.client.*;

public class PalladiumClient {

    public static final IIcon ICON = new TexturedIcon(Palladium.id("textures/icon/palladium.png"));

    @SuppressWarnings("unchecked")
    public static void init() {
        colorHandlers();
        PalladiumKeyMappings.init();
        PowersScreen.register();
        AccessoryScreen.addButton();
        SupporterHandler.clientInit();
        setupDevLogButton();

        // During Setup
        LifecycleEvents.SETUP.register(() -> {
            blockRenderTypes();

            for (Item item : Registry.ITEM) {
                if (item instanceof EnergyItem) {
                    ItemPropertyRegistry.register(item, Palladium.id("energy"), (itemStack, clientLevel, livingEntity, i) -> {
                        var storage = EnergyHelper.getFromItemStack(itemStack);
                        return storage.map(energyStorage -> Math.round(13F * energyStorage.getEnergyAmount() / (float) energyStorage.getEnergyCapacity())).orElse(0);
                    });
                    ItemPropertyRegistry.register(item, Palladium.id("charged"), (itemStack, clientLevel, livingEntity, i) -> {
                        return itemStack.getOrCreateTag().getInt("energy") > 0 ? 1F : 0F;
                    });
                }
            }
        });

        // Entity Renderers
        EntityRendererRegistry.register(PalladiumEntityTypes.EFFECT, EffectEntityRenderer::new);
        EntityRendererRegistry.register(PalladiumEntityTypes.CUSTOM_PROJECTILE, CustomProjectileRenderer::new);
        EntityRendererRegistry.register(PalladiumEntityTypes.SUIT_STAND, SuitStandRenderer::new);
        EntityRendererRegistry.registerModelLayer(SuitStandModel.MODEL_LAYER_LOCATION, SuitStandModel::createBodyLayer);
        EntityRendererRegistry.registerModelLayer(SuitStandBasePlateModel.MODEL_LAYER_LOCATION, SuitStandBasePlateModel::createLayer);

        // Entity Render Layers
        EntityRendererRegistry.addRenderLayer((e) -> true, renderLayerParent -> new PackRenderLayerRenderer((RenderLayerParent<LivingEntity, EntityModel<LivingEntity>>) renderLayerParent));
        EntityRendererRegistry.addRenderLayer((e) -> true, renderLayerParent -> new AbilityEffectsRenderLayer((RenderLayerParent<LivingEntity, EntityModel<LivingEntity>>) renderLayerParent));
        EntityRendererRegistry.addRenderLayerToPlayer(renderLayerParent -> new AccessoryRenderLayer((RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>>) renderLayerParent));

        // Animations
        PalladiumClientEvents.REGISTER_ANIMATIONS.register(registry -> {
            registry.accept(Palladium.id("flight"), new FlightAnimation());
            registry.accept(Palladium.id("aim"), new AimAnimation(100));
//            registry.accept(Palladium.id("test"), new TestAnimation());
        });

        // Overlay Renderer
        OverlayRegistry.registerOverlay("palladium/ability_bar", new AbilityBarRenderer());
        OverlayRegistry.registerOverlay("palladium/gui_overlay_abilities", new GuiOverlayAbility.Renderer());

        // Reload Listeners
        ReloadListenerRegistry.register(PackType.CLIENT_RESOURCES, Palladium.id("pack_render_layers"), new PackRenderLayerManager());
        ReloadListenerRegistry.register(PackType.CLIENT_RESOURCES, Palladium.id("armor_models"), new ArmorModelManager());
        ReloadListenerRegistry.register(PackType.CLIENT_RESOURCES, Palladium.id("accessory_renderers"), new Accessory.ReloadManager());
        ReloadListenerRegistry.register(PackType.CLIENT_RESOURCES, Palladium.id("humanoid_animations"), PalladiumAnimationRegistry.INSTANCE);
    }

    public static void blockRenderTypes() {
        RenderTypeRegistry.registerBlock(RenderType.cutout(),
                PalladiumBlocks.HEART_SHAPED_HERB.get(),
                PalladiumBlocks.POTTED_HEART_SHAPED_HERB.get(),
                PalladiumBlocks.SMALL_REDSTONE_FLUX_CRYSTAL_BUD.get(),
                PalladiumBlocks.MEDIUM_REDSTONE_FLUX_CRYSTAL_BUD.get(),
                PalladiumBlocks.LARGE_REDSTONE_FLUX_CRYSTAL_BUD.get(),
                PalladiumBlocks.REDSTONE_FLUX_CRYSTAL_CLUSTER.get());

        for (Block block : Registry.BLOCK) {
            if (block instanceof IAddonBlock addonBlock) {
                var type = addonBlock.getRenderType();

                if (type != null) {
                    if (type.equalsIgnoreCase("solid")) {
                        RenderTypeRegistry.registerBlock(RenderType.solid(), block);
                    } else if (type.equalsIgnoreCase("cutout_mipped")) {
                        RenderTypeRegistry.registerBlock(RenderType.cutoutMipped(), block);
                    } else if (type.equalsIgnoreCase("cutout")) {
                        RenderTypeRegistry.registerBlock(RenderType.cutout(), block);
                    } else if (type.equalsIgnoreCase("translucent")) {
                        RenderTypeRegistry.registerBlock(RenderType.translucent(), block);
                    }
                }
            }
        }
    }

    public static void colorHandlers() {
        ColorHandlerRegistry.registerItemColors((itemStack, i) -> i > 0 ? -1 : ((DyeableLeatherItem) itemStack.getItem()).getColor(itemStack), PalladiumItems.VIBRANIUM_WEAVE_BOOTS);
    }

    public static void setupDevLogButton() {
        ScreenEvents.INIT_POST.register((screen) -> {
            if (PalladiumConfig.Client.ADDON_PACK_DEV_MODE.get() && (screen instanceof TitleScreen || screen instanceof PauseScreen)) {
                screen.addRenderableWidget(new IconButton(screen.width - 30, 10, ICON, (p_213079_1_) -> {
                    Minecraft.getInstance().setScreen(new AddonPackLogScreen(AddonPackLog.getEntries(), screen));
                }, (button, poseStack, i, j) -> {
                    screen.renderTooltip(poseStack, Component.translatable("gui.palladium.addon_pack_log"), i, j);
                }));
            }
        });
    }

}
