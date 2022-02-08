package net.threetag.palladium.event;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.event.events.common.TickEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public interface PalladiumEvents {

    Event<LivingUpdate> LIVING_UPDATE = EventFactory.createLoop();
    Event<StartTracking> START_TRACKING = EventFactory.createLoop();

    interface LivingUpdate extends TickEvent<LivingEntity> {

        void tick(LivingEntity entity);

    }

    interface StartTracking extends PlayerEvent {

        void startTracking(Player tracker, Entity target);

    }

}
