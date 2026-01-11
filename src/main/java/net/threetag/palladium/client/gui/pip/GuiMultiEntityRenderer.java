package net.threetag.palladium.client.gui.pip;

import com.mojang.blaze3d.ProjectionType;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.GpuDevice;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.textures.TextureFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.gui.render.pip.PictureInPictureRenderer;
import net.minecraft.client.gui.render.state.BlitRenderState;
import net.minecraft.client.gui.render.state.GuiRenderState;
import net.minecraft.client.gui.render.state.pip.GuiEntityRenderState;
import net.minecraft.client.renderer.CachedOrthoProjectionMatrixBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.feature.FeatureRenderDispatcher;
import net.minecraft.client.renderer.state.CameraRenderState;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;

public class GuiMultiEntityRenderer extends PictureInPictureRenderer<GuiMultiEntityRenderState> {

    private final EntityRenderDispatcher entityRenderDispatcher;
    private final Map<GuiEntityRenderState, GpuTexture> textures = new HashMap<>();
    private final Map<GuiEntityRenderState, GpuTextureView> textureViews = new HashMap<>();
    private final Map<GuiEntityRenderState, GpuTexture> depthTextures = new HashMap<>();
    private final Map<GuiEntityRenderState, GpuTextureView> depthTextureViews = new HashMap<>();
    private final CachedOrthoProjectionMatrixBuffer projectionMatrixBuffer = new CachedOrthoProjectionMatrixBuffer(
            "PIP - " + this.getClass().getSimpleName(), -1000.0F, 1000.0F, true
    );

    public GuiMultiEntityRenderer(MultiBufferSource.BufferSource bufferSource, EntityRenderDispatcher entityRenderDispatcher) {
        super(bufferSource);
        this.entityRenderDispatcher = entityRenderDispatcher;
    }

    @Override
    public Class<GuiMultiEntityRenderState> getRenderStateClass() {
        return GuiMultiEntityRenderState.class;
    }

    @Override
    protected void renderToTexture(GuiMultiEntityRenderState renderState, PoseStack poseStack) {

    }

    @Override
    public void prepare(GuiMultiEntityRenderState renderState, GuiRenderState guiRenderState, int guiScale) {
        int ix = 0;
        for (GuiEntityRenderState state : renderState.renderStates) {
            int i = (state.x1() - state.x0()) * guiScale;
            int j = (state.y1() - state.y0()) * guiScale;
            var texture = this.textures.get(state);
            boolean bl = texture == null || texture.getWidth(0) != i || texture.getHeight(0) != j;
            this.prepareTexturesAndProjection(state, bl, i, j, ix);
            RenderSystem.outputColorTextureOverride = this.textureViews.get(state);
            RenderSystem.outputDepthTextureOverride = this.depthTextureViews.get(state);
            PoseStack poseStack = new PoseStack();
            poseStack.translate(i / 2.0F, this.getTranslateY(j, guiScale), 0.0F);
            float f = guiScale * state.scale();
            poseStack.scale(f, f, -f);
            this.renderToTexture(state, poseStack);
            this.bufferSource.endBatch();
            RenderSystem.outputColorTextureOverride = null;
            RenderSystem.outputDepthTextureOverride = null;
            this.blitTexture(state, guiRenderState);
            ix++;
        }
    }

    @Override
    protected float getTranslateY(int height, int guiScale) {
        return height / 2.0F;
    }

    protected void renderToTexture(GuiEntityRenderState renderState, PoseStack poseStack) {
        Minecraft.getInstance().gameRenderer.getLighting().setupFor(Lighting.Entry.ENTITY_IN_UI);
        Vector3f vector3f = renderState.translation();
        poseStack.translate(vector3f.x, vector3f.y, vector3f.z);
        poseStack.mulPose(renderState.rotation());
        Quaternionf quaternionf = renderState.overrideCameraAngle();
        FeatureRenderDispatcher featurerenderdispatcher = Minecraft.getInstance().gameRenderer.getFeatureRenderDispatcher();
        CameraRenderState camerarenderstate = new CameraRenderState();
        if (quaternionf != null) {
            camerarenderstate.orientation = quaternionf.conjugate(new Quaternionf()).rotateY((float) Math.PI);
        }

        this.entityRenderDispatcher
                .submit(renderState.renderState(), camerarenderstate, 0.0, 0.0, 0.0, poseStack, featurerenderdispatcher.getSubmitNodeStorage());
        featurerenderdispatcher.renderAllFeatures();
    }

    private void prepareTexturesAndProjection(GuiEntityRenderState state, boolean resetTexture, int width, int height, int index) {
        var texture = this.textures.get(state);
        var textureView = this.textureViews.get(state);
        var depthTexture = this.depthTextures.get(state);
        var depthTextureView = this.depthTextureViews.get(state);

        if (texture != null && resetTexture) {
            texture.close();
            this.textures.remove(state);
            texture = null;
            textureView.close();
            this.textureViews.remove(state);
            textureView = null;
            depthTexture.close();
            this.depthTextures.remove(state);
            depthTexture = null;
            depthTextureView.close();
            this.depthTextureViews.remove(state);
            depthTextureView = null;
        }

        GpuDevice gpuDevice = RenderSystem.getDevice();
        if (texture == null) {
            texture = gpuDevice.createTexture(() -> "UI " + this.getTextureLabel() + " texture " + index, 12, TextureFormat.RGBA8, width, height, 1, 1);
            textureView = gpuDevice.createTextureView(texture);
            depthTexture = gpuDevice.createTexture(() -> "UI " + this.getTextureLabel() + " depth texture " + index, 8, TextureFormat.DEPTH32, width, height, 1, 1);
            depthTextureView = gpuDevice.createTextureView(depthTexture);
            this.textures.put(state, texture);
            this.textureViews.put(state, textureView);
            this.depthTextures.put(state, depthTexture);
            this.depthTextureViews.put(state, depthTextureView);
        }

        gpuDevice.createCommandEncoder().clearColorAndDepthTextures(texture, 0, depthTexture, 1.0);
        RenderSystem.setProjectionMatrix(this.projectionMatrixBuffer.getBuffer(width, height), ProjectionType.ORTHOGRAPHIC);
    }

    protected void blitTexture(GuiEntityRenderState renderState, GuiRenderState guiRenderState) {
        guiRenderState.submitBlitToCurrentLayer(
                new BlitRenderState(
                        RenderPipelines.GUI_TEXTURED_PREMULTIPLIED_ALPHA,
                        TextureSetup.singleTexture(this.textureViews.get(renderState), RenderSystem.getSamplerCache().getRepeat(FilterMode.NEAREST)),
                        renderState.pose(),
                        renderState.x0(),
                        renderState.y0(),
                        renderState.x1(),
                        renderState.y1(),
                        0.0F,
                        1.0F,
                        1.0F,
                        0.0F,
                        -1,
                        renderState.scissorArea(),
                        null
                )
        );
    }

    @Override
    public void close() {
        for (GpuTexture texture : this.textures.values()) {
            texture.close();
        }

        for (GpuTextureView texture : this.textureViews.values()) {
            texture.close();
        }

        for (GpuTexture texture : this.depthTextures.values()) {
            texture.close();
        }

        for (GpuTextureView texture : this.depthTextureViews.values()) {
            texture.close();
        }

        this.projectionMatrixBuffer.close();
    }

    @Override
    protected String getTextureLabel() {
        return "multi_entity";
    }
}
