package net.threetag.palladium.client.renderer;

import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;

public interface RenderTypeFunction {

    RenderType getRenderType(Identifier texture);

}
