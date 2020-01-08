package net.threetag.threecore.addonpacks.item;

import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.addonpacks.ThreeCoreAddonPacks;
import net.threetag.threecore.util.json.TCJsonUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class SuitSetItemParser implements ItemParser.ISpecialItemParser {

    public static final int resourcePrefix = "items/_suit_sets/".length();
    public static final int resourceSuffix = ".json".length();

    @Override
    public boolean applies(ResourceLocation resourceLocation) {
        return resourceLocation.getPath().startsWith("items/_suit_sets/");
    }

    @Override
    public void process(IResourceManager resourceManager, ResourceLocation resourceLocation, IForgeRegistry<Item> registry) {
        String s = resourceLocation.getPath();
        String baseId = s.substring(resourcePrefix, s.length() - resourceSuffix);

        try (IResource iresource = resourceManager.getResource(resourceLocation)) {
            JsonObject json = JSONUtils.fromJson(ThreeCoreAddonPacks.GSON, new BufferedReader(new InputStreamReader(iresource.getInputStream(), StandardCharsets.UTF_8)), JsonObject.class);
            JsonObject armorParts = JSONUtils.getJsonObject(json, "armor_parts");
            JsonObject overrides = JSONUtils.getJsonObject(json, "overrides", new JsonObject());
            Map<EquipmentSlotType, JsonObject> armorJsons = Maps.newHashMap();
            overrides.addProperty("type", "threecore:armor");

            for (EquipmentSlotType slot : EquipmentSlotType.values()) {
                if (slot.getSlotType() == EquipmentSlotType.Group.ARMOR && JSONUtils.hasField(armorParts, slot.getName().toLowerCase())) {
                    armorJsons.put(slot, JSONUtils.getJsonObject(armorParts, slot.getName().toLowerCase()));
                }
            }

            EquipmentSlotType abilitySlot = JSONUtils.hasField(json, "ability_slot") ? EquipmentSlotType.fromString(JSONUtils.getString(json, "ability_slot")) :
                    (armorJsons.containsKey(EquipmentSlotType.CHEST) ? EquipmentSlotType.CHEST : armorJsons.containsKey(EquipmentSlotType.HEAD) ? EquipmentSlotType.HEAD : armorJsons.containsKey(EquipmentSlotType.LEGS) ? EquipmentSlotType.LEGS : EquipmentSlotType.FEET);

            for (Map.Entry<EquipmentSlotType, JsonObject> entry : armorJsons.entrySet()) {
                JsonObject part = TCJsonUtil.merge(JSONUtils.fromJson(overrides.toString()), entry.getValue());

                if (entry.getKey() == abilitySlot) {
                    JsonObject abilities = convertAbilitiesSection(JSONUtils.getJsonObject(json, "abilities", new JsonObject()), JSONUtils.hasField(json, "suit_item_tag") ? new ResourceLocation(JSONUtils.getString(json, "suit_item_tag")) : null, abilitySlot,
                            Maps.asMap(armorJsons.keySet(), (slot -> new ResourceLocation(resourceLocation.getNamespace(), baseId + "_" + toArmorName(slot)))));
                    JsonObject itemAbilities = JSONUtils.getJsonObject(part, "abilities", new JsonObject());
                    part.add("abilities", TCJsonUtil.merge(abilities, itemAbilities));
                }

                part.addProperty("slot", entry.getKey().getName().toLowerCase());
                Item item = ItemParser.parse(part);

                if (item != null) {
                    ResourceLocation id = new ResourceLocation(resourceLocation.getNamespace(), baseId + "_" + toArmorName(entry.getKey()));
                    item.setRegistryName(id);
                    registry.register(item);
                    ThreeCore.LOGGER.info("Registered addonpack item {}!", id);
                }
            }
        } catch (Throwable throwable) {
            ThreeCore.LOGGER.error("Couldn't read addonpack suitset from {}", resourceLocation, throwable);
        }
    }

    public JsonObject convertAbilitiesSection(JsonObject jsonObject, ResourceLocation suitTag, EquipmentSlotType slotType, Map<EquipmentSlotType, ResourceLocation> armorItems) {
        jsonObject.entrySet().forEach(entry -> {
            JsonObject ability = entry.getValue().getAsJsonObject();
            JsonArray conditions = JSONUtils.getJsonArray(ability, "conditions", new JsonArray());

            {
                JsonObject c = new JsonObject();
                c.addProperty("type", "threecore:equipment_slot");
                c.addProperty("slot", slotType.getName());
                conditions.add(c);
            }

            {
                JsonObject c = new JsonObject();
                if (suitTag == null) {
                    c.addProperty("type", "threecore:wearing_item");
                    for (Map.Entry<EquipmentSlotType, ResourceLocation> pair : armorItems.entrySet()) {
                        JsonObject ingredient = new JsonObject();
                        ingredient.addProperty("item", pair.getValue().toString());
                        c.add(pair.getKey().getName(), ingredient);
                    }
                } else {
                    c.addProperty("type", "threecore:wearing_item_tag");
                    c.addProperty("item_tag", suitTag.toString());
                    for (EquipmentSlotType slot : EquipmentSlotType.values()) {
                        c.addProperty(slot.getName(), false);
                    }
                    for (Map.Entry<EquipmentSlotType, ResourceLocation> pair : armorItems.entrySet()) {
                        c.addProperty(pair.getKey().getName(), true);
                    }
                }
                conditions.add(c);
            }

            ability.add("conditions", conditions);
        });

        return jsonObject;
    }

    public String toArmorName(EquipmentSlotType slot) {
        switch (slot) {
            case FEET:
                return "boots";
            case LEGS:
                return "leggings";
            case CHEST:
                return "chestplate";
            case HEAD:
                return "helmet";
            default:
                return null;
        }
    }

}
