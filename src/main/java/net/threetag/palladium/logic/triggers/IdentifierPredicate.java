package net.threetag.palladium.logic.triggers;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

import java.util.Optional;
import java.util.function.Predicate;

public record IdentifierPredicate(Optional<String> namespace, Optional<String> path) implements Predicate<Identifier> {

    public static final Codec<IdentifierPredicate> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.optionalFieldOf("namespace").forGetter(IdentifierPredicate::namespace),
            Codec.STRING.optionalFieldOf("path").forGetter(IdentifierPredicate::path)
    ).apply(instance, IdentifierPredicate::new));

    public static final Codec<IdentifierPredicate> CODEC = Codec.withAlternative(DIRECT_CODEC,
            Codec.either(Codec.STRING, Identifier.CODEC).xmap(either -> either.map(
                    s -> new IdentifierPredicate(Optional.of(s), Optional.empty()),
                    IdentifierPredicate::new
            ), predicate -> predicate.path().isEmpty() ? Either.left(predicate.namespace().orElse(null)) : Either.right(Identifier.fromNamespaceAndPath(predicate.namespace().get(), predicate.path().get())))
    );

    public IdentifierPredicate(Identifier identifier) {
        this(Optional.of(identifier.getNamespace()), Optional.of(identifier.getPath()));
    }

    @Override
    public boolean test(Identifier identifier) {
        if (this.namespace.isPresent() && !identifier.getNamespace().equals(this.namespace.get())) {
            return false;
        }

        return this.path.isEmpty() || identifier.getPath().equals(this.path.get());
    }
}
