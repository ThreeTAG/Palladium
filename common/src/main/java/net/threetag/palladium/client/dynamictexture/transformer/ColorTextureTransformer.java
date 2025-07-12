package net.threetag.palladium.client.dynamictexture.transformer;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.FastColor;
import net.threetag.palladium.client.renderer.DynamicColor;
import net.threetag.palladium.util.context.DataContext;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.io.IOException;
import java.util.function.Function;

public record ColorTextureTransformer(DynamicColor rawColor, boolean ignoreBlank,
                                      DynamicColor filter) implements ITextureTransformer {

    @Override
    public NativeImage transform(DataContext context, NativeImage texture, ResourceManager manager, Function<String, String> stringConverter) throws IOException {
        var color = this.rawColor.getColor(context, stringConverter);
        var filter = this.filter != null ? this.filter.getColor(context, stringConverter) : null;

        for (int y = 0; y < texture.getHeight(); ++y) {
            for (int x = 0; x < texture.getWidth(); ++x) {
                blendPixel(texture, x, y, color, filter);
            }
        }

        return texture;
    }

    public void blendPixel(NativeImage texture, int x, int y, Color color, @Nullable Color filter) {
        if (texture.format() != NativeImage.Format.RGBA) {
            throw new UnsupportedOperationException("Can only call blendPixel with RGBA format");
        } else {
            int i = texture.getPixelRGBA(x, y);

            if (FastColor.ABGR32.alpha(i) == 0 && this.ignoreBlank) {
                return;
            }

            int origR = FastColor.ABGR32.red(i);
            int origG = FastColor.ABGR32.green(i);
            int origB = FastColor.ABGR32.blue(i);

            if (filter != null && !colorMatches(origR, origG, origB, filter)) {
                return;
            }

            float f = color.getAlpha() / 255.0F;
            float g = color.getBlue() / 255.0F;
            float h = color.getGreen() / 255.0F;
            float j = color.getRed() / 255.0F;
            // skip base texture alpha
            float l = (float) origB / 255.0F;
            float m = (float) origG / 255.0F;
            float n = (float) origR / 255.0F;

            float p = 1.0F - f;
            float r = g * f + l * p;
            float s = h * f + m * p;
            float t = j * f + n * p;

            if (r > 1.0F) {
                r = 1.0F;
            }

            if (s > 1.0F) {
                s = 1.0F;
            }

            if (t > 1.0F) {
                t = 1.0F;
            }

            int v = (int) (r * 255.0F);
            int w = (int) (s * 255.0F);
            int z = (int) (t * 255.0F);
            texture.setPixelRGBA(x, y, FastColor.ABGR32.color(FastColor.ABGR32.alpha(i), v, w, z));
        }
    }

    private static boolean colorMatches(int red, int green, int blue, Color color) {
        return color.getRed() == red && color.getGreen() == green && color.getBlue() == blue;
    }
}
