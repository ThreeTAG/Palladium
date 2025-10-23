package net.threetag.palladium.power.ability;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.ResourceLocationException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.EntityPowerHandler;
import net.threetag.palladium.power.PowerHolder;
import net.threetag.palladium.power.PowerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

public record AbilityReference(@Nullable ResourceLocation powerId, @NotNull String abilityKey) {

    public static final Codec<AbilityReference> CODEC = Codec.STRING.comapFlatMap(AbilityReference::read, AbilityReference::toString).stable();
    public static final StreamCodec<FriendlyByteBuf, AbilityReference> STREAM_CODEC = StreamCodec.of((buf, ref) -> {
        buf.writeNullable(ref.powerId, FriendlyByteBuf::writeResourceLocation);
        buf.writeUtf(ref.abilityKey);
    }, buf -> {
        var powerId = buf.readNullable(FriendlyByteBuf::readResourceLocation);
        var barName = buf.readUtf();
        return new AbilityReference(powerId, barName);
    });

    public static AbilityReference parse(String parse) {
        String[] s = parse.split("#", 2);

        if (s.length == 1) {
            return new AbilityReference(null, s[0]);
        } else {
            return new AbilityReference(ResourceLocation.parse(s[0]), s[1]);
        }
    }

    public static DataResult<AbilityReference> read(String path) {
        try {
            return DataResult.success(parse(path));
        } catch (ResourceLocationException e) {
            return DataResult.error(() -> "Not a valid ability reference: " + path + " " + e.getMessage());
        }
    }

    @Nullable
    public AbilityInstance<?> getInstance(LivingEntity entity) {
        return this.getInstance(entity, null);
    }

    @Nullable
    public AbilityInstance<?> getInstance(LivingEntity entity, @Nullable PowerHolder powerHolder) {
        if (this.powerId != null) {
            EntityPowerHandler handler = PowerUtil.getPowerHandler(entity);

            if (handler != null) {
                powerHolder = handler.getPowerHolder(this.powerId);
            } else {
                powerHolder = null;
            }
        }

        if (powerHolder != null) {
            return powerHolder.getAbilities().get(this.abilityKey);
        }

        return null;
    }

    public Optional<AbilityInstance<?>> optional(LivingEntity entity, @Nullable PowerHolder powerHolder) {
        return Optional.ofNullable(this.getInstance(entity, powerHolder));
    }

    public void toBuffer(FriendlyByteBuf buf) {
        buf.writeNullable(this.powerId, (buf1, resourceLocation) -> buf.writeResourceLocation(resourceLocation));
        buf.writeUtf(this.abilityKey);
    }

    public static AbilityReference fromBuffer(FriendlyByteBuf buf) {
        return new AbilityReference(buf.readNullable(FriendlyByteBuf::readResourceLocation), buf.readUtf());
    }

    @Override
    public String toString() {
        if (this.powerId == null) {
            return this.abilityKey;
        }
        return this.powerId + "#" + this.abilityKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbilityReference that)) return false;
        return Objects.equals(this.powerId, that.powerId) && Objects.equals(this.abilityKey, that.abilityKey);
    }
}
