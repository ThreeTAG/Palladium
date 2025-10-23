package net.threetag.palladium.power.ability;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.power.energybar.EnergyBarUsage;

import java.util.Collections;
import java.util.List;

public class HealingAbility extends Ability {

    public static final MapCodec<HealingAbility> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    ExtraCodecs.POSITIVE_INT.fieldOf("frequency").forGetter(ab -> ab.frequency),
                    ExtraCodecs.POSITIVE_FLOAT.fieldOf("amount").forGetter(ab -> ab.amount),
                    propertiesCodec(), stateCodec(), energyBarUsagesCodec()
            ).apply(instance, HealingAbility::new));

    public final int frequency;
    public final float amount;

    public HealingAbility(int frequency, float amount, AbilityProperties properties, AbilityStateManager conditions, List<EnergyBarUsage> energyBarUsages) {
        super(properties, conditions, energyBarUsages);
        this.frequency = frequency;
        this.amount = amount;
    }

    @Override
    public AbilitySerializer<HealingAbility> getSerializer() {
        return AbilitySerializers.HEALING.get();
    }

    @Override
    public void tick(LivingEntity entity, AbilityInstance<?> entry, boolean enabled) {
        if (enabled && !entity.level().isClientSide()) {
            if (entity.tickCount % this.frequency == 0) {
                entity.heal(this.amount);
            }
        }
    }

    public static class Serializer extends AbilitySerializer<HealingAbility> {

        @Override
        public MapCodec<HealingAbility> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Ability, HealingAbility> builder, HolderLookup.Provider provider) {
            builder.setDescription("An ability that heals the entity every x ticks by y amount.")
                    .add("frequency", TYPE_INT, "The frequency of healing (in ticks)")
                    .add("amount", TYPE_FLOAT, "The amount of hearts for each healing")
                    .setExampleObject(new HealingAbility(20, 1.0F, AbilityProperties.BASIC, AbilityStateManager.EMPTY, Collections.emptyList()));
        }
    }

}
