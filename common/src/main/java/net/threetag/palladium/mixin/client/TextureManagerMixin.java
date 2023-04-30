package net.threetag.palladium.mixin.client;

import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.Tickable;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.client.dynamictexture.transformer.TransformedTexture;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@SuppressWarnings("SuspiciousMethodCalls")
@Mixin(TextureManager.class)
public abstract class TextureManagerMixin {

    @Shadow
    @Final
    public Map<ResourceLocation, AbstractTexture> byPath;

    @Shadow
    @Final
    private Set<Tickable> tickableTextures;

    @Shadow
    protected abstract void safeClose(ResourceLocation path, AbstractTexture texture);

    @Inject(method = "preload", at = @At("RETURN"))
    private void preload(ResourceLocation path, Executor backgroundExecutor, CallbackInfoReturnable<CompletableFuture<Void>> cir) {
        List<ResourceLocation> toRemove = new ArrayList<>();
        for (Map.Entry<ResourceLocation, AbstractTexture> entry : this.byPath.entrySet()) {
            if (entry.getValue() instanceof TransformedTexture) {
                toRemove.add(entry.getKey());
            }
        }

        for (ResourceLocation resourceLocation : toRemove) {
            var abstractTexture = this.byPath.get(resourceLocation);
            if (abstractTexture != null && abstractTexture != MissingTextureAtlasSprite.getTexture()) {
                this.tickableTextures.remove(abstractTexture);
                this.safeClose(path, abstractTexture);
                this.byPath.remove(resourceLocation);
            }
        }
    }

}
