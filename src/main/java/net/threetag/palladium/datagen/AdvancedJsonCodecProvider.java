package net.threetag.palladium.datagen;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.common.conditions.ConditionalOps;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.WithConditions;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public abstract class AdvancedJsonCodecProvider<T> implements DataProvider {
    private static final Logger LOGGER = LogUtils.getLogger();
    protected final PackOutput.PathProvider pathProvider;
    protected final CompletableFuture<HolderLookup.Provider> lookupProvider;
    protected final String modid;
    protected final String directory;
    protected final Codec<T> codec;
    protected final Map<Identifier, WithConditions<T>> conditions = Maps.newHashMap();

    /**
     * @param output    {@linkplain PackOutput} provided by the {@link DataGenerator}.
     * @param directory String representing the directory to generate jsons in, e.g. "dimension" or "cheesemod/cheese".
     * @param codec     Codec to encode values to jsons with using the provided DynamicOps.
     */
    public AdvancedJsonCodecProvider(PackOutput output, PackOutput.Target target, String directory, Codec<T> codec, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId) {
        this.pathProvider = output.createPathProvider(target, directory);
        this.modid = modId;
        this.directory = directory;
        this.codec = codec;
        this.lookupProvider = lookupProvider;
    }

    @Override
    public CompletableFuture<?> run(final CachedOutput cache) {
        ImmutableList.Builder<CompletableFuture<?>> futuresBuilder = new ImmutableList.Builder<>();

        return lookupProvider.thenCompose(provider -> {
            final DynamicOps<JsonElement> dynamicOps = new ConditionalOps<>(RegistryOps.create(JsonOps.INSTANCE, provider), ICondition.IContext.EMPTY);
            this.gather(provider);

            this.conditions.forEach((id, withConditions) -> {
                final Path path = this.pathProvider.json(id);

                futuresBuilder.add(CompletableFuture.supplyAsync(() -> {
                    final Codec<Optional<WithConditions<T>>> withConditionsCodec = ConditionalOps.createConditionalCodecWithConditions(this.codec);
                    return withConditionsCodec.encodeStart(dynamicOps, Optional.of(withConditions)).getOrThrow(msg -> new RuntimeException("Failed to encode %s: %s".formatted(path, msg)));
                }).thenComposeAsync(encoded -> DataProvider.saveStable(cache, encoded, path)));
            });

            return CompletableFuture.allOf(futuresBuilder.build().toArray(CompletableFuture[]::new));
        });
    }

    protected abstract void gather(HolderLookup.Provider provider);

    @Override
    public String getName() {
        return String.format("%s generator for %s", this.directory, this.modid);
    }

    public void unconditional(Identifier id, T value) {
        process(id, new WithConditions<>(List.of(), value));
    }

    public void unconditional(ResourceKey<T> key, T value) {
        this.unconditional(key.identifier(), value);
    }

    public void conditionally(Identifier id, Consumer<WithConditions.Builder<T>> configurator) {
        final WithConditions.Builder<T> builder = new WithConditions.Builder<>();
        configurator.accept(builder);

        final WithConditions<T> withConditions = builder.build();
        process(id, withConditions);
    }

    public void conditionally(ResourceKey<T> key, Consumer<WithConditions.Builder<T>> configurator) {
        this.conditionally(key.identifier(), configurator);
    }

    private void process(Identifier id, WithConditions<T> withConditions) {
        this.conditions.put(id, withConditions);
    }
}