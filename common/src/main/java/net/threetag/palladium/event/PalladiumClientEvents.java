package net.threetag.palladium.event;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import net.minecraft.client.player.Input;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.threetag.palladium.client.model.animation.Animation;

import java.util.function.BiConsumer;

public interface PalladiumClientEvents {

    Event<MovementInputUpdate> MOVEMENT_INPUT_UPDATE = EventFactory.createLoop();
    Event<RegisterAnimations> REGISTER_ANIMATIONS = EventFactory.createLoop();

    interface MovementInputUpdate {

        void update(Player player, Input input);

    }

    interface RegisterAnimations {

        void register(BiConsumer<ResourceLocation, Animation> registry);

    }

}
