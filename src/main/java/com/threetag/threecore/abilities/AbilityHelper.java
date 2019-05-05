package com.threetag.threecore.abilities;

import com.google.common.collect.Maps;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class AbilityHelper {

    private static final Map<ResourceLocation, Function<EntityLivingBase, IAbilityContainer>> REGISTRY = Maps.newHashMap();

    public static Function<EntityLivingBase, IAbilityContainer> registerAbilityContainer(ResourceLocation name, Function<EntityLivingBase, IAbilityContainer> containerSupplier) {
        if (REGISTRY.containsKey(name)) {
            throw new IllegalArgumentException("Duplicate ability supplier " + name.toString());
        } else {
            REGISTRY.put(name, containerSupplier);
            return containerSupplier;
        }
    }

    public static IAbilityContainer getAbilityContainerFromId(EntityLivingBase entity, ResourceLocation id) {
        Function<EntityLivingBase, IAbilityContainer> function = REGISTRY.get(id);
        if (function != null) {
            IAbilityContainer container = function.apply(entity);
            if (container != null) {
                return container;
            }
        }
        return null;
    }

    public static Collection<Function<EntityLivingBase, IAbilityContainer>> getAbilityContainerList() {
        return REGISTRY.values();
    }

    public static List<Ability> getAbilities(EntityLivingBase entity) {
        List<Ability> list = new ArrayList<>();
        getAbilityContainerList().forEach((f) -> {
            IAbilityContainer container = f.apply(entity);
            if (container != null)
                list.addAll(container.getAbilities());
        });
        return list;
    }

    public static Ability getAbilityById(EntityLivingBase entity, String id, @Nullable IAbilityContainer currentContainer) {
        String[] strings = id.split("#", 2);

        if (strings.length == 1 && currentContainer == null)
            return null;
        else if (strings.length == 1 && currentContainer != null)
            return currentContainer.getAbility(strings[0]);
        else {
            IAbilityContainer container = getAbilityContainerFromId(entity, new ResourceLocation(strings[0]));
            return container.getAbility(strings[1]);
        }
    }

}
