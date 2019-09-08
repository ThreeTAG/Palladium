package net.threetag.threecore.abilities.network;

import net.threetag.threecore.abilities.Ability;
import net.threetag.threecore.abilities.AbilityHelper;
import net.threetag.threecore.abilities.AbilityType;
import net.threetag.threecore.abilities.IAbilityContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class AddAbilityMessage {

    public int entityID;
    public ResourceLocation containerId;
    public String abilityId;
    public CompoundNBT data;

    public AddAbilityMessage(int entityID, ResourceLocation containerId, String abilityId, CompoundNBT data) {
        this.entityID = entityID;
        this.containerId = containerId;
        this.abilityId = abilityId;
        this.data = data;
    }

    public AddAbilityMessage(PacketBuffer buffer) {
        this.entityID = buffer.readInt();
        this.containerId = new ResourceLocation(buffer.readString(64));
        this.abilityId = buffer.readString(32);
        this.data = buffer.readCompoundTag();
    }

    public void toBytes(PacketBuffer buffer) {
        buffer.writeInt(this.entityID);
        buffer.writeString(this.containerId.toString());
        buffer.writeString(this.abilityId);
        buffer.writeCompoundTag(this.data);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Entity entity = Minecraft.getInstance().world.getEntityByID(this.entityID);
            if (entity != null && entity instanceof LivingEntity) {
                IAbilityContainer container = AbilityHelper.getAbilityContainerFromId((LivingEntity) entity, this.containerId);

                if (container != null) {
                    AbilityType abilityType = AbilityType.REGISTRY.getValue(new ResourceLocation(this.data.getString("AbilityType")));

                    if (abilityType != null) {
                        Ability ability = abilityType.create();
                        ability.readUpdateTag(this.data);
                        container.addAbility((LivingEntity) entity, this.abilityId, ability);
                    }
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
