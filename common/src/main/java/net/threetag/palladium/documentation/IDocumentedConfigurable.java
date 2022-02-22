package net.threetag.palladium.documentation;

import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public interface IDocumentedConfigurable {

    ResourceLocation getId();

    default String getTitle() {
        return this.getId().toString();
    }

    List<String> getColumns();

    List<Iterable<?>> getRows();

    default JsonElement getExampleJson() {
        return null;
    }

}
