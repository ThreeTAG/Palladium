package net.threetag.palladium.util;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import org.jspecify.annotations.NonNull;

import java.util.*;

public record MixedHolderSet<T>(List<HolderSet<T>> holderSets) implements Iterable<Holder<T>> {

    public static final MixedHolderSet<?> EMPTY = new MixedHolderSet<>(Collections.emptyList());

    public static <T> Codec<MixedHolderSet<T>> codec(ResourceKey<? extends Registry<T>> registryKey) {
        return PalladiumCodecs.listOrPrimitive(RegistryCodecs.homogeneousList(registryKey)).xmap(MixedHolderSet::new, holderSet -> holderSet.holderSets);
    }

    public static <T> StreamCodec<RegistryFriendlyByteBuf, MixedHolderSet<T>> streamCodec(ResourceKey<? extends Registry<T>> registryKey) {
        return ByteBufCodecs.holderSet(registryKey).apply(ByteBufCodecs.list()).map(MixedHolderSet::new, MixedHolderSet::holderSets);
    }

    @SafeVarargs
    public MixedHolderSet(HolderSet<T>... holderSet) {
        this(Arrays.asList(holderSet));
    }

    public MixedHolderSet(Holder<T> holder) {
        this(List.of(HolderSet.direct(holder)));
    }

    public boolean contains(Holder<T> holder) {
        for (HolderSet<T> holderSet : this.holderSets) {
            if (holderSet.contains(holder)) {
                return true;
            }
        }

        return false;
    }

    public Set<Holder<T>> values() {
        Set<Holder<T>> values = new HashSet<>();

        for (HolderSet<T> holderSet : this.holderSets) {
            for (Holder<T> holder : holderSet) {
                values.add(holder);
            }
        }

        return values;
    }

    public MixedHolderSet<T> merge(MixedHolderSet<T> merge) {
        List<HolderSet<T>> list = new ArrayList<>();
        list.addAll(this.holderSets);
        list.addAll(merge.holderSets);
        return new MixedHolderSet<>(list);
    }

    @Override
    public @NonNull Iterator<Holder<T>> iterator() {
        return this.values().iterator();
    }
}
