package net.threetag.palladium;

import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.threetag.palladium.attachment.PalladiumAttachments;
import net.threetag.palladium.block.PalladiumBlocks;
import net.threetag.palladium.command.*;
import net.threetag.palladium.compat.accessories.AccessoriesCompatImpl;
import net.threetag.palladium.compat.geckolib.GeckoLibCompat;
import net.threetag.palladium.component.PalladiumDataComponents;
import net.threetag.palladium.config.PalladiumClientConfig;
import net.threetag.palladium.config.PalladiumServerConfig;
import net.threetag.palladium.customization.CustomizationSerializers;
import net.threetag.palladium.dialog.PalladiumDialogActions;
import net.threetag.palladium.entity.PalladiumEntityTypes;
import net.threetag.palladium.entity.data.PalladiumEntityDataTypes;
import net.threetag.palladium.entity.effect.EntityEffects;
import net.threetag.palladium.entity.flight.FlightTypeSerializers;
import net.threetag.palladium.icon.IconSerializers;
import net.threetag.palladium.item.PalladiumCreativeTabs;
import net.threetag.palladium.item.PalladiumItems;
import net.threetag.palladium.item.recipe.PalladiumRecipeBookCategories;
import net.threetag.palladium.item.recipe.PalladiumRecipeSerializers;
import net.threetag.palladium.item.recipe.PalladiumRecipeTypes;
import net.threetag.palladium.logic.condition.ConditionSerializers;
import net.threetag.palladium.logic.triggers.PalladiumCriteriaTriggers;
import net.threetag.palladium.logic.value.ValueSerializers;
import net.threetag.palladium.menu.PalladiumMenuTypes;
import net.threetag.palladium.network.PalladiumNetwork;
import net.threetag.palladium.power.ability.AbilitySerializers;
import net.threetag.palladium.power.ability.enabling.EnablingHandlerSerializers;
import net.threetag.palladium.power.ability.keybind.KeyBindTypeSerializers;
import net.threetag.palladium.power.ability.unlocking.UnlockingHandlerSerializers;
import net.threetag.palladium.power.dampening.PowerDampeningSources;
import net.threetag.palladium.power.provider.PowerProviders;
import net.threetag.palladium.proxy.PalladiumProxy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Palladium.MOD_ID)
@EventBusSubscriber(modid = Palladium.MOD_ID)
public final class Palladium {

    public static final String MOD_ID = "palladium";
    public static final Logger LOGGER = LogManager.getLogger();
    public static PalladiumProxy PROXY = new PalladiumProxy();

    public Palladium(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.CLIENT, PalladiumClientConfig.SPEC);
        modContainer.registerConfig(ModConfig.Type.SERVER, PalladiumServerConfig.SPEC);

        PalladiumBlocks.BLOCKS.register(modEventBus);
        PalladiumItems.ITEMS.register(modEventBus);
        PalladiumCreativeTabs.CREATIVE_TABS.register(modEventBus);
        PalladiumRecipeTypes.RECIPE_TYPES.register(modEventBus);
        PalladiumRecipeBookCategories.RECIPE_BOOK_CATEGORIES.register(modEventBus);
        PalladiumRecipeSerializers.RECIPE_SERIALIZERS.register(modEventBus);
        PalladiumEntityDataTypes.DATA_TYPES.register(modEventBus);
        PalladiumEntityTypes.ENTITIES.register(modEventBus);
        PalladiumMenuTypes.MENU_TYPES.register(modEventBus);
        PalladiumDataComponents.DATA_COMPONENTS.register(modEventBus);
        PalladiumDialogActions.ACTIONS.register(modEventBus);
        PalladiumCriteriaTriggers.CRITERIA_TRIGGERS.register(modEventBus);
        PalladiumAttachments.ATTACHMENT_TYPES.register(modEventBus);
        EntityEffects.EFFECTS.register(modEventBus);
        KeyBindTypeSerializers.KEY_BIND_TYPES.register(modEventBus);
        UnlockingHandlerSerializers.UNLOCKING_HANDLERS.register(modEventBus);
        EnablingHandlerSerializers.ENABLING_HANDLERS.register(modEventBus);
        AbilitySerializers.ABILITIES.register(modEventBus);
        PowerProviders.PROVIDERS.register(modEventBus);
        PowerDampeningSources.DAMPENING_SOURCES.register(modEventBus);
        IconSerializers.ICON_SERIALIZERS.register(modEventBus);
        CustomizationSerializers.CUSTOMIZATION_SERIALIZERS.register(modEventBus);
        ConditionSerializers.CONDITION_SERIALIZERS.register(modEventBus);
        ValueSerializers.VALUE_SERIALIZERS.register(modEventBus);
        FlightTypeSerializers.FLIGHT_TYPE_SERIALIZERS.register(modEventBus);

        PalladiumNetwork.init();

        // Compat
        if (ModList.get().isLoaded("accessories")) {
            AccessoriesCompatImpl.init();
        }
        if (ModList.get().isLoaded("geckolib")) {
            GeckoLibCompat.init(modEventBus);
        }
    }

    @SubscribeEvent
    static void commandRegister(RegisterCommandsEvent e) {
        PalladiumCommand.register(e.getDispatcher(), e.getBuildContext());
    }

    @SubscribeEvent
    static void palladiumCommands(RegisterPalladiumCommandsEvent e) {
        SuperpowerCommand.register(e.getBuilder(), e.getBuildContext());
        RegistryDumpCommand.register(e.getBuilder(), e.getBuildContext());
        DataAttachmentCommand.register(e.getBuilder(), e.getBuildContext());
        CustomizationCommand.register(e.getBuilder(), e.getBuildContext());
        ScreenCommand.register(e.getBuilder(), e.getBuildContext());
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}
