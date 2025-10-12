package net.threetag.palladium.fabric.mixin.client;

import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.threetag.palladium.client.model.ModelLayerManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ReloadableResourceManager.class)
public class ReloadableResourceManagerMixin {

    @Shadow
    @Final
    private PackType type;

    @Shadow
    public List<PreparableReloadListener> listeners;

    @Inject(at = @At("RETURN"), method = "registerReloadListener")
    private void registerReloadListener(PreparableReloadListener listener, CallbackInfo callbackInfo) {
        if (this.type == PackType.CLIENT_RESOURCES && listener instanceof ModelManager) {
            this.listeners.add(new ModelLayerManager());
        }
    }

}
