package net.threetag.palladium.client.beam;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.threetag.palladium.client.particleemitter.ParticleEmitter;

public record BeamParticle(Holder<ParticleType<?>> particleType, CompoundTag options, ParticleEmitter emitter) {

    public static final Codec<BeamParticle> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BuiltInRegistries.PARTICLE_TYPE.holderByNameCodec().fieldOf("particle_type").forGetter(BeamParticle::particleType),
            CompoundTag.CODEC.optionalFieldOf("options", new CompoundTag()).forGetter(BeamParticle::options),
            ParticleEmitter.CODEC.optionalFieldOf("emitter", ParticleEmitter.DEFAULT).forGetter(BeamParticle::emitter)
    ).apply(instance, BeamParticle::new));

    public void spawn(Level level, Vec3 pos) {
        ParticleType<?> type = this.particleType.value();
        ParticleOptions options = type.codec().codec().parse(level.registryAccess().createSerializationContext(NbtOps.INSTANCE), this.options).getOrThrow();
        this.emitter.spawnAtPosition(level, pos, options);
    }

}
