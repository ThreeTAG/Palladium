package com.threetag.threecore.abilities;

import com.google.common.collect.Maps;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class AbilityHelper {

    private static final Map<ResourceLocation, Function<LivingEntity, IAbilityContainer>> REGISTRY = Maps.newHashMap();

    public static Function<LivingEntity, IAbilityContainer> registerAbilityContainer(ResourceLocation name, Function<LivingEntity, IAbilityContainer> containerSupplier) {
        if (REGISTRY.containsKey(name)) {
            throw new IllegalArgumentException("Duplicate ability supplier " + name.toString());
        } else {
            REGISTRY.put(name, containerSupplier);
            return containerSupplier;
        }
    }

    public static IAbilityContainer getAbilityContainerFromId(LivingEntity entity, ResourceLocation id) {
        Function<LivingEntity, IAbilityContainer> function = REGISTRY.get(id);
        if (function != null) {
            IAbilityContainer container = function.apply(entity);
            if (container != null) {
                return container;
            }
        }
        return null;
    }

    public static Collection<Function<LivingEntity, IAbilityContainer>> getAbilityContainerList() {
        return REGISTRY.values();
    }

    public static List<Ability> getAbilities(LivingEntity entity) {
        List<Ability> list = new ArrayList<>();
        getAbilityContainerList().forEach((f) -> {
            IAbilityContainer container = f.apply(entity);
            if (container != null)
                list.addAll(container.getAbilities());
        });
        return list;
    }

    public static Ability getAbilityById(LivingEntity entity, String id, @Nullable IAbilityContainer currentContainer) {
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
