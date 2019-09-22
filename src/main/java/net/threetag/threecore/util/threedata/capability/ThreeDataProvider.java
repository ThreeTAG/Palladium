package net.threetag.threecore.util.threedata.capability;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.NetworkDirection;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.abilities.capability.CapabilityAbilityContainer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ThreeDataProvider implements ICapabilitySerializable<CompoundNBT> {

    public final IThreeData threeData;
    public final LazyOptional<IThreeData> lazyOptional;

    public ThreeDataProvider(IThreeData threeData) {
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

    public static class EventHandler {

        @SubscribeEvent
        public void onAttachCapabilities(AttachCapabilitiesEvent<Entity> e) {
            if (e.getObject() instanceof Entity && !e.getObject().getCapability(CapabilityAbilityContainer.ABILITY_CONTAINER).isPresent()) {
                e.addCapability(new ResourceLocation(ThreeCore.MODID, "three_data"), new ThreeDataProvider(new CapabilityThreeData(e.getObject())));
            }
        }

        @SubscribeEvent
        public void onStartTracking(PlayerEvent.StartTracking e) {
            e.getTarget().getCapability(CapabilityAbilityContainer.ABILITY_CONTAINER).ifPresent(sizeChanging -> {
                if (sizeChanging instanceof INBTSerializable && e.getPlayer() instanceof ServerPlayerEntity) {
                    ThreeCore.NETWORK_CHANNEL.sendTo(new SyncThreeDataMessage(e.getTarget().getEntityId(), (CompoundNBT) ((INBTSerializable) sizeChanging).serializeNBT()), ((ServerPlayerEntity) e.getPlayer()).connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
                }
            });
        }

    }

}
