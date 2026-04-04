package net.threetag.palladium.client.dynamictexture;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.client.dynamictexture.transformer.TransformedTexture;
import net.threetag.palladium.client.dynamictexture.variable.ITextureVariable;
import net.threetag.palladium.util.context.DataContext;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class DefaultDynamicTexture extends DynamicTexture {

    private final String base;
    @Nullable
    private final String baseFallback;
    @Nullable
    private String output;

    public DefaultDynamicTexture(String base) {
        this(base, null, null);
    }

    public DefaultDynamicTexture(ResourceLocation texture, @Nullable String baseFallback) {
        this(texture.toString(), baseFallback, null);
    }

    public DefaultDynamicTexture(String base, @Nullable String baseFallback, @Nullable String output) {
        this.base = base;
        this.baseFallback = baseFallback;
        this.output = output;
    }

    @Override
    public ResourceLocation getTexture(DataContext context) {
        var base = replaceVariables(this.base, context, this.textureVariableMap);
        ResourceLocation baseTexture = new ResourceLocation(base);
        boolean fallbackUsed = false;

        if (this.baseFallback != null && !Minecraft.getInstance().getTextureManager().byPath.containsKey(baseTexture)) {
            base = this.baseFallback;
            baseTexture = new ResourceLocation(this.baseFallback);
            fallbackUsed = true;
        }

        if (this.transformers.isEmpty()) {
            return baseTexture;
        }

        if (this.output == null || this.output.isEmpty()) {
            this.output = this.base;

            for (String var : this.textureVariableMap.keySet()) {
                if (!this.output.contains("#" + var)) {
                    this.output += "_#" + var;
                }
            }
        }

        var outputString = this.output;

        if (fallbackUsed) {
            outputString += "_fallback";
        }

        ResourceLocation output = new ResourceLocation(replaceVariables(outputString, context, this.textureVariableMap));

        if (!Minecraft.getInstance().getTextureManager().byPath.containsKey(output)) {
            Minecraft.getInstance().getTextureManager().register(output, new TransformedTexture(baseTexture, null, this.transformers, context, transformerPath -> replaceVariables(transformerPath, context, this.textureVariableMap)));
        }

        return output;
    }

    public static String replaceVariables(String base, DataContext context, Map<String, ITextureVariable> textureVariableMap) {
        for (Map.Entry<String, ITextureVariable> entry : textureVariableMap.entrySet()) {
            ITextureVariable variable = entry.getValue();
            base = variable.replace(base, entry.getKey(), context);
        }

        return base;
    }
}
