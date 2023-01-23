package net.threetag.palladium.client.dynamictexture;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.client.dynamictexture.transformer.ITextureTransformer;
import net.threetag.palladium.client.dynamictexture.variable.ITextureVariable;

public class EntityDynamicTexture extends DynamicTexture {

    public static final EntityDynamicTexture INSTANCE = new EntityDynamicTexture();

    @Override
    public ResourceLocation getTexture(LivingEntity entity) {
        return Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(entity).getTextureLocation(entity);
    }

    @Override
    public DynamicTexture transform(ITextureTransformer textureTransformer) {
        return this;
    }

    @Override
    public DynamicTexture addVariable(String name, ITextureVariable variable) {
        return this;
    }
}
