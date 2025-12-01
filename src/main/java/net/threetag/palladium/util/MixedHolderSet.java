package net.threetag.palladium.util;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.resources.ResourceKey;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public record MixedHolderSet<T>(List<HolderSet<T>> holderSets) {

    public static final MixedHolderSet<?> EMPTY = new MixedHolderSet<>(Collections.emptyList());

    public static <T> Codec<MixedHolderSet<T>> codec(ResourceKey<? extends Registry<T>> registryKey) {
        return PalladiumCodecs.listOrPrimitive(RegistryCodecs.homogeneousList(registryKey)).xmap(MixedHolderSet::new, holderSet -> holderSet.holderSets);
    }

    public boolean contains(Holder<T> holder) {
        for (HolderSet<T> holderSet : this.holderSets) {
            if (holderSet.contains(holder)) {
                return true;
            }
        }

        return false;
    }

    public MixedHolderSet<T> merge(MixedHolderSet<T> merge) {
        List<HolderSet<T>> list = new ArrayList<>();
        list.addAll(this.holderSets);
        list.addAll(merge.holderSets);
        return new MixedHolderSet<>(list);
    }
}
