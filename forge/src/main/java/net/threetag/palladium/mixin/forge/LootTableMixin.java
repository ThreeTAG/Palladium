package net.threetag.palladium.mixin.forge;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.threetag.palladium.loot.LootTableModificationManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.Consumer;

@Mixin(LootTable.class)
public abstract class LootTableMixin {

    @Shadow
    public abstract ResourceLocation getLootTableId();

    @Shadow
    @Final
    private List<LootPool> pools;

    @Inject(method = "getRandomItemsRaw(Lnet/minecraft/world/level/storage/loot/LootContext;Ljava/util/function/Consumer;)V", at = @At("HEAD"))
    private void getRandomItems(LootContext context, Consumer<ItemStack> output, CallbackInfo ci) {
        var mod = LootTableModificationManager.getInstance().getFor(this.getLootTableId());

        if (mod != null && mod.markApplied()) {
            this.pools.addAll(mod.getLootPools());
        }
    }

}
