package net.threetag.palladium.documentation;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

public interface IDocumentedConfigurable {

    void generateDocumentation(JsonDocumentationBuilder builder);

    default JsonObject buildExampleJson(JsonObject json, JsonDocumentationBuilder builder) {
        json.addProperty("type", this.getId().toString());

        for (JsonDocumentationBuilder.Entry<?> entry : builder.getEntries()) {
            json.add(entry.getName(), entry.getExampleJson());
        }

        return json;
    }

    ResourceLocation getId();

}
