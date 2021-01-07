package net.threetag.threecore.capability;

import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.common.thread.EffectiveSide;
import net.minecraftforge.fml.network.PacketDistributor;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.accessoires.Accessoire;
import net.threetag.threecore.accessoires.AccessoireSlot;
import net.threetag.threecore.network.SyncAccessoiresMessage;

import javax.annotation.Nullable;
import java.util.*;

public class CapabilityAccessoires implements IAccessoireHolder, INBTSerializable<CompoundNBT> {

    @CapabilityInject(IAccessoireHolder.class)
    public static Capability<IAccessoireHolder> ACCESSOIRES;

    public Map<AccessoireSlot, Collection<Accessoire>> accessoires = new HashMap<>();

    @Override
    public void enable(AccessoireSlot slot, Accessoire accessoire, PlayerEntity player) {
        if (slot != null && accessoire != null && accessoire.getPossibleSlots().contains(slot) && canEnable(accessoire, player)) {
            if (slot.allowsMultiple()) {
                Collection<Accessoire> accessoires = getOrCreateSlotList(slot);
                if (!accessoires.contains(accessoire)) {
                    accessoires.add(accessoire);
                }
            } else {
                this.accessoires.put(slot, Collections.singletonList(accessoire));
            }

            if (!player.world.isRemote)
                ThreeCore.NETWORK_CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player), new SyncAccessoiresMessage(player.getEntityId(), this.getSlots()));
        }
    }

    public boolean canEnable(Accessoire accessoire, PlayerEntity player) {
        return EffectiveSide.get().isClient() || accessoire.isAvailable(player);
    }

    @Override
    public void disable(AccessoireSlot slot, @Nullable Accessoire accessoire, PlayerEntity player) {
        if (slot != null && accessoire != null) {
            if (slot.allowsMultiple()) {
                if (accessoire == null) {
                    this.accessoires.put(slot, new ArrayList<>());
                } else {
                    Collection<Accessoire> accessoires = getOrCreateSlotList(slot);
                    accessoires.remove(accessoire);
                }
            } else {
                this.accessoires.put(slot, new ArrayList<>());
            }
            if (!player.world.isRemote)
                ThreeCore.NETWORK_CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player), new SyncAccessoiresMessage(player.getEntityId(), this.getSlots()));
        }
    }

    @Override
    public void validate(PlayerEntity player) {
        List<Pair<AccessoireSlot, Accessoire>> disable = new ArrayList<>();

        this.accessoires.forEach((slot, accessoires) -> {
            for (Accessoire accessoire : accessoires) {
                if (!canEnable(accessoire, player)) {
                    disable.add(Pair.of(slot, accessoire));
                }
            }
        });
        for (Pair<AccessoireSlot, Accessoire> pair : disable) {
            this.disable(pair.getFirst(), pair.getSecond(), player);
        }
    }

    public Collection<Accessoire> getOrCreateSlotList(AccessoireSlot slot) {
        Collection<Accessoire> accessoires = this.accessoires.get(slot);
        if (slot != null) {
            return accessoires;
        } else {
            accessoires = new ArrayList<>();
            this.accessoires.put(slot, accessoires);
            return accessoires;
        }
    }

    @Override
    public void clear(PlayerEntity player) {
        this.accessoires.clear();
        for (AccessoireSlot slot : AccessoireSlot.getSlots()) {
            this.accessoires.put(slot, new ArrayList<>());
        }
        if (!player.world.isRemote)
            ThreeCore.NETWORK_CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player), new SyncAccessoiresMessage(player.getEntityId(), this.getSlots()));
    }

    @Override
    public Map<AccessoireSlot, Collection<Accessoire>> getSlots() {
        return this.accessoires;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        this.accessoires.forEach((slot, list) -> {
            ListNBT listNBT = new ListNBT();
            for (Accessoire accessoire : list) {
                listNBT.add(StringNBT.valueOf(Accessoire.REGISTRY.getKey(accessoire).toString()));
            }
            nbt.put(slot.getName(), listNBT);
        });
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.accessoires = new HashMap<>();
        for (AccessoireSlot slot : AccessoireSlot.getSlots()) {
            ListNBT listNBT = nbt.getList(slot.getName(), Constants.NBT.TAG_STRING);
            List<Accessoire> accessoires = new ArrayList<>();
            for (int i = 0; i < listNBT.size(); i++) {
                Accessoire accessoire = Accessoire.REGISTRY.getValue(new ResourceLocation(listNBT.getString(i)));
                if (accessoire != null) {
                    accessoires.add(accessoire);
                }
            }
            this.accessoires.put(slot, accessoires);
        }
    }
}
