package net.threetag.palladium.client.dynamictexture.transformer;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;

import java.awt.*;
import java.io.IOException;
import java.util.function.Function;

public record AlphaMaskTextureTransformer(String maskLocation) implements ITextureTransformer {

    @Override
    public NativeImage transform(NativeImage texture, ResourceManager manager, Function<String, String> stringConverter) throws IOException {
        NativeImage overlay = NativeImage.read(manager.getResource(new ResourceLocation(stringConverter.apply(this.maskLocation))).get().open());

        for (int y = 0; y < overlay.getHeight(); ++y) {
            for (int x = 0; x < overlay.getWidth(); ++x) {
                int pixelOrig = texture.getPixelRGBA(x, y);
                int pixelMask = overlay.getPixelRGBA(x, y);
                if (pixelOrig != 0) {
                    // I suck with this so I use the colors
                    Color color = new Color(pixelMask, true);
                    Color colorOrig = new Color(pixelOrig, true);
                    float hue = 1F - (color.getRed() + color.getGreen() + color.getBlue()) / 3F / 255F;
                    int newAlpha = pixelMask == 0 ? 0 : (int) (colorOrig.getAlpha() * hue);
                    Color newColor = new Color(colorOrig.getRed(), colorOrig.getGreen(), colorOrig.getBlue(), newAlpha);
                    texture.setPixelRGBA(x, y, newColor.getRGB());
                }
            }
        }

        overlay.close();

        return texture;
    }
}
