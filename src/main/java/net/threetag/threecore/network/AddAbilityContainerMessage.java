package net.threetag.threecore.network;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.threetag.threecore.ability.container.AbilityContainerTypes;
import net.threetag.threecore.ability.container.IAbilityContainer;
import net.threetag.threecore.capability.CapabilityAbilityContainer;
import net.threetag.threecore.client.gui.toast.SuperpowerTimerToast;

import java.util.function.Supplier;

public class AddAbilityContainerMessage {

    private final int entityID;
    private final CompoundNBT nbt;

    public AddAbilityContainerMessage(int entityID, CompoundNBT nbt) {
        this.entityID = entityID;
        this.nbt = nbt;
    }

    public AddAbilityContainerMessage(PacketBuffer buffer) {
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
            if (entity instanceof LivingEntity) {
                IAbilityContainer container = AbilityContainerTypes.deserialize(this.nbt, false);

                if (container != null) {
                    entity.getCapability(CapabilityAbilityContainer.MULTI_ABILITY_CONTAINER).ifPresent(multiContainer -> {
                        multiContainer.addContainer((LivingEntity) entity, container);
                        if(entity == Minecraft.getInstance().player) {
                            SuperpowerTimerToast.add(container.getId());
                        }
                    });
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
