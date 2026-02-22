package net.threetag.palladium.power.ability;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.documentation.SettingType;
import net.threetag.palladium.power.energybar.EnergyBarUsage;
import net.threetag.palladium.util.PalladiumCodecs;

import java.util.List;

public class ParticleAbility extends Ability {

    public static final MapCodec<ParticleAbility> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    PalladiumCodecs.listOrPrimitive(Identifier.CODEC).fieldOf("emitter").forGetter(ab -> ab.particleEmitterIds),
                    BuiltInRegistries.PARTICLE_TYPE.holderByNameCodec().fieldOf("particle_type").forGetter(ab -> ab.particleTypeHolder),
                    CompoundTag.CODEC.optionalFieldOf("options", new CompoundTag()).forGetter(ab -> ab.options),
                    propertiesCodec(), stateCodec(), energyBarUsagesCodec()
            ).apply(instance, ParticleAbility::new));

    public final List<Identifier> particleEmitterIds;
    public final Holder<ParticleType<?>> particleTypeHolder;
    public final CompoundTag options;

    public ParticleAbility(List<Identifier> particleEmitterIds, Holder<ParticleType<?>> particleTypeHolder, CompoundTag options, AbilityProperties properties, AbilityStateManager conditions, List<EnergyBarUsage> energyBarUsages) {
        super(properties, conditions, energyBarUsages);
        this.particleEmitterIds = particleEmitterIds;
        this.particleTypeHolder = particleTypeHolder;
        this.options = options;
    }

    @Override
    public AbilitySerializer<ParticleAbility> getSerializer() {
        return AbilitySerializers.PARTICLES.get();
    }

    @Override
    public boolean tick(LivingEntity entity, AbilityInstance<?> abilityInstance, boolean enabled) {
        if (enabled && entity.level().isClientSide()) {
            Palladium.PROXY.spawnParticleEmitter(entity, this.particleEmitterIds, this.particleTypeHolder, this.options);
        }

        return super.tick(entity, abilityInstance, enabled);
    }

    public static class Serializer extends AbilitySerializer<ParticleAbility> {

        @Override
        public MapCodec<ParticleAbility> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Ability, ParticleAbility> builder, HolderLookup.Provider provider) {
            var particleType = BuiltInRegistries.PARTICLE_TYPE.wrapAsHolder(ParticleTypes.DUST);

            builder.setDescription("Spawns particles around the entity.")
                    .add("emitter", SettingType.listOrPrimitive(TYPE_IDENTIFIER), "List of emitter IDs where the particles spawn at.")
                    .add("particle_type", TYPE_PARTICLE_TYPE, "ID of the particle you want to spawn.")
                    .addOptional("options", TYPE_NBT, "Additional options for the particle (like color of a dust particle).")
                    .addExampleObject(new ParticleAbility(
                            List.of(Identifier.fromNamespaceAndPath("example", "emitter_id")),
                            particleType,
                            new CompoundTag(),
                            AbilityProperties.BASIC, AbilityStateManager.EMPTY, List.of()
                    ));
        }
    }
}
