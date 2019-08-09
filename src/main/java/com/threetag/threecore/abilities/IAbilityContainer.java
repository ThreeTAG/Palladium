package com.threetag.threecore.abilities;

import com.threetag.threecore.ThreeCore;
import com.threetag.threecore.abilities.data.EnumSync;
import com.threetag.threecore.abilities.network.AddAbilityMessage;
import com.threetag.threecore.abilities.network.RemoveAbilityMessage;
import com.threetag.threecore.abilities.network.UpdateAbilityMessage;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.PacketDistributor;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public interface IAbilityContainer {

    default void tick(LivingEntity entity) {
        getAbilityMap().forEach((s, a) -> {
            a.container = this;
            a.tick(entity);
            if (a.sync != EnumSync.NONE) {
                onUpdated(entity, a, a.sync);
                a.sync = EnumSync.NONE;
            }
        });
    }

    default void onUpdated(LivingEntity entity, Ability ability, EnumSync sync) {
        if (entity.world.isRemote)
            return;
        if (sync != EnumSync.NONE && entity instanceof ServerPlayerEntity) {
            ThreeCore.NETWORK_CHANNEL.sendTo(new UpdateAbilityMessage(entity.getEntityId(), this.getId(), ability.getId(), ability.getUpdateTag()), ((ServerPlayerEntity) entity).connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
        }
        if (sync == EnumSync.EVERYONE && entity.world instanceof ServerWorld) {
            ThreeCore.NETWORK_CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), new UpdateAbilityMessage(entity.getEntityId(), this.getId(), ability.getId(), ability.getUpdateTag()));
        }
    }

    default Collection<Ability> getAbilities() {
        return getAbilityMap().values();
    }

    default Ability getAbility(String id) {
        return getAbilityMap().get(id);
    }

    AbilityMap getAbilityMap();

    default void addAbilities(@Nullable LivingEntity entity, IAbilityProvider provider) {
        provider.getAbilities().forEach((s, a) -> addAbility(entity, s, a));
    }

    default boolean addAbility(@Nullable LivingEntity entity, String id, Ability ability) {
        if (this.getAbilityMap().containsKey(id))
            return false;
        this.getAbilityMap().put(id, ability);
        if (entity != null && entity.world instanceof ServerWorld) {
            ThreeCore.NETWORK_CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), new AddAbilityMessage(entity.getEntityId(), getId(), id, ability.getUpdateTag()));
        }
        return true;
    }

    default boolean removeAbility(@Nullable LivingEntity entity, String id) {
        if (!this.getAbilityMap().containsKey(id))
            return false;
        this.getAbilityMap().remove(id);
        if (entity != null && entity.world instanceof ServerWorld) {
            ThreeCore.NETWORK_CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), new RemoveAbilityMessage(entity.getEntityId(), getId(), id));
        }
        return true;
    }

    default void clearAbilities(@Nullable LivingEntity entity) {
        List<Ability> copy = this.getAbilities().stream().collect(Collectors.toList());

        for (Ability ab : copy) {
            removeAbility(entity, ab.getId());
        }
    }

    default void clearAbilities(@Nullable LivingEntity entity, Predicate<Ability> predicate) {
        List<Ability> copy = this.getAbilities().stream().collect(Collectors.toList());

        for (Ability ab : copy) {
            if (predicate.test(ab)) {
                removeAbility(entity, ab.getId());
            }
        }
    }

    ResourceLocation getId();

}
