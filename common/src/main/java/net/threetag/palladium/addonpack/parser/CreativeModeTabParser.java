package net.threetag.palladium.addonpack.parser;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.addonpack.builder.AddonBuilder;
import net.threetag.palladium.addonpack.builder.CreativeModeTabBuilder;
import net.threetag.palladium.addonpack.log.AddonPackLog;
import net.threetag.palladium.documentation.HTMLBuilder;
import net.threetag.palladium.documentation.JsonDocumentationBuilder;
import net.threetag.palladium.util.json.GsonUtil;
import net.threetag.palladiumcore.registry.CreativeModeTabRegistry;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class CreativeModeTabParser extends AddonParser<CreativeModeTab> {

    public CreativeModeTabParser() {
        super(AddonParser.GSON, "creative_mode_tabs", Registries.CREATIVE_MODE_TAB);
    }

    @Override
    public AddonBuilder<CreativeModeTab> parse(ResourceLocation id, JsonElement jsonElement) {
        JsonObject json = GsonHelper.convertToJsonObject(jsonElement, "$");
        var builder = new CreativeModeTabBuilder(id);
        builder.itemIconId(GsonUtil.getAsResourceLocation(json, "icon"));

        if (GsonHelper.isValidNode(json, "title")) {
            builder.title(Component.Serializer.fromJson(json.get("title")));
        }

        return builder;
    }

    public static HTMLBuilder documentationBuilder() {
        JsonDocumentationBuilder builder = new JsonDocumentationBuilder()
                .setDescription("Each creative mode tab goes into a seperate file into the 'addon/[namespace]/creative_mode_tabs' folder, which can then be used for items.");

        builder.addProperty("icon", Item.class)
                .description("Icon for tab, can only be an item.")
                .required().exampleJson(new JsonPrimitive("minecraft:trident"));

        builder.addProperty("title", Component.class)
                .description("Custom title for the tab. Will fall back to a translation key based on the ID.")
                .fallback(null).exampleJson(new JsonPrimitive("itemGroup.namespace.example"));

        return new HTMLBuilder(new ResourceLocation(Palladium.MOD_ID, "creative_mode_tabs"), "Creative Mode Tabs").add(HTMLBuilder.heading("Creative Mode Tabs")).addDocumentation(builder);
    }
}
