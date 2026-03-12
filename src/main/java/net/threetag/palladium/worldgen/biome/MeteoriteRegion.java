package net.threetag.palladium.worldgen.biome;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import terrablender.api.Region;
import terrablender.api.RegionType;
import terrablender.api.VanillaParameterOverlayBuilder;

import java.util.function.Consumer;

import static terrablender.api.ParameterUtils.*;

public class MeteoriteRegion extends Region {

    private static final Climate.Parameter FULL_RANGE = Climate.Parameter.span(-1.0F, 1.0F);

    public MeteoriteRegion(Identifier name, int weight) {
        super(name, RegionType.OVERWORLD, weight);
    }

    @Override
    public void addBiomes(Registry<Biome> registry, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
        this.addModifiedVanillaOverworldBiomes(mapper, builder -> {});

        VanillaParameterOverlayBuilder builder = new VanillaParameterOverlayBuilder();
        new ParameterPointListBuilder()
                .continentalness(Continentalness.FAR_INLAND)
                .depth(Depth.UNDERGROUND)
                .build().forEach(point -> builder.add(point, PalladiumBiomes.VIBRANIUM_METEORITE));

        builder.build().forEach(mapper);
    }
}
