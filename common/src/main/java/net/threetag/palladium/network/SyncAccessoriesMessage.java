package net.threetag.palladium.network;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.threetag.palladium.accessory.Accessory;
import net.threetag.palladium.accessory.AccessorySlot;
import net.threetag.palladium.client.screen.AccessoryScreen;

import java.util.*;

public class SyncAccessoriesMessage extends BaseS2CMessage {

    public int entityId;
    public Map<AccessorySlot, Collection<Accessory>> accessories;

    public SyncAccessoriesMessage(int entityId, Map<AccessorySlot, Collection<Accessory>> accessories) {
        this.entityId = entityId;
        this.accessories = accessories;
    }

    public SyncAccessoriesMessage(FriendlyByteBuf buf) {
        this.entityId = buf.readInt();
        int amount = buf.readInt();
        this.accessories = new HashMap<>();
        for (int i = 0; i < amount; i++) {
            AccessorySlot slot = AccessorySlot.getSlotByName(buf.readUtf());
            List<Accessory> accessories1 = new ArrayList<>();
            int slotAmount = buf.readInt();
            for (int j = 0; j < slotAmount; j++) {
                Accessory accessory = Accessory.REGISTRY.get(buf.readResourceLocation());
                if (accessory != null) {
                    accessories1.add(accessory);
                }
            }
            this.accessories.put(slot, accessories1);
        }
    }

    @Override
    public MessageType getType() {
        return PalladiumNetwork.SYNC_ACCESSORIES;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.entityId);
        buf.writeInt(this.accessories.size());

        this.accessories.forEach((slot, accessories) -> {
            buf.writeUtf(slot.getName());
            buf.writeInt(accessories.size());
            for (Accessory accessory : accessories) {
                buf.writeResourceLocation(Accessory.REGISTRY.getId(accessory));
            }
        });
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        context.queue(() -> {
            Entity entity = Objects.requireNonNull(Minecraft.getInstance().level).getEntity(this.entityId);

            if (entity instanceof AbstractClientPlayer player) {
                Accessory.getPlayerData(player).ifPresent(data -> {
                    data.clear(player);
                    this.accessories.forEach((slot, accessories) -> {
                        for (Accessory accessory : accessories) {
                            data.enable(slot, accessory, player);
                        }
                    });
                    if (Minecraft.getInstance().screen instanceof AccessoryScreen) {
                        ((AccessoryScreen) Minecraft.getInstance().screen).accessoryList.refreshList();
                    }
                });
            }
        });
    }
}
