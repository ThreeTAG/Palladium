package net.threetag.palladium.icon;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.EquipmentSlot;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
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

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Icon, ItemInSlotIcon> builder, HolderLookup.Provider provider) {
            builder.setName("Item in Slot").setDescription("Renders an item that's currently in the slot of the player")
                    .add("slot", TYPE_PLAYER_SLOT, "The slot that will be have its item displayed")
                    .addExampleObject(new ItemInSlotIcon(PlayerSlot.get(EquipmentSlot.CHEST)));
        }
    }

}
