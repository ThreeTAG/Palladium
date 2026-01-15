package net.threetag.palladium.customization;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

import java.util.Optional;
import java.util.function.Function;

public record PreviewableRenderLayerIdentifier(Identifier main, Optional<Identifier> preview) {

    private static final Codec<PreviewableRenderLayerIdentifier> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Identifier.CODEC.fieldOf("main").forGetter(PreviewableRenderLayerIdentifier::main),
            Identifier.CODEC.optionalFieldOf("preview").forGetter(PreviewableRenderLayerIdentifier::preview)
    ).apply(instance, PreviewableRenderLayerIdentifier::new));

    public static final Codec<PreviewableRenderLayerIdentifier> CODEC = Codec.either(
            Identifier.CODEC,
            DIRECT_CODEC
    ).xmap(either -> either.map(p -> new PreviewableRenderLayerIdentifier(p, Optional.empty()), Function.identity()),
            previewable -> previewable.preview().isEmpty() ? Either.left(previewable.main()) : Either.right(previewable));

    public PreviewableRenderLayerIdentifier(Identifier main) {
        this(main, Optional.empty());
    }

    public Identifier get(boolean preview) {
        if (preview && this.preview().isPresent()) {
            return this.preview().get();
        } else {
            return this.main();
        }
    }

}
