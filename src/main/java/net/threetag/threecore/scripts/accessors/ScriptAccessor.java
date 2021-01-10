package net.threetag.threecore.scripts.accessors;

import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.ability.Ability;
import net.threetag.threecore.ability.condition.Condition;
import net.threetag.threecore.scripts.ScriptParameterName;
import net.threetag.threecore.util.documentation.DocumentationBuilder;

import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

import static net.threetag.threecore.util.documentation.DocumentationBuilder.*;

public class ScriptAccessor<T> {

    public final T value;

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
        if (object instanceof Vector3d)
            return (ScriptAccessor<T>) new Vector3dAccessor((Vector3d) object);
        if (object instanceof Material)
            return (ScriptAccessor<T>) new MaterialAccessor((Material) object);
        if (object instanceof EntityRayTraceResult)
            return (ScriptAccessor<T>) new EntityRayTraceResultAccessor((EntityRayTraceResult) object);
        if (object instanceof BlockRayTraceResult)
            return (ScriptAccessor<T>) new BlockRayTraceResultAccessor((BlockRayTraceResult) object);
        return null;
    }

    public static List<Class<? extends ScriptAccessor<?>>> accessorClasses = new ArrayList<>();

    static {
        accessorClasses.addAll(Arrays.asList(EntityAccessor.class, LivingEntityAccessor.class,
                WorldAccessor.class, BlockStateAccessor.class, DamageSourceAccessor.class, AbilityAccessor.class, ConditionAccessor.class, CompoundNBTAccessor.class, Vector3dAccessor.class, MaterialAccessor.class, ItemStackAccessor.class,
                BlockRayTraceResultAccessor.class, EntityRayTraceResultAccessor.class));
    }

    @OnlyIn(Dist.CLIENT)
    public static void generateDocumentation() {
        List<String> ignoredMethods = Arrays.asList("fire", "wait", "equals", "toString", "hashCode", "getClass", "notify", "notifyAll");

        DocumentationBuilder builder = new DocumentationBuilder(new ResourceLocation(ThreeCore.MODID, "scripts/accessors"), "Script Accessors")
                .add(heading("Script Accessors")).add(hr())
                .add(paragraph(subHeading("Overview")).add(list(accessorClasses.stream().map(clazz -> link(clazz.getSimpleName(), "#" + clazz.getSimpleName())).collect(Collectors.toList()))));

        for (Class<? extends ScriptAccessor<?>> clazz : accessorClasses) {
            builder.add(hr()).add(div().setId(clazz.getSimpleName()).add(subHeading(clazz.getSimpleName() + (clazz.getSuperclass() != ScriptAccessor.class ? " <code>extends " + clazz.getSuperclass().getSimpleName() + "</code>" : "")))
                    .add(table(Arrays.asList("Function", "Return Type", "Parameters"), Arrays.stream(clazz.getMethods()).filter(method -> !ignoredMethods.contains(method.getName()) && !Modifier.isStatic(method.getModifiers())).map(method -> {
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
