package net.threetag.palladium.addonpack.parser;

import com.google.gson.*;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.crafting.Ingredient;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.addonpack.log.AddonPackLog;
import net.threetag.palladium.documentation.HTMLBuilder;
import net.threetag.palladium.documentation.JsonDocumentationBuilder;
import net.threetag.palladium.util.json.GsonUtil;
import net.threetag.palladiumcore.item.SimpleArmorMaterial;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ArmorMaterialParser extends SimpleJsonResourceReloadListener {

    private static final Map<ResourceLocation, ArmorMaterial> ARMOR_MATERIALS = new HashMap<>();

    public ArmorMaterialParser() {
        super(AddonParser.GSON, "armor_materials");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler) {
        registerArmorMaterial(new ResourceLocation("leather"), ArmorMaterials.LEATHER);
        registerArmorMaterial(new ResourceLocation("chainmail"), ArmorMaterials.CHAIN);
        registerArmorMaterial(new ResourceLocation("iron"), ArmorMaterials.IRON);
        registerArmorMaterial(new ResourceLocation("gold"), ArmorMaterials.GOLD);
        registerArmorMaterial(new ResourceLocation("diamond"), ArmorMaterials.DIAMOND);
        registerArmorMaterial(new ResourceLocation("turtle"), ArmorMaterials.TURTLE);
        registerArmorMaterial(new ResourceLocation("netherite"), ArmorMaterials.NETHERITE);

        AtomicInteger i = new AtomicInteger();
        object.forEach((id, jsonElement) -> {
            try {
                JsonObject json = GsonHelper.convertToJsonObject(jsonElement, "$");
                SimpleArmorMaterial armorMaterial = parse(id, json);
                registerArmorMaterial(id, armorMaterial);
                i.getAndIncrement();
            } catch (Exception e) {
                CrashReport crashReport = CrashReport.forThrowable(e, "Error while parsing addonpack armor material " + " '" + id + "'");

                CrashReportCategory reportCategory = crashReport.addCategory("Addon Armor Material", 1);
                reportCategory.setDetail("Resource name", id);

                throw new ReportedException(crashReport);
            }
        });

        AddonPackLog.info("Registered " + i.get() + " addonpack armor materials");
    }

    public static void registerArmorMaterial(ResourceLocation id, ArmorMaterial armorMaterial) {
        ARMOR_MATERIALS.put(id, armorMaterial);
    }

    public static ArmorMaterial getArmorMaterial(ResourceLocation id) {
        return ARMOR_MATERIALS.get(id);
    }

    public static Set<ResourceLocation> getIds() {
        return ARMOR_MATERIALS.keySet();
    }

    public static SimpleArmorMaterial parse(ResourceLocation id, JsonObject json) {
        return new SimpleArmorMaterial(id.getPath(),
                GsonUtil.getAsIntMin(json, "durability_multiplier", 0),
                parseArmorProtectionMap(json.get("slot_protections")),
                GsonUtil.getAsIntMin(json, "enchantment_value", 0),
                () -> BuiltInRegistries.SOUND_EVENT.get(GsonUtil.getAsResourceLocation(json, "equip_sound")),
                GsonHelper.getAsFloat(json, "toughness", 0),
                GsonHelper.getAsFloat(json, "knockback_resistance", 0),
                () -> json.has("repair_ingredient") ? Ingredient.fromJson(json.get("repair_ingredient")) : Ingredient.EMPTY);
    }

    public static EnumMap<ArmorItem.Type, Integer> parseArmorProtectionMap(JsonElement jsonElement) {
        EnumMap<ArmorItem.Type, Integer> map = new EnumMap<>(ArmorItem.Type.class);
        if (jsonElement.isJsonArray()) {
            var array = jsonElement.getAsJsonArray();

            for (int i = 0; i < array.size(); i++) {
                ArmorItem.Type type = ArmorItem.Type.values()[3 - i];
                map.put(type, array.get(i).getAsInt());
            }
        } else if (jsonElement.isJsonObject()) {
            var object = jsonElement.getAsJsonObject();

            for (ArmorItem.Type type : ArmorItem.Type.values()) {
                if (GsonHelper.isValidNode(object, type.getName())) {
                    map.put(type, GsonUtil.getAsIntMin(object, type.getName(), 0));
                }
            }
        } else {
            throw new JsonParseException("slot_protections must be json array or object");
        }

        return map;
    }

    public static HTMLBuilder documentationBuilder() {
        JsonDocumentationBuilder builder = new JsonDocumentationBuilder()
                .setDescription("Each armor material goes into a seperate file into the 'addon/[namespace]/armor_materials' folder, which can then be used for custom armor items/suit sets.");

        builder.addProperty("durability_multiplier", Integer.class)
                .description("A value that gets multiplied with 13, 15, 16 or 11 (feet, legs, chest, head) depending on the slot. Used for the durability value of the item.")
                .required().exampleJson(new JsonPrimitive(12));

        JsonObject slotProtections = new JsonObject();
        slotProtections.addProperty(ArmorItem.Type.HELMET.getName(), 3);
        slotProtections.addProperty(ArmorItem.Type.CHESTPLATE.getName(), 8);
        slotProtections.addProperty(ArmorItem.Type.LEGGINGS.getName(), 6);
        slotProtections.addProperty(ArmorItem.Type.BOOTS.getName(), 3);
        builder.addProperty("slot_protections", JsonObject.class)
                .description("Protection values for the armor pieces, determines the defense value of each slot. For reference (Order: feet, legs, chest, head), iron has [2, 5, 6, 2], diamond is in the example.")
                .required().exampleJson(slotProtections);

        builder.addProperty("enchantment_value", Integer.class)
                .description("Determines the enchantibility of the item. For reference: iron has 9, diamond 10, gold 25.")
                .required().exampleJson(new JsonPrimitive(12));

        builder.addProperty("equip_sound", ResourceLocation.class)
                .description("Sound that is played when equipping the item into the slot.")
                .required().exampleJson(new JsonPrimitive(Objects.requireNonNull(BuiltInRegistries.SOUND_EVENT.getKey(SoundEvents.ARMOR_EQUIP_IRON)).toString()));

        builder.addProperty("toughness", Float.class)
                .description("Adds additional armor toughness. For reference: diamond has 2.0, netherite has 3.0, rest has 0.")
                .fallback(0F).exampleJson(new JsonPrimitive(1.5F));

        builder.addProperty("knockback_resistance", Float.class)
                .description("Adds knockback resistance. For reference: netherite has 0.1, rest has 0.")
                .fallback(0F).exampleJson(new JsonPrimitive(0.1F));

        builder.addProperty("repair_ingredient", Ingredient.class)
                .description("Ingredient definition for repairing the item in an anvil. Can be defined like in recipes.")
                .fallback(Ingredient.EMPTY, "empty ingredient").exampleJson(Ingredient.of(ItemTags.DIRT).toJson());

        return new HTMLBuilder(new ResourceLocation(Palladium.MOD_ID, "armor_materials"), "Armor Materials").add(HTMLBuilder.heading("Armor Materials")).addDocumentation(builder);
    }

}
