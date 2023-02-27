package net.threetag.palladium.mixin.fabric;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.threetag.palladium.power.ability.RestrictSlotsAbility;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(TrinketsApi.class)
public class TrinketsApiMixin {

    @Inject(method = "evaluatePredicateSet", at = @At("HEAD"), cancellable = true)
    private static void evaluatePredicateSet(Set<ResourceLocation> set, ItemStack stack, SlotReference ref, LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
        var key = "trinkets:" + ref.inventory().getSlotType().getGroup() + "/" + ref.inventory().getSlotType().getName();
        if (RestrictSlotsAbility.isRestricted(entity, key)) {
            cir.setReturnValue(false);
        }
    }

}
