package net.threetag.palladium.mixin;

import net.minecraft.server.packs.metadata.MetadataSectionType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.neoforged.neoforge.resource.EmptyPackResources;
import net.threetag.palladium.addonpack.AddonPackManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EmptyPackResources.class)
public class EmptyPackResourcesMixin {

    @Shadow
    @Final
    private PackMetadataSection packMeta;

    @Inject(method = "getMetadataSection", at = @At("HEAD"), cancellable = true)
    public void getMetadataSection(MetadataSectionType type, CallbackInfoReturnable<Object> cir) {
        if (type.equals(AddonPackManager.ADDON_TYPE)) {
            cir.setReturnValue(this.packMeta);
        }
    }

}
