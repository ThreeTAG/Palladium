package net.threetag.palladium.client.renderer;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.RenderType;
import net.threetag.palladium.Palladium;

public abstract class PalladiumRenderTypes extends RenderType {

    public static final RenderType LASER = create(Palladium.MOD_ID + ":laser", 1536, false, true, Pipelines.ADD, CompositeState.builder()
            .setTextureState(NO_TEXTURE)
            .createCompositeState(true));

    public PalladiumRenderTypes(String name, int bufferSize, boolean affectsCrumbling, boolean sortOnUpload, Runnable setupState, Runnable clearState) {
        super(name, bufferSize, affectsCrumbling, sortOnUpload, setupState, clearState);
    }

    public static class Pipelines {

        public static final RenderPipeline ADD = RenderPipeline.builder(RenderPipelines.ENTITY_SNIPPET)
                .withCull(false)
                .withLocation(Palladium.id("add"))
                .withVertexShader("core/rendertype_lightning")
                .withFragmentShader("core/rendertype_lightning")
                .withBlend(BlendFunction.LIGHTNING)
                .withVertexFormat(DefaultVertexFormat.POSITION_COLOR_LIGHTMAP, VertexFormat.Mode.QUADS)
                .build();

    }
}
