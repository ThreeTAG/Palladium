package net.threetag.palladium.documentation;

import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public interface IDocumentationSettings {

    ResourceLocation getId();

    List<String> getColumns();

    List<Iterable<?>> getRows();

    default JsonElement getExampleJson() {
        return null;
    }

}
