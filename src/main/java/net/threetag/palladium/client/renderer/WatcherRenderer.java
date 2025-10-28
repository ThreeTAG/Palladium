package net.threetag.palladium.client.renderer;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.addonpack.log.AddonPackLog;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import org.joml.Vector3f;
import org.joml.Vector4f;

import javax.annotation.Nullable;
import java.util.OptionalDouble;
import java.util.OptionalInt;

@EventBusSubscriber(modid = Palladium.MOD_ID, value = Dist.CLIENT)
public class WatcherRenderer {

    public static final WatcherRenderer INSTANCE = new WatcherRenderer();
    private static final ResourceLocation TEXTURE = Palladium.id("textures/environment/watcher.png");
    private static final int OCCURRENCE_INTERVAL = 24000; // full Minecraft day
    private static final float OCCURRENCE_CHANCE = 0.01F; // 1% chance per day
    private static final int OCCURRENCE_DURATION = 20 * 60; // 1 minute
    private static final int OCCURRENCE_FADE = 40; // 2 seconds

    @Nullable
    private AbstractTexture texture;
    private GpuBuffer buffer;
    private RenderSystem.AutoStorageIndexBuffer quadIndices;
    private int ticksTilOccurrence = OCCURRENCE_INTERVAL;
    private int visibleTicks = 0;
    private int visibility = 0;
    private int prevVisibility = 0;

    public void init() {
        this.texture = this.getTexture(TEXTURE);

        if (this.buffer == null) {
            this.buffer = this.buildQuad();
        }

        if (this.quadIndices == null) {
            this.quadIndices = RenderSystem.getSequentialBuffer(VertexFormat.Mode.QUADS);
        }
    }

    private AbstractTexture getTexture(ResourceLocation location) {
        TextureManager texturemanager = Minecraft.getInstance().getTextureManager();
        AbstractTexture abstracttexture = texturemanager.getTexture(location);
        abstracttexture.setUseMipmaps(false);
        return abstracttexture;
    }

    public void render(PoseStack poseStack, float visibility) {
        if (this.texture != null && visibility > 0F) {
            poseStack.pushPose();
            poseStack.mulPose(Axis.YP.rotationDegrees(-135.0F));
            poseStack.mulPose(Axis.XP.rotationDegrees(60F));

            Matrix4fStack matrix4fstack = RenderSystem.getModelViewStack();
            matrix4fstack.pushMatrix();
            matrix4fstack.mul(poseStack.last().pose());
            matrix4fstack.translate(0.0F, 100.0F, 0.0F);
            matrix4fstack.scale(40.0F, 1.0F, 40.0F);
            GpuBufferSlice gpubufferslice = RenderSystem.getDynamicUniforms()
                    .writeTransform(matrix4fstack, new Vector4f(1.0F, 1.0F, 1.0F, visibility), new Vector3f(), new Matrix4f(), 0.0F);
            GpuTextureView gputextureview = Minecraft.getInstance().getMainRenderTarget().getColorTextureView();
            GpuTextureView gputextureview1 = Minecraft.getInstance().getMainRenderTarget().getDepthTextureView();
            GpuBuffer gpubuffer = this.quadIndices.getBuffer(6);

            try (RenderPass renderpass = RenderSystem.getDevice()
                    .createCommandEncoder()
                    .createRenderPass(() -> "Sky watcher", gputextureview, OptionalInt.empty(), gputextureview1, OptionalDouble.empty())) {
                renderpass.setPipeline(RenderPipelines.CELESTIAL);
                RenderSystem.bindDefaultUniforms(renderpass);
                renderpass.setUniform("DynamicTransforms", gpubufferslice);
                renderpass.bindSampler("Sampler0", this.texture.getTextureView());
                renderpass.setVertexBuffer(0, this.buffer);
                renderpass.setIndexBuffer(gpubuffer, this.quadIndices.type());
                renderpass.drawIndexed(0, 0, 6, 1);
            }

            matrix4fstack.popMatrix();
            poseStack.popPose();
        }
    }

    private GpuBuffer buildQuad() {
        GpuBuffer gpubuffer;
        try (ByteBufferBuilder bytebufferbuilder = ByteBufferBuilder.exactlySized(4 * DefaultVertexFormat.POSITION_TEX.getVertexSize())) {
            BufferBuilder bufferbuilder = new BufferBuilder(bytebufferbuilder, VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
            Matrix4f matrix4f = new Matrix4f();
            bufferbuilder.addVertex(matrix4f, -1.0F, 0.0F, -1.0F).setUv(0.0F, 0.0F);
            bufferbuilder.addVertex(matrix4f, 1.0F, 0.0F, -1.0F).setUv(1.0F, 0.0F);
            bufferbuilder.addVertex(matrix4f, 1.0F, 0.0F, 1.0F).setUv(1.0F, 1.0F);
            bufferbuilder.addVertex(matrix4f, -1.0F, 0.0F, 1.0F).setUv(0.0F, 1.0F);

            try (MeshData meshdata = bufferbuilder.buildOrThrow()) {
                gpubuffer = RenderSystem.getDevice().createBuffer(() -> "Sun quad", 40, meshdata.vertexBuffer());
            }
        }

        return gpubuffer;
    }

    @SubscribeEvent
    static void clientTick(ClientTickEvent.Post e) {
        INSTANCE.prevVisibility = INSTANCE.visibility;

        if (INSTANCE.ticksTilOccurrence > 0) {
            INSTANCE.ticksTilOccurrence--;

            if (INSTANCE.ticksTilOccurrence <= 0) {
                if (Math.random() < OCCURRENCE_CHANCE) {
                    INSTANCE.visibleTicks = OCCURRENCE_DURATION;
                    AddonPackLog.info("Someone is watching...");
                }

                INSTANCE.ticksTilOccurrence = (int) (OCCURRENCE_INTERVAL * (1 + Math.random()));
            }
        }

        if (INSTANCE.visibleTicks > 0) {
            INSTANCE.visibleTicks--;

            if (INSTANCE.visibility < OCCURRENCE_FADE) {
                INSTANCE.visibility++;
            }
        } else if (INSTANCE.visibility > 0) {
            INSTANCE.visibility--;
        }
    }

    public float getVisibility(float partialTick) {
        return Mth.lerp(partialTick, this.prevVisibility, this.visibility) / (float) OCCURRENCE_FADE;
    }
}
