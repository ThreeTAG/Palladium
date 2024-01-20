package net.threetag.palladium.mixin;

import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.threetag.palladium.PalladiumMixinPlugin;
import net.threetag.palladium.loot.LootTableModificationManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(ReloadableServerResources.class)
public class ReloadableServerResourcesMixin {

    @Inject(method = "listeners", at = @At("RETURN"), cancellable = true)
    public void listeners(CallbackInfoReturnable<List<PreparableReloadListener>> cir) {
        if (!PalladiumMixinPlugin.HAS_QUILT) {
            var list = new ArrayList<>(cir.getReturnValue());
            list.add(0, LootTableModificationManager.getInstance());
            cir.setReturnValue(list);
        }
    }

}
