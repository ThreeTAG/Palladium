package net.threetag.palladium.client.dynamictexture;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.client.dynamictexture.transformer.TransformedTexture;
import net.threetag.palladium.util.context.DataContext;
import org.jetbrains.annotations.Nullable;

public class EntityDynamicTexture extends DynamicTexture {

    public static boolean IGNORE_SKIN_CHANGE = false;

    private final boolean ignoreSkinChange;
    @Nullable
    private String output;

    public EntityDynamicTexture(boolean ignoreSkinChange) {
        this.ignoreSkinChange = ignoreSkinChange;
    }

    @Override
    public ResourceLocation getTexture(DataContext context) {
        ResourceLocation texture;

        if (this.ignoreSkinChange) {
            IGNORE_SKIN_CHANGE = true;
            texture = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(context.getEntity()).getTextureLocation(context.getEntity());
            IGNORE_SKIN_CHANGE = false;
            return texture;
        } else {
            texture = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(context.getEntity()).getTextureLocation(context.getEntity());
        }

        if (this.transformers.isEmpty()) {
            return texture;
        }

        if (this.output == null || this.output.isEmpty()) {
            this.output = texture.toString();

            for (String var : this.textureVariableMap.keySet()) {
                if (!this.output.contains("#" + var)) {
                    this.output += "_#" + var;
                }
            }
        }

        ResourceLocation output = new ResourceLocation(DefaultDynamicTexture.replaceVariables(this.output, context, this.textureVariableMap));

        if (!Minecraft.getInstance().getTextureManager().byPath.containsKey(output)) {
            Minecraft.getInstance().getTextureManager().register(output, new TransformedTexture(texture, getNativeImageIfPossible(texture), this.transformers, context, transformerPath -> DefaultDynamicTexture.replaceVariables(transformerPath, context, this.textureVariableMap)));
        }

        return output;
    }

    public static ImageCache getNativeImageIfPossible(ResourceLocation texture) {
        if (Minecraft.getInstance().getTextureManager().getTexture(texture) instanceof NativeImageCached httpTexture) {
            return httpTexture.palladium$getImageCache();
        }
        return null;
    }
}
