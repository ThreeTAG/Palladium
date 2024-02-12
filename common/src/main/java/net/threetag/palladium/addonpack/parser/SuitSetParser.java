package net.threetag.palladium.addonpack.parser;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.EquipmentSlot;
import net.threetag.palladium.addonpack.AddonPackManager;
import net.threetag.palladium.addonpack.builder.SuitSetBuilder;
import net.threetag.palladium.item.SuitSet;
import net.threetag.palladium.util.json.GsonUtil;

public class SuitSetParser extends AddonParser<SuitSetBuilder> {

    public SuitSetParser() {
        super(AddonParser.GSON, "suit_sets", SuitSet.REGISTRY.getRegistryKey());
    }

    @Override
    public SuitSetBuilder parse(ResourceLocation id, JsonElement jsonElement) {
        JsonObject copy = jsonElement.getAsJsonObject().deepCopy();
        SuitSetBuilder builder = new SuitSetBuilder(id);

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            copy.remove(slot.getName());
        }

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            GsonUtil.ifHasObject(jsonElement.getAsJsonObject(), slot.getName(), jsonObject -> {
                JsonObject json = GsonUtil.merge(copy, jsonObject);

                if (slot.getType() == EquipmentSlot.Type.ARMOR) {
                    if (!GsonHelper.isValidNode(json, "type")) {
                        json.addProperty("type", "palladium:armor");
                    }

                    if (!GsonHelper.isValidNode(json, "slot")) {
                        json.addProperty("slot", slot.getName());
                    }

                    if (!GsonHelper.isValidNode(json, "armor_model_layer")) {
                        json.addProperty("armor_model_layer", slot == EquipmentSlot.LEGS ? "minecraft:player#inner_armor" : "minecraft:player#outer_armor");
                    }
                }

                String name = GsonHelper.isValidNode(json, "item_name") ? GsonHelper.getAsString(json, "item_name") : id.getPath() + "_" + slot.getName();
                var itemId = new ResourceLocation(id.getNamespace(), name);
                AddonPackManager.ITEM_PARSER.inject(itemId, json);

                if (slot == EquipmentSlot.MAINHAND)
                    builder.mainHand(itemId);
                else if (slot == EquipmentSlot.OFFHAND)
                    builder.offHand(itemId);
                else if (slot == EquipmentSlot.HEAD)
                    builder.helmet(itemId);
                else if (slot == EquipmentSlot.CHEST)
                    builder.chestplate(itemId);
                else if (slot == EquipmentSlot.LEGS)
                    builder.leggings(itemId);
                else if (slot == EquipmentSlot.FEET)
                    builder.boots(itemId);
            });
        }

        return builder;
    }
}
