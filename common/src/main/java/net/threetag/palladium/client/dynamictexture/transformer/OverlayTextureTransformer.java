package net.threetag.palladium.client.dynamictexture.transformer;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.FastColor;
import net.threetag.palladium.addonpack.log.AddonPackLog;
import net.threetag.palladium.util.context.DataContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public final class OverlayTextureTransformer implements ITextureTransformer {

    private final String overlayLocation;
    private final boolean ignoreBlank;
    private final List<ResourceLocation> reported = new ArrayList<>();

    public OverlayTextureTransformer(String overlayLocation, boolean ignoreBlank) {
        this.overlayLocation = overlayLocation;
        this.ignoreBlank = ignoreBlank;
    }

    @Override
    public NativeImage transform(DataContext context, NativeImage texture, ResourceManager manager, Function<String, String> stringConverter) throws IOException {
        var overlayPath = new ResourceLocation(stringConverter.apply(this.overlayLocation));
        var overlayResource = manager.getResource(overlayPath);

        if (overlayResource.isEmpty()) {
            if (!this.reported.contains(overlayPath)) {
                this.reported.add(overlayPath);
                AddonPackLog.error("Missing overlay texture %s", overlayPath);
            }
            return texture;
        }

        NativeImage overlay = NativeImage.read(overlayResource.get().open());

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
            if (k == 0 & ignoreBlank) return;
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

            int u = (int) (q * 255.0F);
            int v = (int) (r * 255.0F);
            int w = (int) (s * 255.0F);
            int z = (int) (t * 255.0F);
            texture.setPixelRGBA(x, y, FastColor.ABGR32.color(u, v, w, z));
        }
    }

    public String overlayLocation() {
        return overlayLocation;
    }

    public boolean ignoreBlank() {
        return ignoreBlank;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (OverlayTextureTransformer) obj;
        return Objects.equals(this.overlayLocation, that.overlayLocation) &&
                this.ignoreBlank == that.ignoreBlank;
    }

    @Override
    public int hashCode() {
        return Objects.hash(overlayLocation, ignoreBlank);
    }

    @Override
    public String toString() {
        return "OverlayTextureTransformer[" +
                "overlayLocation=" + overlayLocation + ", " +
                "ignoreBlank=" + ignoreBlank + ']';
    }

}
