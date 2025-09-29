package net.threetag.palladium.mixin.client;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.renderer.texture.HttpTexture;
import net.threetag.palladium.client.dynamictexture.ImageCache;
import net.threetag.palladium.client.dynamictexture.NativeImageCached;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.InputStream;

@Mixin(HttpTexture.class)
public class MixinCached implements NativeImageCached {

    @Unique
    private ImageCache palladium$nativeImage;

    @Inject(method = "load(Ljava/io/InputStream;)Lcom/mojang/blaze3d/platform/NativeImage;", at = @At("RETURN"))
    private void upload(InputStream stream, CallbackInfoReturnable<NativeImage> cir) {
        this.palladium$nativeImage = ImageCache.fromNativeImage(cir.getReturnValue());
    }

    @Override
    public ImageCache palladium$getImageCache() {
        return this.palladium$nativeImage;
    }
}
