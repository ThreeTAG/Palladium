package net.threetag.palladium;

import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.platform.Platform;
import net.threetag.palladium.addonpack.AddonPackManager;
import net.threetag.palladium.block.PalladiumBlocks;
import net.threetag.palladium.command.SuperpowerCommand;
import net.threetag.palladium.documentation.DocumentationBuilder;
import net.threetag.palladium.event.PalladiumEvents;
import net.threetag.palladium.item.PalladiumItems;
import net.threetag.palladium.network.PalladiumNetwork;
import net.threetag.palladium.power.ItemPowerManager;
import net.threetag.palladium.power.PowerManager;
import net.threetag.palladium.power.ability.Abilities;
import net.threetag.palladium.power.ability.Ability;
import net.threetag.palladium.power.ability.AbilityEventHandler;
import net.threetag.palladium.power.ability.condition.ConditionSerializer;
import net.threetag.palladium.power.ability.condition.ConditionSerializers;
import net.threetag.palladium.power.holderfactory.PowerProviderFactories;
import net.threetag.palladium.util.icon.IconSerializers;
import net.threetag.palladium.world.PalladiumFeatures;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Consumer;

public class Palladium {

    public static final String MOD_ID = "palladium";
    public static final Logger LOGGER = LogManager.getLogger();

    public static void init() {
        PalladiumBlocks.BLOCKS.register();
        PalladiumItems.ITEMS.register();
        Abilities.ABILITIES.register();
        ConditionSerializers.CONDITION_SERIALIZERS.register();
        PowerProviderFactories.FACTORIES.register();
        IconSerializers.ICON_SERIALIZERS.register();
        PalladiumFeatures.FEATURES.register();

        PalladiumNetwork.init();
        PowerManager.init();
        ItemPowerManager.init();
        AbilityEventHandler.init();
        AddonPackManager.init();
        generateDocumentation();

        LifecycleEvent.SETUP.register(() -> {
            PalladiumFeatures.init();
        });

        CommandRegistrationEvent.EVENT.register((dispatcher, selection) -> {
            SuperpowerCommand.register(dispatcher);
        });

        if (Platform.isDevelopmentEnvironment()) {
            PalladiumDebug.init();
        }
    }

    public static void generateDocumentation() {
        LifecycleEvent.SETUP.register(() -> {
            Consumer<DocumentationBuilder> consumer = DocumentationBuilder::save;
            consumer.accept(Ability.documentationBuilder());
            consumer.accept(ConditionSerializer.documentationBuilder());
            PalladiumEvents.GENERATE_DOCUMENTATION.invoker().generate(consumer);
        });
    }

}
