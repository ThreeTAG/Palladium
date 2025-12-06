package net.threetag.palladium.client.dynamictexture;

import com.mojang.blaze3d.platform.NativeImage;

public class ImageCache {

    private final int width;
    private final int height;
    private final int[][] pixels;

    public ImageCache(int width, int height) {
        this.width = width;
        this.height = height;
        this.pixels = new int[width][height];
    }

    public int getPixel(int x, int y) {
        return this.pixels[x][y];
    }

    public void setPixel(int x, int y, int color) {
        this.pixels[x][y] = color;
    }

    public static ImageCache fromNativeImage(NativeImage image) {
        ImageCache cache = new ImageCache(image.getWidth(), image.getHeight());
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                cache.setPixel(x, y, image.getPixelRGBA(x, y));
            }
        }
        return cache;
    }

    public NativeImage toNativeImage() {
        NativeImage image = new NativeImage(NativeImage.Format.RGBA, this.width, this.height, false);
        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                image.setPixelRGBA(x, y, this.getPixel(x, y));
            }
        }
        return image;
    }

}
