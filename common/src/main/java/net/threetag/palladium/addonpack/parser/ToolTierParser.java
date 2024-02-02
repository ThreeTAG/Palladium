package net.threetag.palladium.addonpack.parser;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.addonpack.log.AddonPackLog;
import net.threetag.palladium.documentation.HTMLBuilder;
import net.threetag.palladium.documentation.JsonDocumentationBuilder;
import net.threetag.palladium.util.json.GsonUtil;
import net.threetag.palladiumcore.item.SimpleToolTier;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class ToolTierParser extends SimpleJsonResourceReloadListener {

    private static final Map<ResourceLocation, Tier> TIERS = new HashMap<>();

    public ToolTierParser() {
        super(AddonParser.GSON, "tool_tiers");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler) {
        registerToolTier(new ResourceLocation("wood"), Tiers.WOOD);
        registerToolTier(new ResourceLocation("stone"), Tiers.STONE);
        registerToolTier(new ResourceLocation("iron"), Tiers.IRON);
        registerToolTier(new ResourceLocation("gold"), Tiers.GOLD);
        registerToolTier(new ResourceLocation("diamond"), Tiers.DIAMOND);
        registerToolTier(new ResourceLocation("netherite"), Tiers.NETHERITE);

        AtomicInteger i = new AtomicInteger();
        object.forEach((id, jsonElement) -> {
            try {
                JsonObject json = GsonHelper.convertToJsonObject(jsonElement, "$");
                SimpleToolTier toolTier = parse(id, json);
                registerToolTier(id, toolTier);
                i.getAndIncrement();
            } catch (Exception e) {
                CrashReport crashReport = CrashReport.forThrowable(e, "Error while parsing addonpack tool tier " + " '" + id + "'");

                CrashReportCategory reportCategory = crashReport.addCategory("Addon Tool Tier", 1);
                reportCategory.setDetail("Resource name", id);

                throw new ReportedException(crashReport);
            }
        });

        AddonPackLog.info("Registered " + i.get() + " addonpack tool tiers");
    }

    public static void registerToolTier(ResourceLocation id, Tier tier) {
        TIERS.put(id, tier);
    }

    public static Tier getToolTier(ResourceLocation id) {
        return TIERS.get(id);
    }

    public static Set<ResourceLocation> getIds() {
        return TIERS.keySet();
    }

    public static SimpleToolTier parse(ResourceLocation id, JsonObject json) {
        return new SimpleToolTier(
                GsonUtil.getAsIntMin(json, "level", 0),
                GsonUtil.getAsIntMin(json, "uses", 1),
                GsonUtil.getAsFloatMin(json, "speed", 0),
                GsonUtil.getAsFloatMin(json, "attack_damage_bonus", 0),
                GsonUtil.getAsIntMin(json, "enchantment_value", 0),
                () -> json.has("repair_ingredient") ? Ingredient.fromJson(json.get("repair_ingredient")) : Ingredient.EMPTY);
    }

    public static HTMLBuilder documentationBuilder() {
        JsonDocumentationBuilder builder = new JsonDocumentationBuilder()
                .setDescription("Each tool type goes into a seperate file into the 'addon/[namespace]/tool_tiers' folder, which can then be used for custom tools (swords, pickaxes, etc.).");

        builder.addProperty("level", Integer.class)
                .description("Determines the mining level and what blocks can be harvested. For reference: iron has 2, diamond has 3. So obsidian can only be mined with tools with the level 3 or above, thats why you need a diamond pickaxe for it")
                .required().exampleJson(new JsonPrimitive(2));

        builder.addProperty("uses", Integer.class)
                .description("Determines the durability for tool. For reference: iron has 250, diamond has 1561")
                .required().exampleJson(new JsonPrimitive(420));

        builder.addProperty("speed", Float.class)
                .description("Determines the mining speed. For reference: iron has 6.0, diamond has 8.0")
                .required().exampleJson(new JsonPrimitive(6.9F));

        builder.addProperty("attack_damage_bonus", Float.class)
                .description("Determines the additional attack damage. For reference: iron has 2.0, diamond has 3.0")
                .required().exampleJson(new JsonPrimitive(2.5F));

        builder.addProperty("enchantment_value", Integer.class)
                .description("Determines the enchantibility of the item. For reference: iron has 9, diamond 10, gold 25.")
                .required().exampleJson(new JsonPrimitive(12));

        builder.addProperty("repair_ingredient", Ingredient.class)
                .description("Ingredient definition for repairing the item in an anvil. Can be defined like in recipes.")
                .fallback(Ingredient.EMPTY, "empty ingredient").exampleJson(Ingredient.of(ItemTags.DIRT).toJson());

        return new HTMLBuilder(new ResourceLocation(Palladium.MOD_ID, "tool_tiers"), "Tool Tiers").add(HTMLBuilder.heading("Tool Tiers")).addDocumentation(builder);
    }

}
