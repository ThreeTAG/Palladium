package com.threetag.threecore.abilities.network;

import com.threetag.threecore.abilities.Ability;
import com.threetag.threecore.abilities.AbilityHelper;
import com.threetag.threecore.abilities.AbilityType;
import com.threetag.threecore.abilities.IAbilityContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageAddAbility {

    public int entityID;
    public ResourceLocation containerId;
    public String abilityId;
    public NBTTagCompound data;

    public MessageAddAbility(int entityID, ResourceLocation containerId, String abilityId, NBTTagCompound data) {
        this.entityID = entityID;
        this.containerId = containerId;
        this.abilityId = abilityId;
        this.data = data;
    }

    public MessageAddAbility(PacketBuffer buffer) {
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
            if (entity != null && entity instanceof EntityLivingBase) {
                IAbilityContainer container = AbilityHelper.getAbilityContainerFromId((EntityLivingBase) entity, this.containerId);

                if (container != null) {
                    AbilityType abilityType = AbilityType.REGISTRY.getValue(new ResourceLocation(this.data.getString("AbilityType")));

                    if (abilityType != null) {
                        Ability ability = abilityType.create();
                        ability.readUpdateTag(this.data);
                        container.addAbility((EntityLivingBase) entity, this.abilityId, ability);
                    }
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
