package net.threetag.palladium.client.renderer;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.Palladium;

import java.util.function.Function;

public class PalladiumRenderTypes extends RenderType {

    public PalladiumRenderTypes(String string, VertexFormat vertexFormat, VertexFormat.Mode mode, int i, boolean bl, boolean bl2, Runnable runnable, Runnable runnable2) {
        super(string, vertexFormat, mode, i, bl, bl2, runnable, runnable2);
    }

    private static final Function<ResourceLocation, RenderType> GLOWING = Util.memoize((resourceLocation) -> {
        RenderType.CompositeState compositeState = RenderType.CompositeState.builder().setShaderState(RENDERTYPE_ENERGY_SWIRL_SHADER).setTextureState(new TextureStateShard(resourceLocation, false, false)).setTransparencyState(TRANSLUCENT_TRANSPARENCY).setCullState(NO_CULL).setLightmapState(LIGHTMAP).setOverlayState(OVERLAY).setLayeringState(VIEW_OFFSET_Z_LAYERING).createCompositeState(true);
        return create(Palladium.MOD_ID + ":glowing", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, true, compositeState);
    });

    public static final RenderType LASER = create(Palladium.MOD_ID + ":laser", DefaultVertexFormat.POSITION_COLOR_LIGHTMAP, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder()
            .setShaderState(RENDERTYPE_LIGHTNING_SHADER)
            .setTextureState(NO_TEXTURE)
            .setCullState(NO_CULL)
            .setWriteMaskState(COLOR_DEPTH_WRITE)
            .setLightmapState(LIGHTMAP)
            .setTransparencyState(RenderStateShard.LIGHTNING_TRANSPARENCY)
            .setLayeringState(VIEW_OFFSET_Z_LAYERING)
            .createCompositeState(true));

    private static final Function<ResourceLocation, RenderType> ARMOR_CUTOUT_NO_CULL_TRANSPARENCY = Util.memoize(
            resourceLocation -> {
                CompositeState compositeState = CompositeState.builder()
                        .setShaderState(RENDERTYPE_ARMOR_CUTOUT_NO_CULL_SHADER)
                        .setTextureState(new TextureStateShard(resourceLocation, false, false))
                        .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
                        .setCullState(NO_CULL)
                        .setLightmapState(LIGHTMAP)
                        .setOverlayState(OVERLAY)
                        .setLayeringState(VIEW_OFFSET_Z_LAYERING)
                        .createCompositeState(true);
                return create(Palladium.MOD_ID + ":armor_cutout_no_cull_transparency", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, false, compositeState);
            }
    );

    public static RenderType getGlowing(ResourceLocation texture) {
        return GLOWING.apply(texture);
    }

    public static RenderType getArmorTranslucent(ResourceLocation texture) {
        return ARMOR_CUTOUT_NO_CULL_TRANSPARENCY.apply(texture);
    }

}
