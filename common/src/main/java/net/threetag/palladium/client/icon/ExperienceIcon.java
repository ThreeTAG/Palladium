package net.threetag.palladium.client.icon;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.logic.context.DataContext;

public record ExperienceIcon(int amount, boolean level) implements Icon {

    public static final MapCodec<ExperienceIcon> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance
            .group(
                    ExtraCodecs.NON_NEGATIVE_INT.fieldOf("amount").forGetter(ExperienceIcon::amount),
                    Codec.BOOL.optionalFieldOf("level", true).forGetter(ExperienceIcon::level)
            )
            .apply(instance, ExperienceIcon::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, ExperienceIcon> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, ExperienceIcon::amount,
            ByteBufCodecs.BOOL, ExperienceIcon::level,
            ExperienceIcon::new
    );

    private static final TexturedIcon BACKGROUND_ICON = new TexturedIcon(Palladium.id("experience"));

    @Override
    public void draw(Minecraft mc, GuiGraphics guiGraphics, DataContext context, int x, int y, int width, int height) {
        BACKGROUND_ICON.draw(mc, guiGraphics, context, x, y, width, height);

        var stack = guiGraphics.pose();
        stack.pushMatrix();
        stack.translate(x, y);

        if (width != 16 || height != 16) {
            stack.scale(width / 16F, height / 16F);
        }

        String text = this.amount + (this.level ? "L" : "");
        guiGraphics.drawString(mc.font, text, 9, 8, 0, false);
        guiGraphics.drawString(mc.font, text, 7, 8, 0, false);
        guiGraphics.drawString(mc.font, text, 8, 9, 0, false);
        guiGraphics.drawString(mc.font, text, 8, 7, 0, false);
        guiGraphics.drawString(mc.font, text, 8, 8, 8453920, false);

        stack.popMatrix();
    }

    @Override
    public IconSerializer<ExperienceIcon> getSerializer() {
        return IconSerializers.EXPERIENCE.get();
    }

    @Override
    public String toString() {
        return "ExperienceIcon{" +
                "amount=" + amount +
                ", level=" + level +
                '}';
    }

    public static class Serializer extends IconSerializer<ExperienceIcon> {

        @Override
        public MapCodec<ExperienceIcon> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ExperienceIcon> streamCodec() {
            return STREAM_CODEC;
        }

    }
}
