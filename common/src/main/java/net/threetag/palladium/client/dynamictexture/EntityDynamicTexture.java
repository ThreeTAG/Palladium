package net.threetag.palladium.client.dynamictexture;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.client.dynamictexture.transformer.ITextureTransformer;
import net.threetag.palladium.client.dynamictexture.variable.ITextureVariable;
import net.threetag.palladium.util.context.DataContext;

public class EntityDynamicTexture extends DynamicTexture {

    public static boolean IGNORE_SKIN_CHANGE = false;

    private final boolean ignoreSkinChange;

    public EntityDynamicTexture(boolean ignoreSkinChange) {
        this.ignoreSkinChange = ignoreSkinChange;
    }

    @Override
    public ResourceLocation getTexture(DataContext context) {
        if (this.ignoreSkinChange) {
            IGNORE_SKIN_CHANGE = true;
            var texture = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(context.getEntity()).getTextureLocation(context.getEntity());
            IGNORE_SKIN_CHANGE = false;
            return texture;
        } else {
            return Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(context.getEntity()).getTextureLocation(context.getEntity());
        }
    }

    @Override
    public DynamicTexture transform(ITextureTransformer textureTransformer) {
        throw new IllegalStateException("Cant transform entity textures");
    }

    @Override
    public DynamicTexture addVariable(String name, ITextureVariable variable) {
        throw new IllegalStateException("Cant add variables to entity textures");
    }
}
