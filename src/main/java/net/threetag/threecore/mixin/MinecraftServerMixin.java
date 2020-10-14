package net.threetag.threecore.mixin;

import net.minecraft.resources.ResourcePackList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.datafix.codec.DatapackCodec;
import net.threetag.threecore.addonpacks.AddonPackManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

    @Inject(at = @At("HEAD"), method = "func_240772_a_(Lnet/minecraft/resources/ResourcePackList;Lnet/minecraft/util/datafix/codec/DatapackCodec;Z)Lnet/minecraft/util/datafix/codec/DatapackCodec;")
    private static void func_240772_a_(ResourcePackList p_240772_0_, DatapackCodec p_240772_1_, boolean p_240772_2_, CallbackInfoReturnable<DatapackCodec> callbackInfoReturnable) {
        p_240772_0_.addPackFinder(new AddonPackManager.AddonPackFinder());
    }

}
