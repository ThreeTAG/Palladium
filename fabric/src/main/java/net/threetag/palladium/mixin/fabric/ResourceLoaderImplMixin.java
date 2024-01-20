package net.threetag.palladium.mixin.fabric;

import net.threetag.palladium.loot.LootTableModificationManager;
import org.quiltmc.qsl.resource.loader.impl.ResourceLoaderImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@SuppressWarnings({"rawtypes", "UnstableApiUsage"})
@Mixin(ResourceLoaderImpl.class)
public class ResourceLoaderImplMixin {

    // Reeeaallly ugly fix to get Quilt to load reload listener for more than 2 pack types

    @Inject(method = "sort(Ljava/util/List;)V", at = @At("RETURN"), remap = false)
    private void sort(List reloaders, CallbackInfo ci) {
        reloaders.add(0, LootTableModificationManager.getInstance());
    }
}
