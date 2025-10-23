package net.threetag.palladium.mixin;

import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackFormat;
import net.threetag.palladium.addonpack.AddonPackManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PackFormat.class)
public class PackFormatMixin {

    @Inject(method = "lastPreMinorVersion", at = @At("HEAD"), cancellable = true)
    private static void lastPreMinorVersion(PackType type, CallbackInfoReturnable<Integer> cir) {
        if (type == AddonPackManager.getPackType()) {
            cir.setReturnValue(1);
        }
    }

}
