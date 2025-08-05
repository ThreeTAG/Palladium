package net.threetag.palladium.client.icon;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.threetag.palladium.data.DataContext;
import net.threetag.palladium.entity.PlayerSlot;

public record ItemInSlotIcon(PlayerSlot slot) implements Icon {

    public static final MapCodec<ItemInSlotIcon> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance
            .group(PlayerSlot.CODEC.fieldOf("slot").forGetter(ItemInSlotIcon::slot))
            .apply(instance, ItemInSlotIcon::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, ItemInSlotIcon> STREAM_CODEC = StreamCodec.composite(
            PlayerSlot.STREAM_CODEC, ItemInSlotIcon::slot, ItemInSlotIcon::new
    );

    @Override
    public void draw(Minecraft mc, GuiGraphics guiGraphics, DataContext context, int x, int y, int width, int height) {
        var stack = guiGraphics.pose();
        stack.pushMatrix();
        stack.translate(x + width / 2F, y + height / 2F);

        if (width != 16 || height != 16) {
            int s = Math.min(width, height);
            stack.scale(s / 16F, s / 16F);
        }

        var item = new ItemStack(Items.BARRIER);
        var items = this.slot.getItems(context.getLivingEntity());

        if (!items.isEmpty()) {
            var found = items.getFirst();

            if (!found.isEmpty()) {
                item = found;
            }
        }

        guiGraphics.renderItem(item, -width / 2, -height / 2);
        stack.popMatrix();
    }

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
