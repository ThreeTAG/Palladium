package net.threetag.threecore;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.network.PacketBuffer;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.threetag.threecore.ability.AbilityClientEventHandler;
import net.threetag.threecore.ability.AbilityHelper;
import net.threetag.threecore.ability.AbilityType;
import net.threetag.threecore.ability.condition.ConditionType;
import net.threetag.threecore.ability.superpower.SuperpowerManager;
import net.threetag.threecore.addonpacks.AddonPackManager;
import net.threetag.threecore.block.TCBlocks;
import net.threetag.threecore.capability.CapabilityAbilityContainer;
import net.threetag.threecore.capability.ThreeCoreCapabilities;
import net.threetag.threecore.client.renderer.AbilityBarRenderer;
import net.threetag.threecore.client.renderer.KarmaBarRenderer;
import net.threetag.threecore.client.renderer.entity.model.EntityModelManager;
import net.threetag.threecore.client.renderer.entity.modellayer.ModelLayerLoader;
import net.threetag.threecore.command.ArmorStandPoseCommand;
import net.threetag.threecore.command.KarmaCommand;
import net.threetag.threecore.command.SizeChangeCommand;
import net.threetag.threecore.command.SuperpowerCommand;
import net.threetag.threecore.container.TCContainerTypes;
import net.threetag.threecore.data.ThreeCoreBlockTagsProvider;
import net.threetag.threecore.data.ThreeCoreItemTagsProvider;
import net.threetag.threecore.data.ThreeCoreRecipeProvider;
import net.threetag.threecore.entity.TCEntityTypes;
import net.threetag.threecore.entity.armorstand.ArmorStandPoseManager;
import net.threetag.threecore.item.TCItems;
import net.threetag.threecore.item.recipe.TCRecipeSerializers;
import net.threetag.threecore.item.recipe.ToolIngredient;
import net.threetag.threecore.loot.function.TCLootFunctions;
import net.threetag.threecore.network.*;
import net.threetag.threecore.scripts.ScriptEventManager;
import net.threetag.threecore.scripts.accessors.ScriptAccessor;
import net.threetag.threecore.sound.TCSounds;
import net.threetag.threecore.tileentity.TCTileEntityTypes;
import net.threetag.threecore.util.RenderUtil;
import net.threetag.threecore.util.SupporterHandler;
import net.threetag.threecore.util.entityeffect.EntityEffectUpdateMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Mod(ThreeCore.MODID)
public class ThreeCore {

    public static final String MODID = "threecore";
    public static final Logger LOGGER = LogManager.getLogger();
    public static SimpleChannel NETWORK_CHANNEL;
    private static int networkId = -1;
    public static final File MOD_SUBFOLDER = new File("mods/" + MODID);
    private static boolean htmlGenerated = false;

