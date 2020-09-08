package net.threetag.threecore.ability;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.ability.condition.AbilityUnlockedCondition;
import net.threetag.threecore.ability.condition.Condition;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class AbilityHelper {

    private static final List<IAbilityContainerProvider> REGISTRY = Lists.newArrayList();

    public static IAbilityContainerProvider registerAbilityContainer(IAbilityContainerProvider containerSupplier) {
        REGISTRY.add(containerSupplier);
        return containerSupplier;
    }

    public static IAbilityContainer getAbilityContainerFromId(LivingEntity entity, ResourceLocation id) {
        for (IAbilityContainerProvider provider : REGISTRY) {
            for (IAbilityContainer container : provider.getAbilityContainer(entity)) {
                if (container.getId().equals(id)) {
                    return container;
                }
            }
        }
        return null;
    }

    public static Collection<IAbilityContainer> getAbilityContainers(LivingEntity entity) {
        List<IAbilityContainer> containers = Lists.newArrayList();
        for (IAbilityContainerProvider provider : REGISTRY) {
            containers.addAll(provider.getAbilityContainer(entity));
        }
        return containers;
    }

    public static Collection<IAbilityContainerProvider> getAbilityContainerProviders() {
        return ImmutableList.copyOf(REGISTRY);
    }

    public static List<Ability> getAbilities(LivingEntity entity) {
        List<Ability> list = new ArrayList<>();
        getAbilityContainers(entity).forEach((container) -> {
            if (container != null)
                list.addAll(container.getAbilities());
        });
        return list;
    }

    public static Ability getAbilityById(LivingEntity entity, String id, @Nullable IAbilityContainer currentContainer) {
        String[] strings = id.split("#", 2);

        if (strings.length == 1 && currentContainer == null)
            return null;
        else if (strings.length == 1)
            return currentContainer.getAbility(strings[0]);
        else {
            IAbilityContainer container = getAbilityContainerFromId(entity, new ResourceLocation(strings[0]));
            return container.getAbility(strings[1]);
        }
    }

    public static <T extends Ability> List<T> getAbilitiesFromClass(LivingEntity entity, Class<T> abilityClass) {
        return getAbilitiesFromClass(getAbilities(entity), abilityClass);
    }

    public static <T extends Ability> List<T> getAbilitiesFromClass(List<Ability> list, Class<T> abilityClass) {
        List<T> abilities = new LinkedList<>();
        for (Ability ab : list) {
            if (ab.getClass() == abilityClass) {
                abilities.add((T) ab);
            }
        }

        return abilities;
    }

    // TODO maybe make this a setting for conditions, instead of hardcoding it to AbilityUnlockedCondition
    public static List<Ability> findChildrenAbilities(LivingEntity entity, Ability ability, IAbilityContainer container) {
        List<Ability> list = Lists.newLinkedList();
        for (Ability all : container.getAbilities()) {
            for (Condition condition : all.getConditionManager().getConditions()) {
                if (condition instanceof AbilityUnlockedCondition && !condition.get(Condition.ENABLING) && !condition.get(Condition.INVERT)) {
                    Ability a = AbilityHelper.getAbilityById(entity, condition.get(AbilityUnlockedCondition.ABILITY_ID), ability.container);

                    if (a == ability) {
                        list.add(all);
                    }
                }
            }
        }
        return list;
    }

    public static List<Ability> findParentAbilities(LivingEntity entity, Ability ability, IAbilityContainer container) {
        List<Ability> list = Lists.newLinkedList();
        for (Condition condition : ability.getConditionManager().getConditions()) {
            if (condition instanceof AbilityUnlockedCondition && !condition.getDataManager().get(Condition.ENABLING)) {
                Ability a = AbilityHelper.getAbilityById(entity, condition.getDataManager().get(AbilityUnlockedCondition.ABILITY_ID), ability.container);

                if (a != null)
                    list.add(a);
            }
        }
        return list;
    }

    public static CompoundNBT saveToNBT(AbilityMap map, boolean network) {
        CompoundNBT nbt = new CompoundNBT();
        map.forEach((s, a) -> nbt.put(s, network ? a.getUpdateTag() : a.serializeNBT()));
        return nbt;
    }

    public static CompoundNBT saveToNBT(AbilityMap map) {
        return saveToNBT(map, false);
    }

    public static AbilityMap loadFromNBT(CompoundNBT nbt, AbilityMap map, boolean network) {
        nbt.keySet().forEach((s) -> {
            CompoundNBT tag = nbt.getCompound(s);
            AbilityType abilityType = AbilityType.REGISTRY.getValue(new ResourceLocation(tag.getString("AbilityType")));
            if (abilityType != null) {
                Ability ability = abilityType.create(s);
                if (network)
                    ability.readUpdateTag(tag);
                else
                    ability.deserializeNBT(tag);
                map.put(s, ability);
            } else {
                ThreeCore.LOGGER.error("Ability type " + tag.getString("AbilityType") + " does not exist!");
            }
        });
        return map;
    }

    public static AbilityMap loadFromNBT(CompoundNBT nbt, AbilityMap map) {
        return loadFromNBT(nbt, map, false);
    }

    public static List<AbilityGenerator> parseAbilityGenerators(JsonObject jsonObject, boolean useId) {
        List<AbilityGenerator> abilityGenerators = Lists.newArrayList();
        jsonObject.entrySet().forEach((e) -> {
            if (e.getValue() instanceof JsonObject) {
                JsonObject o = (JsonObject) e.getValue();
                if (useId) {
                    abilityGenerators.add(new AbilityGenerator(e.getKey(), new ResourceLocation(JSONUtils.getString(o, "ability")), o));
                } else {
                    AbilityType type = AbilityType.REGISTRY.getValue(new ResourceLocation(JSONUtils.getString(o, "ability")));
                    if (type == null)
                        throw new JsonSyntaxException("Expected 'ability' to be an ability, was unknown string '" + JSONUtils.getString(o, "ability") + "'");
                    abilityGenerators.add(new AbilityGenerator(e.getKey(), type, o));
                }
            }
        });
        return abilityGenerators;
    }

    public static List<AbilityGenerator> parseAbilityGenerators(JsonObject jsonObject) {
        return parseAbilityGenerators(jsonObject, false);
    }

    public interface IAbilityContainerProvider {

        Collection<IAbilityContainer> getAbilityContainer(LivingEntity entity);

    }

}
