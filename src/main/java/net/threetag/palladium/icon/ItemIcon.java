package net.threetag.palladium.icon;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public class ItemIcon implements Icon {

    public static final MapCodec<ItemIcon> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance
            .group(ItemStack.CODEC.fieldOf("item").forGetter(ItemIcon::getItem))
            .apply(instance, ItemIcon::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, ItemIcon> STREAM_CODEC = StreamCodec.composite(
            ItemStack.STREAM_CODEC, ItemIcon::getItem, ItemIcon::new
    );

    public final ItemStack stack;

    public ItemIcon(ItemStack stack) {
        this.stack = stack;
    }

    public ItemIcon(ItemLike itemLike) {
        this.stack = new ItemStack(itemLike);
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
    }

}
