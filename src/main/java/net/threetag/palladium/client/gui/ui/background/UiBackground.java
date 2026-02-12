package net.threetag.palladium.client.gui.ui.background;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.Identifier;

import java.util.function.Function;

public abstract class UiBackground {

    public abstract void render(GuiGraphics guiGraphics, int x, int y, int width, int height);

    public abstract UiBackgroundSerializer<?> getSerializer();

    @Override
    public String toString() {
        return Codecs.CODEC.encodeStart(JsonOps.INSTANCE, this).getOrThrow().toString();
    }

    public static class Codecs {

        private static final Codec<UiBackground> DIRECT_CODEC = UiBackgroundSerializer.TYPE_CODEC.dispatch(UiBackground::getSerializer, UiBackgroundSerializer::codec);

        public static final Codec<UiBackground> CODEC = Codec.either(
                DIRECT_CODEC,
                Identifier.CODEC
        ).xmap(either -> either.map(
                Function.identity(),
                RepeatingTextureBackground::new
        ), bg -> bg instanceof RepeatingTextureBackground rep && rep.getWidth() == 16 && rep.getHeight() == 16 ? Either.right(rep.getTexture()) : Either.left(bg));

    }
}
