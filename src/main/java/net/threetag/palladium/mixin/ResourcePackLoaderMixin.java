package net.threetag.palladium.mixin;

import com.mojang.serialization.Codec;
import net.minecraft.SharedConstants;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackSelectionConfig;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.MetadataSectionType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.util.InclusiveRange;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.resource.EmptyPackResources;
import net.neoforged.neoforge.resource.ResourcePackLoader;
import net.neoforged.neoforgespi.language.IModFileInfo;
import net.neoforged.neoforgespi.language.IModInfo;
import net.neoforged.neoforgespi.locating.IModFile;
import net.threetag.palladium.addonpack.AddonPackManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Mixin(ResourcePackLoader.class)
public abstract class ResourcePackLoaderMixin {

    @Unique
    private static final String MOD_ADDON_ID = "mod_addon";

    @Shadow
    private static Codec<PackMetadataSection> metadataCodecForPackType(PackType type) {
        return null;
    }

    @Shadow
    private static boolean hasResourcePack(IModFile mf) {
        return false;
    }

    @Unique
    private static final MetadataSectionType<PackMetadataSection> OPTIONAL_ADDON_FORMAT = new MetadataSectionType<>("pack", Objects.requireNonNull(metadataCodecForPackType(AddonPackManager.getPackType())));

    @Inject(method = "metadataTypeForPackType", at = @At("HEAD"), cancellable = true)
    private static void metadataTypeForPackType(PackType type, CallbackInfoReturnable<MetadataSectionType<PackMetadataSection>> cir) {
        if (type == AddonPackManager.getPackType()) {
            cir.setReturnValue(OPTIONAL_ADDON_FORMAT);
        }
    }

    @Inject(method = "makePack", at = @At("HEAD"), cancellable = true)
    private static void makePack(PackType packType, ArrayList<Pack> hiddenPacks, CallbackInfoReturnable<Pack> cir) {
        if (packType == AddonPackManager.getPackType()) {
            final String name = "Mod Addon";
            final String descriptionKey = packType == PackType.CLIENT_RESOURCES ? "fml.resources.modresources" : "fml.resources.moddata";
            cir.setReturnValue(Pack.readMetaAndCreate(
                    new PackLocationInfo(MOD_ADDON_ID, Component.literal(name), PackSource.DEFAULT, Optional.empty()),
                    new EmptyPackResources.EmptyResourcesSupplier(new PackMetadataSection(Component.translatable(descriptionKey, hiddenPacks.size()),
                            new InclusiveRange<>(SharedConstants.getCurrentVersion().packVersion(packType)))),
                    packType,
                    new PackSelectionConfig(true, Pack.Position.TOP, false)).withChildren(hiddenPacks));
        }
    }

    @Inject(method = "getPackNames", at = @At("HEAD"), cancellable = true)
    private static void getPackNames(PackType packType, CallbackInfoReturnable<List<String>> cir) {
        if (packType == AddonPackManager.getPackType()) {
            List<String> ids = new ArrayList<>(ModList.get().getModFiles().stream()
                    .filter(IModFileInfo::showAsDataPack)
                    .map(IModFileInfo::getFile)
                    .filter(ResourcePackLoaderMixin::hasResourcePack)
                    .map(mf -> "mod/" + mf.getModInfos().stream().map(IModInfo::getModId).collect(Collectors.joining()))
                    .toList());
            ids.add(MOD_ADDON_ID);
            cir.setReturnValue(ids);
        }
    }

}
