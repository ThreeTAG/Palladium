package net.threetag.threecore.capability;

import com.google.common.collect.Lists;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.network.PacketDistributor;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.accessoires.Accessoire;
import net.threetag.threecore.network.SyncAccessoiresMessage;

import java.util.Collection;
import java.util.List;

public class CapabilityAccessoires implements IAccessoireHolder, INBTSerializable<CompoundNBT> {

    @CapabilityInject(IAccessoireHolder.class)
    public static Capability<IAccessoireHolder> ACCESSOIRES;

    public List<Accessoire> accessoireList = Lists.newArrayList();

    @Override
    public void enable(Accessoire accessoire, PlayerEntity player) {
        if (accessoire != null && !this.accessoireList.contains(accessoire) && accessoire.isAvailable(player)) {
            Accessoire.PlayerPart playerPart = accessoire.getPlayerPart();

            if (playerPart != null) {
                Accessoire accessoire1 = null;

                for (Accessoire a : this.accessoireList) {
                    if (a.getPlayerPart() == playerPart) {
                        accessoire1 = a;
                        break;
                    }
                }

                if (accessoire1 != null) {
                    this.accessoireList.remove(accessoire1);
                }
            }

            this.accessoireList.add(accessoire);
            if (!player.world.isRemote)
                ThreeCore.NETWORK_CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player), new SyncAccessoiresMessage(player.getEntityId(), this.getActiveAccessoires()));
        }
    }

    @Override
    public void disable(Accessoire accessoire, PlayerEntity player) {
        if (accessoire != null) {
            this.accessoireList.remove(accessoire);
            if (!player.world.isRemote)
                ThreeCore.NETWORK_CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player), new SyncAccessoiresMessage(player.getEntityId(), this.getActiveAccessoires()));
        }
    }

    @Override
    public Collection<Accessoire> getActiveAccessoires() {
        return this.accessoireList;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        ListNBT listNBT = new ListNBT();
        for (Accessoire accessoire : this.accessoireList) {
            listNBT.add(StringNBT.valueOf(Accessoire.REGISTRY.getKey(accessoire).toString()));
        }
        nbt.put("Accessoires", listNBT);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.accessoireList = Lists.newArrayList();
        ListNBT listNBT = nbt.getList("Accessoires", Constants.NBT.TAG_STRING);
        for (int i = 0; i < listNBT.size(); i++) {
            Accessoire accessoire = Accessoire.REGISTRY.getValue(new ResourceLocation(listNBT.getString(i)));
            if (accessoire != null) {
                this.accessoireList.add(accessoire);
            }
        }
    }
}
