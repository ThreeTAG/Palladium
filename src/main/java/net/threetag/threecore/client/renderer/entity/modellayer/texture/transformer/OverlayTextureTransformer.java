package net.threetag.threecore.client.renderer.entity.modellayer.texture.transformer;

import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.util.function.Function;

public class OverlayTextureTransformer implements ITextureTransformer {

    private final String overlayLocation;

    public OverlayTextureTransformer(String overlayLocation) {
        this.overlayLocation = overlayLocation;
    }

    @Override
    public NativeImage transform(NativeImage texture, IResourceManager manager, Function<String, String> stringConverter) throws IOException {
        NativeImage overlay = NativeImage.read(manager.getResource(new ResourceLocation(stringConverter.apply(this.overlayLocation))).getInputStream());

        for (int y = 0; y < overlay.getHeight(); ++y) {
            for (int x = 0; x < overlay.getWidth(); ++x) {
                blendPixel(texture, x, y, overlay.getPixelRGBA(x, y));
            }
        }

        return texture;
    }


    public void blendPixel(NativeImage texture, int xIn, int yIn, int colIn) {
        if (texture.getFormat() != NativeImage.PixelFormat.RGBA) {
            throw new UnsupportedOperationException("Can only call blendPixel with RGBA format");
        } else {
            int i = texture.getPixelRGBA(xIn, yIn);
            float f = (float) NativeImage.getAlpha(colIn) / 255.0F;
            float f1 = (float) NativeImage.getBlue(colIn) / 255.0F;
            float f2 = (float) NativeImage.getGreen(colIn) / 255.0F;
            float f3 = (float) NativeImage.getRed(colIn) / 255.0F;
            float f4 = (float) NativeImage.getAlpha(i) / 255.0F;
            float f5 = (float) NativeImage.getBlue(i) / 255.0F;
            float f6 = (float) NativeImage.getGreen(i) / 255.0F;
            float f7 = (float) NativeImage.getRed(i) / 255.0F;
            float f8 = 1.0F - f;
            float f9 = f * f + f4 * f8;
            float f10 = f1 * f + f5 * f8;
            float f11 = f2 * f + f6 * f8;
            float f12 = f3 * f + f7 * f8;
            if (f9 > 1.0F) {
                f9 = 1.0F;
            }

            if (f10 > 1.0F) {
                f10 = 1.0F;
            }

            if (f11 > 1.0F) {
                f11 = 1.0F;
            }

            if (f12 > 1.0F) {
                f12 = 1.0F;
            }

            int j = (int)(f9 * 255.0F);
            int k = (int)(f10 * 255.0F);
            int l = (int)(f11 * 255.0F);
            int i1 = (int)(f12 * 255.0F);
            texture.setPixelRGBA(xIn, yIn, NativeImage.getCombined(j, k, l, i1));
        }
    }
}
