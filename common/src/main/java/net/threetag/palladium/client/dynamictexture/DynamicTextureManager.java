package net.threetag.palladium.client.dynamictexture;

import com.google.common.collect.ImmutableMap;
import com.google.gson.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.addonpack.log.AddonPackLog;
import net.threetag.palladium.client.dynamictexture.transformer.AlphaMaskTextureTransformer;
import net.threetag.palladium.client.dynamictexture.transformer.ColorTextureTransformer;
import net.threetag.palladium.client.dynamictexture.transformer.ITextureTransformer;
import net.threetag.palladium.client.dynamictexture.transformer.OverlayTextureTransformer;
import net.threetag.palladium.client.dynamictexture.variable.*;
import net.threetag.palladium.client.renderer.DynamicColor;
import net.threetag.palladium.documentation.HTMLBuilder;
import net.threetag.palladium.util.json.GsonUtil;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DynamicTextureManager extends SimpleJsonResourceReloadListener {

    private static final Map<ResourceLocation, Function<JsonObject, DynamicTexture>> TYPE_PARSERS = new HashMap<>();
    private static final Map<ResourceLocation, Function<JsonObject, ITextureTransformer>> TRANSFORMER_PARSERS = new HashMap<>();
    private static final Map<ResourceLocation, ITextureVariableSerializer> VARIABLE_PARSERS = new HashMap<>();

    static {
        registerType(Palladium.id("simple"), j -> new SimpleDynamicTexture(GsonUtil.getAsResourceLocation(j, "texture")));
        registerType(Palladium.id("default"), j -> new DefaultDynamicTexture(GsonHelper.getAsString(j, "base"), GsonHelper.getAsString(j, "output", "")));
        registerType(Palladium.id("entity"), j -> new EntityDynamicTexture(GsonHelper.getAsBoolean(j, "ignore_skin_change", false)));

        registerTransformer(Palladium.id("alpha_mask"), j -> new AlphaMaskTextureTransformer(GsonHelper.getAsString(j, "mask")));
        registerTransformer(Palladium.id("overlay"), j -> new OverlayTextureTransformer(GsonHelper.getAsString(j, "overlay"), GsonHelper.getAsBoolean(j, "ignore_blank", false)));
        registerTransformer(Palladium.id("color"), j -> new ColorTextureTransformer(DynamicColor.getFromJson(j, "color"), GsonHelper.getAsBoolean(j, "ignore_blank"), DynamicColor.getFromJson(j, "filter", null)));

        registerVariable(new ConditionTextureVariable.Serializer());
        registerVariable(new CrouchingTextureVariable.Serializer());
        registerVariable(new SmallArmsTextureVariable.Serializer());
        registerVariable(new AbilityTicksTextureVariable.Serializer());
        registerVariable(new EntityTicksTextureVariable.Serializer());
        registerVariable(new AbilityIntegerPropertyVariable.Serializer());
        registerVariable(new AbilityFloatPropertyVariable.Serializer());
        registerVariable(new EntityHealthTextureVariable.Serializer());
        registerVariable(new MoonPhaseTextureVariable.Serializer());
        registerVariable(new IntegerPropertyVariable.Serializer());
        registerVariable(new FloatPropertyVariable.Serializer());
        registerVariable(new CapeTextureVariable.Serializer());
        registerVariable(new AbilityIdTextureVariable.Serializer());
        registerVariable(new OpenableEquipmentProgressVariable.Serializer());
        registerVariable(new ObjectiveScoreTextureVariable.Serializer());
        registerVariable(new AnimationTimerAbilityVariable.Serializer());
        registerVariable(new EnergyBarTextureVariable.Serializer());
        registerVariable(new AccessoryVariable.Serializer());
        registerVariable(new StringPropertyVariable.Serializer());
        registerVariable(new AbilityWheelSelectionTextureVariable.Serializer());
        registerVariable(new AbilityWheelHoveredVariable.Serializer());
        registerVariable(new AbilityWheelDisplayedVariable.Serializer());
    }

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    public static DynamicTextureManager INSTANCE;

    public Map<ResourceLocation, DynamicTexture> byName = ImmutableMap.of();

    public DynamicTextureManager() {
        super(GSON, "palladium/dynamic_textures");
        INSTANCE = this;
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler) {
        ImmutableMap.Builder<ResourceLocation, DynamicTexture> builder = ImmutableMap.builder();
        object.forEach((id, json) -> {
            try {
                builder.put(id, fromJson(json));
            } catch (Exception e) {
                AddonPackLog.error("Parsing error loading dynamic texture {}", id, e);
            }
        });
        this.byName = builder.build();
        AddonPackLog.info("Loaded {} dynamic textures", this.byName.size());
    }

    @Nullable
    public DynamicTexture get(ResourceLocation id) {
        return this.byName.get(id);
    }

    public static DynamicTexture fromJson(JsonElement jsonElement) {
        if (jsonElement.isJsonPrimitive()) {
            var input = jsonElement.getAsString();
            if (input.equalsIgnoreCase("#entity")) {
                return new EntityDynamicTexture(false);
            } else if (input.startsWith("#")) {
                var dyn = DynamicTextureManager.INSTANCE.get(new ResourceLocation(input.substring(1)));

                if (dyn == null) {
                    throw new JsonParseException("Dynamic texture '" + new ResourceLocation(input.substring(1)) + "' can not be found");
                }

                return dyn;
            } else {
                return new SimpleDynamicTexture(new ResourceLocation(input));
            }
        } else if (jsonElement.isJsonObject()) {
            JsonObject json = jsonElement.getAsJsonObject();
            ResourceLocation typeId = GsonUtil.getAsResourceLocation(json, "type", new ResourceLocation(Palladium.MOD_ID, "default"));

            if (!TYPE_PARSERS.containsKey(typeId)) {
                throw new JsonParseException("Unknown dynamic texture type '" + typeId + "'");
            }

            DynamicTexture texture = TYPE_PARSERS.get(typeId).apply(json);

            if (GsonHelper.isValidNode(json, "transformers")) {
                GsonUtil.forEachInListOrPrimitive(json.get("transformers"), j -> {
                    JsonObject transformerJson = GsonHelper.convertToJsonObject(j, "transformers[].$");
                    ResourceLocation transformerId = GsonUtil.getAsResourceLocation(transformerJson, "type", new ResourceLocation(Palladium.MOD_ID, "default"));
                    if (TRANSFORMER_PARSERS.containsKey(transformerId)) {
                        ITextureTransformer transformer = TRANSFORMER_PARSERS.get(transformerId).apply(transformerJson);
                        texture.transform(transformer);
                    } else {
                        AddonPackLog.error("Unknown texture transformer '" + transformerId + "'");
                    }
                });
            }

            if (GsonHelper.isValidNode(json, "variables")) {
                JsonObject variables = GsonHelper.getAsJsonObject(json, "variables");

                for (Map.Entry<String, JsonElement> entry : variables.entrySet()) {
                    JsonObject variableJson = entry.getValue().getAsJsonObject();
                    ResourceLocation variableId = GsonUtil.getAsResourceLocation(variableJson, "type");
                    if (VARIABLE_PARSERS.containsKey(variableId)) {
                        ITextureVariable variable = VARIABLE_PARSERS.get(variableId).parse(variableJson);
                        texture.addVariable(entry.getKey(), variable);
                    } else {
                        AddonPackLog.error("Unknown texture variable '" + variableId + "'");
                    }
                }
            }

            return texture;
        }

        throw new JsonParseException("Dynamic texture must either be a primitive or object");
    }

    public static void registerType(ResourceLocation id, Function<JsonObject, DynamicTexture> function) {
        TYPE_PARSERS.put(id, function);
    }

    public static void registerTransformer(ResourceLocation id, Function<JsonObject, ITextureTransformer> function) {
        TRANSFORMER_PARSERS.put(id, function);
    }

    public static void registerVariable(ITextureVariableSerializer serializer) {
        VARIABLE_PARSERS.put(serializer.getId(), serializer);
    }

    @Nullable
    public static ITextureVariableSerializer getTextureVariableSerializer(ResourceLocation id) {
        return VARIABLE_PARSERS.get(id);
    }

    public static HTMLBuilder variableDocumentationBuilder() {
        return new HTMLBuilder(new ResourceLocation(Palladium.MOD_ID, "dynamic_textures/variables"), "Dynamic Texture Variables")
                .add(HTMLBuilder.heading("Dynamic Texture Variables"))
                .addDocumentationSettings(VARIABLE_PARSERS.values().stream().sorted(Comparator.comparing(o -> o.getId().toString())).collect(Collectors.toList()));
    }
}
