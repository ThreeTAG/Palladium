package net.threetag.palladium.client.particleemitter;

import com.mojang.serialization.Codec;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.level.Level;
import net.threetag.palladium.util.PalladiumCodecs;

import java.util.List;

public class ParticleEmitterConfiguration {

    public static final Codec<ParticleEmitterConfiguration> CODEC = PalladiumCodecs.listOrPrimitive(ParticleEmitter.CODEC).xmap(ParticleEmitterConfiguration::new, e -> e.emitters);

    private final List<ParticleEmitter> emitters;

    public ParticleEmitterConfiguration(List<ParticleEmitter> emitters) {
        this.emitters = emitters;
    }

    public void spawnParticles(Level level, AbstractClientPlayer player, ParticleOptions particleOptions, float partialTick) {
        for (ParticleEmitter emitter : this.emitters) {
            emitter.spawnParticles(level, player, particleOptions, partialTick);
        }
    }

}