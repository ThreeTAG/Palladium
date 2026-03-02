package net.threetag.palladium.network;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.PowerInstance;
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

    public static void handle(SyncEntityPowersPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> Palladium.PROXY.packetHandleSyncEntityPowers(packet, context));
    }

    public static SyncEntityPowersPacket create(LivingEntity entity, List<PowerInstance> removed, List<PowerInstance> added) {
        List<NewPowerChange> add = new ArrayList<>();
        added.forEach((powerInstance) -> add.add(new NewPowerChange(powerInstance)));
        return new SyncEntityPowersPacket(entity.getId(), removed.stream().map(PowerInstance::getPower).toList(), add);
    }

    public static SyncEntityPowersPacket create(LivingEntity entity) {
        List<NewPowerChange> add = new ArrayList<>();
        PowerUtil.getPowerHandler(entity).getPowers().forEach((powerInstance) -> {
            add.add(new NewPowerChange(powerInstance));
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

        public NewPowerChange(PowerInstance powerInstance) {
            this.power = powerInstance.getPower();
            this.priority = powerInstance.getPriority();

            this.abilityComponents = new ArrayList<>();
            powerInstance.getAbilities().forEach((s, ability) -> {
                for (TypedDataComponent<?> component : ability.getComponents()) {
                    DataComponentType type = component.type();
                    DataComponentPatch patch = DataComponentPatch.builder().set(type, component.value()).build();
                    this.abilityComponents.add(Pair.of(ability.getReference().abilityKey(), patch));
                }
            });

            this.energyBars = new ArrayList<>();
            powerInstance.getEnergyBars().forEach((s, energyBar) -> {
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
