package net.threetag.palladium.addonpack.builder;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.threetag.palladium.addonpack.parser.ItemParser;
import net.threetag.palladium.item.AddonItem;
import net.threetag.palladium.item.IAddonItem;
import net.threetag.palladium.item.PalladiumCreativeModeTabs;
import net.threetag.palladium.util.Utils;

import org.jetbrains.annotations.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemBuilder extends AddonBuilder<Item> {

    private final JsonObject json;
    private ItemParser.ItemTypeSerializer typeSerializer = null;
    private Integer maxStackSize = null;
    private Integer maxDamage = null;
    private Boolean isFireResistant = null;
    private ResourceLocation creativeModeTab = null;
    private Rarity rarity = null;
    private List<Component> tooltipLines = null;
    private Map<EquipmentSlot, Multimap<ResourceLocation, AttributeModifier>> attributeModifiers;
    private FoodProperties foodProperties = null;

    public ItemBuilder(ResourceLocation id, JsonObject json) {
        super(id);
        this.json = json;
    }

    @Override
    protected Item create() {
        var properties = new Item.Properties();

        if (this.maxDamage != null && this.maxDamage != 0) {
            Utils.ifNotNull(this.maxDamage, properties::durability);
        }

        if (this.maxStackSize != null && this.maxStackSize != 64) {
            Utils.ifNotNull(this.maxStackSize, properties::stacksTo);
        }

        Utils.ifNotNull(this.rarity, properties::rarity);
        Utils.ifNotNull(this.rarity, properties::rarity);
        Utils.ifTrue(this.isFireResistant, properties::fireResistant);

        if (this.creativeModeTab != null) {
            CreativeModeTab tab = PalladiumCreativeModeTabs.getTab(this.creativeModeTab);
            if (tab != null) {
                properties.tab(tab);
            }
        }

        properties.food(this.foodProperties);

        IAddonItem item = this.typeSerializer != null ? this.typeSerializer.parse(this.json, properties) : new AddonItem(properties);

        Utils.ifNotNull(this.tooltipLines, item::setTooltip);

        if (this.attributeModifiers != null) {
            for (EquipmentSlot slot : this.attributeModifiers.keySet()) {
                for (ResourceLocation attributeId : this.attributeModifiers.get(slot).keySet()) {
                    Attribute attribute = Registry.ATTRIBUTE.get(attributeId);

                    if (attribute != null) {
                        for (AttributeModifier attributeModifier : this.attributeModifiers.get(slot).get(attributeId)) {
                            item.addAttributeModifier(slot, attribute, attributeModifier);
                        }
                    } else {
                        throw new JsonParseException("Unknown attribute '" + attributeId + "'");
                    }
                }
            }
        }

        return (Item) item;
    }

    public ItemBuilder type(ItemParser.ItemTypeSerializer serializer) {
        this.typeSerializer = serializer;
        return this;
    }

    public ItemBuilder maxStackSize(int stackSize) {
        this.maxStackSize = stackSize;
        return this;
    }

    public ItemBuilder maxDamage(int maxDamage) {
        this.maxDamage = maxDamage;
        return this;
    }

    public ItemBuilder creativeModeTab(ResourceLocation name) {
        this.creativeModeTab = name;
        return this;
    }

    public ItemBuilder rarity(Rarity rarity) {
        this.rarity = rarity;
        return this;
    }

    public ItemBuilder fireResistant(boolean isFireResistant) {
        this.isFireResistant = isFireResistant;
        return this;
    }

    public ItemBuilder tooltipLines(List<Component> tooltipLines) {
        this.tooltipLines = tooltipLines;
        return this;
    }

    public ItemBuilder addAttributeModifier(@Nullable EquipmentSlot slot, ResourceLocation attributeId, AttributeModifier modifier) {
        if (this.attributeModifiers == null) {
            this.attributeModifiers = new HashMap<>();
        }

        this.attributeModifiers.computeIfAbsent(slot, equipmentSlot -> ArrayListMultimap.create()).put(attributeId, modifier);
        return this;
    }

    public ItemBuilder food(FoodProperties foodProperties) {
        this.foodProperties = foodProperties;
        return this;
    }

}
