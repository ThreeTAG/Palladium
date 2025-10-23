package net.threetag.palladium.mixin;

import net.minecraft.server.packs.OverlayMetadataSection;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.MetadataSectionType;
import net.threetag.palladium.addonpack.AddonPackManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(OverlayMetadataSection.class)
public class OverlayMetadataSectionMixin {

    @Unique
    private static final MetadataSectionType<OverlayMetadataSection> ADDON_TYPE = new MetadataSectionType<>("overlays", OverlayMetadataSection.codecForPackType(AddonPackManager.getPackType()));
    @Unique
    private static final MetadataSectionType<OverlayMetadataSection> NEOFORGE_ADDON_TYPE = new MetadataSectionType<>("neoforge:overlays", ADDON_TYPE.codec());

    @Inject(method = "forPackType", at = @At("HEAD"), cancellable = true)
    private static void forPackType(PackType packType, CallbackInfoReturnable<MetadataSectionType<OverlayMetadataSection>> cir) {
        if (packType == AddonPackManager.getPackType()) {
            cir.setReturnValue(ADDON_TYPE);
        }
    }

    @Inject(method = "forPackTypeNeoForge", at = @At("HEAD"), cancellable = true)
    private static void forPackTypeNeoForge(PackType packType, CallbackInfoReturnable<MetadataSectionType<OverlayMetadataSection>> cir) {
        if (packType == AddonPackManager.getPackType()) {
            cir.setReturnValue(NEOFORGE_ADDON_TYPE);
        }
    }

}
