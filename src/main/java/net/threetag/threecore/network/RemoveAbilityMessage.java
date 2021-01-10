package net.threetag.threecore.network;

import net.threetag.threecore.ability.AbilityHelper;
import net.threetag.threecore.ability.container.IAbilityContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class RemoveAbilityMessage {

    public int entityID;
    public ResourceLocation containerId;
    public String abilityId;

    public RemoveAbilityMessage(int entityID, ResourceLocation containerId, String abilityId) {
        this.entityID = entityID;
        this.containerId = containerId;
        this.abilityId = abilityId;
    }

    public RemoveAbilityMessage(PacketBuffer buffer) {
        this.entityID = buffer.readInt();
        this.containerId = new ResourceLocation(buffer.readString(32767));
        this.abilityId = buffer.readString(32767);
    }

    public void toBytes(PacketBuffer buffer) {
        buffer.writeInt(this.entityID);
        buffer.writeString(this.containerId.toString());
        buffer.writeString(this.abilityId);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Entity entity = Minecraft.getInstance().world.getEntityByID(this.entityID);
            if (entity != null && entity instanceof LivingEntity) {
                IAbilityContainer container = AbilityHelper.getAbilityContainerFromId((LivingEntity) entity, this.containerId);

                if (container != null) {
                    container.removeAbility((LivingEntity) entity, this.abilityId);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
