package com.threetag.threecore.abilities;

import com.threetag.threecore.abilities.capability.IAbilityContainer;
import net.minecraft.entity.EntityLivingBase;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

public class AbilityHelper {

    private static final List<Function<EntityLivingBase, IAbilityContainer>> abilityContainerList = new LinkedList<>();

    public static void registerAbilityContainer(Function<EntityLivingBase, IAbilityContainer> containerSupplier) {
        abilityContainerList.add(containerSupplier);
    }

    public static List<Function<EntityLivingBase, IAbilityContainer>> getAbilityContainerList() {
        return abilityContainerList;
    }

    public static List<Ability> getAbilities(EntityLivingBase entity) {
        List<Ability> list = new ArrayList<>();
        abilityContainerList.forEach((f) -> {
            IAbilityContainer container = f.apply(entity);
            if (container != null)
                list.addAll(container.getAbilities());
        });
        return list;
    }

}
