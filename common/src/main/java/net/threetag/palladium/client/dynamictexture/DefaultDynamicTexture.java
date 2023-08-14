package net.threetag.palladium.client.dynamictexture;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.client.dynamictexture.transformer.ITextureTransformer;
import net.threetag.palladium.client.dynamictexture.transformer.TransformedTexture;
import net.threetag.palladium.client.dynamictexture.variable.ITextureVariable;
import net.threetag.palladium.util.context.DataContext;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class DefaultDynamicTexture extends DynamicTexture {

    private final String base;
    @Nullable
    private String output;
    private final Map<String, ITextureVariable> textureVariableMap = Maps.newHashMap();
    private final List<ITextureTransformer> transformers = Lists.newLinkedList();

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
                if(!this.output.contains("#" + var)) {
                    this.output += "_#" + var;
                }
            }
        }

        ResourceLocation output = new ResourceLocation(replaceVariables(this.output, context, this.textureVariableMap));

        if (!Minecraft.getInstance().getTextureManager().byPath.containsKey(output)) {
            String s = replaceVariables(this.base, context, this.textureVariableMap);
            ResourceLocation texture = new ResourceLocation(s);
            Minecraft.getInstance().getTextureManager().register(output, new TransformedTexture(texture, this.transformers, transformerPath -> replaceVariables(transformerPath, context, this.textureVariableMap)));
        }

        return output;
    }

    @Override
    public DynamicTexture transform(ITextureTransformer textureTransformer) {
        this.transformers.add(textureTransformer);
        return this;
    }

    @Override
    public DynamicTexture addVariable(String name, ITextureVariable variable) {
        this.textureVariableMap.put(name, variable);
        return this;
    }

    public static String replaceVariables(String base, DataContext context, Map<String, ITextureVariable> textureVariableMap) {
        for (Map.Entry<String, ITextureVariable> entry : textureVariableMap.entrySet()) {
            ITextureVariable variable = entry.getValue();
            base = variable.replace(base, entry.getKey(), context);
        }

        return base;
    }
}
