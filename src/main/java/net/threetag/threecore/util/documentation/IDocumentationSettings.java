package net.threetag.threecore.util.documentation;

import com.google.gson.JsonElement;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public interface IDocumentationSettings {

    @OnlyIn(Dist.CLIENT)
    ResourceLocation getId();

    @OnlyIn(Dist.CLIENT)
    List<String> getColumns();

    @OnlyIn(Dist.CLIENT)
    List<Iterable<?>> getRows();

    @OnlyIn(Dist.CLIENT)
    default JsonElement getExampleJson() {
        return null;
    }

}
