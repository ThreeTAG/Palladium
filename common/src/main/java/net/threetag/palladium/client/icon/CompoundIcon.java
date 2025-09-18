package net.threetag.palladium.client.icon;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.threetag.palladium.logic.context.DataContext;

import java.util.List;

public record CompoundIcon(List<Icon> icons) implements Icon {

    public static final MapCodec<CompoundIcon> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance
            .group(Icon.CODEC.listOf().fieldOf("icons").forGetter(CompoundIcon::icons))
            .apply(instance, CompoundIcon::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, CompoundIcon> STREAM_CODEC = StreamCodec.composite(
            Icon.STREAM_CODEC.apply(ByteBufCodecs.list()), CompoundIcon::icons, CompoundIcon::new
    );

    @Override
    public void draw(Minecraft mc, GuiGraphics guiGraphics, DataContext context, int x, int y, int width, int height) {
        for (Icon icon : this.icons) {
            icon.draw(mc, guiGraphics, context, x, y, width, height);
        }
    }

    @Override
    public IconSerializer<CompoundIcon> getSerializer() {
        return IconSerializers.COMPOUND.get();
    }

    public static class Serializer extends IconSerializer<CompoundIcon> {

        @Override
        public MapCodec<CompoundIcon> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, CompoundIcon> streamCodec() {
            return STREAM_CODEC;
        }

    }
}
