package net.threetag.palladium.addonpack.parser;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.block.Block;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.addonpack.builder.AddonBuilder;
import net.threetag.palladium.addonpack.builder.BlockBuilder;
import net.threetag.palladium.block.AddonBlock;
import net.threetag.palladium.block.BlockMaterialRegistry;
import net.threetag.palladium.block.IAddonBlock;
import net.threetag.palladium.documentation.HTMLBuilder;
import net.threetag.palladium.documentation.IDocumentedConfigurable;
import net.threetag.palladium.documentation.JsonDocumentationBuilder;
import net.threetag.palladium.util.json.GsonUtil;

import java.util.ArrayList;
import java.util.Arrays;
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
        BlockBuilder builder = new BlockBuilder(id, json)
                .type(TYPE_SERIALIZERS.get(GsonUtil.getAsResourceLocation(json, "type", null)));

        var materialId = GsonUtil.getAsResourceLocation(json, "material");
        var material = BlockMaterialRegistry.get(materialId);

        if (material == null) {
            throw new JsonParseException("Unknown block material '" + materialId + "'");
        }

        builder.material(material);
        GsonUtil.ifHasKey(json, "material_color", el -> builder.materialColor(BlockMaterialRegistry.getColor(GsonUtil.convertToResourceLocation(el, "material_color"))));
        GsonUtil.ifHasKey(json, "destroy_time", el -> builder.destroyTime(GsonHelper.convertToFloat(el, "destroy_time")));
        GsonUtil.ifHasKey(json, "explosion_resistance", el -> builder.explosionResistance(GsonHelper.convertToFloat(el, "explosion_resistance")));
        builder.renderType(GsonHelper.getAsString(json, "render_type", null));

        return builder;
    }

    public static void registerTypeSerializer(BlockTypeSerializer serializer) {
        TYPE_SERIALIZERS.put(serializer.getId(), serializer);
    }

    static {
        registerTypeSerializer(new AddonBlock.Parser());
    }

    public static HTMLBuilder documentationBuilder() {
        return new HTMLBuilder(new ResourceLocation(Palladium.MOD_ID, "blocks"), "Blocks")
                .add(HTMLBuilder.heading("Blocks"))
                .add(HTMLBuilder.subHeading("Global Settings"))
                .addDocumentation(getDefaultDocumentationBuilder())
                .addDocumentationSettings(new ArrayList<>(TYPE_SERIALIZERS.values()));
    }

    public static JsonDocumentationBuilder getDefaultDocumentationBuilder() {
        JsonDocumentationBuilder builder = new JsonDocumentationBuilder();

        builder.setDescription("These settings apply to ALL block types. Keep in mind that if fields are not required, you do NOT need to write them into your json.");

        builder.addProperty("type", ResourceLocation.class)
                .description("Block Type, each come with new different settings. Listed below on this page.")
                .fallback(new ResourceLocation("palladium:default"));
        builder.addProperty("material", ResourceLocation.class)
                .description("Material of the block. Determines some properties like flammability, piston reaction, solid, and more. Possible values: " + Arrays.toString(BlockMaterialRegistry.getAllIds().toArray()))
                .required().exampleJson(new JsonPrimitive("minecraft:stone"));
        builder.addProperty("material_color", ResourceLocation.class)
                .description("Material color of the block. Determines the color displayed on maps. If not specified, it will use the default color of the material. Possible values: " + Arrays.toString(BlockMaterialRegistry.getAllColorIds().toArray()))
                .fallback(null).exampleJson(new JsonPrimitive("minecraft:color_blue"));
        builder.addProperty("destroy_time", Float.class)
                .description("Value that determines how long a player needs to break this block. For reference: stone has 1.5, oak planks have 2.0, obsidian has 50. For insta-break blocks, leave it at 0")
                .fallback(0F).exampleJson(new JsonPrimitive(1.5F));
        builder.addProperty("explosion_resistance", Float.class)
                .description("Value that determines how resistant a block is against explosion. For reference: stone has 6, oak planks have 3, obsidian has 1200. For insta-break blocks, leave it at 0")
                .fallback(0F).exampleJson(new JsonPrimitive(1.5F));
        builder.addProperty("render_type", String.class)
                .description("If your block has a non-cube model or transparent texture, you will NEED to change this. 'solid' is default. 'cutout_mipped' is usually used for leaves. 'cutout' is used for stuff like iron bars, glass, and more. 'translucent' is used for blocks where the background is blended with the alpha values of the texture (used in stained glass, they tint the background behind them). 'translucent' is the most performance-heavy, so use sparingly!")
                .fallback("solid").exampleJson(new JsonPrimitive("solid"));

        return builder;
    }

    public interface BlockTypeSerializer extends IDocumentedConfigurable {

        IAddonBlock parse(JsonObject json, Block.Properties properties);
    }
}
