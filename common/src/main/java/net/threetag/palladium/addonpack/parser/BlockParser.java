package net.threetag.palladium.addonpack.parser;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.block.Block;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.addonpack.AddonPackManager;
import net.threetag.palladium.addonpack.builder.BlockBuilder;
import net.threetag.palladium.block.AddonBlock;
import net.threetag.palladium.block.BlockMaterialRegistry;
import net.threetag.palladium.block.IAddonBlock;
import net.threetag.palladium.documentation.HTMLBuilder;
import net.threetag.palladium.documentation.IDocumentedConfigurable;
import net.threetag.palladium.documentation.JsonDocumentationBuilder;
import net.threetag.palladium.util.json.GsonUtil;

import java.util.*;

public class BlockParser extends AddonParser<BlockBuilder> {

    public static final ResourceLocation FALLBACK_SERIALIZER = Palladium.id("default");
    private static final Map<ResourceLocation, BlockTypeSerializer> TYPE_SERIALIZERS = new LinkedHashMap<>();


    public BlockParser() {
        super(GSON, "blocks", Registries.BLOCK);
    }

    @Override
    public BlockBuilder parse(ResourceLocation id, JsonElement jsonElement) {
        JsonObject json = GsonHelper.convertToJsonObject(jsonElement, "$");
        BlockBuilder builder = new BlockBuilder(id, json)
                .type(GsonUtil.getAsResourceLocation(json, "type", null));

        var soundTypeId = GsonUtil.getAsResourceLocation(json, "sound_type");
        var soundType = BlockMaterialRegistry.getSoundType(soundTypeId);

        if (soundType == null) {
            throw new JsonParseException("Unknown block sound type '" + soundTypeId + "'");
        }

        builder.soundType(soundType);
        GsonUtil.ifHasKey(json, "map_color", el -> builder.mapColor(BlockMaterialRegistry.getColor(GsonUtil.convertToResourceLocation(el, "material_color"))));
        GsonUtil.ifHasKey(json, "destroy_time", el -> builder.destroyTime(GsonHelper.convertToFloat(el, "destroy_time")));
        GsonUtil.ifHasKey(json, "explosion_resistance", el -> builder.explosionResistance(GsonHelper.convertToFloat(el, "explosion_resistance")));
        builder.renderType(GsonHelper.getAsString(json, "render_type", null));

        if (GsonHelper.getAsBoolean(json, "no_occlusion", false)) {
            builder.noOcclusion();
        }

        if (GsonHelper.getAsBoolean(json, "requires_correct_tool_for_drops", false)) {
            builder.requiresCorrectToolForDrops();
        }

        if (GsonHelper.getAsBoolean(json, "register_item", true)) {
            List<ItemParser.PlacedTabPlacement> placements = new ArrayList<>();
            GsonUtil.ifHasKey(json, "creative_mode_tab", je -> {
                placements.addAll(GsonUtil.fromListOrPrimitive(je, ItemParser.PlacedTabPlacement::fromJson));
            });
            AddonPackManager.ITEM_PARSER.autoRegisteredBlockItems.put(id, placements);
        }

        return builder;
    }

    public static void registerTypeSerializer(BlockTypeSerializer serializer) {
        TYPE_SERIALIZERS.put(serializer.getId(), serializer);
    }

    public static BlockTypeSerializer getTypeSerializer(ResourceLocation id) {
        return TYPE_SERIALIZERS.get(id);
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
        builder.addProperty("map_color", ResourceLocation.class)
                .description("Determines the color displayed on maps. Possible values: " + Arrays.toString(BlockMaterialRegistry.getAllColorIds().toArray()))
                .fallback(null).exampleJson(new JsonPrimitive("minecraft:color_blue"));
        builder.addProperty("sound_type", ResourceLocation.class)
                .description("Place/break/step sound type of the block. Possible values: " + Arrays.toString(BlockMaterialRegistry.getAllSoundTypeIds().toArray()))
                .required();
        builder.addProperty("destroy_time", Float.class)
                .description("Value that determines how long a player needs to break this block. For reference: stone has 1.5, oak planks have 2.0, obsidian has 50. For insta-break blocks, leave it at 0")
                .fallback(0F).exampleJson(new JsonPrimitive(1.5F));
        builder.addProperty("explosion_resistance", Float.class)
                .description("Value that determines how resistant a block is against explosion. For reference: stone has 6, oak planks have 3, obsidian has 1200. For insta-break blocks, leave it at 0")
                .fallback(0F).exampleJson(new JsonPrimitive(1.5F));
        builder.addProperty("render_type", String.class)
                .description("If your block has a non-cube model or transparent texture, you will NEED to change this. 'solid' is default. 'cutout_mipped' is usually used for leaves. 'cutout' is used for stuff like iron bars, glass, and more. 'translucent' is used for blocks where the background is blended with the alpha values of the texture (used in stained glass, they tint the background behind them). 'translucent' is the most performance-heavy, so use sparingly!")
                .fallback("solid").exampleJson(new JsonPrimitive("solid"));
        builder.addProperty("no_occlusion", Boolean.class)
                .description("Necessary to enable if your block has transparency, like glass")
                .fallback(false).exampleJson(new JsonPrimitive(false));
        builder.addProperty("requires_correct_tool_for_drops", Boolean.class)
                .description("Enable this if your block can only be mined with the correct tool. Requires the tags to be set up!")
                .fallback(false).exampleJson(new JsonPrimitive(false));

        builder.addProperty("register_item", Boolean.class)
                .description("If enabled, a corresponding item for your block will automatically be created. If you want to give the item some unique properties, turn this setting off and make a json for the item using the block_item type")
                .fallback(true).exampleJson(new JsonPrimitive(true));
        builder.addProperty("creative_mode_tab", ResourceLocation.class)
                .description("If register_item is enabled, you can use this setting to set the creative mode tab of the block item")
                .fallback(null).exampleJson(new JsonPrimitive("test:test_tab"));

        return builder;
    }

    public interface BlockTypeSerializer extends IDocumentedConfigurable {

        IAddonBlock parse(JsonObject json, Block.Properties properties);
    }
}
