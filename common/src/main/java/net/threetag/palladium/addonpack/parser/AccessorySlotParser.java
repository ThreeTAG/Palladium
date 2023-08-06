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
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.accessory.AccessorySlot;
import net.threetag.palladium.addonpack.log.AddonPackLog;
import net.threetag.palladium.condition.*;
import net.threetag.palladium.documentation.HTMLBuilder;
import net.threetag.palladium.documentation.JsonDocumentationBuilder;
import net.threetag.palladium.power.ability.AbilityReference;
import net.threetag.palladium.util.json.GsonUtil;
import net.threetag.palladiumcore.event.LifecycleEvents;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class AccessorySlotParser extends SimpleJsonResourceReloadListener {

    public AccessorySlotParser() {
        super(AddonParser.GSON, "accessory_slots");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler) {
        AtomicInteger i = new AtomicInteger();
        object.forEach((id, jsonElement) -> {
            try {
                JsonObject json = GsonHelper.convertToJsonObject(jsonElement, "$");
                ResourceLocation icon = GsonUtil.getAsResourceLocation(json, "icon");
                var slot = AccessorySlot.register(id).setIcon(icon);

                if (GsonHelper.getAsBoolean(json, "allows_multiple", false)) {
                    slot.allowMultiple();
                }

                if (json.has("menu_visibility")) {
                    LifecycleEvents.CLIENT_SETUP.register(() -> ConditionSerializer.listFromJSON(json.get("menu_visibility"), ConditionEnvironment.ASSETS).forEach(slot::addVisibilityCondition));
                }

                i.getAndIncrement();
            } catch (Exception e) {
                CrashReport crashReport = CrashReport.forThrowable(e, "Error while parsing addonpack creative mode tab " + " '" + id + "'");

                CrashReportCategory reportCategory = crashReport.addCategory("Addon Creative Mode Tab", 1);
                reportCategory.setDetail("Resource name", id);

                throw new ReportedException(crashReport);
            }
        });

        AddonPackLog.info("Registered " + i.get() + " addonpack accessory slots");
    }

    public static HTMLBuilder documentationBuilder() {
        JsonDocumentationBuilder builder = new JsonDocumentationBuilder()
                .setDescription("Each accessory slot goes into a seperate file into the 'addon/[namespace]/accessory_slots' folder, which can then be used for accessories.");

        builder.addProperty("icon", ResourceLocation.class)
                .description("Texture path for the icon of the slot.")
                .required().exampleJson(new JsonPrimitive("namespace:textures/gui/accessory_slots/example.png"));

        builder.addProperty("allows_multiple", Boolean.class)
                .description("Determines if multiple accessories in this slot can be equipped")
                .fallback(false).exampleJson(new JsonPrimitive(false));

        builder.addProperty("menu_visibility", Condition[].class)
                .description("Determines if the slot is visible in the menu. Can be ignored, set to 'false' or defined by conditions")
                .fallback(null).exampleJson(new JsonPrimitive(true));

        return new HTMLBuilder(new ResourceLocation(Palladium.MOD_ID, "accessory_slots"), "Accessory Slots").add(HTMLBuilder.heading("Accessory Slots")).addDocumentation(builder);
    }
}
