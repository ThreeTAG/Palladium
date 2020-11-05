package net.threetag.threecore.capability;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.threetag.threecore.ability.container.IAbilityContainer;

import javax.annotation.Nullable;
import java.util.Collection;

public interface IMultiAbilityContainer {

    void tick(LivingEntity entity);

    IAbilityContainer getContainerById(ResourceLocation id);

    Collection<IAbilityContainer> getAllContainers();

    boolean addContainer(@Nullable LivingEntity entity, IAbilityContainer container);

    boolean removeContainer(@Nullable LivingEntity entity, ResourceLocation id);

}
