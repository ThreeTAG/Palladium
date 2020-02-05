package net.threetag.threecore.client.renderer.texture;

import com.mojang.blaze3d.platform.TextureUtil;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class AlphaMaskTexture extends SimpleTexture {

    private final ResourceLocation maskLocation;

    public AlphaMaskTexture(ResourceLocation texture, ResourceLocation maskLocation) {
        super(texture);
        this.maskLocation = maskLocation;
    }

    @Override
    public void loadTexture(IResourceManager manager) throws IOException {
        deleteGlTexture();
        InputStream textureStream = null;
        InputStream maskStream = null;

        try {
            NativeImage image = NativeImage.read(textureStream = manager.getResource(textureLocation).getInputStream());
            NativeImage overlay = NativeImage.read(maskStream = manager.getResource(maskLocation).getInputStream());
            for (int y = 0; y < overlay.getHeight(); ++y) {
                for (int x = 0; x < overlay.getWidth(); ++x) {
                    int pixelOrig = image.getPixelRGBA(x, y);
                    int pixelMask = overlay.getPixelRGBA(x, y);
                    if (pixelOrig != 0) {
                        // I suck with this so I use the colors
                        Color color = new Color(pixelMask, true);
                        Color colorOrig = new Color(pixelOrig, true);
                        float hue = 1F - (color.getRed() + color.getGreen() + color.getBlue()) / 3F / 255F;
                        int newAlpha = pixelMask == 0 ? 0 : (int) (colorOrig.getAlpha() * hue);
                        Color newColor = new Color(colorOrig.getRed(), colorOrig.getGreen(), colorOrig.getBlue(), newAlpha);
                        image.setPixelRGBA(x, y, newColor.getRGB());
                    }
                }
            }

            TextureUtil.prepareImage(this.getGlTextureId(), image.getWidth(), image.getHeight());
            image.uploadTextureSub(0, 0, 0, false);
        } finally {
            if (textureStream != null) {
                textureStream.close();
            }

            if (maskStream != null) {
                maskStream.close();
            }
        }
    }

}
