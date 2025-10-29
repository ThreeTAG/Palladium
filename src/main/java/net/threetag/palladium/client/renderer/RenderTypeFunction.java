package net.threetag.palladium.client.renderer;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public interface RenderTypeFunction {

    RenderType getRenderType(ResourceLocation texture);

}
