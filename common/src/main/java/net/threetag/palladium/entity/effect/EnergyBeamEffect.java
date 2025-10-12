package net.threetag.palladium.entity.effect;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.threetag.palladium.client.beam.BeamManager;
import net.threetag.palladium.entity.EffectEntity;
import net.threetag.palladium.power.ability.AbilityInstance;
import net.threetag.palladium.power.ability.AbilityReference;
import net.threetag.palladium.power.ability.BeamAbility;

public class EnergyBeamEffect extends EntityEffect {

    @SuppressWarnings("unchecked")
    @Override
    public void tick(EffectEntity entity, Entity anchor) {
        if (entity.level().isClientSide && anchor instanceof Player player) {
            var ref = getAbilityReference(entity.getExtraData());
            AbilityInstance<?> instance = ref.getInstance(player);

            if (instance != null && instance.getAbility() instanceof BeamAbility) {
                var ability = (AbilityInstance<BeamAbility>) instance;
                var beam = BeamManager.INSTANCE.get(ability.getAbility().beamId);

                if (beam == null) {
                    this.stopPlaying(entity);
                    return;
                }

                boolean isDonePlaying = ability.getAbility().beamLengthMultiplier(ability, 0F) <= 0F
                        && !ability.isEnabled();
                if (isDonePlaying != entity.isDonePlaying()) {
                    this.stopPlaying(entity);
                }
            } else {
                this.stopPlaying(entity);
            }
        } else {
            this.stopPlaying(entity);
        }
    }

    public static AbilityReference getAbilityReference(CompoundTag tag) {
        return AbilityReference.parse(tag.getString("ability").orElse(""));
    }

    public static void setAbilityReference(EffectEntity entity, AbilityReference abilityReference) {
        entity.changeExtraData(compound -> compound.putString("ability", abilityReference.toString()));
    }
}
