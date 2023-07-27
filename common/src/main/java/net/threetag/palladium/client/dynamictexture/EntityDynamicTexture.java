package net.threetag.palladium.client.dynamictexture;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.threetag.palladium.client.dynamictexture.transformer.ITextureTransformer;
import net.threetag.palladium.client.dynamictexture.variable.ITextureVariable;

public class EntityDynamicTexture extends DynamicTexture {

    public static boolean IGNORE_SKIN_CHANGE = false;

    private final boolean ignoreSkinChange;

    public EntityDynamicTexture(boolean ignoreSkinChange) {
        this.ignoreSkinChange = ignoreSkinChange;
    }

    @Override
    public ResourceLocation getTexture(Entity entity) {
        if (this.ignoreSkinChange) {
            IGNORE_SKIN_CHANGE = true;
            var texture = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(entity).getTextureLocation(entity);
            IGNORE_SKIN_CHANGE = false;
            return texture;
        } else {
            return Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(entity).getTextureLocation(entity);
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
