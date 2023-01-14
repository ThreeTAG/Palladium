package net.threetag.palladium.client.renderer.renderlayer;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.threetag.palladium.power.ability.AbilityEntry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IRenderLayerContext {

    @NotNull
    LivingEntity getEntity();

    @NotNull
    ItemStack getItem();

    @Nullable
    AbilityEntry getAbilityEntry();

    static IRenderLayerContext ofItem(LivingEntity entity, ItemStack stack) {
        return new IRenderLayerContext() {
            @Override
            public @NotNull LivingEntity getEntity() {
                return entity;
            }

            @Override
            public @NotNull ItemStack getItem() {
                return stack;
            }

            @Override
            public @Nullable AbilityEntry getAbilityEntry() {
                return null;
            }
        };
    }

    static IRenderLayerContext ofAbility(LivingEntity entity, AbilityEntry entry) {
        return new IRenderLayerContext() {
            @Override
            public @NotNull LivingEntity getEntity() {
                return entity;
            }

            @Override
            public @NotNull ItemStack getItem() {
                return ItemStack.EMPTY;
            }

            @Override
            public @Nullable AbilityEntry getAbilityEntry() {
                return entry;
            }
        };
    }

}
