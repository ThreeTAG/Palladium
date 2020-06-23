package net.threetag.threecore.ability;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
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
import java.util.*;
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
            return function.apply(entity);
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
                if (condition instanceof AbilityUnlockedCondition && !condition.getDataManager().get(Condition.ENABLING)) {
                    Ability a = AbilityHelper.getAbilityById(entity, condition.getDataManager().get(AbilityUnlockedCondition.ABILITY_ID), ability.container);

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

}
