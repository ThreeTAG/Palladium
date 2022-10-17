package net.threetag.palladium.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.threetag.palladium.power.PowerManager;
import net.threetag.palladiumcore.network.MessageContext;
import net.threetag.palladiumcore.network.MessageS2C;
import net.threetag.palladiumcore.network.MessageType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UpdatePowersMessage extends MessageS2C {

    private final int entityId;
    private final List<ResourceLocation> toRemove, toAdd;

    public UpdatePowersMessage(int entityId, List<ResourceLocation> toRemove, List<ResourceLocation> toAdd) {
        this.entityId = entityId;
        this.toRemove = toRemove;
        this.toAdd = toAdd;
    }

    public UpdatePowersMessage(FriendlyByteBuf buf) {
        this.entityId = buf.readInt();
        this.toRemove = new ArrayList<>();
        this.toAdd = new ArrayList<>();

        int amount = buf.readInt();
        for (int i = 0; i < amount; i++) {
            this.toRemove.add(buf.readResourceLocation());
        }

        amount = buf.readInt();
        for (int i = 0; i < amount; i++) {
            this.toAdd.add(buf.readResourceLocation());
        }
    }

    @Override
    public MessageType getType() {
        return PalladiumNetwork.UPDATE_POWERS;
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(this.entityId);
        buf.writeInt(this.toRemove.size());

        for (ResourceLocation id : this.toRemove) {
            buf.writeResourceLocation(id);
        }

        buf.writeInt(this.toAdd.size());

        for (ResourceLocation id : this.toAdd) {
            buf.writeResourceLocation(id);
        }
    }

    @Override
    public void handle(MessageContext context) {
        this.handleClient();
    }

    @Environment(EnvType.CLIENT)
    public void handleClient() {
        Level level = Minecraft.getInstance().level;
        if (level != null && level.getEntity(this.entityId) instanceof LivingEntity livingEntity) {
            PowerManager manager = PowerManager.getInstance(level);
            var toRemove = this.toRemove.stream().map(manager::getPower).filter(Objects::nonNull).toList();
            var toAdd = this.toAdd.stream().map(manager::getPower).filter(Objects::nonNull).toList();
            PowerManager.getPowerHandler(livingEntity).ifPresent(handler -> handler.removeAndAddPowers(toRemove, toAdd));
        }
    }
}
