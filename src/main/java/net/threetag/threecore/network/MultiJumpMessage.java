package net.threetag.threecore.network;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;
import net.threetag.threecore.ability.Ability;
import net.threetag.threecore.ability.AbilityHelper;
import net.threetag.threecore.ability.container.IAbilityContainer;
import net.threetag.threecore.ability.MultiJumpAbility;

import java.util.function.Supplier;

public class MultiJumpMessage {

    public ResourceLocation containerId;
    public String abilityId;

    public MultiJumpMessage(ResourceLocation containerId, String abilityId) {
        this.containerId = containerId;
        this.abilityId = abilityId;
    }

    public MultiJumpMessage(PacketBuffer buffer) {
        this.containerId = new ResourceLocation(buffer.readString(32767));
        this.abilityId = buffer.readString(32767);
    }

    public void toBytes(PacketBuffer buffer) {
        buffer.writeString(this.containerId.toString());
        buffer.writeString(this.abilityId);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity player = ctx.get().getSender();
            if (player != null) {
                IAbilityContainer container = AbilityHelper.getAbilityContainerFromId(player, this.containerId);

                if (container != null) {
                    Ability ability = container.getAbilityMap().get(this.abilityId);

                    if (ability instanceof MultiJumpAbility) {
                        ((MultiJumpAbility) ability).jump(player);
                    }
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
