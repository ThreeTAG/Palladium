package net.threetag.palladium.client.dynamictexture.transformer;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.threetag.palladium.addonpack.log.AddonPackLog;
import net.threetag.palladium.util.context.DataContext;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public final class AlphaMaskTextureTransformer implements ITextureTransformer {

    private final String maskLocation;
    private final List<ResourceLocation> reported = new ArrayList<>();

    public AlphaMaskTextureTransformer(String maskLocation) {
        this.maskLocation = maskLocation;
    }

    @Override
    public NativeImage transform(DataContext context, NativeImage texture, ResourceManager manager, Function<String, String> stringConverter) throws IOException {
        var overlayPath = new ResourceLocation(stringConverter.apply(this.maskLocation));
        var overlayResource = manager.getResource(overlayPath);

        if (overlayResource.isEmpty()) {
            if (!this.reported.contains(overlayPath)) {
                this.reported.add(overlayPath);
                AddonPackLog.error("Missing alpha mask texture %s", overlayPath);
            }
            return texture;
        }

        NativeImage overlay = NativeImage.read(overlayResource.get().open());

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
