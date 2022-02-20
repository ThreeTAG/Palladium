package net.threetag.palladium;

import dev.architectury.platform.Platform;
import net.threetag.palladium.block.PalladiumBlocks;
import net.threetag.palladium.item.PalladiumItems;
import net.threetag.palladium.network.PalladiumNetwork;
import net.threetag.palladium.power.PowerManager;
import net.threetag.palladium.power.ability.Abilities;
import net.threetag.palladium.power.ability.condition.ConditionSerializers;
import net.threetag.palladium.util.icon.IconSerializers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Palladium {

    public static final String MOD_ID = "palladium";
    public static final Logger LOGGER = LogManager.getLogger();

    public static void init() {
        PalladiumBlocks.BLOCKS.register();
        PalladiumItems.ITEMS.register();
        Abilities.ABILITIES.register();
        ConditionSerializers.CONDITION_SERIALIZERS.register();
        PowerManager.PROVIDERS.register();
        IconSerializers.ICON_SERIALIZERS.register();
        PalladiumNetwork.init();
        PowerManager.init();

        if (Platform.isDevelopmentEnvironment()) {
            PalladiumDebug.init();
        }
    }

}
