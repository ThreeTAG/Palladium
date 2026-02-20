package net.threetag.palladium.icon;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.util.PalladiumCodecs;

public record ItemIcon(ItemStack stack) implements Icon {

    public static final MapCodec<ItemIcon> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance
            .group(PalladiumCodecs.SIMPLE_ITEM_STACK.fieldOf("item").forGetter(ItemIcon::getItem))
            .apply(instance, ItemIcon::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, ItemIcon> STREAM_CODEC = StreamCodec.composite(
            ItemStack.STREAM_CODEC, ItemIcon::getItem, ItemIcon::new
    );

    public ItemIcon(ItemLike itemLike) {
        this(itemLike.asItem().getDefaultInstance());
    }

    @Override
    public IconSerializer<ItemIcon> getSerializer() {
        return IconSerializers.ITEM.get();
    }

    public ItemStack getItem() {
        return this.stack;
    }

    @Override
    public String toString() {
        return "ItemIcon{" + "stack=" + stack + '}';
    }

    public static class Serializer extends IconSerializer<ItemIcon> {

        @Override
        public MapCodec<ItemIcon> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ItemIcon> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Icon, ItemIcon> builder, HolderLookup.Provider provider) {
            builder.setName("Item").setDescription("Displays an item")
                    .add("item", TYPE_ITEM_STACK, "The item that will be displayed")
                    .addExampleObject(new ItemIcon(Items.APPLE));
        }
    }

}
