package net.threetag.palladium.component;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.threetag.palladium.entity.PlayerSlot;
import net.threetag.palladium.util.MixedHolderSet;

import java.util.HashMap;
import java.util.Map;

public record SlotDependentHolderSetComponent<T>(Map<PlayerSlot, MixedHolderSet<T>> entries) {

    public static <T> Codec<SlotDependentHolderSetComponent<T>> codec(ResourceKey<? extends Registry<T>> registryKey) {
        return Codec.unboundedMap(
                PlayerSlot.CODEC,
                MixedHolderSet.codec(registryKey)
        ).xmap(SlotDependentHolderSetComponent::new, SlotDependentHolderSetComponent::entries);
    }

    public boolean contains(PlayerSlot slot, Holder<T> holder) {
        if (this.entries.containsKey(PlayerSlot.AnySlot.INSTANCE) && this.entries.get(PlayerSlot.AnySlot.INSTANCE).contains(holder)) {
            return true;
        }

        return this.entries.containsKey(slot) && this.entries.get(slot).contains(holder);
    }

    public static class Builder<T> {

        private final Map<PlayerSlot, MixedHolderSet<T>> entries = new HashMap<>();

        public Builder<T> add(PlayerSlot slot, MixedHolderSet<T> holderSet) {
            if (this.entries.containsKey(slot)) {
                this.entries.put(slot, this.entries.get(slot).merge(holderSet));
            } else {
                this.entries.put(slot, holderSet);
            }

            return this;
        }

        public SlotDependentHolderSetComponent<T> build() {
            return new SlotDependentHolderSetComponent<>(this.entries);
        }
    }

}
