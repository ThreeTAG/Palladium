package net.threetag.threecore.capability;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.PacketDistributor;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.network.UpdateThreeDataMessage;
import net.threetag.threecore.util.threedata.*;
import net.threetag.threecore.event.RegisterThreeDataEvent;

public class CapabilityThreeData implements IWrappedThreeDataHolder, INBTSerializable<CompoundNBT> {

    @CapabilityInject(IThreeDataHolder.class)
    public static Capability<IThreeDataHolder> THREE_DATA;
    public final ThreeDataManager dataManager;
    public final Entity entity;

    public CapabilityThreeData(Entity entity) {
        this.entity = entity;
        this.dataManager = new ThreeDataManager().setListener(new ThreeDataManager.Listener() {
            @Override
            public <T> void dataChanged(ThreeData<T> data, T oldValue, T value) {
                update(data, value);
            }
        });
        MinecraftForge.EVENT_BUS.post(new RegisterThreeDataEvent(entity, this));
    }

    @Override
    public IThreeDataHolder getThreeDataHolder() {
        return this.dataManager;
    }

    public <T> void update(ThreeData<T> data, T value) {
        if (entity.world.isRemote)
            return;

        CompoundNBT nbt = new CompoundNBT();
        data.writeToNBT(nbt, value);

        if (data.getSyncType() != EnumSync.NONE && entity instanceof ServerPlayerEntity) {
            ThreeCore.NETWORK_CHANNEL.sendTo(new UpdateThreeDataMessage(entity.getEntityId(), data.getKey(), nbt), ((ServerPlayerEntity) entity).connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
        }
        if (data.getSyncType() == EnumSync.EVERYONE && entity.world instanceof ServerWorld) {
            ThreeCore.NETWORK_CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), new UpdateThreeDataMessage(entity.getEntityId(), data.getKey(), nbt));
        }
    }

    @Override
    public CompoundNBT serializeNBT() {
        return this.dataManager.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.dataManager.deserializeNBT(nbt);
    }

}
