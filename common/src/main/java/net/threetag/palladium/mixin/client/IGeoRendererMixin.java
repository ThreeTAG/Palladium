package net.threetag.palladium.mixin.client;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.threetag.palladium.client.renderer.entity.HumanoidRendererModifications;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import software.bernie.geckolib.cache.object.GeoQuad;
import software.bernie.geckolib.cache.object.GeoVertex;

@Mixin(IGeoRenderer.class)
public interface IGeoRendererMixin {

    /**
     * @author Lucraft
     * @reason Used to globally change alpha
     */
    @Overwrite
    default void createVerticesOfQuad(GeoQuad quad, Matrix4f poseState, Vector3f normal, VertexConsumer buffer,
                                      int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        for (GeoVertex vertex : quad.vertices) {
            Vector4f vector4f = new Vector4f(vertex.position.x(), vertex.position.y(), vertex.position.z(), 1);

            vector4f.transform(poseState);
            buffer.vertex(vector4f.x(), vector4f.y(), vector4f.z(), red, green, blue, alpha * HumanoidRendererModifications.ALPHA_MULTIPLIER, vertex.textureU,
                    vertex.textureV, packedOverlay, packedLight, normal.x(), normal.y(), normal.z());
        }
    }

}
