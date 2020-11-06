package net.threetag.threecore.ability.container;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.PacketDistributor;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.ability.Ability;
import net.threetag.threecore.ability.AbilityMap;
import net.threetag.threecore.ability.IAbilityProvider;
import net.threetag.threecore.network.AddAbilityMessage;
import net.threetag.threecore.network.RemoveAbilityMessage;
import net.threetag.threecore.network.UpdateAbilityMessage;
import net.threetag.threecore.util.icon.IIcon;
import net.threetag.threecore.util.threedata.EnumSync;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

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
        this.getAbilityMap().get(id).lastTick(entity);
        this.getAbilityMap().remove(id);
        if (entity != null && entity.world instanceof ServerWorld) {
            ThreeCore.NETWORK_CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), new RemoveAbilityMessage(entity.getEntityId(), getId(), id));
        }
        return true;
    }

    default void clearAbilities(@Nullable LivingEntity entity) {
        List<Ability> copy = new ArrayList<>(this.getAbilities());

        for (Ability ab : copy) {
            removeAbility(entity, ab.getId());
        }
    }

    default void clearAbilities(@Nullable LivingEntity entity, Predicate<Ability> predicate) {
        List<Ability> copy = new ArrayList<>(this.getAbilities());

        for (Ability ab : copy) {
            if (predicate.test(ab)) {
                removeAbility(entity, ab.getId());
            }
        }
    }

    ResourceLocation getId();

    ITextComponent getTitle();

    default ITextComponent getSubtitle() {
        return null;
    }

    IIcon getIcon();

    CompoundNBT getNbtTag(@Nonnull LivingEntity entity);

    @Nonnull
    default <T> LazyOptional<T> getCapability(@Nonnull LivingEntity entity, @Nonnull Capability<T> cap, @Nullable Direction side) {
        return LazyOptional.empty();
    }

    default boolean isObsolete() {
        return false;
    }

}
