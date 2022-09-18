package net.threetag.palladium.addonpack.parser;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.EquipmentSlot;
import net.threetag.palladium.addonpack.AddonPackManager;
import net.threetag.palladium.addonpack.builder.AddonBuilder;
import net.threetag.palladium.addonpack.builder.SuitSetBuilder;
import net.threetag.palladium.item.SuitSet;
import net.threetag.palladium.util.json.GsonUtil;

public class SuitSetParser extends AddonParser<SuitSet> {

    public SuitSetParser() {
        super(AddonParser.GSON, "suit_sets", SuitSet.REGISTRY.getRegistryKey());
    }

    @Override
    public AddonBuilder<SuitSet> parse(ResourceLocation id, JsonElement jsonElement) {
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

                    if (!GsonHelper.isValidNode(json, "armor_model")) {
                        json.addProperty("armor_model", slot == EquipmentSlot.LEGS ? "minecraft:player#inner_armor" : "minecraft:player#outer_armor");
                    }
                }

                String name = GsonHelper.isValidNode(json, "item_name") ? GsonHelper.getAsString(json, "item_name") : id.getPath() + "_" + slot.getName();
                var itemBuilder = AddonPackManager.ITEM_PARSER.parse(new ResourceLocation(id.getNamespace(), name), json);

                if (slot == EquipmentSlot.MAINHAND)
                    builder.mainHand(itemBuilder);
                else if (slot == EquipmentSlot.OFFHAND)
                    builder.offHand(itemBuilder);
                else if (slot == EquipmentSlot.HEAD)
                    builder.helmet(itemBuilder);
                else if (slot == EquipmentSlot.CHEST)
                    builder.chestplate(itemBuilder);
                else if (slot == EquipmentSlot.LEGS)
                    builder.leggings(itemBuilder);
                else if (slot == EquipmentSlot.FEET)
                    builder.boots(itemBuilder);

                AddonParser.register(Registry.ITEM_REGISTRY, itemBuilder);
            });
        }

        return builder;
    }
}
