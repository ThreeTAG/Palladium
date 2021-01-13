package net.threetag.threecore.network;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.threetag.threecore.ability.container.DefaultAbilityContainer;
import net.threetag.threecore.ability.container.IAbilityContainer;
import net.threetag.threecore.capability.CapabilityAbilityContainer;
import net.threetag.threecore.client.gui.toast.SuperpowerTimerToast;

import java.util.function.Supplier;

public class SendPlayerAbilityContainerMessage {

    private int entityID;
    public CompoundNBT nbt;

    public SendPlayerAbilityContainerMessage(int entityID, CompoundNBT nbt) {
        this.entityID = entityID;
        this.nbt = nbt;
    }

    public SendPlayerAbilityContainerMessage(PacketBuffer buffer) {
        this.entityID = buffer.readInt();
        this.nbt = buffer.readCompoundTag();
    }

    public void toBytes(PacketBuffer buffer) {
        buffer.writeInt(this.entityID);
        buffer.writeCompoundTag(this.nbt);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Entity entity = Minecraft.getInstance().world.getEntityByID(this.entityID);
            if (entity != null) {
                entity.getCapability(CapabilityAbilityContainer.MULTI_ABILITY_CONTAINER).ifPresent((a) -> {
                    if (a instanceof CapabilityAbilityContainer) {
                        ((CapabilityAbilityContainer) a).readUpdateTag(this.nbt);
                        if(entity == Minecraft.getInstance().player) {
                            for(IAbilityContainer container : a.getAllContainers()) {
                                if(container instanceof DefaultAbilityContainer && ((DefaultAbilityContainer) container).getMaxLifetime() >= 0) {
                                    SuperpowerTimerToast.add(container.getId());
                                }
                            }
                        }
                    }
                });
            }
        });
        ctx.get().setPacketHandled(true);
    }

}
