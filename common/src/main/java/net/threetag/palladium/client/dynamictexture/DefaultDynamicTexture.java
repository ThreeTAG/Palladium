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
    private String output;

    public DefaultDynamicTexture(ResourceLocation texture) {
        this(texture.toString(), null);
    }

    public DefaultDynamicTexture(String base, @Nullable String output) {
        this.base = base;
        this.output = output;
    }

    @Override
    public ResourceLocation getTexture(DataContext context) {
        if (this.transformers.isEmpty()) {
            return new ResourceLocation(replaceVariables(this.base, context, this.textureVariableMap));
        }

        if (this.output == null || this.output.isEmpty()) {
            this.output = this.base;

            for (String var : this.textureVariableMap.keySet()) {
                if (!this.output.contains("#" + var)) {
                    this.output += "_#" + var;
                }
            }
        }

        ResourceLocation output = new ResourceLocation(replaceVariables(this.output, context, this.textureVariableMap));

        if (!Minecraft.getInstance().getTextureManager().byPath.containsKey(output)) {
            String s = replaceVariables(this.base, context, this.textureVariableMap);
            ResourceLocation texture = new ResourceLocation(s);
            Minecraft.getInstance().getTextureManager().register(output, new TransformedTexture(texture, null, this.transformers, context, transformerPath -> replaceVariables(transformerPath, context, this.textureVariableMap)));
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
