package net.threetag.threecore.util.scripts;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.util.scripts.events.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
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

    @OnlyIn(Dist.CLIENT)
    public static void generateHtmlFile(File file) {
        List<String> ignoredMethods = Arrays.asList("fire", "wait", "equals", "toString", "hashCode", "getClass", "notify", "notifyAll");
        try {
            if (!file.getParentFile().exists())
                file.getParentFile().mkdirs();

            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            List<String> lines = Lists.newLinkedList();
            lines.add("<html><head><title>Script Events</title><style>\n" +
                    "table{font-family:arial, sans-serif;border-collapse:collapse;}\n" +
                    "td,th{border:1px solid #666666;text-align:left;padding:8px;min-width:45px;}\n" +
                    "th{background-color:#CCCCCC;}\n" +
                    "p{margin:0;}\n" +
                    "tr:nth-child(even){background-color:#D8D8D8;}\n" +
                    "tr:nth-child(odd){background-color:#EEEEEE;}\n" +
                    "td.true{background-color:#72FF85AA;}\n" +
                    "td.false{background-color:#FF6666AA;}\n" +
                    "td.other{background-color:#42A3FFAA;}\n" +
                    "td.error{color:#FF0000;}\n" +
                    "th,td.true,td.false,td.other{text-align:center;}\n" +
                    "</style><link rel=\"shortcut icon\" type=\"image/x-icon\" href=\"https://i.imgur.com/am80ox1.png\">" +
                    "</head><body>");

            for (Map.Entry<String, Class<? extends ScriptEvent>> entry : events.entrySet()) {
                String name = entry.getKey();
                Class<? extends ScriptEvent> clazz = entry.getValue();

                lines.add("<p><h1 id=\"" + name + "\">" + name + "</h1>");

                lines.add("<table>\n<tr><th>Function</th><th>Return Type</th><th>Parameters</th></tr>");

                for (Method method : clazz.getMethods()) {
                    if (!ignoredMethods.contains(method.getName())) {
                        lines.add("<tr>");
                        lines.add("<td>" + method.getName() + "</td>");
                        lines.add("<td>" + method.getReturnType().getSimpleName() + "</td>");

                        lines.add("<td>");
                        if (method.getParameterCount() <= 0)
                            lines.add("/");
                        else
                            for (int i = 0; i < method.getParameterCount(); i++) {
                                String parameterName = method.getParameters()[i].getName();
                                ScriptParameterName scriptParameterName = method.getParameters()[i].getAnnotation(ScriptParameterName.class);
                                if (scriptParameterName != null)
                                    parameterName = scriptParameterName.value();

                                lines.add("<strong>" + parameterName + "</strong> - " + method.getParameterTypes()[i].getSimpleName());
                                if (method.getParameterCount() > 1 && i - 2 <= method.getParameterCount())
                                    lines.add("<br>");
                            }
                        lines.add("</td>");

                        lines.add("</tr>");
                    }
                }
                lines.add("</table>");
                lines.add("</p><hr>\n");
            }

            for (String s : lines)
                bw.write(s + "\n");
            bw.close();

            ThreeCore.LOGGER.info("Successfully generated script_events.html!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