    public ThreeCore() {
        // Basic stuff
        FMLJavaModLoadingContext.get().getModEventBus().register(this);
        MinecraftForge.EVENT_BUS.register(this);
        SupporterHandler.load();
        registerMessages();
//        SupporterHandler.enableSupporterCheck();

        // Config
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ThreeCoreCommonConfig.generateConfig());
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ThreeCoreServerConfig.generateConfig());

        // AddonPacks
        AddonPackManager.init();

        // Loot
        TCLootFunctions.register();

        // Construction Table Tabs
        TCContainerTypes.registerConstructionTableTabls();

        // Ability Container
        AbilityHelper.registerAbilityContainer(CapabilityAbilityContainer.ID, (p) -> p.getCapability(CapabilityAbilityContainer.ABILITY_CONTAINER).orElse(null));
        for (EquipmentSlotType slots : EquipmentSlotType.values())
            AbilityHelper.registerAbilityContainer(new ResourceLocation(ThreeCore.MODID, "item_" + slots.getName().toLowerCase()), (p) -> p.getItemStackFromSlot(slots).getCapability(CapabilityAbilityContainer.ABILITY_CONTAINER).orElse(null));

        // Misc
        CraftingHelper.register(ToolIngredient.ID, ToolIngredient.Serializer.INSTANCE);

        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
            // Rendering Stuff
            MinecraftForge.EVENT_BUS.addListener(RenderUtil::onRenderGlobal);
            MinecraftForge.EVENT_BUS.register(new KarmaBarRenderer());
            MinecraftForge.EVENT_BUS.register(new AbilityBarRenderer());

            // Client Setup
            MinecraftForge.EVENT_BUS.register(new AbilityClientEventHandler());

            if (Minecraft.getInstance() != null) {
                ((IReloadableResourceManager) Minecraft.getInstance().getResourceManager()).addReloadListener(new EntityModelManager());
                ((IReloadableResourceManager) Minecraft.getInstance().getResourceManager()).addReloadListener(new ModelLayerLoader());
            }
        });
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void registries(RegistryEvent.NewRegistry e) {
        TCItems.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        TCBlocks.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        TCTileEntityTypes.TILE_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
        TCContainerTypes.CONTAINER_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
        TCRecipeSerializers.RECIPE_SERIALIZERS.register(FMLJavaModLoadingContext.get().getModEventBus());
        TCEntityTypes.ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
        TCSounds.SOUND_EVENTS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    @SubscribeEvent
    public void setup(FMLCommonSetupEvent e) {
        // Capabilities
        ThreeCoreCapabilities.init();

        // Item Colors
        TCItems.loadItemColors();
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void setupClient(FMLClientSetupEvent e) {
        TCTileEntityTypes.initRenderers();
        TCEntityTypes.initRenderers();
        TCContainerTypes.initContainerScreens();
        ArmorStandPoseManager.init();
    }

    public void registerMessages() {
        // Blocks
        registerMessage(OpenConstructionTableTabMessage.class, OpenConstructionTableTabMessage::toBytes, OpenConstructionTableTabMessage::new, OpenConstructionTableTabMessage::handle);

        // Ability
        ThreeCore.registerMessage(SendPlayerAbilityContainerMessage.class, SendPlayerAbilityContainerMessage::toBytes, SendPlayerAbilityContainerMessage::new, SendPlayerAbilityContainerMessage::handle);
        ThreeCore.registerMessage(UpdateAbilityMessage.class, UpdateAbilityMessage::toBytes, UpdateAbilityMessage::new, UpdateAbilityMessage::handle);
        ThreeCore.registerMessage(AddAbilityMessage.class, AddAbilityMessage::toBytes, AddAbilityMessage::new, AddAbilityMessage::handle);
        ThreeCore.registerMessage(RemoveAbilityMessage.class, RemoveAbilityMessage::toBytes, RemoveAbilityMessage::new, RemoveAbilityMessage::handle);
        ThreeCore.registerMessage(AbilityKeyMessage.class, AbilityKeyMessage::toBytes, AbilityKeyMessage::new, AbilityKeyMessage::handle);
        ThreeCore.registerMessage(SendSuperpowerToastMessage.class, SendSuperpowerToastMessage::toBytes, SendSuperpowerToastMessage::new, SendSuperpowerToastMessage::handle);
        ThreeCore.registerMessage(BuyConditionMessage.class, BuyConditionMessage::toBytes, BuyConditionMessage::new, BuyConditionMessage::handle);
        ThreeCore.registerMessage(SetAbilityKeybindMessage.class, SetAbilityKeybindMessage::toBytes, SetAbilityKeybindMessage::new, SetAbilityKeybindMessage::handle);

        // Karma
        ThreeCore.registerMessage(SyncKarmaMessage.class, SyncKarmaMessage::toBytes, SyncKarmaMessage::new, SyncKarmaMessage::handle);
        ThreeCore.registerMessage(KarmaInfoMessage.class, KarmaInfoMessage::toBytes, KarmaInfoMessage::new, KarmaInfoMessage::handle);

        // Size Changing
        ThreeCore.registerMessage(SyncSizeMessage.class, SyncSizeMessage::toBytes, SyncSizeMessage::new, SyncSizeMessage::handle);
        ThreeCore.registerMessage(UpdateSizeData.class, UpdateSizeData::toBytes, UpdateSizeData::new, UpdateSizeData::handle);

        // ArmorStandPoses
        registerMessage(SetArmorStandPoseMessage.class, SetArmorStandPoseMessage::toBytes, SetArmorStandPoseMessage::new, SetArmorStandPoseMessage::handle);
        registerMessage(SendArmorStandCommandMessage.class, SendArmorStandCommandMessage::toBytes, SendArmorStandCommandMessage::new, SendArmorStandCommandMessage::handle);

        // ThreeData Capability
        registerMessage(UpdateThreeDataMessage.class, UpdateThreeDataMessage::toBytes, UpdateThreeDataMessage::new, UpdateThreeDataMessage::handle);
        registerMessage(SyncThreeDataMessage.class, SyncThreeDataMessage::toBytes, SyncThreeDataMessage::new, SyncThreeDataMessage::handle);

        // EntityEffect
        registerMessage(EntityEffectUpdateMessage.class, EntityEffectUpdateMessage::toBytes, EntityEffectUpdateMessage::new, EntityEffectUpdateMessage::handle);
    }

    @SubscribeEvent
    public void gatherData(GatherDataEvent e) {
        e.getGenerator().addProvider(new ThreeCoreBlockTagsProvider(e.getGenerator()));
        e.getGenerator().addProvider(new ThreeCoreItemTagsProvider(e.getGenerator()));
        e.getGenerator().addProvider(new ThreeCoreRecipeProvider(e.getGenerator()));
    }

    @SubscribeEvent
    public void serverStarting(FMLServerStartingEvent e) {
        SuperpowerCommand.register(e.getCommandDispatcher());
        KarmaCommand.register(e.getCommandDispatcher());
        SizeChangeCommand.register(e.getCommandDispatcher());
        ArmorStandPoseCommand.register(e.getCommandDispatcher());
    }

    @SubscribeEvent
    public void serverAboutToStart(FMLServerAboutToStartEvent e) {
        e.getServer().getResourceManager().addReloadListener(new SuperpowerManager());
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void initGui(GuiScreenEvent.InitGuiEvent e) {
        // abilities.html
        if (e.getGui() instanceof MainMenuScreen && !htmlGenerated) {
            DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
                AbilityType.generateHtmlFile(new File(ThreeCore.MOD_SUBFOLDER, "abilities.html"));
                ConditionType.generateHtmlFile(new File(ThreeCore.MOD_SUBFOLDER, "conditions.html"));
                ScriptAccessor.generateHtmlFile(new File(ThreeCore.MOD_SUBFOLDER, "script_accessors.html"));
                ScriptEventManager.generateHtmlFile(new File(ThreeCore.MOD_SUBFOLDER, "script_events.html"));
            });
            htmlGenerated = true;
        }
    }

    public static <MSG> int registerMessage(Class<MSG> messageType, BiConsumer<MSG, PacketBuffer> encoder, Function<PacketBuffer, MSG> decoder, BiConsumer<MSG, Supplier<NetworkEvent.Context>> messageConsumer) {
        if (NETWORK_CHANNEL == null)
            NETWORK_CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation(ThreeCore.MODID, ThreeCore.MODID), () -> "1.0", (s) -> true, (s) -> true);

        int id = networkId++;
        NETWORK_CHANNEL.registerMessage(id, messageType, encoder, decoder, messageConsumer);
        return id;
    }

}
