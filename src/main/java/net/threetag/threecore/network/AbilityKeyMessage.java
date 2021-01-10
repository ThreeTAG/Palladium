package net.threetag.threecore.network;

import net.threetag.threecore.ability.Ability;
import net.threetag.threecore.ability.AbilityHelper;
import net.threetag.threecore.ability.container.IAbilityContainer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class AbilityKeyMessage {

    public ResourceLocation containerId;
    public String abilityId;
    public boolean pressed;

    public AbilityKeyMessage(ResourceLocation containerId, String abilityId, boolean pressed) {
        this.containerId = containerId;
        this.abilityId = abilityId;
        this.pressed = pressed;
    }

    public AbilityKeyMessage(PacketBuffer buffer) {
        this.containerId = new ResourceLocation(buffer.readString(32767));
        this.abilityId = buffer.readString(32767);
        this.pressed = buffer.readBoolean();
    }

    public void toBytes(PacketBuffer buffer) {
        buffer.writeString(this.containerId.toString());
        buffer.writeString(this.abilityId);
        buffer.writeBoolean(this.pressed);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            IAbilityContainer container = AbilityHelper.getAbilityContainerFromId(ctx.get().getSender(), this.containerId);

            if (container != null) {
                Ability ability = container.getAbility(this.abilityId);

                if (ability != null && ability.getConditionManager().isUnlocked()) {
                    if (this.pressed) {
                        ability.getConditionManager().onKeyPressed();
                    } else
                        ability.getConditionManager().onKeyReleased();
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
