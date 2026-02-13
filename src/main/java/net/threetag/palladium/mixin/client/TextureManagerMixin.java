package net.threetag.palladium.mixin.client;

import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.threetag.palladium.client.texture.transformer.TransformedTexture;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mixin(TextureManager.class)
public abstract class TextureManagerMixin {

    @Shadow
    @Final
    public Map<Identifier, AbstractTexture> byPath;

    @Shadow
    public abstract void release(Identifier id);

    @Inject(method = "reload", at = @At("HEAD"))
    private void reload(PreparableReloadListener.SharedState sharedState, Executor executor, PreparableReloadListener.PreparationBarrier barrier, Executor executor1, CallbackInfoReturnable<CompletableFuture<Void>> cir) {
        List<Identifier> toRemove = new ArrayList<>();
        for (Map.Entry<Identifier, AbstractTexture> e : this.byPath.entrySet()) {
            if (e.getValue() instanceof TransformedTexture) {
                toRemove.add(e.getKey());
            }
        }

        for (Identifier id : toRemove) {
            this.release(id);
        }
    }

}
