package net.threetag.palladium.mixin;

import com.google.gson.JsonElement;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.threetag.palladium.addonpack.AddonObjectLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(SimpleJsonResourceReloadListener.class)
public class SimpleJsonResourceReloadListenerMixin {

    @Inject(method = "scanDirectory(Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/resources/FileToIdConverter;Lcom/mojang/serialization/DynamicOps;Lcom/mojang/serialization/Codec;Ljava/util/Map;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/packs/resources/Resource;openAsReader()Ljava/io/BufferedReader;"))
    private static <T> void scanDirectory(ResourceManager resourceManager, FileToIdConverter lister, DynamicOps<JsonElement> ops, Codec<T> codec, Map<ResourceLocation, T> output, CallbackInfo ci, @Local(ordinal = 1) ResourceLocation id) {
        AddonObjectLoader.ID_TO_SET = id;
    }

}
