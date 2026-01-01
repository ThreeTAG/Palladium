package net.threetag.palladium.client.trail;

import com.mojang.serialization.Codec;
import net.threetag.palladium.util.PalladiumCodecs;

import java.util.List;

public record TrailConfiguration(List<Trail> trails) {

    public static final Codec<TrailConfiguration> CODEC = PalladiumCodecs.listOrPrimitive(Trail.CODEC).xmap(TrailConfiguration::new, TrailConfiguration::trails);

}
