package net.threetag.threecore.capability;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkDirection;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.network.SyncThreeDataMessage;
import net.threetag.threecore.util.threedata.IThreeDataHolder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ThreeDataProvider implements ICapabilitySerializable<CompoundNBT> {

    public final IThreeDataHolder threeData;
    public final LazyOptional<IThreeDataHolder> lazyOptional;

    public ThreeDataProvider(IThreeDataHolder threeData) {
        this.threeData = threeData;
        this.lazyOptional = LazyOptional.of(() -> threeData);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == CapabilityThreeData.THREE_DATA ? (LazyOptional<T>) this.lazyOptional : LazyOptional.empty();
    }

    @Override
    public CompoundNBT serializeNBT() {
        if (this.threeData instanceof INBTSerializable)
            return ((INBTSerializable<CompoundNBT>) this.threeData).serializeNBT();
        return new CompoundNBT();
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        if (this.threeData instanceof INBTSerializable)
            ((INBTSerializable<CompoundNBT>) this.threeData).deserializeNBT(nbt);
    }

    @Mod.EventBusSubscriber(modid = ThreeCore.MODID)
    public static class EventHandler {

        @SubscribeEvent
        public static void onStartTracking(PlayerEvent.StartTracking e) {
            e.getTarget().getCapability(CapabilityThreeData.THREE_DATA).ifPresent(threeData -> {
                if (threeData instanceof INBTSerializable && e.getPlayer() instanceof ServerPlayerEntity) {
                    ThreeCore.NETWORK_CHANNEL.sendTo(new SyncThreeDataMessage(e.getTarget().getEntityId(), (CompoundNBT) ((INBTSerializable) threeData).serializeNBT()), ((ServerPlayerEntity) e.getPlayer()).connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
                }
            });
        }

    }

}
