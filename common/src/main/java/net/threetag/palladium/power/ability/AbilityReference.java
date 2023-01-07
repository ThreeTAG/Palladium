package net.threetag.palladium.power.ability;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.condition.BuyableCondition;
import net.threetag.palladium.network.OpenAbilityBuyScreenMessage;
import net.threetag.palladium.power.IPowerHandler;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.PowerManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class AbilityReference {

    @NotNull
    private final ResourceLocation powerId;
    @NotNull
    private final String abilityId;

    public AbilityReference(@NotNull ResourceLocation powerId, @NotNull String abilityId) {
        this.powerId = powerId;
        this.abilityId = abilityId;
    }

    @NotNull
    public ResourceLocation getPowerId() {
        return powerId;
    }

    @NotNull
    public String getAbilityId() {
        return abilityId;
    }

    @Nullable
    public AbilityEntry getEntry(LivingEntity entity) {
        IPowerHandler handler = PowerManager.getPowerHandler(entity).orElse(null);
        Power power = PowerManager.getInstance(entity.level).getPower(this.powerId);

        if (power != null && handler != null) {
            IPowerHolder holder = handler.getPowerHolder(power);

            if (holder != null) {
                return holder.getAbilities().get(this.abilityId);
            }
        }

        return null;
    }

    public void toBuffer(FriendlyByteBuf buf) {
        buf.writeResourceLocation(this.powerId);
        buf.writeUtf(this.abilityId);
    }

    public static AbilityReference fromBuffer(FriendlyByteBuf buf) {
        return new AbilityReference(buf.readResourceLocation(), buf.readUtf());
    }

    @Override
    public String toString() {
        return this.powerId + "#" + this.abilityId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(powerId, abilityId);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (!(object instanceof AbilityReference)) {
            return false;
        } else {
            AbilityReference reference = (AbilityReference) object;
            return this.powerId.equals(reference.powerId) && this.abilityId.equals(reference.abilityId);
        }
    }
}
