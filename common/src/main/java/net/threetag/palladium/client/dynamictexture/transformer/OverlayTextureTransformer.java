package net.threetag.palladium.client.dynamictexture.transformer;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.FastColor;

import java.io.IOException;
import java.util.function.Function;

public record OverlayTextureTransformer(String overlayLocation) implements ITextureTransformer {

    @Override
    public NativeImage transform(NativeImage texture, ResourceManager manager, Function<String, String> stringConverter) throws IOException {
        NativeImage overlay = NativeImage.read(manager.getResource(new ResourceLocation(stringConverter.apply(this.overlayLocation))).get().open());

        for (int y = 0; y < overlay.getHeight(); ++y) {
            for (int x = 0; x < overlay.getWidth(); ++x) {
                blendPixel(texture, x, y, overlay.getPixelRGBA(x, y));
            }
        }

        return texture;
    }


    public void blendPixel(NativeImage texture, int x, int y, int abgrColor) {
        if (texture.format() != NativeImage.Format.RGBA) {
            throw new UnsupportedOperationException("Can only call blendPixel with RGBA format");
        } else {
            int i = texture.getPixelRGBA(x, y);
            float f = (float) FastColor.ABGR32.alpha(abgrColor) / 255.0F;
            float g = (float) FastColor.ABGR32.blue(abgrColor) / 255.0F;
            float h = (float) FastColor.ABGR32.green(abgrColor) / 255.0F;
            float j = (float) FastColor.ABGR32.red(abgrColor) / 255.0F;
            float k = (float) FastColor.ABGR32.alpha(i) / 255.0F;
            float l = (float) FastColor.ABGR32.blue(i) / 255.0F;
            float m = (float) FastColor.ABGR32.green(i) / 255.0F;
            float n = (float) FastColor.ABGR32.red(i) / 255.0F;
            float p = 1.0F - f;
            float q = f * f + k * p;
            float r = g * f + l * p;
            float s = h * f + m * p;
            float t = j * f + n * p;
            if (q > 1.0F) {
                q = 1.0F;
            }

            if (r > 1.0F) {
                r = 1.0F;
            }

            if (s > 1.0F) {
                s = 1.0F;
            }

            if (t > 1.0F) {
                t = 1.0F;
            }

            int u = (int)(q * 255.0F);
            int v = (int)(r * 255.0F);
            int w = (int)(s * 255.0F);
            int z = (int)(t * 255.0F);
            texture.setPixelRGBA(x, y, FastColor.ABGR32.color(u, v, w, z));
        }
    }
}
