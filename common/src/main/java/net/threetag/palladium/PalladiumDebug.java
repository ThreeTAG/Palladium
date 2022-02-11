package net.threetag.palladium;

import dev.architectury.event.CompoundEventResult;
import dev.architectury.event.events.common.InteractionEvent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.PowerManager;
import net.threetag.palladium.power.provider.SuperpowerPowerProvider;

public class PalladiumDebug {

    public static void init() {
        InteractionEvent.RIGHT_CLICK_ITEM.register((player, hand) -> {
            if (player.getItemInHand(hand).getItem() == Items.STICK && !player.level.isClientSide) {
                if (!player.isCrouching()) {
                    SuperpowerPowerProvider.SUPERPOWER_ID.set(player, new ResourceLocation("testmod", "test_power"));
                    player.displayClientMessage(new TextComponent("New Power! " + SuperpowerPowerProvider.SUPERPOWER_ID.get(player).toString()), false);
                } else {
                    SuperpowerPowerProvider.SUPERPOWER_ID.set(player, null);
                    player.displayClientMessage(new TextComponent("Power removed"), false);
                }
            }

            if (player.getItemInHand(hand).getItem() == Items.BLAZE_ROD && ((!player.level.isClientSide && player.isCrouching()) || (player.level.isClientSide && !player.isCrouching()))) {
                for (int i = 0; i < 20; i++)
                    player.displayClientMessage(TextComponent.EMPTY, false);
                player.displayClientMessage(new TextComponent("POWERS: " + (player.level.isClientSide ? "CLIENT" : "SERVER")), false);
                for (Power power : PowerManager.getInstance(player.level).getPowers()) {
                    player.displayClientMessage(new TextComponent("  - " + power.getName().getString() + ": " + power.getAbilities().size() + " abilities"), false);
                }
                IPowerHolder holder = PowerManager.getPowerHandler(player).getPowerHolder(PowerManager.SUPERPOWER_PROVIDER.get());
                if (holder != null) {
                    player.displayClientMessage(new TextComponent("PLAYER POWER: " + (holder.getPower() != null ? holder.getPower().getName().getString() : "/")), false);
                    if (holder.getPower() != null)
                        player.displayClientMessage(new TextComponent("  -> " + holder.getPower().getAbilities().size()), false);
                }
            }

            return CompoundEventResult.pass();
        });
    }

}
