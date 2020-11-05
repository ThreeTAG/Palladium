package net.threetag.threecore.capability;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.network.PacketDistributor;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.ability.Ability;
import net.threetag.threecore.ability.container.AbilityContainerTypes;
import net.threetag.threecore.ability.container.DefaultAbilityContainer;
import net.threetag.threecore.ability.container.IAbilityContainer;
import net.threetag.threecore.network.AddAbilityContainerMessage;
import net.threetag.threecore.network.RemoveAbilityContainerMessage;
import net.threetag.threecore.util.icon.IIcon;
import net.threetag.threecore.util.icon.TexturedIcon;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CapabilityAbilityContainer implements IMultiAbilityContainer, INBTSerializable<CompoundNBT> {

    @CapabilityInject(IMultiAbilityContainer.class)
    public static Capability<IMultiAbilityContainer> MULTI_ABILITY_CONTAINER;
    @CapabilityInject(IAbilityContainer.class)
    public static Capability<IAbilityContainer> ABILITY_CONTAINER;
    public static final IIcon STEVE_HEAD_ICON = new TexturedIcon(TexturedIcon.ICONS_TEXTURE, 112, 16, 16, 16);

    private final Map<ResourceLocation, IAbilityContainer> containers;

    public CapabilityAbilityContainer() {
        this.containers = Maps.newHashMap();
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        containers.forEach((id, container) -> {
            if (container instanceof INBTSerializable<?>) {
                nbt.put(id.toString(), ((INBTSerializable<?>) container).serializeNBT());
            }
        });
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.containers.clear();
        for (String id : nbt.keySet()) {
            IAbilityContainer container = AbilityContainerTypes.deserialize(nbt.getCompound(id), false);
            if (container != null) {
                this.containers.put(container.getId(), container);
            }
        }
    }

    public CompoundNBT getUpdateTag() {
        CompoundNBT nbt = new CompoundNBT();
        containers.forEach((id, container) -> {
            if (container instanceof DefaultAbilityContainer) {
                nbt.put(id.toString(), ((DefaultAbilityContainer) container).getUpdateTag());
            }
        });
        return nbt;
    }

    public void readUpdateTag(CompoundNBT nbt) {
        this.containers.clear();
        for (String id : nbt.keySet()) {
            IAbilityContainer container = AbilityContainerTypes.deserialize(nbt.getCompound(id), true);
            if (container != null) {
                this.containers.put(container.getId(), container);
            }
        }
    }

    @Override
    public void tick(LivingEntity entity) {
        if (!entity.world.isRemote) {
            List<ResourceLocation> remove = Lists.newArrayList();

            for (IAbilityContainer container : this.containers.values()) {
                if (container.isObsolete()) {
                    remove.add(container.getId());
                }
            }

            for (ResourceLocation id : remove) {
                for (Ability ability : this.getContainerById(id).getAbilities()) {
                    ability.lastTick(entity);
                }
                this.removeContainer(entity, id);
            }
        }
    }

    @Override
    public boolean addContainer(@Nullable LivingEntity entity, IAbilityContainer container) {
        if (!this.containers.containsKey(container.getId())) {
            this.containers.put(container.getId(), container);
            if (entity != null && entity.world instanceof ServerWorld && container instanceof INBTSerializable<?>) {
                ThreeCore.NETWORK_CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), new AddAbilityContainerMessage(entity.getEntityId(), (CompoundNBT) ((INBTSerializable<?>) container).serializeNBT()));
            }

            return true;
        }
        return false;
    }

    @Override
    public boolean removeContainer(@Nullable LivingEntity entity, ResourceLocation id) {
        if (this.containers.containsKey(id)) {
            if(entity != null) {
                for(Ability ability : this.containers.get(id).getAbilities()) {
                    ability.lastTick(entity);
                }
            }
            this.containers.remove(id);
            if (entity != null && entity.world instanceof ServerWorld) {
                ThreeCore.NETWORK_CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), new RemoveAbilityContainerMessage(entity.getEntityId(), id));
            }
            return true;
        }
        return false;
    }

    @Override
    public IAbilityContainer getContainerById(ResourceLocation id) {
        return this.containers.get(id);
    }

    @Override
    public Collection<IAbilityContainer> getAllContainers() {
        return this.containers.values();
    }

}
