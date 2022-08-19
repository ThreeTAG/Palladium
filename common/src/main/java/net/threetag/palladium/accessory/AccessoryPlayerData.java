package net.threetag.palladium.accessory;

import com.mojang.datafixers.util.Pair;
import dev.architectury.platform.Platform;
import net.fabricmc.api.EnvType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.threetag.palladium.network.SyncAccessoriesMessage;

import javax.annotation.Nullable;
import java.util.*;

public class AccessoryPlayerData {

    public Map<AccessorySlot, Collection<Accessory>> accessories = new HashMap<>();

    public void enable(AccessorySlot slot, Accessory accessory, Player player) {
        if (slot != null && accessory != null && accessory.getPossibleSlots().contains(slot) && canEnable(accessory, player)) {
            if (slot.allowsMultiple()) {
                Collection<Accessory> accessories = this.accessories.computeIfAbsent(slot, accessorySlot -> new ArrayList<>());
                if (!accessories.contains(accessory)) {
                    accessories.add(accessory);
                }
            } else {
                this.accessories.put(slot, Collections.singletonList(accessory));
            }

            if (!player.level.isClientSide)
                new SyncAccessoriesMessage(player.getId(), this.accessories).sendToLevel((ServerLevel) player.level);
        }
    }

    public boolean canEnable(Accessory accessory, Player player) {
        return Platform.getEnv() == EnvType.CLIENT || accessory.isAvailable(player);
    }

    public void disable(AccessorySlot slot, @Nullable Accessory accessory, Player player) {
        if (slot != null && accessory != null) {
            if (slot.allowsMultiple()) {
                Collection<Accessory> accessories = this.accessories.computeIfAbsent(slot, accessorySlot -> new ArrayList<>());
                accessories.remove(accessory);
            } else {
                this.accessories.put(slot, new ArrayList<>());
            }
            if (!player.level.isClientSide)
                new SyncAccessoriesMessage(player.getId(), this.accessories).sendToLevel((ServerLevel) player.level);
        }
    }

    public void validate(Player player) {
        List<Pair<AccessorySlot, Accessory>> disable = new ArrayList<>();

        this.accessories.forEach((slot, accessories) -> {
            for (Accessory accessory : accessories) {
                if (!canEnable(accessory, player)) {
                    disable.add(Pair.of(slot, accessory));
                }
            }
        });
        for (Pair<AccessorySlot, Accessory> pair : disable) {
            this.disable(pair.getFirst(), pair.getSecond(), player);
        }
    }

    public void clear(Player player) {
        this.accessories.clear();
        for (AccessorySlot slot : AccessorySlot.getSlots()) {
            this.accessories.put(slot, new ArrayList<>());
        }
        if (!player.level.isClientSide)
            new SyncAccessoriesMessage(player.getId(), this.accessories).sendToLevel((ServerLevel) player.level);
    }

    public Map<AccessorySlot, Collection<Accessory>> getSlots() {
        return this.accessories;
    }

    public CompoundTag toNBT() {
        CompoundTag nbt = new CompoundTag();
        this.accessories.forEach((slot, list) -> {
            ListTag listNBT = new ListTag();
            for (Accessory accessory : list) {
                listNBT.add(StringTag.valueOf(Accessory.REGISTRY.getId(accessory).toString()));
            }
            nbt.put(slot.getName(), listNBT);
        });
        return nbt;
    }

    public void fromNBT(CompoundTag nbt) {
        this.accessories = new HashMap<>();
        for (AccessorySlot slot : AccessorySlot.getSlots()) {
            ListTag listNBT = nbt.getList(slot.getName(), 8);
            List<Accessory> accessories = new ArrayList<>();
            for (int i = 0; i < listNBT.size(); i++) {
                Accessory accessory = Accessory.REGISTRY.get(new ResourceLocation(listNBT.getString(i)));
                if (accessory != null) {
                    accessories.add(accessory);
                }
            }
            this.accessories.put(slot, accessories);
        }
    }

}
