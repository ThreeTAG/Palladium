package net.threetag.palladium.client.icon;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.client.texture.TextureReference;
import net.threetag.palladium.data.DataContext;
import net.threetag.palladium.util.PalladiumCodecs;

import java.awt.*;

public class TexturedIcon implements Icon {

    public static final MapCodec<TexturedIcon> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance
            .group(
                    TextureReference.CODEC.fieldOf("texture").forGetter(TexturedIcon::getTexture),
                    PalladiumCodecs.COLOR_CODEC.optionalFieldOf("texture", Color.WHITE).forGetter(TexturedIcon::getTint)
            )
            .apply(instance, TexturedIcon::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, TexturedIcon> STREAM_CODEC = StreamCodec.composite(
            TextureReference.STREAM_CODEC, icon -> icon.texture,
            PalladiumCodecs.COLOR_STREAM_CODEC, icon -> icon.tint,
            TexturedIcon::new
    );

    public final TextureReference texture;
    public final Color tint;

    public TexturedIcon(TextureReference texture) {
        this(texture, Color.WHITE);
    }

    public TexturedIcon(TextureReference texture, Color tint) {
        this.texture = texture;
        this.tint = tint;
    }

    public TexturedIcon(ResourceLocation texture) {
        this(texture, Color.WHITE);
    }

    public TexturedIcon(ResourceLocation texture, Color tint) {
        this(TextureReference.normal(texture), tint);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void draw(Minecraft mc, GuiGraphics guiGraphics, DataContext context, int x, int y, int w, int h) {
        var texture = this.texture.withPath(context, "textures/icon/", ".png");
        var m = guiGraphics.pose().last().pose();

        var r = this.tint.getRed();
        var g = this.tint.getGreen();
        var b = this.tint.getBlue();
        var a = this.tint.getAlpha();

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(x, y, 0);
        guiGraphics.pose().scale(w / 16F, h / 16F, 1.0F);
        RenderSystem.setShaderColor(r / 255F, g / 255F, b / 255F, a / 255F);
        guiGraphics.blit(RenderType::guiTextured, texture, 0, 0, 0, 0, 16, 16, 16, 16);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        guiGraphics.pose().popPose();
    }

    public TextureReference getTexture() {
        return texture;
    }

    public Color getTint() {
        return tint;
    }

    @Override
    public IconSerializer<TexturedIcon> getSerializer() {
        return IconSerializers.TEXTURE.get();
    }

    @Override
    public String toString() {
        return "TexturedIcon{" +
                "texture=" + texture +
                ", tint=" + tint +
                '}';
    }

    public static class Serializer extends IconSerializer<TexturedIcon> {

        @Override
        public MapCodec<TexturedIcon> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, TexturedIcon> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
