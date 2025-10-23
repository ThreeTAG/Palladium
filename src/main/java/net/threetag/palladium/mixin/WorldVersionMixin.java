package net.threetag.palladium.mixin;

import net.minecraft.WorldVersion;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackFormat;
import net.threetag.palladium.addonpack.AddonPackManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WorldVersion.Simple.class)
public class WorldVersionMixin {

    @Inject(method = "packVersion", at = @At("HEAD"), cancellable = true)
    public void packVersion(PackType packType, CallbackInfoReturnable<PackFormat> cir) {
        if(packType == AddonPackManager.getPackType()) {
            cir.setReturnValue(AddonPackManager.PACK_FORMAT);
        }
    }

}
