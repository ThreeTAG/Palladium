package net.threetag.threecore.util.modellayer.texture;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.threetag.threecore.util.modellayer.IModelLayerContext;
import net.threetag.threecore.util.modellayer.texture.transformer.ITextureTransformer;
import net.threetag.threecore.util.modellayer.texture.transformer.TransformedTexture;
import net.threetag.threecore.util.modellayer.texture.variable.ITextureVariable;

import java.util.List;
import java.util.Map;

public class DefaultModelTexture extends ModelLayerTexture {

    private final String base;
    private final String output;
    private final Map<String, ITextureVariable> textureVariableMap = Maps.newHashMap();
    private List<ITextureTransformer> transformers = Lists.newLinkedList();

    public DefaultModelTexture(String base, String output) {
        this.base = base;
        this.output = output;
    }

    @Override
    public ResourceLocation getTexture(IModelLayerContext context) {
        String s = replaceVariables(base, context, this.textureVariableMap);

        ResourceLocation texture = new ResourceLocation(s);
        ResourceLocation output = new ResourceLocation(replaceVariables(this.output, context, this.textureVariableMap));

        if (Minecraft.getInstance().getTextureManager().getTexture(output) == null) {
            Minecraft.getInstance().getTextureManager().loadTexture(output, new TransformedTexture(texture, this.transformers, transformerPath -> replaceVariables(transformerPath, context, this.textureVariableMap)));
        }

        return output;
    }

    @Override
    public ModelLayerTexture transform(ITextureTransformer textureTransformer) {
        this.transformers.add(textureTransformer);
        return this;
    }

    public DefaultModelTexture addVariable(String key, ITextureVariable textureVariable) {
        this.textureVariableMap.put(key, textureVariable);
        return this;
    }

    public static String replaceVariables(String base, IModelLayerContext context, Map<String, ITextureVariable> textureVariableMap) {
        for (Map.Entry<String, ITextureVariable> entry : textureVariableMap.entrySet()) {
            ITextureVariable variable = entry.getValue();
            base = variable.replace(base, entry.getKey(), context);
        }

        return base;
    }
}
