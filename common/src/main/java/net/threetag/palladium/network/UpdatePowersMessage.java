package net.threetag.palladium.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.PowerManager;
import net.threetag.palladium.power.energybar.EnergyBar;
import net.threetag.palladium.power.energybar.EnergyBarReference;
import net.threetag.palladiumcore.network.MessageContext;
import net.threetag.palladiumcore.network.MessageS2C;
import net.threetag.palladiumcore.network.MessageType;
import org.apache.commons.lang3.tuple.Triple;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UpdatePowersMessage extends MessageS2C {

    private final int entityId;
    private final List<ResourceLocation> toRemove, toAdd;
    private final List<Triple<EnergyBarReference, Integer, Integer>> energyBars;

    public UpdatePowersMessage(LivingEntity livingEntity, List<ResourceLocation> toRemove, List<ResourceLocation> toAdd) {
        this.entityId = livingEntity.getId();
        this.toRemove = toRemove;
        this.toAdd = toAdd;

        this.energyBars = new ArrayList<>();

        var opt = PowerManager.getPowerHandler(livingEntity);

        if (opt.isPresent()) {
            for (IPowerHolder holder : opt.get().getPowerHolders().values()) {
                for (EnergyBar energyBar : holder.getEnergyBars().values()) {
                    this.energyBars.add(Triple.of(new EnergyBarReference(holder.getPower().getId(), energyBar.getConfiguration().getName()), energyBar.get(), energyBar.getMax()));
                }
            }
        }
    }

    public UpdatePowersMessage(FriendlyByteBuf buf) {
        this.entityId = buf.readInt();
        this.toRemove = buf.readList(FriendlyByteBuf::readResourceLocation);
        this.toAdd = buf.readList(FriendlyByteBuf::readResourceLocation);
        this.energyBars = buf.readList(buf1 -> {
            var ref = EnergyBarReference.fromBuffer(buf1);
            int val = buf1.readInt();
            int max = buf1.readInt();
            return Triple.of(ref, val, max);
        });
    }

    @Override
    public @NotNull MessageType getType() {
        return PalladiumNetwork.UPDATE_POWERS;
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(this.entityId);
        buf.writeCollection(this.toRemove, FriendlyByteBuf::writeResourceLocation);
        buf.writeCollection(this.toAdd, FriendlyByteBuf::writeResourceLocation);
        buf.writeCollection(this.energyBars, (buf1, pair) -> {
            pair.getLeft().toBuffer(buf1);
            buf1.writeInt(pair.getMiddle());
            buf1.writeInt(pair.getRight());
        });
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

            for (Triple<EnergyBarReference, Integer, Integer> pair : this.energyBars) {
                var bar = pair.getLeft().getEntry(livingEntity);

                if (bar != null) {
                    bar.set(pair.getMiddle());
                    bar.setMax(pair.getRight());
                }
            }
        }
    }
}
