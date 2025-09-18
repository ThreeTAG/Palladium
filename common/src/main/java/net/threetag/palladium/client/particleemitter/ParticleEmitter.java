package net.threetag.palladium.client.particleemitter;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.threetag.palladium.logic.condition.PerspectiveAwareConditions;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.entity.BodyPart;
import net.threetag.palladium.util.PalladiumCodecs;
import net.threetag.palladium.util.PerspectiveValue;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.Optional;

@Environment(EnvType.CLIENT)
public class ParticleEmitter {

    public static final ParticleEmitter DEFAULT = new ParticleEmitter(null, 1F, new PerspectiveValue<>(new Vector3f()), new PerspectiveValue<>(new Vector3f()), new PerspectiveValue<>(new Vector3f()), new PerspectiveValue<>(new Vector3f()), PerspectiveAwareConditions.EMPTY);

    @Nullable
    private final BodyPart anchor;
    private final float amount;
    private final PerspectiveValue<Vector3f> offset;
    private final PerspectiveValue<Vector3f> offsetRandom;
    private final PerspectiveValue<Vector3f> motion;
    private final PerspectiveValue<Vector3f> motionRandom;
    private final PerspectiveAwareConditions conditions;

    public ParticleEmitter(@Nullable BodyPart anchor, float amount, PerspectiveValue<Vector3f> offset, PerspectiveValue<Vector3f> offsetRandom, PerspectiveValue<Vector3f> motion, PerspectiveValue<Vector3f> motionRandom, PerspectiveAwareConditions conditions) {
        this.anchor = anchor;
        this.amount = amount;
        this.offset = offset;
        this.offsetRandom = offsetRandom;
        this.motion = motion;
        this.motionRandom = motionRandom;
        this.conditions = conditions;
    }

    public static final Codec<ParticleEmitter> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BodyPart.CODEC.optionalFieldOf("body_part").forGetter(e -> Optional.ofNullable(e.anchor)),
            ExtraCodecs.NON_NEGATIVE_FLOAT.optionalFieldOf("amount", 1F).forGetter(e -> e.amount),
            PerspectiveValue.codec(PalladiumCodecs.VOXEL_VECTOR_3F).optionalFieldOf("offset", new PerspectiveValue<>(new Vector3f())).forGetter(e -> e.offset),
            PerspectiveValue.codec(PalladiumCodecs.VOXEL_VECTOR_3F).optionalFieldOf("offset_random", new PerspectiveValue<>(new Vector3f())).forGetter(e -> e.offset),
            PerspectiveValue.codec(PalladiumCodecs.VOXEL_VECTOR_3F).optionalFieldOf("motion", new PerspectiveValue<>(new Vector3f())).forGetter(e -> e.offset),
            PerspectiveValue.codec(PalladiumCodecs.VOXEL_VECTOR_3F).optionalFieldOf("motion_random", new PerspectiveValue<>(new Vector3f())).forGetter(e -> e.offset),
            PerspectiveAwareConditions.CODEC.optionalFieldOf("conditions", PerspectiveAwareConditions.EMPTY).forGetter(e -> e.conditions)
    ).apply(instance, (bodyPart, amount, offset, offsetRandom, motion, motionRandom, conditions) -> {
        return new ParticleEmitter(bodyPart.orElse(null), amount, offset, offsetRandom, motion, motionRandom, conditions);
    }));

    public Vec3 getCenter(AbstractClientPlayer player, float partialTick) {
        if (this.anchor != null) {
            return BodyPart.getInWorldPosition(this.anchor, this.offset.get(), player, partialTick);
        } else {
            var offset = this.offset.get();
            return player.getPosition(partialTick).add(0, player.getBbHeight() / 2D, 0).add(offset.x, offset.y, offset.z);
        }
    }

    public void spawnParticles(Level level, AbstractClientPlayer player, ParticleOptions particleOptions, float partialTick) {
        var minecraft = Minecraft.getInstance();

        if (!this.conditions.test(DataContext.forEntity(player), player == minecraft.player ? PerspectiveAwareConditions.Perspective.fromCameraType(minecraft.options.getCameraType()) : PerspectiveAwareConditions.Perspective.THIRD_PERSON)) {
            return;
        }

        var random = RandomSource.create();

        if (this.amount < 1) {
            if (Math.random() < this.amount) {
                this.spawnParticleOnPlayer(level, player, particleOptions, random, partialTick);
            }
        } else {
            for (int i = 0; i < this.amount; i++) {
                this.spawnParticleOnPlayer(level, player, particleOptions, random, partialTick);
            }
        }
    }

    public void spawnAtPosition(Level level, Vec3 position, ParticleOptions particleOptions) {
        var random = RandomSource.create();

        if (this.amount < 1) {
            if (Math.random() < this.amount) {
                this.spawnParticleOnPosition(level, position, particleOptions, random);
            }
        } else {
            for (int i = 0; i < this.amount; i++) {
                this.spawnParticleOnPosition(level, position, particleOptions, random);
            }
        }
    }

    private void spawnParticleOnPlayer(Level level, AbstractClientPlayer player, ParticleOptions particleOptions, RandomSource random, float partialTick) {
        var cameraType = Minecraft.getInstance().options.getCameraType();
        Vector3f offset = randomizeVector(random, this.offset.get(cameraType), this.offsetRandom.get(cameraType));
        var motion = randomizeVector(random, this.motion.get(cameraType), this.motionRandom.get(cameraType));

        Vec3 pos = this.anchor != null ? BodyPart.getInWorldPosition(this.anchor, offset, player, partialTick) :
                player.getPosition(partialTick).add(0, player.getBbHeight() / 2D, 0).add(offset.x, offset.y, offset.z);

        if (this.anchor != null) {
            var combined = new Vector3f(offset).add(motion);
            var transformed = BodyPart.getInWorldPosition(this.anchor, combined, player, partialTick);
            var motionRotated = transformed.subtract(pos);
            level.addParticle(particleOptions, pos.x, pos.y, pos.z, motionRotated.x, motionRotated.y, motionRotated.z);
        } else {
            level.addParticle(particleOptions, pos.x, pos.y, pos.z, motion.x, motion.y, motion.z);
        }
    }

    private void spawnParticleOnPosition(Level level, Vec3 position, ParticleOptions particleOptions, RandomSource random) {
        var cameraType = Minecraft.getInstance().options.getCameraType();
        Vector3f offset = randomizeVector(random, this.offset.get(cameraType), this.offsetRandom.get(cameraType));
        var motion = randomizeVector(random, this.motion.get(cameraType), this.motionRandom.get(cameraType));
        Vec3 pos = position.add(offset.x, -offset.y, offset.z);
        level.addParticle(particleOptions, pos.x, pos.y, pos.z, motion.x, -motion.y, motion.z);
    }

    private static Vector3f randomizeVector(RandomSource random, Vector3f center, Vector3f randomOffset) {
        return new Vector3f(center).add((random.nextFloat() - 0.5F) * 2F * randomOffset.x, (random.nextFloat() - 0.5F) * 2F * randomOffset.y, (random.nextFloat() - 0.5F) * 2F * randomOffset.z);
    }

}
