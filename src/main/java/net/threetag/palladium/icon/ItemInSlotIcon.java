package net.threetag.palladium.icon;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.threetag.palladium.entity.PlayerSlot;

public record ItemInSlotIcon(PlayerSlot slot) implements Icon {

    public static final MapCodec<ItemInSlotIcon> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance
            .group(PlayerSlot.CODEC.fieldOf("slot").forGetter(ItemInSlotIcon::slot))
            .apply(instance, ItemInSlotIcon::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, ItemInSlotIcon> STREAM_CODEC = StreamCodec.composite(
            PlayerSlot.STREAM_CODEC, ItemInSlotIcon::slot, ItemInSlotIcon::new
    );

    @Override
    public IconSerializer<ItemInSlotIcon> getSerializer() {
        return IconSerializers.ITEM_IN_SLOT.get();
    }

    @Override
    public String toString() {
        return "ItemIcon{" + "slot=" + this.slot.toString() + '}';
    }

    public static class Serializer extends IconSerializer<ItemInSlotIcon> {

        @Override
        public MapCodec<ItemInSlotIcon> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ItemInSlotIcon> streamCodec() {
            return STREAM_CODEC;
        }

    }

}
