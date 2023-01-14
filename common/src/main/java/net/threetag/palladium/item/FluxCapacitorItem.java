package net.threetag.palladium.item;

import com.google.common.collect.Multimap;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.addonpack.parser.ItemParser;
import net.threetag.palladium.documentation.JsonDocumentationBuilder;
import net.threetag.palladium.energy.EnergyHelper;
import net.threetag.palladium.util.Utils;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FluxCapacitorItem extends EnergyItem implements IAddonItem {

    private static final int BAR_COLOR = Mth.color(0.9F, 0.1F, 0F);
    private List<Component> tooltipLines;
    private final Map<EquipmentSlot, Multimap<Attribute, AttributeModifier>> attributeModifiers = new HashMap<>();
    private RenderLayerContainer renderLayerContainer = null;

    public FluxCapacitorItem(Properties properties, int capacity, int maxInput, int maxOutput) {
        super(properties, capacity, maxInput, maxOutput);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        var stored = EnergyHelper.getEnergyStoredInItem(stack);
        tooltipComponents.add(Component.translatable("item.palladium.flux_capacitor.desc",
                Component.literal(Utils.getFormattedNumber(stored)).withStyle(ChatFormatting.GOLD),
                Component.literal(Utils.getFormattedNumber(this.getEnergyCapacity(stack))).withStyle(ChatFormatting.GOLD)
        ).withStyle(ChatFormatting.GRAY));
        if (this.tooltipLines != null) {
            tooltipComponents.addAll(this.tooltipLines);
        }
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        var storage = EnergyHelper.getFromItemStack(stack);
        return storage.map(energyStorage -> energyStorage.getEnergyAmount() < energyStorage.getEnergyCapacity()).orElse(false);
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        var storage = EnergyHelper.getFromItemStack(stack);
        return storage.map(energyStorage -> Math.round(13F * energyStorage.getEnergyAmount() / (float) energyStorage.getEnergyCapacity())).orElse(0);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return BAR_COLOR;
    }

    @Override
    public void fillItemCategory(CreativeModeTab category, NonNullList<ItemStack> items) {
        if (this.allowedIn(category)) {
            items.add(this.getDefaultInstance());
            var filled = this.getDefaultInstance();
            filled.getOrCreateTag().putInt("energy", this.getEnergyCapacity(filled));
            items.add(filled);
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
    public void setRenderLayerContainer(RenderLayerContainer container) {
        this.renderLayerContainer = container;
    }

    @Override
    public RenderLayerContainer getRenderLayerContainer() {
        return this.renderLayerContainer;
    }

    public static class Parser implements ItemParser.ItemTypeSerializer {

        @Override
        public IAddonItem parse(JsonObject json, Properties properties) {
            int capacity = GsonHelper.getAsInt(json, "capacity");
            int maxInput = GsonHelper.getAsInt(json, "max_input");
            int maxOutput = GsonHelper.getAsInt(json, "max_output");

            if (capacity <= 0) {
                throw new JsonParseException("Energy capacity must be greater than 0");
            }

            if (maxInput < 0) {
                throw new JsonParseException("Energy max input can not be negative");
            }

            if (maxOutput < 0) {
                throw new JsonParseException("Energy max output can not be negative");
            }

            return new FluxCapacitorItem(properties, capacity, maxInput, maxOutput);
        }

        @Override
        public void generateDocumentation(JsonDocumentationBuilder builder) {
            builder.setTitle("Flux Capacitor (Energy Containing Item)");

            builder.addProperty("capacity", Integer.class)
                    .description("Max amount of energy the item can hold")
                    .required().exampleJson(new JsonPrimitive(500000));

            builder.addProperty("max_input", Integer.class)
                    .description("Maximum amount of energy the item can be inserted with during one insertion. Using 0 makes the item not accept any energy")
                    .required().exampleJson(new JsonPrimitive(1000));

            builder.addProperty("max_output", Integer.class)
                    .description("Maximum amount of energy the item can extract with during one withdrawal. Using 0 makes the item not extract any energy")
                    .required().exampleJson(new JsonPrimitive(1000));
        }

        @Override
        public ResourceLocation getId() {
            return new ResourceLocation(Palladium.MOD_ID, "flux_capacitor");
        }
    }
}
