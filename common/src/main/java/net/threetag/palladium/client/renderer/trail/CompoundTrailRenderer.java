package net.threetag.palladium.client.renderer.trail;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.documentation.JsonDocumentationBuilder;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("rawtypes")
public class CompoundTrailRenderer extends TrailRenderer {

    private final List<TrailRenderer<?>> trailRenderers;

    public CompoundTrailRenderer(List<TrailRenderer<?>> trailRenderers) {
        this.trailRenderers = trailRenderers;
    }

    public List<TrailRenderer<?>> getTrailRenderers() {
        return this.trailRenderers;
    }

    @Override
    public float getSpacing() {
        throw new AssertionError();
    }

    @Override
    public int getLifetime() {
        throw new AssertionError();
    }

    public static class Serializer implements TrailRendererManager.TypeSerializer {

        @Override
        public TrailRenderer<?> parse(JsonObject json) {
            var trailsElement = GsonHelper.getAsJsonArray(json, "trails");
            List<TrailRenderer<?>> trails = new ArrayList<>();

            for (JsonElement element : trailsElement) {
                trails.add(TrailRendererManager.fromJson(GsonHelper.convertToJsonObject(element, "trails[].$")));
            }

            return new CompoundTrailRenderer(trails);
        }

        @Override
        public void generateDocumentation(JsonDocumentationBuilder builder) {
            builder.setTitle("Compound");
            builder.setDescription("Can be used to merge multiple trails into one file.");

            builder.addProperty("trails", TrailRenderer[].class)
                    .description("Can be used to merge multiple trails into one file. Simply declare them in this array");
        }

        @Override
        public ResourceLocation getId() {
            return Palladium.id("compound");
        }
    }
}
