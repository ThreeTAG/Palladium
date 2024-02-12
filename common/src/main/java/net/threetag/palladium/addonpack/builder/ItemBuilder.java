package net.threetag.palladium.addonpack.builder;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.threetag.palladium.addonpack.log.AddonPackLog;
import net.threetag.palladium.addonpack.parser.ItemParser;
import net.threetag.palladium.compat.curiostinkets.CuriosTrinketsUtil;
import net.threetag.palladium.item.AddonItem;
import net.threetag.palladium.item.IAddonItem;
import net.threetag.palladium.util.PlayerSlot;
import net.threetag.palladium.util.RegistrySynonymsHandler;
import net.threetag.palladium.util.Utils;
import net.threetag.palladiumcore.registry.CreativeModeTabRegistry;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemBuilder extends AddonBuilder<Item, ItemBuilder> {

    private final JsonObject json;
    private ResourceLocation typeSerializerId = null;
    private Integer maxStackSize = null;
    private Integer maxDamage = null;
    private Boolean isFireResistant = null;
    private List<ItemParser.PlacedTabPlacement> creativeModeTabs;
    private Rarity rarity = null;
    private List<Component> tooltipLines = null;
    private Multimap<ResourceLocation, AttributeModifier> attributeModifiersAllSlots;
    private Map<PlayerSlot, Multimap<ResourceLocation, AttributeModifier>> attributeModifiers;
    private FoodProperties foodProperties = null;
    private IAddonItem.RenderLayerContainer renderLayerContainer = null;

    public ItemBuilder(ResourceLocation id, JsonObject json) {
        super(id);
        this.json = json;
    }

    @Override
    protected Item create() {
        var properties = new Item.Properties();

        var maxDamage = this.getValue(b -> b.maxDamage);
        if (maxDamage != null && maxDamage != 0) {
            Utils.ifNotNull(maxDamage, properties::durability);
        }

        var maxStackSize = this.getValue(b -> b.maxStackSize);
        if (maxStackSize != null && maxStackSize != 64) {
            Utils.ifNotNull(maxStackSize, properties::stacksTo);
        }

        Utils.ifNotNull(this.getValue(b -> b.rarity), properties::rarity);
        Utils.ifTrue(this.getValue(b -> b.isFireResistant), properties::fireResistant);

        properties.food(this.getValue(b -> b.foodProperties));

        if (this.getParent() == null) {
            if (this.typeSerializerId == null) {
                this.typeSerializerId = ItemParser.FALLBACK_SERIALIZER;
            }
        }

        ItemParser.ItemTypeSerializer serializer = ItemParser.getTypeSerializer(this.typeSerializerId);

        if (serializer == null) {
            AddonPackLog.warning("Unknown item type '" + this.typeSerializerId + "', falling back to '" + ItemParser.FALLBACK_SERIALIZER + "'");
        }

        IAddonItem item = serializer != null ? serializer.parse(this.json, properties) : new AddonItem(properties);

        Utils.ifNotNull(this.getValue(b -> b.tooltipLines), item::setTooltip);

        var attributeModifiers = this.getValue(b -> b.attributeModifiers);
        if (attributeModifiers != null) {
            for (PlayerSlot slot : attributeModifiers.keySet()) {
                for (ResourceLocation attributeId : attributeModifiers.get(slot).keySet()) {
                    Attribute attribute = BuiltInRegistries.ATTRIBUTE.get(RegistrySynonymsHandler.getReplacement(BuiltInRegistries.ATTRIBUTE, attributeId));

                    if (attribute != null) {
                        for (AttributeModifier attributeModifier : attributeModifiers.get(slot).get(attributeId)) {
                            item.getAttributeContainer().add(slot, attribute, attributeModifier);
                        }
                    } else {
                        throw new JsonParseException("Unknown attribute '" + attributeId + "'");
                    }
                }
            }
        }

        var attributeModifiersAllSlots = this.getValue(b -> b.attributeModifiersAllSlots);
        if (attributeModifiersAllSlots != null) {
            for (ResourceLocation attributeId : attributeModifiersAllSlots.keySet()) {
                Attribute attribute = BuiltInRegistries.ATTRIBUTE.get(RegistrySynonymsHandler.getReplacement(BuiltInRegistries.ATTRIBUTE, attributeId));

                if (attribute != null) {
                    for (AttributeModifier attributeModifier : attributeModifiersAllSlots.get(attributeId)) {
                        item.getAttributeContainer().addForAllSlots(attribute, attributeModifier);
                    }
                } else {
                    throw new JsonParseException("Unknown attribute '" + attributeId + "'");
                }
            }
        }

        item.setRenderLayerContainer(this.getValue(b -> b.renderLayerContainer));

        if (attributeModifiers != null || attributeModifiersAllSlots != null) {
            CuriosTrinketsUtil.getInstance().registerCurioTrinket((Item) item, new CurioTrinket(item));
        }

        for (ItemParser.PlacedTabPlacement creativeModeTab : this.getValue(b -> b.creativeModeTabs, new ArrayList<ItemParser.PlacedTabPlacement>())) {
            ResourceKey<CreativeModeTab> tabKey = ResourceKey.create(Registries.CREATIVE_MODE_TAB, creativeModeTab.getTab());
            CreativeModeTabRegistry.addToTab(tabKey, entries -> creativeModeTab.addToTab(entries, (Item) item));
        }

        return (Item) item;
    }

    public ItemBuilder type(ResourceLocation serializerId) {
        this.typeSerializerId = serializerId;
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

    public ItemBuilder creativeModeTab(ItemParser.PlacedTabPlacement tabPlacement) {
        if (this.creativeModeTabs == null) {
            this.creativeModeTabs = new ArrayList<>();
        }
        this.creativeModeTabs.add(tabPlacement);
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

    public ItemBuilder addAttributeModifier(@Nullable PlayerSlot slot, ResourceLocation attributeId, AttributeModifier modifier) {
        if (slot == null) {
            if (this.attributeModifiersAllSlots == null) {
                this.attributeModifiersAllSlots = ArrayListMultimap.create();
            }
            this.attributeModifiersAllSlots.put(attributeId, modifier);
            return this;
        }

        if (this.attributeModifiers == null) {
            this.attributeModifiers = new HashMap<>();
        }

        this.attributeModifiers.computeIfAbsent(slot, equipmentSlot -> ArrayListMultimap.create()).put(attributeId, modifier);
        return this;
    }

    public ItemBuilder setRenderLayerContainer(IAddonItem.RenderLayerContainer container) {
        this.renderLayerContainer = container;
        return this;
    }

    public ItemBuilder food(FoodProperties foodProperties) {
        this.foodProperties = foodProperties;
        return this;
    }

    public static class CurioTrinket implements net.threetag.palladium.compat.curiostinkets.CurioTrinket {

        private final IAddonItem item;

        public CurioTrinket(IAddonItem item) {
            this.item = item;
        }

        @Override
        public Multimap<Attribute, AttributeModifier> getModifiers(PlayerSlot slot, LivingEntity entity) {
            return item.getAttributeContainer().get(slot, ArrayListMultimap.create());
        }
    }

}
