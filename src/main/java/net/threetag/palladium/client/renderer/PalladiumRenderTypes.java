package net.threetag.palladium.client.renderer;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.OutputTarget;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterRenderPipelinesEvent;
import net.threetag.palladium.Palladium;

@EventBusSubscriber(modid = Palladium.MOD_ID, value = Dist.CLIENT)
public abstract class PalladiumRenderTypes  {

    public static final RenderType LASER = RenderType.create(Palladium.MOD_ID + ":laser", RenderSetup.builder(Pipelines.ADD)
            .setOutputTarget(OutputTarget.WEATHER_TARGET).sortOnUpload().createRenderSetup());

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

    @SubscribeEvent
    static void registerPipelines(RegisterRenderPipelinesEvent e) {
        e.registerPipeline(Pipelines.ADD);
    }
}
