package net.threetag.palladium.client.beam;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.threetag.palladium.client.util.ModelUtil;
import net.threetag.palladium.client.util.PerspectiveValue;
import net.threetag.palladium.logic.condition.Condition;
import net.threetag.palladium.logic.condition.TrueCondition;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.util.EntityScaleUtil;
import net.threetag.palladium.util.PalladiumCodecs;
import org.joml.Vector3f;

import java.util.Collections;
import java.util.List;

public record Beam(String anchor, PerspectiveValue<Vector3f> offset, PerspectiveValue<Condition> visibility,
                   BeamRenderer renderer, List<BeamParticle> particles) {

    public static final PerspectiveValue<Vector3f> DEFAULT_OFFSET = new PerspectiveValue<>(new Vector3f());
    public static final PerspectiveValue<Condition> DEFAULT_VISIBILITY = new PerspectiveValue<>(TrueCondition.INSTANCE);

    public static final Codec<Beam> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("body_part").forGetter(Beam::anchor),
            PerspectiveValue.codec(PalladiumCodecs.VOXEL_VECTOR_3F).optionalFieldOf("offset", DEFAULT_OFFSET).forGetter(Beam::offset),
            PerspectiveValue.codec(Condition.CODEC).optionalFieldOf("visibility", DEFAULT_VISIBILITY).forGetter(Beam::visibility),
            BeamRenderer.CODEC.fieldOf("renderer").forGetter(Beam::renderer),
            PalladiumCodecs.listOrPrimitive(BeamParticle.CODEC).optionalFieldOf("particles", Collections.emptyList()).forGetter(Beam::particles)
    ).apply(instance, Beam::new));

    public Vec3 getOriginPosition(AbstractClientPlayer player, float partialTick) {
        return ModelUtil.getInWorldPosition(this.anchor, this.offset.get(), player, partialTick);
    }

    public void spawnParticles(Level level, Vec3 pos) {
        for (BeamParticle particle : this.particles) {
            particle.spawn(level, pos);
        }
    }

    public void submit(DataContext context, Vec3 start, Vec3 end, Vec2 sizeMultiplier, float lengthMultiplier, float opacityMultiplier, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int packedLightIn, int ageInTicks, float partialTick) {
        if ((context.getEntity() instanceof Player pl ? this.visibility.getForPlayer(pl) : this.visibility.get()).test(context)) {
            poseStack.pushPose();
            poseStack.translate(start.x, start.y, start.z);
            this.renderer.submit(context, start, end, sizeMultiplier, lengthMultiplier, opacityMultiplier, poseStack, submitNodeCollector, packedLightIn, ageInTicks, partialTick);
            poseStack.popPose();
        }
    }

    public void submitOnPlayer(AbstractClientPlayer player, Vec3 anchor, Vec3 target, float lengthMultiplier, float opacityMultiplier, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int packedLightIn, float partialTick) {
        if (this.visibility.getForPlayer(player).test(DataContext.forEntity(player))) {
            var sizeMultiplier = new Vec2(
                    EntityScaleUtil.getInstance().getModelWidthScale(player, partialTick),
                    EntityScaleUtil.getInstance().getModelHeightScale(player, partialTick)
            );
            var origin = this.getOriginPosition(player, partialTick).subtract(anchor);
            target = target.subtract(anchor);

            poseStack.pushPose();
            poseStack.translate(origin.x, origin.y, origin.z);
            this.renderer.submit(DataContext.forEntity(player), origin, target, sizeMultiplier, lengthMultiplier, opacityMultiplier, poseStack, submitNodeCollector, packedLightIn, player.tickCount, partialTick);
            poseStack.popPose();
        }
    }

}
