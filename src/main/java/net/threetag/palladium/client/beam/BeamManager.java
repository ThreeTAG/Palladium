package net.threetag.palladium.client.beam;

import com.google.common.collect.ImmutableMap;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.addonpack.log.AddonPackLog;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class BeamManager extends SimpleJsonResourceReloadListener<BeamConfiguration> {

    public static final Identifier ID = Palladium.id("beams");
    public static final BeamManager INSTANCE = new BeamManager();

    public Map<Identifier, BeamConfiguration> byName = ImmutableMap.of();

    public BeamManager() {
        super(BeamConfiguration.CODEC, FileToIdConverter.json("palladium/beams"));
    }

    @Override
    protected void apply(Map<Identifier, BeamConfiguration> objects, ResourceManager resourceManager, ProfilerFiller profiler) {
        ImmutableMap.Builder<Identifier, BeamConfiguration> builder = ImmutableMap.builder();
        objects.forEach(builder::put);
        this.byName = builder.build();
        AddonPackLog.info("Loaded {} beams", this.byName.size());
    }

    @Nullable
    public BeamConfiguration get(Identifier id) {
        return this.byName.get(id);
    }
}
