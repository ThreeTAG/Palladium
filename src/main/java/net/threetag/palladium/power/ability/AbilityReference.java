package net.threetag.palladium.power.ability;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.IdentifierException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.EntityPowerHandler;
import net.threetag.palladium.power.PowerInstance;
import net.threetag.palladium.power.PowerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

public record AbilityReference(@Nullable Identifier powerId, @NotNull String abilityKey) {

    public static final Codec<AbilityReference> CODEC = Codec.STRING.comapFlatMap(AbilityReference::read, AbilityReference::toString).stable();
    public static final StreamCodec<FriendlyByteBuf, AbilityReference> STREAM_CODEC = StreamCodec.of((buf, ref) -> {
        buf.writeNullable(ref.powerId, FriendlyByteBuf::writeIdentifier);
        buf.writeUtf(ref.abilityKey);
    }, buf -> {
        var powerId = buf.readNullable(FriendlyByteBuf::readIdentifier);
        var barName = buf.readUtf();
        return new AbilityReference(powerId, barName);
    });

    public static AbilityReference parse(String parse) {
        String[] s = parse.split("#", 2);

        if (s.length == 1) {
            return new AbilityReference(null, s[0]);
        } else {
            return new AbilityReference(Identifier.parse(s[0]), s[1]);
        }
    }

    public static DataResult<AbilityReference> read(String path) {
        try {
            return DataResult.success(parse(path));
        } catch (IdentifierException e) {
            return DataResult.error(() -> "Not a valid ability reference: " + path + " " + e.getMessage());
        }
    }

    @Nullable
    public AbilityInstance<?> getInstance(LivingEntity entity) {
        return this.getInstance(entity, null);
    }

    @Nullable
    public AbilityInstance<?> getInstance(LivingEntity entity, @Nullable PowerInstance powerInstance) {
        if (this.powerId != null) {
            EntityPowerHandler handler = PowerUtil.getPowerHandler(entity);

            if (handler != null) {
                powerInstance = handler.getPowerInstance(this.powerId);
            } else {
                powerInstance = null;
            }
        }

        if (powerInstance != null) {
            return powerInstance.getAbilities().get(this.abilityKey);
        }

        return null;
    }

    public Optional<AbilityInstance<?>> optional(LivingEntity entity, @Nullable PowerInstance powerInstance) {
        return Optional.ofNullable(this.getInstance(entity, powerInstance));
    }

    public void toBuffer(FriendlyByteBuf buf) {
        buf.writeNullable(this.powerId, (buf1, identifier) -> buf.writeIdentifier(identifier));
        buf.writeUtf(this.abilityKey);
    }

    public static AbilityReference fromBuffer(FriendlyByteBuf buf) {
        return new AbilityReference(buf.readNullable(FriendlyByteBuf::readIdentifier), buf.readUtf());
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
