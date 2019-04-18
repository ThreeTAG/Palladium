package com.threetag.threecore.abilities;

import com.threetag.threecore.ThreeCore;
import com.threetag.threecore.abilities.data.EnumSync;
import com.threetag.threecore.abilities.network.MessageAddAbility;
import com.threetag.threecore.abilities.network.MessageRemoveAbility;
import com.threetag.threecore.abilities.network.MessageUpdateAbility;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.network.NetworkDirection;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public interface IAbilityContainer {

    default void tick(EntityLivingBase entity) {
        getAbilityMap().forEach((s, a) -> {
            a.container = this;
            a.tick(entity);
            if (a.sync != EnumSync.NONE) {
                onUpdated(entity, a, a.sync);
                a.sync = EnumSync.NONE;
            }
        });
    }

    default void onUpdated(EntityLivingBase entity, Ability ability, EnumSync sync) {
        if (entity.world.isRemote)
            return;
        if (sync != EnumSync.NONE && entity instanceof EntityPlayerMP) {
            ThreeCore.NETWORK_CHANNEL.sendTo(new MessageUpdateAbility(entity.getEntityId(), this.getId(), ability.getId(), ability.dataManager.getUpdatePacket()), ((EntityPlayerMP) entity).connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
        }
        if (sync == EnumSync.EVERYONE && entity.world instanceof WorldServer) {
            ((WorldServer) entity.world).getEntityTracker().getTrackingPlayers(entity).forEach((p) -> {
                if (p instanceof EntityPlayerMP)
                    ThreeCore.NETWORK_CHANNEL.sendTo(new MessageUpdateAbility(entity.getEntityId(), this.getId(), ability.getId(), ability.dataManager.getUpdatePacket()), ((EntityPlayerMP) p).connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
            });
        }
    }

    default Collection<Ability> getAbilities() {
        return getAbilityMap().values();
    }

    default Ability getAbility(String id) {
        return getAbilityMap().get(id);
    }

    AbilityMap getAbilityMap();

    default void addAbilities(@Nullable EntityLivingBase entity, IAbilityProvider provider) {
        provider.getAbilities().forEach((s, a) -> addAbility(entity, s, a));
    }

    default boolean addAbility(@Nullable EntityLivingBase entity, String id, Ability ability) {
        if (this.getAbilityMap().containsKey(id))
            return false;
        this.getAbilityMap().put(id, ability);
        if (entity != null && entity.world instanceof WorldServer) {
            if (entity instanceof EntityPlayerMP)
                ThreeCore.NETWORK_CHANNEL.sendTo(new MessageAddAbility(entity.getEntityId(), getId(), id, ability.getUpdateTag()), ((EntityPlayerMP) entity).connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
            ((WorldServer) entity.world).getEntityTracker().getTrackingPlayers(entity).forEach((p) -> ThreeCore.NETWORK_CHANNEL.sendTo(new MessageAddAbility(entity.getEntityId(), getId(), id, ability.getUpdateTag()), ((EntityPlayerMP) p).connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT));
        }
        return true;
    }

    default boolean removeAbility(@Nullable EntityLivingBase entity, String id) {
        if (!this.getAbilityMap().containsKey(id))
            return false;
        this.getAbilityMap().remove(id);
        if (entity != null && entity.world instanceof WorldServer) {
            if (entity instanceof EntityPlayerMP)
                ThreeCore.NETWORK_CHANNEL.sendTo(new MessageRemoveAbility(entity.getEntityId(), getId(), id), ((EntityPlayerMP) entity).connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
            ((WorldServer) entity.world).getEntityTracker().getTrackingPlayers(entity).forEach((p) -> ThreeCore.NETWORK_CHANNEL.sendTo(new MessageRemoveAbility(entity.getEntityId(), getId(), id), ((EntityPlayerMP) p).connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT));
        }
        return true;
    }

    default void clearAbilities(@Nullable EntityLivingBase entity) {
        List<Ability> copy = this.getAbilities().stream().collect(Collectors.toList());

        for (Ability ab : copy) {
            removeAbility(entity, ab.getId());
        }
    }

    default void clearAbilities(@Nullable EntityLivingBase entity, Predicate<Ability> predicate) {
        List<Ability> copy = this.getAbilities().stream().collect(Collectors.toList());

        for (Ability ab : copy) {
            if (predicate.test(ab)) {
                removeAbility(entity, ab.getId());
            }
        }
    }

    ResourceLocation getId();

}
