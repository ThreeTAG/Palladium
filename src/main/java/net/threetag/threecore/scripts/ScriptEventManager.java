package net.threetag.threecore.scripts;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.event.AbilityEnableChangeEvent;
import net.threetag.threecore.event.RegisterThreeDataEvent;
import net.threetag.threecore.event.SetRotationAnglesEvent;
import net.threetag.threecore.network.EmptyHandInteractMessage;
import net.threetag.threecore.scripts.events.*;
import net.threetag.threecore.util.documentation.DocumentationBuilder;

import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

import static net.threetag.threecore.util.documentation.DocumentationBuilder.*;

public class ScriptEventManager {

    private static Map<Class<? extends ScriptEvent>, List<IEventHandler>> subscribed = Maps.newHashMap();
    private static Map<String, Class<? extends ScriptEvent>> events = Maps.newHashMap();

    static {
        registerEvent("entityJoinWorld", EntityJoinWorldScriptEvent.class);
        registerEvent("entityStruckByLightning", EntityStruckByLightningScriptEvent.class);
        registerEvent("livingUpdate", LivingUpdateScriptEvent.class);
        registerEvent("livingJump", LivingJumpScriptEvent.class);
        registerEvent("livingAttack", LivingAttackScriptEvent.class);
        registerEvent("livingHurt", LivingHurtScriptEvent.class);
        registerEvent("livingDeath", LivingDeathScriptEvent.class);
        registerEvent("livingFall", LivingFallScriptEvent.class);
        registerEvent("abilityTick", AbilityTickScriptEvent.class);
        registerEvent("abilityLocked", AbilityLockedScriptEvent.class);
        registerEvent("abilityUnlocked", AbilityUnlockedScriptEvent.class);
        registerEvent("abilityEnabled", AbilityEnabledScriptEvent.class);
        registerEvent("abilityDisabled", AbilityDisabledScriptEvent.class);
        registerEvent("abilityDataUpdated", AbilityDataUpdatedScriptEvent.class);
        registerEvent("conditionDataUpdated", ConditionDataUpdatedScriptEvent.class);
        registerEvent("registerEntityThreeData", RegisterEntityThreeDataScriptEvent.class);
        registerEvent("registerAbilityThreeData", RegisterAbilityThreeDataScriptEvent.class);
        registerEvent("multiJump", MultiJumpScriptEvent.class);
        registerEvent("projectileImpact", ProjectileImpactScriptEvent.class);
        registerEvent("projectileTick", ProjectileTickScriptEvent.class);
        registerEvent("superpowerSet", SuperpowerSetScriptEvent.class);
        registerEvent("entityInteract", EntityInteractSpecificScriptEvent.class);
        registerEvent("rightClickBlock", RightClickBlockScriptEvent.class);
        registerEvent("rightClickItem", RightClickItemScriptEvent.class);
        registerEvent("leftClickBlock", LeftClickBlockScriptEvent.class);
        registerEvent("leftClickEmpty", LeftClickEmptyScriptEvent.class);
        registerEvent("rightClickEmpty", RightClickEmptyScriptEvent.class);
        registerEvent("setRotationAngles", SetRotationAnglesScriptEvent.class);
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
        public static void onLivingUpdate(LivingEvent.LivingUpdateEvent e) {
            new LivingUpdateScriptEvent(e.getEntityLiving()).fire(e);
        }

        @SubscribeEvent
        public static void onLivingJump(LivingEvent.LivingJumpEvent e) {
            new LivingJumpScriptEvent(e.getEntityLiving()).fire(e);
        }

        @SubscribeEvent
        public static void onEntityJoinWorld(EntityJoinWorldEvent e) {
            new EntityJoinWorldScriptEvent(e.getEntity()).fire(e);
        }

        @SubscribeEvent
        public static void onLivingAttack(LivingAttackEvent e) {
            new LivingAttackScriptEvent(e.getEntityLiving(), e.getSource(), e.getAmount()).fire(e);
        }

        @SubscribeEvent
        public static void onLivingDeath(LivingDeathEvent e) {
            new LivingDeathScriptEvent(e.getEntityLiving(), e.getSource()).fire(e);
        }

        @SubscribeEvent
        public static void onLivingFall(LivingFallEvent e) {
            LivingFallScriptEvent scriptEvent = new LivingFallScriptEvent(e.getEntityLiving(), e.getDistance(), e.getDamageMultiplier());
            scriptEvent.fire(e);
            e.setDistance(scriptEvent.getDistance());
            e.setDamageMultiplier(scriptEvent.getDamageMultiplier());
        }

        @SubscribeEvent
        public static void onLivingHurt(LivingHurtEvent e) {
            LivingHurtScriptEvent scriptEvent = new LivingHurtScriptEvent(e.getEntityLiving(), e.getSource(), e.getAmount());
            scriptEvent.fire(e);
            e.setAmount(scriptEvent.getAmount());
        }

        @SubscribeEvent
        public static void onEntityStruckByLightning(EntityStruckByLightningEvent e) {
            new EntityStruckByLightningScriptEvent(e.getEntity(), e.getLightning()).fire(e);
        }

        @SubscribeEvent
        public static void onRegisterThreeData(RegisterThreeDataEvent e) {
            new RegisterEntityThreeDataScriptEvent(e.getEntity(), e.getThreeData()).fire(e);
        }

        @SubscribeEvent
        public static void onProjectileImpactEvent(ProjectileImpactEvent e) {
            new ProjectileImpactScriptEvent(e.getEntity()).fire(e);
        }

        @SubscribeEvent
        public static void onEntityInteractSpecificEvent(PlayerInteractEvent.EntityInteractSpecific e) {
            new EntityInteractSpecificScriptEvent(e).fire(e);
        }

        @SubscribeEvent
        public static void onRightClickBlockEvent(PlayerInteractEvent.RightClickBlock e) {
            new RightClickBlockScriptEvent(e).fire(e);
        }

        @SubscribeEvent
        public static void onRightClickItemEvent(PlayerInteractEvent.RightClickItem e) {
            new RightClickItemScriptEvent(e).fire(e);
        }

        @SubscribeEvent
        public static void onLeftClickBlockEvent(PlayerInteractEvent.LeftClickBlock e) {
            new LeftClickBlockScriptEvent(e).fire(e);
        }

        @SubscribeEvent
        public static void onLeftClickEmptyEvent(PlayerInteractEvent.LeftClickEmpty e) {
            new LeftClickEmptyScriptEvent(e).fire(e);
            ThreeCore.NETWORK_CHANNEL.sendToServer(new EmptyHandInteractMessage(true));
        }

        @SubscribeEvent
        public static void onRightClickEmptyEvent(PlayerInteractEvent.RightClickEmpty e) {
            new RightClickEmptyScriptEvent(e).fire(e);
            ThreeCore.NETWORK_CHANNEL.sendToServer(new EmptyHandInteractMessage(false));
        }

        @OnlyIn(Dist.CLIENT)
        @SubscribeEvent
        public static void onSetRotationAngles(SetRotationAnglesEvent e) {
            new SetRotationAnglesScriptEvent(e).fire(e);
        }

        @SubscribeEvent
        public static void onAbilityEnableChange(AbilityEnableChangeEvent e) {
            switch (e.type) {
                case ENABLED:
                    if (new AbilityEnabledScriptEvent(e.getEntityLiving(), e.ability).fire())
                        e.setCanceled(true);
                    break;
                case DISABLED:
                    if (new AbilityDisabledScriptEvent(e.getEntityLiving(), e.ability).fire())
                        e.setCanceled(true);
                    break;
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void generateDocumentation() {
        List<String> ignoredMethods = Arrays.asList("fire", "wait", "equals", "toString", "hashCode", "getClass", "notify", "notifyAll");

        DocumentationBuilder builder = new DocumentationBuilder(new ResourceLocation(ThreeCore.MODID, "scripts/events"), "Script Events")
                .add(heading("Script Events")).add(hr())
                .add(paragraph(subHeading("Overview")).add(list(events.entrySet().stream().map(entry -> link(entry.getKey(), "#" + entry.getKey())).collect(Collectors.toList()))));

        for (Map.Entry<String, Class<? extends ScriptEvent>> entry : events.entrySet()) {
            builder.add(hr()).add(div().setId(entry.getKey()).add(subHeading(entry.getKey()))
                    .add(table(Arrays.asList("Function", "Return Type", "Parameters"), Arrays.stream(entry.getValue().getMethods()).filter(method -> !ignoredMethods.contains(method.getName()) && !Modifier.isStatic(method.getModifiers())).map(method -> {
                        Collection<String> columns = new LinkedList<>();
                        columns.add(method.getName());
                        columns.add(method.getReturnType().getSimpleName());

                        if (method.getParameterCount() <= 0)
                            columns.add("/");
                        else {
                            StringBuilder stringBuilder = new StringBuilder();
                            for (int i = 0; i < method.getParameterCount(); i++) {
                                String parameterName = method.getParameters()[i].getName();
                                ScriptParameterName scriptParameterName = method.getParameters()[i].getAnnotation(ScriptParameterName.class);
                                if (scriptParameterName != null)
                                    parameterName = scriptParameterName.value();

                                stringBuilder.append("<strong>").append(parameterName).append("</strong> - ").append(method.getParameterTypes()[i].getSimpleName());
                                if (method.getParameterCount() > 1)
                                    stringBuilder.append("<br>");
                            }
                            columns.add(stringBuilder.toString());
                        }

                        return columns;
                    }).collect(Collectors.toList()))));
        }

        builder.save();
    }

}
