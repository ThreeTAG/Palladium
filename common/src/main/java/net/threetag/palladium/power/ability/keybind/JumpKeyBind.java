package net.threetag.palladium.power.ability.keybind;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public class JumpKeyBind extends KeyBindType {

    public static final MapCodec<JumpKeyBind> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.BOOL.optionalFieldOf("cancel_jump", false).forGetter(k -> k.cancelJump)
    ).apply(instance, JumpKeyBind::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, JumpKeyBind> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, k -> k.cancelJump,
            JumpKeyBind::new
    );

    public final boolean cancelJump;

    public JumpKeyBind(boolean cancelJump) {
        this.cancelJump = cancelJump;
    }

    @Override
    public KeyBindTypeSerializer<?> getSerializer() {
        return KeyBindTypeSerializers.JUMP.get();
    }

    public static class Serializer extends KeyBindTypeSerializer<JumpKeyBind> {

        @Override
        public MapCodec<JumpKeyBind> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, JumpKeyBind> streamCodec() {
            return STREAM_CODEC;
        }
    }

}
