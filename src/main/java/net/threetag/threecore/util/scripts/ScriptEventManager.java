package net.threetag.threecore.util.scripts;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.util.scripts.events.*;

import java.util.List;
import java.util.Map;

public class ScriptEventManager {

    private static Map<Class<? extends ScriptEvent>, List<IEventHandler>> subscribed = Maps.newHashMap();
    private static Map<String, Class<? extends ScriptEvent>> events = Maps.newHashMap();

    static {
        registerEvent("entityJoinWorld", EntityJoinWorldScriptEvent.class);
        registerEvent("livingJump", LivingJumpScriptEvent.class);
        registerEvent("livingAttack", LivingAttackScriptEvent.class);
        registerEvent("livingHurt", LivingHurtScriptEvent.class);
        registerEvent("livingDeath", LivingDeathScriptEvent.class);
        registerEvent("livingFall", LivingFallScriptEvent.class);
    }

    public static void reset() {
        subscribed.clear();
    }

    public static void subscribe(Class<? extends ScriptEvent> eventClass, IEventHandler eventHandler) {
        if (!subscribed.containsKey(eventClass))
            subscribed.put(eventClass, Lists.newLinkedList());

        subscribed.get(eventClass).add(eventHandler);
    }

    public static void subscribe(String eventName, IEventHandler eventHandler) {
        Class<? extends ScriptEvent> clazz = events.get(eventName);
        if (clazz != null) {
            subscribe(clazz, eventHandler);
        }
    }

    public static void registerEvent(String name, Class<? extends ScriptEvent> eventClass) {
        events.put(name, eventClass);
    }

    public static void fireEvent(ScriptEvent event) {
        if (subscribed.containsKey(event.getClass())) {
            subscribed.get(event.getClass()).forEach((inv) -> inv.onEvent(event));
        }
    }

    public static class EventManagerAccessor {

        public void on(String eventName, IEventHandler eventHandler) {
            if (eventHandler != null) {
                subscribe(eventName, eventHandler);
            }
        }

    }

    public interface IEventHandler {

        void onEvent(ScriptEvent event);

    }

    @Mod.EventBusSubscriber(modid = ThreeCore.MODID)
    public static class EventHandler {

        @SubscribeEvent
        public static void onLivingJump(LivingEvent.LivingJumpEvent e) {
            new LivingJumpScriptEvent(e).fire();
        }

    }

}
