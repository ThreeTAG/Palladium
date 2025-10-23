package net.threetag.palladium.datagen.internal;

import com.google.gson.JsonElement;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.documentation.Documented;
import net.threetag.palladium.entity.flight.FlightType;
import net.threetag.palladium.entity.flight.FlightTypeSerializer;
import net.threetag.palladium.power.ability.Ability;
import net.threetag.palladium.power.ability.AbilitySerializer;
import net.threetag.palladium.registry.PalladiumRegistries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class PalladiumDocumentationGenerator implements DataProvider {

    private final CompletableFuture<HolderLookup.Provider> registryLookup;

    public PalladiumDocumentationGenerator(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        this.registryLookup = registryLookup;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        return this.registryLookup.thenAccept(provider -> {
            Map<ResourceLocation, List<JsonElement>> generated = new HashMap<>();

            for (AbilitySerializer<?> serializer : PalladiumRegistries.ABILITY_SERIALIZER) {
                if (serializer instanceof Documented<Ability, ? extends Ability> doc) {
                    PalladiumRegistries.ABILITY_SERIALIZER.getResourceKey(serializer).ifPresent(key -> {
                        var json = doc.getDocumentation(provider).build(key);
                        generated.computeIfAbsent(key.registry(), x -> new ArrayList<>()).add(json);
                    });
                }
            }
            for (FlightTypeSerializer<?> serializer : PalladiumRegistries.FLIGHT_TYPE_SERIALIZERS) {
                if (serializer instanceof Documented<FlightType, ? extends FlightType> doc) {
                    PalladiumRegistries.FLIGHT_TYPE_SERIALIZERS.getResourceKey(serializer).ifPresent(key -> {
                        var json = doc.getDocumentation(provider).build(key);
                        generated.computeIfAbsent(key.registry(), x -> new ArrayList<>()).add(json);
                    });
                }
            }

            CodecDocumentationBuilder.createDocFiles(generated);
        });
    }

    @Override
    public String getName() {
        return "Palladium Documentation Generator";
    }
}
