package net.threetag.palladium.event;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import net.minecraft.client.player.Input;
import net.minecraft.world.entity.player.Player;

public interface PalladiumClientEvents {

    Event<MovementInputUpdate> MOVEMENT_INPUT_UPDATE = EventFactory.createLoop();

    interface MovementInputUpdate {

        void update(Player player, Input input);

    }

}
