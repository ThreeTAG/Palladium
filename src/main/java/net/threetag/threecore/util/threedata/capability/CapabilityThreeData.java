package net.threetag.threecore.util.threedata.capability;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.PacketDistributor;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.util.threedata.EnumSync;
import net.threetag.threecore.util.threedata.ThreeData;
import net.threetag.threecore.util.threedata.ThreeDataManager;

import javax.annotation.Nullable;

public class CapabilityThreeData implements IThreeData, INBTSerializable<CompoundNBT> {

    @CapabilityInject(IThreeData.class)
    public static Capability<IThreeData> THREE_DATA;
    public final ThreeDataManager dataManager;
    public final Entity entity;

    public CapabilityThreeData(Entity entity) {
        this.entity = entity;
        this.dataManager = new ThreeDataManager(this);
        MinecraftForge.EVENT_BUS.post(new RegisterThreeDataEvent(entity, this));
    }

    @Override
    public <T> void register(ThreeData<T> data, T defaultValue) {
        this.dataManager.register(data, defaultValue);
    }

    @Override
    public <T> void setData(ThreeData<T> data, T value) {
        this.dataManager.set(data, value);
    }

    @Override
    public <T> T getData(ThreeData<T> data) {
        return this.dataManager.get(data);
    }

    @Override
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
    public void setData(String dataKey, CompoundNBT dataTag) {
        ThreeData data = this.dataManager.getDataByName(dataKey);

        if (data != null)
            this.dataManager.set(data, data.readFromNBT(dataTag, this.dataManager.getDefaultValue(data)));
    }

    @Override
    public void setDirty() {

    }

    @Override
    public ThreeData<?> getDataByName(String name) {
        return this.dataManager.getDataByName(name);
    }

    @Override
    public CompoundNBT serializeNBT() {
        return this.dataManager.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.dataManager.deserializeNBT(nbt);
    }

    public static void init() {
        CapabilityManager.INSTANCE.register(IThreeData.class, new Capability.IStorage<IThreeData>() {
                    @Nullable
                    @Override
                    public INBT writeNBT(Capability<IThreeData> capability, IThreeData instance, Direction direction) {
                        if (instance instanceof INBTSerializable)
                            return ((INBTSerializable) instance).serializeNBT();
                        throw new IllegalArgumentException("Can not serialize an instance that isn't an instance of INBTSerializable");
                    }

                    @Override
                    public void readNBT(Capability<IThreeData> capability, IThreeData instance, Direction direction, INBT nbt) {
                        if (instance instanceof INBTSerializable)
                            ((INBTSerializable) instance).deserializeNBT(nbt);
                        else
                            throw new IllegalArgumentException("Can not serialize to an instance that isn't an instance of INBTSerializable");
                    }
                },
                () -> new CapabilityThreeData(null));
    }

}
