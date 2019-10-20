package net.threetag.threecore.util.scripts.accessors;

import com.google.common.collect.Lists;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.abilities.Ability;
import net.threetag.threecore.abilities.condition.Condition;
import net.threetag.threecore.util.scripts.ScriptParameterName;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

public class ScriptAccessor<T> {

    protected final T value;

    protected ScriptAccessor(T value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        return this.value.equals(obj);
    }

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    public static <T> ScriptAccessor<T> makeAccessor(T object) {
        if (object instanceof LivingEntity)
            return (ScriptAccessor<T>) new LivingEntityAccessor((LivingEntity) object);
        if (object instanceof Entity)
            return (ScriptAccessor<T>) new EntityAccessor((Entity) object);
        if (object instanceof World)
            return (ScriptAccessor<T>) new WorldAccessor((World) object);
        if (object instanceof BlockState)
            return (ScriptAccessor<T>) new BlockStateAccessor((BlockState) object);
        if (object instanceof DamageSource)
            return (ScriptAccessor<T>) new DamageSourceAccessor((DamageSource) object);
        if (object instanceof Ability)
            return (ScriptAccessor<T>) new AbilityAccessor((Ability) object);
        if (object instanceof Condition)
            return (ScriptAccessor<T>) new ConditionAccessor((Condition) object);
        return null;
    }

    @OnlyIn(Dist.CLIENT)
    public static void generateHtmlFile(File file) {
        List<String> ignoredMethods = Arrays.asList("fire", "wait", "equals", "toString", "hashCode", "getClass", "notify", "notifyAll");
        List<Class<? extends ScriptAccessor>> accessorClasses = Arrays.asList(EntityAccessor.class, LivingEntityAccessor.class,
                WorldAccessor.class, BlockStateAccessor.class, DamageSourceAccessor.class, AbilityAccessor.class, ConditionAccessor.class);
        try {
            if (!file.getParentFile().exists())
                file.getParentFile().mkdirs();

            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            List<String> lines = Lists.newLinkedList();
            lines.add("<html><head><title>Script Accessors</title><style>\n" +
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

            lines.add("<ul>");
            for (Class<? extends ScriptAccessor> clazz : accessorClasses) {
                lines.add("<li><a href=\"#" + clazz.getSimpleName() + "\">" + clazz.getSimpleName() + "</a></li>");
            }
            lines.add("</ul>");
            lines.add("<hr>\n");

            for (Class<? extends ScriptAccessor> clazz : accessorClasses) {
                String name = clazz.getSimpleName();

                lines.add("<p><h1 id=\"" + name + "\">" + name + "</h1>");

                lines.add("<table>\n<tr><th>Function</th><th>Return Type</th><th>Parameters</th></tr>");

                for (Method method : clazz.getMethods()) {
                    if (!ignoredMethods.contains(method.getName()) && !Modifier.isStatic(method.getModifiers())) {
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

            ThreeCore.LOGGER.info("Successfully generated " + file.getName() + "!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
