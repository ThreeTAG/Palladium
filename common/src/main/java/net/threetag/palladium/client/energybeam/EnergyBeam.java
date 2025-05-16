package net.threetag.palladium.client.energybeam;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.threetag.palladium.entity.BodyPart;
import net.threetag.palladium.util.PalladiumCodecs;
import net.threetag.palladium.util.PerspectiveValue;
import org.joml.Vector3f;

import java.util.Collections;
import java.util.List;

@Environment(EnvType.CLIENT)
public class EnergyBeam {

    public static final Codec<EnergyBeam> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BodyPart.CODEC.fieldOf("body_part").forGetter(beam -> beam.anchor),
            PerspectiveValue.codec(PalladiumCodecs.VOXEL_VECTOR_3F).optionalFieldOf("offset", new PerspectiveValue<>(new Vector3f())).forGetter(beam -> beam.offset),
            EnergyBeamRenderer.CODEC.fieldOf("renderer").forGetter(beam -> beam.renderer),
            PalladiumCodecs.listOrPrimitive(EnergyBeamParticle.CODEC).optionalFieldOf("particles", Collections.emptyList()).forGetter(beam -> beam.particles)
    ).apply(instance, EnergyBeam::new));

    private final BodyPart anchor;
    private final PerspectiveValue<Vector3f> offset;
    private final EnergyBeamRenderer renderer;
    private final List<EnergyBeamParticle> particles;

    public EnergyBeam(BodyPart anchor, PerspectiveValue<Vector3f> offset, EnergyBeamRenderer renderer, List<EnergyBeamParticle> particles) {
        this.anchor = anchor;
        this.offset = offset;
        this.renderer = renderer;
        this.particles = particles;
    }

    public Vec3 getOriginPosition(AbstractClientPlayer player, float partialTick) {
        return BodyPart.getInWorldPosition(this.anchor, this.offset.get(), player, partialTick);
    }

    public void spawnParticles(Level level, Vec3 pos) {
        for (EnergyBeamParticle particle : this.particles) {
            particle.spawn(level, pos);
        }
    }

    public void render(AbstractClientPlayer player, Vec3 anchor, Vec3 target, float lengthMultiplier, PoseStack poseStack, MultiBufferSource bufferSource, int packedLightIn, boolean isFirstPerson, float partialTick) {
        var origin = this.getOriginPosition(player, partialTick).subtract(anchor);
        target = target.subtract(anchor);

        poseStack.pushPose();
        poseStack.translate(origin.x, origin.y, origin.z);
        this.renderer.render(player, origin, target, lengthMultiplier, poseStack, bufferSource, packedLightIn, isFirstPerson, partialTick);
        poseStack.popPose();
    }

}
