package net.threetag.palladium;

import dev.architectury.event.CompoundEventResult;
import dev.architectury.event.events.common.InteractionEvent;
import dev.architectury.registry.ReloadListenerRegistry;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.Items;
import net.threetag.palladium.block.PalladiumBlocks;
import net.threetag.palladium.item.PalladiumItems;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.PowerManager;
import net.threetag.palladium.power.PowerUtil;
import net.threetag.palladium.power.ability.Abilities;
import net.threetag.palladium.power.ability.condition.ConditionSerializers;
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

        InteractionEvent.RIGHT_CLICK_ITEM.register((player, hand) -> {
            if (player.getItemInHand(hand).getItem() == Items.STICK) {
                PowerUtil.getPowerHolder(player).setPower(new Power(new ResourceLocation(Palladium.MOD_ID, "test_power"), new TextComponent("Test")));
                player.displayClientMessage(new TextComponent("New Power!"), false);
            }
            return CompoundEventResult.pass();
        });

        ReloadListenerRegistry.register(PackType.SERVER_DATA, new PowerManager());
    }
}
