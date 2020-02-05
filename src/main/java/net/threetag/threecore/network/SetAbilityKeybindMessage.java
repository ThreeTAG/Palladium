package net.threetag.threecore.network;

import net.threetag.threecore.ability.Ability;
import net.threetag.threecore.ability.AbilityHelper;
import net.threetag.threecore.ability.IAbilityContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SetAbilityKeybindMessage {

    public ResourceLocation containerId;
    public String abilityId;
    public int keyBind;

    public SetAbilityKeybindMessage(ResourceLocation containerId, String abilityId, int keyBind) {
        this.containerId = containerId;
        this.abilityId = abilityId;
        this.keyBind = keyBind;
    }

    public SetAbilityKeybindMessage(PacketBuffer buffer) {
        this.containerId = new ResourceLocation(buffer.readString(64));
        this.abilityId = buffer.readString(32);
        this.keyBind = buffer.readInt();
    }

    public void toBytes(PacketBuffer buffer) {
        buffer.writeString(this.containerId.toString());
        buffer.writeString(this.abilityId);
        buffer.writeInt(this.keyBind);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity player = ctx.get().getSender();
            if (player != null) {
                IAbilityContainer container = AbilityHelper.getAbilityContainerFromId(player, this.containerId);

                if (container != null) {
                    Ability ability = container.getAbilityMap().get(this.abilityId);

                    if (ability != null) {
                        ability.getDataManager().set(Ability.KEYBIND, this.keyBind);
                    }
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
