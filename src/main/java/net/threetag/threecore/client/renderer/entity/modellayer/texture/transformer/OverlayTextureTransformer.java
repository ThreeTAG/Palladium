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
                texture.blendPixel(x, y, overlay.getPixelRGBA(x, y));
            }
        }

        return texture;
    }

}
