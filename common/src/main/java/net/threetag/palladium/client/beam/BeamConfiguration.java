package net.threetag.palladium.client.beam;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.Codec;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.util.PalladiumCodecs;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record BeamConfiguration(List<Beam> beams) {

    public static final Codec<BeamConfiguration> CODEC = PalladiumCodecs.listOrPrimitive(Beam.CODEC).xmap(BeamConfiguration::new, BeamConfiguration::beams);

    public void renderOnPlayer(AbstractClientPlayer player, Vec3 anchor, Vec3 target, float lengthMultiplier, float opacityMultiplier, PoseStack poseStack, MultiBufferSource bufferSource, int packedLightIn, float partialTick) {
        for (Beam beam : this.beams) {
            beam.renderOnPlayer(player, anchor, target, lengthMultiplier, opacityMultiplier, poseStack, bufferSource, packedLightIn, partialTick);
        }
    }

    public void render(DataContext context, Vec3 start, Vec3 end, Vec2 sizeMultiplier, float lengthMultiplier, float opacityMultiplier, PoseStack poseStack, MultiBufferSource bufferSource, int packedLightIn, int ageInTicks, float partialTick) {
        for (Beam beam : this.beams) {
            beam.render(context, start, end, sizeMultiplier, lengthMultiplier, opacityMultiplier, poseStack, bufferSource, packedLightIn, ageInTicks, partialTick);
        }
    }

    public Map<Beam, Vec3> getAnchorPositions(AbstractClientPlayer player, float partialTick) {
        return this.beams.stream().collect(Collectors.toMap(beam -> beam, beam -> beam.getOriginPosition(player, partialTick)));
    }

    public void spawnParticles(Level level, Vec3 pos) {
        for (Beam beam : this.beams) {
            beam.spawnParticles(level, pos);
        }
    }

}
