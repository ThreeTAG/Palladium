package net.threetag.palladium.client.renderer.entity.layer.pack;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.threetag.palladium.util.PalladiumCodecs;

import java.util.Collections;
import java.util.List;

public record PackRenderLayerProperties(List<String> hiddenModelParts) {

    public static final PackRenderLayerProperties DEFAULT = new PackRenderLayerProperties(Collections.emptyList());

    public static final Codec<PackRenderLayerProperties> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            PalladiumCodecs.listOrPrimitive(Codec.STRING).fieldOf("hidden_model_parts").forGetter(PackRenderLayerProperties::hiddenModelParts)
    ).apply(instance, PackRenderLayerProperties::new));

}
