package net.threetag.palladium.client.energybeam;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.Codec;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.threetag.palladium.util.PalladiumCodecs;

import java.util.List;

public class EnergyBeamConfiguration {

    public static final Codec<EnergyBeamConfiguration> CODEC = PalladiumCodecs.listOrPrimitive(EnergyBeam.CODEC).xmap(EnergyBeamConfiguration::new, e -> e.beams);

    private final List<EnergyBeam> beams;

    public EnergyBeamConfiguration(List<EnergyBeam> energyBeams) {
        this.beams = energyBeams;
    }

    public void render(AbstractClientPlayer player, Vec3 anchor, Vec3 target, float lengthMultiplier, PoseStack poseStack, MultiBufferSource bufferSource, int packedLightIn, boolean isFirstPerson, float partialTick) {
        for (EnergyBeam beam : this.beams) {
            beam.render(player, anchor, target, lengthMultiplier, poseStack, bufferSource, packedLightIn, isFirstPerson, partialTick);
        }
    }

    public void spawnParticles(Level level, Vec3 pos) {
        for (EnergyBeam beam : this.beams) {
            beam.spawnParticles(level, pos);
        }
    }

}
