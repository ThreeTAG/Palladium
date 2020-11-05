package net.threetag.threecore.network;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;
import net.threetag.threecore.capability.CapabilityAbilityContainer;

import java.util.function.Supplier;

public class RemoveAbilityContainerMessage {

    private final int entityID;
    private final ResourceLocation id;

    public RemoveAbilityContainerMessage(int entityID, ResourceLocation id) {
        this.entityID = entityID;
        this.id = id;
    }

    public RemoveAbilityContainerMessage(PacketBuffer buffer) {
        this.entityID = buffer.readInt();
        this.id = buffer.readResourceLocation();
    }

    public void toBytes(PacketBuffer buffer) {
        buffer.writeInt(this.entityID);
        buffer.writeResourceLocation(this.id);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Entity entity = Minecraft.getInstance().world.getEntityByID(this.entityID);
            if (entity instanceof LivingEntity) {
                entity.getCapability(CapabilityAbilityContainer.MULTI_ABILITY_CONTAINER).ifPresent(multiContainer -> {
                    multiContainer.removeContainer((LivingEntity) entity, this.id);
                });
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
