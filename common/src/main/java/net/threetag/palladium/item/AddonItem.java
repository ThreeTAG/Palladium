package net.threetag.palladium.item;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.JsonObject;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.addonpack.parser.ItemParser;
import net.threetag.palladium.documentation.JsonDocumentationBuilder;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddonItem extends Item implements IAddonItem {

    private List<Component> tooltipLines;
    private final Map<EquipmentSlot, Multimap<Attribute, AttributeModifier>> attributeModifiers = new HashMap<>();

    public AddonItem(Properties properties) {
        super(properties);

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            Multimap<Attribute, AttributeModifier> multimap = ArrayListMultimap.create();
            multimap.putAll(super.getDefaultAttributeModifiers(slot));
            this.attributeModifiers.put(slot, multimap);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
        if (this.tooltipLines != null) {
            tooltipComponents.addAll(this.tooltipLines);
        }
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        var modifiers = this.attributeModifiers.get(slot);
        if (modifiers != null) {
            return modifiers;
        } else {
            return super.getDefaultAttributeModifiers(slot);
        }
    }

    @Override
    public void setTooltip(List<Component> lines) {
        this.tooltipLines = lines;
    }

    @Override
    public void addAttributeModifier(@Nullable EquipmentSlot slot, Attribute attribute, AttributeModifier modifier) {
        if (slot != null) {
            this.attributeModifiers.get(slot).put(attribute, modifier);
        } else {
            for (EquipmentSlot slot1 : EquipmentSlot.values()) {
                this.attributeModifiers.get(slot1).put(attribute, modifier);
            }
        }
    }

    public static class Parser implements ItemParser.ItemTypeSerializer {

        @Override
        public IAddonItem parse(JsonObject json, Properties properties) {
            return new AddonItem(properties);
        }

        @Override
        public void generateDocumentation(JsonDocumentationBuilder builder) {
            builder.setTitle("Default Item");
            builder.setDescription("Default Item Type, you don't need to specify that you want this one, leaving 'type' out of the json will make it fall back to this one.");
        }

        @Override
        public ResourceLocation getId() {
            return new ResourceLocation(Palladium.MOD_ID, "default");
        }
    }
}
