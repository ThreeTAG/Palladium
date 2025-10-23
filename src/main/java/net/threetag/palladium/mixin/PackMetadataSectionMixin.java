package net.threetag.palladium.mixin;

import com.mojang.serialization.Codec;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.MetadataSectionType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.threetag.palladium.addonpack.AddonPackManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PackMetadataSection.class)
public abstract class PackMetadataSectionMixin {

    @Shadow
    private static Codec<PackMetadataSection> codecForPackType(PackType packType) {
        return null;
    }

    @Inject(method = "forPackType", at = @At("HEAD"), cancellable = true)
    private static void forPackType(PackType packType, CallbackInfoReturnable<MetadataSectionType<PackMetadataSection>> cir) {
        if (packType == AddonPackManager.getPackType()) {
            cir.setReturnValue(AddonPackManager.ADDON_TYPE);
        }
    }

}
