package net.threetag.palladium.mixin.client;

import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.ResourceKey;
import net.threetag.palladium.addonpack.AddonPackErrorScreenUtil;
import net.threetag.palladium.config.PalladiumClientConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

/**
 * Original: ThePotatoArchivist/DatapackErrorViewer, 2025, MIT-License <br>
 * <a href="https://github.com/ThePotatoArchivist/DatapackErrorViewer/blob/main/src/main/java/archives/tater/datapackerrors/mixin/CreateWorldScreenMixin.java">Repository</a>
 */
@Mixin(RegistryDataLoader.class)
public class RegistryDataLoaderMixin {

    @Inject(
            method = "load(Lnet/minecraft/resources/RegistryDataLoader$LoadingFunction;Ljava/util/List;Ljava/util/List;)Lnet/minecraft/core/RegistryAccess$Frozen;",
            at = @At("HEAD")
    )
    private static void clearErrors(CallbackInfoReturnable<RegistryAccess.Frozen> cir) {
        AddonPackErrorScreenUtil.ERRORS = null;
    }

    @ModifyArg(
            method = "load(Lnet/minecraft/resources/RegistryDataLoader$LoadingFunction;Ljava/util/List;Ljava/util/List;Z)Lnet/minecraft/core/RegistryAccess$Frozen;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/resources/RegistryDataLoader;logErrors(Ljava/util/Map;)Lnet/minecraft/ReportedException;"),
            index = 0
    )
    private static Map<ResourceKey<?>, Exception> getErrors(Map<ResourceKey<?>, Exception> errors) {
        if (PalladiumClientConfig.DEV_MODE.getAsBoolean()) {
            AddonPackErrorScreenUtil.ERRORS = errors;
        }
        return errors;
    }

}
