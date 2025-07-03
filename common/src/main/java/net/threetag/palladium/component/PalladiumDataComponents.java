package net.threetag.palladium.component;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.phys.Vec3;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.core.registry.DeferredRegister;
import net.threetag.palladium.core.registry.RegistryHolder;

public class PalladiumDataComponents {

    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS = DeferredRegister.create(Palladium.MOD_ID, Registries.DATA_COMPONENT_TYPE);

    static {
        Items.RENDER_LAYERS.getId();
        Abilities.UNLOCKED.getId();
    }

    public static class Items {

        public static final RegistryHolder<DataComponentType<SlotDependentIdComponent>> RENDER_LAYERS = DATA_COMPONENTS.register("render_layers", () -> DataComponentType.<SlotDependentIdComponent>builder()
                .persistent(SlotDependentIdComponent.CODEC)
                .networkSynchronized(SlotDependentIdComponent.STREAM_CODEC)
                .build());

        public static final RegistryHolder<DataComponentType<SlotDependentIdComponent>> POWERS = DATA_COMPONENTS.register("powers", () -> DataComponentType.<SlotDependentIdComponent>builder()
                .persistent(SlotDependentIdComponent.CODEC)
                .networkSynchronized(SlotDependentIdComponent.STREAM_CODEC)
                .build());

        public static final RegistryHolder<DataComponentType<CustomData>> BOTTLE_ENTITY_DATA = DATA_COMPONENTS.register("bottle_entity_data", () -> DataComponentType.<CustomData>builder()
                .persistent(CustomData.CODEC)
                .networkSynchronized(CustomData.STREAM_CODEC)
                .build());

        public static final RegistryHolder<DataComponentType<Integer>> OPENING = DATA_COMPONENTS.register("opening", () -> DataComponentType.<Integer>builder()
                .persistent(ExtraCodecs.NON_NEGATIVE_INT)
                .networkSynchronized(ByteBufCodecs.VAR_INT)
                .build());
        public static final RegistryHolder<DataComponentType<Boolean>> OPENED = DATA_COMPONENTS.register("opened", () -> DataComponentType.<Boolean>builder()
                .persistent(Codec.BOOL)
                .networkSynchronized(ByteBufCodecs.BOOL)
                .build());
    }

    public static class Abilities {

        public static final RegistryHolder<DataComponentType<Boolean>> UNLOCKED = DATA_COMPONENTS.register("unlocked", () -> DataComponentType.<Boolean>builder()
                .networkSynchronized(ByteBufCodecs.BOOL)
                .build());

        public static final RegistryHolder<DataComponentType<Boolean>> ENABLED = DATA_COMPONENTS.register("enabled", () -> DataComponentType.<Boolean>builder()
                .networkSynchronized(ByteBufCodecs.BOOL)
                .build());

        public static final RegistryHolder<DataComponentType<Boolean>> KEY_PRESSED = DATA_COMPONENTS.register("key_pressed", () -> DataComponentType.<Boolean>builder()
                .networkSynchronized(ByteBufCodecs.BOOL)
                .build());

        public static final RegistryHolder<DataComponentType<Integer>> COOLDOWN = DATA_COMPONENTS.register("cooldown", () -> DataComponentType.<Integer>builder()
                .networkSynchronized(ByteBufCodecs.VAR_INT)
                .build());

        public static final RegistryHolder<DataComponentType<Integer>> ACTIVATED_TIME = DATA_COMPONENTS.register("activated_time", () -> DataComponentType.<Integer>builder()
                .networkSynchronized(ByteBufCodecs.VAR_INT)
                .build());

        public static final RegistryHolder<DataComponentType<Integer>> MAX_ACTIVATED_TIME = DATA_COMPONENTS.register("max_activated_time", () -> DataComponentType.<Integer>builder()
                .networkSynchronized(ByteBufCodecs.VAR_INT)
                .build());

        public static final RegistryHolder<DataComponentType<Boolean>> BOUGHT = DATA_COMPONENTS.register("bought", () -> DataComponentType.<Boolean>builder()
                .persistent(Codec.BOOL)
                .networkSynchronized(ByteBufCodecs.BOOL)
                .build());

        public static final RegistryHolder<DataComponentType<Vec3>> ENERGY_BEAM_TARGET = DATA_COMPONENTS.register("energy_beam_target", () -> DataComponentType.<Vec3>builder()
                .networkSynchronized(ByteBufCodecs.VECTOR3F.map(Vec3::new, Vec3::toVector3f))
                .build());

        public static final RegistryHolder<DataComponentType<Component>> NAME_CHANGE_CACHED = DATA_COMPONENTS.register("name_change_cached", () -> DataComponentType.<Component>builder()
                .networkSynchronized(ComponentSerialization.STREAM_CODEC)
                .build());

        private static DataComponentMap COMMON_COMPONENTS = null;

        public static DataComponentMap getCommonComponents() {
            if (COMMON_COMPONENTS == null) {
                COMMON_COMPONENTS = DataComponentMap.builder()
                        .set(UNLOCKED.get(), true)
                        .set(ENABLED.get(), true)
                        .build();
            }

            return COMMON_COMPONENTS;
        }
    }
}
