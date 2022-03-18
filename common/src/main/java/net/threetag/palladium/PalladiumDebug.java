package net.threetag.palladium;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.InteractionEvent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.threetag.palladium.energy.IBlockEntityEnergyContainer;
import net.threetag.palladium.energy.IEnergyStorage;

public class PalladiumDebug {

    public static void init() {
        InteractionEvent.RIGHT_CLICK_BLOCK.register((player, hand, pos, face) -> {
            if (!player.level.isClientSide) {
                BlockEntity blockEntity = player.level.getBlockEntity(pos);

                if (blockEntity instanceof IBlockEntityEnergyContainer energyContainer) {
                    IEnergyStorage storage = energyContainer.getEnergyStorage(face);
                    player.displayClientMessage(new TextComponent("Energy: " + storage.getEnergyAmount() + "/" + storage.getEnergyCapacity()), true);
                }
            }
            return EventResult.pass();
        });
    }

}
