package net.threetag.palladium.client.texture;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;
import net.threetag.palladium.client.texture.transformer.TextureTransformer;
import net.threetag.palladium.client.texture.transformer.TransformedTexture;
import net.threetag.palladium.documentation.SettingType;
import net.threetag.palladium.logic.value.Value;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.util.PalladiumCodecs;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ConfiguredTexture extends DynamicTexture {

    public static final MapCodec<ConfiguredTexture> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("base").forGetter(t -> t.base),
            Codec.unboundedMap(Codec.STRING, Value.CODEC).optionalFieldOf("variables", Collections.emptyMap()).forGetter(t -> t.variables),
            PalladiumCodecs.listOrPrimitive(TextureTransformer.CODEC).optionalFieldOf("transformers", Collections.emptyList()).forGetter(t -> t.transformers)
    ).apply(instance, ConfiguredTexture::new));

    private final String base;
    protected final Map<String, Value> variables;
    protected final List<TextureTransformer> transformers;
    public final String rawOutputPath;

    public ConfiguredTexture(String base, Map<String, Value> variables, List<TextureTransformer> transformers) {
        this.base = base;
        this.variables = variables;
        this.transformers = transformers;

        if (this.variables.isEmpty()) {
            this.rawOutputPath = this.base + "_modified";
        } else {
            StringBuilder output = new StringBuilder(this.base);

            for (String var : this.variables.keySet()) {
                if (!output.toString().contains("#" + var)) {
                    output.append("_#").append(var);
                }
            }

            this.rawOutputPath = output.toString();
        }
    }

    @Override
    public Identifier getTexture(DataContext context) {
        if (this.variables.isEmpty() && this.transformers.isEmpty()) {
            return Identifier.parse(this.base);
        }

        if (this.transformers.isEmpty()) {
            return Identifier.parse(replaceVariables(this.base, context, this.variables));
        }

        Identifier output = Identifier.parse(replaceVariables(this.rawOutputPath, context, this.variables));

        if (!Minecraft.getInstance().getTextureManager().byPath.containsKey(output)) {
            String s = replaceVariables(this.base, context, this.variables);
            Identifier texture = Identifier.parse(s);
            Minecraft.getInstance().getTextureManager().registerAndLoad(output, new TransformedTexture(output, texture, this.transformers, transformerPath -> replaceVariables(transformerPath, context, this.variables)));
        }

        return output;
    }

    public static String replaceVariables(String base, DataContext context, Map<String, Value> variables) {
        for (Map.Entry<String, Value> entry : variables.entrySet()) {
            Value variable = entry.getValue();
            base = variable.replace(base, entry.getKey(), context);
        }

        return base;
    }

    @Override
    public DynamicTextureSerializer<?> getSerializer() {
        return DynamicTextureSerializers.CONFIGURED;
    }

    public static class Serializer extends DynamicTextureSerializer<ConfiguredTexture> {

        @Override
        public MapCodec<ConfiguredTexture> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<DynamicTexture, ConfiguredTexture> builder, HolderLookup.Provider provider) {
            builder.setName("Configured Texture").setDescription("Creates a dynamic texture based on variables and transformers.")
                    .add("base", TYPE_IDENTIFIER, "Base texture")
                    .addOptional("variables", TYPE_MAP_VARIABLES, "A map of variables that can be used in the texture path.")
                    .addOptional("transformers", SettingType.listOrPrimitive("Texture Transformer"), "Texture transformers applied on this texture");
        }
    }
}
