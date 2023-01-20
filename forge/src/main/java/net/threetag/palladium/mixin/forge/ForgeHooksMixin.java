package net.threetag.palladium.mixin.forge;

import com.google.gson.JsonObject;
import net.minecraft.util.GsonHelper;
import net.minecraftforge.common.ForgeHooks;
import net.threetag.palladium.loot.LootTableModificationManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ForgeHooks.class)
public class ForgeHooksMixin {

    @Inject(method = "readPoolName", at = @At("HEAD"), remap = false, cancellable = true)
    private static void readPoolName(JsonObject json, CallbackInfoReturnable<String> cir) {
        if (LootTableModificationManager.OVERRIDE_FORGE_NAME_LOGIC) {
            cir.setReturnValue(GsonHelper.getAsString(json, "name"));
        }
    }

}
