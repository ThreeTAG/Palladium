package net.threetag.palladium.client.dynamictexture.variable;

import com.google.gson.JsonObject;
import net.threetag.palladium.documentation.IDocumentedConfigurable;
import net.threetag.palladium.documentation.JsonDocumentationBuilder;

public interface ITextureVariableSerializer extends IDocumentedConfigurable {

    ITextureVariable parse(JsonObject json);

    @Override
    default void generateDocumentation(JsonDocumentationBuilder builder) {
        builder.setTitle(this.getId().toString());
        builder.setDescription(this.getDocumentationDescription());
        this.addDocumentationFields(builder);
    }

    void addDocumentationFields(JsonDocumentationBuilder builder);

    String getDocumentationDescription();

}
