package net.threetag.palladium.client.beam;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.Codec;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.threetag.palladium.logic.context.DataContext;

public abstract class BeamRenderer {

    public static Codec<BeamRenderer> CODEC = BeamRendererSerializer.TYPE_CODEC.dispatch(BeamRenderer::getSerializer, BeamRendererSerializer::codec);

    public abstract void submit(DataContext context, Vec3 origin, Vec3 target, Vec2 sizeMultiplier, float lengthMultiplier, float opacityMultiplier, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int packedLightIn, int ageInTicks, float partialTick);

    public abstract BeamRendererSerializer<?> getSerializer();

}
