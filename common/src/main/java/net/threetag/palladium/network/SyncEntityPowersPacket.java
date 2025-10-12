package net.threetag.palladium.network;

import dev.architectury.networking.NetworkManager;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.PowerHolder;
import net.threetag.palladium.power.PowerUtil;
import net.threetag.palladium.registry.PalladiumRegistryKeys;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public record SyncEntityPowersPacket(int entityId, List<Holder<Power>> remove,
                                     List<NewPowerChange> add) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<SyncEntityPowersPacket> TYPE = new CustomPacketPayload.Type<>(Palladium.id("sync_entity_powers"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncEntityPowersPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, SyncEntityPowersPacket::entityId,
            ByteBufCodecs.holderRegistry(PalladiumRegistryKeys.POWER).apply(ByteBufCodecs.list()), SyncEntityPowersPacket::remove,
            NewPowerChange.STREAM_CODEC.apply(ByteBufCodecs.list()), SyncEntityPowersPacket::add,
            SyncEntityPowersPacket::new
    );

    @Override
    public @NotNull Type<SyncEntityPowersPacket> type() {
        return TYPE;
    }

    public static void handle(SyncEntityPowersPacket packet, NetworkManager.PacketContext context) {
        context.queue(() -> Palladium.PROXY.packetHandleSyncEntityPowers(packet, context));
    }

    public static SyncEntityPowersPacket create(LivingEntity entity, List<PowerHolder> removed, List<PowerHolder> added) {
        List<NewPowerChange> add = new ArrayList<>();
        added.forEach((powerHolder) -> add.add(new NewPowerChange(powerHolder)));
        return new SyncEntityPowersPacket(entity.getId(), removed.stream().map(PowerHolder::getPower).toList(), add);
    }

    public static SyncEntityPowersPacket create(LivingEntity entity) {
        List<NewPowerChange> add = new ArrayList<>();
        PowerUtil.getPowerHandler(entity).getPowerHolders().forEach((resourceLocation, powerHolder) -> {
            add.add(new NewPowerChange(powerHolder));
        });
        return new SyncEntityPowersPacket(entity.getId(), Collections.emptyList(), add);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static class NewPowerChange {

        private static final StreamCodec<RegistryFriendlyByteBuf, Pair<String, DataComponentPatch>> PAIR_STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8, Pair::getLeft,
                DataComponentPatch.STREAM_CODEC, Pair::getRight,
                Pair::of
        );

        private static final StreamCodec<RegistryFriendlyByteBuf, Triple<String, Integer, Integer>> TRIPLE_STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8, Triple::getLeft,
                ByteBufCodecs.VAR_INT, Triple::getMiddle,
                ByteBufCodecs.VAR_INT, Triple::getRight,
                Triple::of
        );

        public static final StreamCodec<RegistryFriendlyByteBuf, NewPowerChange> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.holderRegistry(PalladiumRegistryKeys.POWER), NewPowerChange::getPower,
                ByteBufCodecs.VAR_INT, NewPowerChange::getPriority,
                PAIR_STREAM_CODEC.apply(ByteBufCodecs.list()), NewPowerChange::getAbilityComponents,
                TRIPLE_STREAM_CODEC.apply(ByteBufCodecs.list()), NewPowerChange::getEnergyBars,
                NewPowerChange::new
        );

        public final Holder<Power> power;
        public final int priority;
        public final List<Pair<String, DataComponentPatch>> abilityComponents;
        public final List<Triple<String, Integer, Integer>> energyBars;

        public NewPowerChange(Holder<Power> power, int priority, List<Pair<String, DataComponentPatch>> abilityComponents, List<Triple<String, Integer, Integer>> energyBars) {
            this.power = power;
            this.priority = priority;
            this.abilityComponents = abilityComponents;
            this.energyBars = energyBars;
        }

        public NewPowerChange(PowerHolder powerHolder) {
            this.power = powerHolder.getPower();
            this.priority = powerHolder.getPriority();

            this.abilityComponents = new ArrayList<>();
            powerHolder.getAbilities().forEach((s, ability) -> {
                for (TypedDataComponent<?> component : ability.getComponents()) {
                    DataComponentType type = component.type();
                    DataComponentPatch patch = DataComponentPatch.builder().set(type, component.value()).build();
                    this.abilityComponents.add(Pair.of(ability.getReference().abilityKey(), patch));
                }
            });

            this.energyBars = new ArrayList<>();
            powerHolder.getEnergyBars().forEach((s, energyBar) -> {
                this.energyBars.add(Triple.of(energyBar.getReference().energyBarKey(), energyBar.get(), energyBar.getMax()));
            });
        }

        public Holder<Power> getPower() {
            return power;
        }

        public int getPriority() {
            return priority;
        }

        public List<Pair<String, DataComponentPatch>> getAbilityComponents() {
            return abilityComponents;
        }

        public List<Triple<String, Integer, Integer>> getEnergyBars() {
            return energyBars;
        }
    }

}
