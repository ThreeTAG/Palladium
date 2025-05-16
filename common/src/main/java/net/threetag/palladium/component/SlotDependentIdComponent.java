package net.threetag.palladium.component;

import com.mojang.serialization.Codec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.entity.PlayerSlot;
import net.threetag.palladium.util.PalladiumCodecs;
import net.threetag.palladium.util.Utils;

import java.util.*;

public record SlotDependentIdComponent(Map<PlayerSlot, List<ResourceLocation>> entries) {

    public static final Codec<SlotDependentIdComponent> CODEC = Codec.unboundedMap(
            PlayerSlot.CODEC,
            PalladiumCodecs.listOrPrimitive(ResourceLocation.CODEC)
    ).xmap(SlotDependentIdComponent::new, SlotDependentIdComponent::entries);
    public static final StreamCodec<FriendlyByteBuf, SlotDependentIdComponent> STREAM_CODEC = ByteBufCodecs.map(
            Utils::newMap,
            PlayerSlot.STREAM_CODEC,
            ByteBufCodecs.collection(Utils::newList, ResourceLocation.STREAM_CODEC)
    ).map(SlotDependentIdComponent::new, SlotDependentIdComponent::entries);

    public List<ResourceLocation> forSlot(PlayerSlot slot) {
        List<ResourceLocation> list = new ArrayList<>();
        list.addAll(this.entries.getOrDefault(PlayerSlot.AnySlot.INSTANCE, Collections.emptyList()));
        list.addAll(this.entries.getOrDefault(slot, Collections.emptyList()));
        return list;
    }

    public static class Builder {

        private final Map<PlayerSlot, List<ResourceLocation>> entries = new HashMap<>();

        public Builder add(PlayerSlot slot, ResourceLocation... renderLayerId) {
            var list = this.entries.computeIfAbsent(slot, s -> new ArrayList<>());
            list.addAll(Arrays.asList(renderLayerId));
            return this;
        }

        public SlotDependentIdComponent build() {
            return new SlotDependentIdComponent(this.entries);
        }
    }

}
