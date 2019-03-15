package com.threetag.threecore.abilities.capability;

import com.threetag.threecore.abilities.Ability;
import com.threetag.threecore.abilities.AbilityMap;
import com.threetag.threecore.abilities.IAbilityProvider;
import net.minecraft.entity.EntityLivingBase;

import java.util.Collection;

public interface IAbilityContainer {

    default void tick(EntityLivingBase entity) {
        getAbilityMap().forEach((s, a) -> a.tick(entity));
    }

    default Collection<Ability> getAbilities() {
        return getAbilityMap().values();
    }

    default Ability getAbility(String id) {
        return getAbilityMap().get(id);
    }

    AbilityMap getAbilityMap();

    default void setAbilities(IAbilityProvider provider) {
        this.getAbilities().clear();
        provider.getAbilities().forEach((s, a) -> addAbility(s, a));
    }

    default boolean addAbility(String id, Ability ability) {
        if (this.getAbilityMap().containsKey(id))
            return false;
        this.getAbilityMap().put(id, ability);
        return true;
    }

    default boolean removeAbility(String id) {
        if (!this.getAbilityMap().containsKey(id))
            return false;
        this.getAbilityMap().remove(id);
        return true;
    }

}
