package net.threetag.palladium.power.ability.enabling;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.threetag.palladium.component.PalladiumDataComponents;
import net.threetag.palladium.network.AbilityKeyChangePacket;
import net.threetag.palladium.power.ability.AbilityInstance;
import net.threetag.palladium.power.ability.keybind.AbilityKeyBind;
import net.threetag.palladium.power.ability.keybind.KeyBindType;
import net.threetag.palladium.util.PalladiumCodecs;
import org.jetbrains.annotations.NotNull;

import java.util.function.IntFunction;

public class KeyBindEnablingHandler extends EnablingHandler {

    public static final MapCodec<KeyBindEnablingHandler> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            KeyBindType.CODEC.optionalFieldOf("key_bind", AbilityKeyBind.INSTANCE).forGetter(KeyBindEnablingHandler::getKeyBindType),
            Behaviour.CODEC.optionalFieldOf("behaviour", Behaviour.ACTION).forGetter(KeyBindEnablingHandler::getBehaviour),
            PalladiumCodecs.TIME.optionalFieldOf("cooldown", 0).forGetter(h -> h.cooldown)
    ).apply(instance, KeyBindEnablingHandler::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, KeyBindEnablingHandler> STREAM_CODEC = StreamCodec.composite(
            KeyBindType.STREAM_CODEC, KeyBindEnablingHandler::getKeyBindType,
            Behaviour.STREAM_CODEC, KeyBindEnablingHandler::getBehaviour,
            ByteBufCodecs.INT, h -> h.cooldown,
            KeyBindEnablingHandler::new
    );

    private final KeyBindType keyBindType;
    private final Behaviour behaviour;
    private final int cooldown;

    public KeyBindEnablingHandler(KeyBindType keyBindType, Behaviour behaviour, int cooldown) {
        this.keyBindType = keyBindType;
        this.behaviour = behaviour;
        this.cooldown = cooldown;
    }

    @Override
    public boolean check(LivingEntity entity, AbilityInstance<?> abilityInstance) {
        boolean result = abilityInstance.getOrDefault(PalladiumDataComponents.Abilities.KEY_PRESSED.get(), false);

        if (result && this.behaviour == Behaviour.ACTION) {
            abilityInstance.setSilently(PalladiumDataComponents.Abilities.KEY_PRESSED.get(), false);
        }

        return result;
    }

    @Override
    public void tick(LivingEntity entity, AbilityInstance<?> abilityInstance, boolean enabled) {
        if (this.cooldown > 0) {
            int cooldown = abilityInstance.getOrDefault(PalladiumDataComponents.Abilities.COOLDOWN.get(), 0);

            if (this.behaviour == Behaviour.ACTION) {
                if (cooldown > 0) {
                    if (cooldown == 1) {
                        abilityInstance.set(PalladiumDataComponents.Abilities.COOLDOWN.get(), 0);
                    } else {
                        abilityInstance.setSilently(PalladiumDataComponents.Abilities.COOLDOWN.get(), cooldown - 1);
                    }
                }
            } else {
                if (enabled) {
                    if (cooldown < this.cooldown) {
                        if (cooldown == this.cooldown - 1) {
                            abilityInstance.set(PalladiumDataComponents.Abilities.COOLDOWN.get(), this.cooldown);

                            if (!entity.level().isClientSide()) {
                                abilityInstance.set(PalladiumDataComponents.Abilities.KEY_PRESSED.get(), false);
                            }
                        } else {
                            abilityInstance.setSilently(PalladiumDataComponents.Abilities.COOLDOWN.get(), cooldown + 1);
                        }
                    }
                } else {
                    if (cooldown > 0) {
                        if (cooldown == 1) {
                            abilityInstance.set(PalladiumDataComponents.Abilities.COOLDOWN.get(), 0);
                        } else {
                            abilityInstance.setSilently(PalladiumDataComponents.Abilities.COOLDOWN.get(), cooldown - 1);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void registerDataComponents(DataComponentMap.Builder builder) {
        builder.set(PalladiumDataComponents.Abilities.KEY_PRESSED.get(), false);

        if (this.cooldown > 0) {
            builder.set(PalladiumDataComponents.Abilities.COOLDOWN.get(), 0);
        }
    }

    @Override
    public void onUnlocked(LivingEntity entity, AbilityInstance<?> abilityInstance) {
        if (abilityInstance.getOrDefault(PalladiumDataComponents.Abilities.KEY_PRESSED.get(), false)) {
            abilityInstance.set(PalladiumDataComponents.Abilities.KEY_PRESSED.get(), false);
        }
    }

    @Override
    public EnablingHandlerSerializer<?> getSerializer() {
        return EnablingHandlerSerializers.KEY_BIND.get();
    }

    @Override
    public boolean isKeyBound() {
        return true;
    }

    public KeyBindType getKeyBindType() {
        return this.keyBindType;
    }

    public Behaviour getBehaviour() {
        return this.behaviour;
    }

    public boolean displayCooldown(AbilityInstance<?> abilityInstance) {
        if (this.cooldown > 0) {
            if (this.behaviour == Behaviour.ACTION) {
                return abilityInstance.getOrDefault(PalladiumDataComponents.Abilities.COOLDOWN.get(), 0) > 0;
            } else {
                return true;
            }
        }
        return false;
    }

    public float getCooldownPercentage(AbilityInstance<?> abilityInstance) {
        if (this.cooldown > 0) {
            if (this.behaviour == Behaviour.ACTION) {
                return (float) abilityInstance.getOrDefault(PalladiumDataComponents.Abilities.COOLDOWN.get(), 0) / this.cooldown;
            } else {
                float val = this.cooldown - abilityInstance.getOrDefault(PalladiumDataComponents.Abilities.COOLDOWN.get(), 0);
                return val / this.cooldown;
            }
        }

        return 0;
    }

    public void onKeyPressed(LivingEntity entity, AbilityInstance<?> abilityInstance) {
        if (entity.level().isClientSide()) {
            ClientPacketDistributor.sendToServer(new AbilityKeyChangePacket(abilityInstance.getReference(), true));
        } else {
            if (this.behaviour == Behaviour.TOGGLE) {
                abilityInstance.set(PalladiumDataComponents.Abilities.KEY_PRESSED.get(), !abilityInstance.getOrDefault(PalladiumDataComponents.Abilities.KEY_PRESSED.get(), false));
            } else {
                if (this.behaviour == Behaviour.ACTION && this.cooldown > 0) {
                    if (abilityInstance.getOrDefault(PalladiumDataComponents.Abilities.COOLDOWN.get(), 0) == 0) {
                        abilityInstance.setSilently(PalladiumDataComponents.Abilities.KEY_PRESSED.get(), true);
                        abilityInstance.set(PalladiumDataComponents.Abilities.COOLDOWN.get(), this.cooldown);
                    }
                } else {
                    abilityInstance.set(PalladiumDataComponents.Abilities.KEY_PRESSED.get(), true);
                }
            }
        }
    }

    public void onKeyReleased(LivingEntity entity, AbilityInstance<?> abilityInstance) {
        if (this.behaviour == Behaviour.HELD && abilityInstance.isEnabled()) {
            if (entity.level().isClientSide()) {
                ClientPacketDistributor.sendToServer(new AbilityKeyChangePacket(abilityInstance.getReference(), false));
            } else {
                abilityInstance.set(PalladiumDataComponents.Abilities.KEY_PRESSED.get(), false);
            }
        }
    }

    public static class Serializer extends EnablingHandlerSerializer<KeyBindEnablingHandler> {

        @Override
        public MapCodec<KeyBindEnablingHandler> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, KeyBindEnablingHandler> streamCodec() {
            return STREAM_CODEC;
        }
    }

    public enum Behaviour implements StringRepresentable {

        ACTION(0, "action"),
        TOGGLE(1, "toggle"),
        HELD(2, "held");

        public static final IntFunction<Behaviour> BY_ID = ByIdMap.continuous(Behaviour::getId, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
        public static final Codec<Behaviour> CODEC = StringRepresentable.fromEnum(Behaviour::values);
        public static final StreamCodec<ByteBuf, Behaviour> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Behaviour::getId);

        private final int id;
        private final String name;

        Behaviour(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        @Override
        public @NotNull String getSerializedName() {
            return this.name;
        }
    }
}
