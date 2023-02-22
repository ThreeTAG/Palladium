package net.threetag.palladium.addonpack.parser;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.block.Block;
import net.threetag.palladium.addonpack.builder.AddonBuilder;
import net.threetag.palladium.addonpack.builder.BlockBuilder;
import net.threetag.palladium.block.IAddonBlock;
import net.threetag.palladium.documentation.IDocumentedConfigurable;

import java.util.LinkedHashMap;
import java.util.Map;

public class BlockParser extends AddonParser<Block> {

    private static final Map<ResourceLocation, BlockTypeSerializer> TYPE_SERIALIZERS = new LinkedHashMap<>();

    public BlockParser() {
        super(GSON, "blocks", Registry.BLOCK_REGISTRY);
    }

    @Override
    public AddonBuilder<Block> parse(ResourceLocation id, JsonElement jsonElement) {
        JsonObject json = GsonHelper.convertToJsonObject(jsonElement, "$");
        BlockBuilder builder = new BlockBuilder(id, json);



        return builder;
    }

    public static void registerTypeSerializer(BlockTypeSerializer serializer) {
        TYPE_SERIALIZERS.put(serializer.getId(), serializer);
    }

    public interface BlockTypeSerializer extends IDocumentedConfigurable {

        IAddonBlock parse(JsonObject json, Block.Properties properties);
    }
}
