package net.threetag.palladium.power.ability;

import net.minecraft.ResourceLocationException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.IPowerHandler;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.PowerManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

public class AbilityReference {

    @Nullable
    private final ResourceLocation powerId;
    @NotNull
    private final String abilityId;

    public AbilityReference(@Nullable ResourceLocation powerId, @NotNull String abilityId) {
        this.powerId = powerId;
        this.abilityId = abilityId;
    }

    public static AbilityReference fromString(String parse) {
        String[] s = parse.split("#", 2);

        if (s.length == 1) {
            return new AbilityReference(null, s[0]);
        } else {
            return new AbilityReference(new ResourceLocation(s[0]), s[1]);
        }
    }

    public static boolean validateFull(String raw) {
        String[] s = raw.split("#", 2);

        if (s.length != 2) {
            throw new ResourceLocationException("Ability reference needs a power and the ability key, seperated by '#'");
        } else {
            return ResourceLocation.isValidResourceLocation(s[0]);
        }
    }

    @Nullable
    public ResourceLocation getPowerId() {
        return powerId;
    }

    @NotNull
    public String getAbilityId() {
        return abilityId;
    }

    @Nullable
    public AbilityInstance getEntry(LivingEntity entity) {
        return this.getEntry(entity, null);
    }

    @Nullable
    public AbilityInstance getEntry(LivingEntity entity, @Nullable IPowerHolder powerHolder) {
        if (this.powerId != null) {
            IPowerHandler handler = PowerManager.getPowerHandler(entity).orElse(null);
            Power power = PowerManager.getInstance(entity.level()).getPower(this.powerId);

            if (power != null && handler != null) {
                powerHolder = handler.getPowerHolder(power);
            } else {
                powerHolder = null;
            }
        }

        if (powerHolder != null) {
            return powerHolder.getAbilities().get(this.abilityId);
        }

        return null;
    }

    public Optional<AbilityInstance> optional(LivingEntity entity, @Nullable IPowerHolder powerHolder) {
        return Optional.ofNullable(this.getEntry(entity, powerHolder));
    }

    public void toBuffer(FriendlyByteBuf buf) {
        buf.writeNullable(this.powerId, (buf1, resourceLocation) -> buf.writeResourceLocation(resourceLocation));
        buf.writeUtf(this.abilityId);
    }

    public static AbilityReference fromBuffer(FriendlyByteBuf buf) {
        return new AbilityReference(buf.readNullable(FriendlyByteBuf::readResourceLocation), buf.readUtf());
    }

    @Override
    public String toString() {
        if (this.powerId == null) {
            return this.abilityId;
        }
        return this.powerId + "#" + this.abilityId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.powerId, this.abilityId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbilityReference that)) return false;
        return Objects.equals(this.powerId, that.powerId) && Objects.equals(this.abilityId, that.abilityId);
    }
}
