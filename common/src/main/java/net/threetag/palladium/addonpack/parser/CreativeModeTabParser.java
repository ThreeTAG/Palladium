package net.threetag.palladium.addonpack.parser;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.addonpack.builder.AddonBuilder;
import net.threetag.palladium.addonpack.builder.CreativeModeTabBuilder;
import net.threetag.palladium.documentation.HTMLBuilder;
import net.threetag.palladium.documentation.JsonDocumentationBuilder;
import net.threetag.palladium.util.json.GsonUtil;

import java.util.List;

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

        if (GsonHelper.isValidNode(json, "items")) {
            var array = GsonHelper.getAsJsonArray(json, "items");

            for (JsonElement element : array) {
                builder.addItem(GsonUtil.convertToResourceLocation(element, "items[].$"));
            }
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

        JsonArray jsonArray = new JsonArray();
        jsonArray.add("minecraft:cactus");
        jsonArray.add("minecraft:bread");
        jsonArray.add("minecraft:stick");
        builder.addProperty("items", List.class)
                .description("You can list your items for the tab in the correct order here.")
                .fallback(null).exampleJson(jsonArray);

        return new HTMLBuilder(new ResourceLocation(Palladium.MOD_ID, "creative_mode_tabs"), "Creative Mode Tabs").add(HTMLBuilder.heading("Creative Mode Tabs")).addDocumentation(builder);
    }
}
