package net.threetag.palladium.power.energybar;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.threetag.palladium.power.PowerHolder;

public record EnergyBarUsage(EnergyBarReference energyBar, int amount) {

    public static final Codec<EnergyBarUsage> CODEC = RecordCodecBuilder.create(instance ->
        instance.group(
                EnergyBarReference.CODEC.fieldOf("energy_bar").forGetter(EnergyBarUsage::energyBar),
                Codec.INT.fieldOf("amount").forGetter(EnergyBarUsage::amount)
        ).apply(instance, EnergyBarUsage::new)
    );

    public static final StreamCodec<FriendlyByteBuf, EnergyBarUsage> STREAM_CODEC = StreamCodec.composite(EnergyBarReference.STREAM_CODEC, EnergyBarUsage::energyBar, ByteBufCodecs.INT, EnergyBarUsage::amount, EnergyBarUsage::new);

    public void consume(PowerHolder holder) {
        var energyBar = this.energyBar.getBar(holder.getEntity(), holder);

        if (energyBar != null) {
            energyBar.add(-this.amount);
        }
    }
}
