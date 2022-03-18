package net.threetag.palladium.item;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.addonpack.parser.ArmorMaterialParser;
import net.threetag.palladium.addonpack.parser.ItemParser;
import net.threetag.palladium.client.ArmorModelManager;
import net.threetag.palladium.documentation.JsonDocumentationBuilder;
import net.threetag.palladium.util.json.GsonUtil;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddonArmorItem extends ArmorItem implements IAddonItem, ICustomArmorTexture {

    private List<Component> tooltipLines;
    private final Map<EquipmentSlot, Multimap<Attribute, AttributeModifier>> attributeModifiers = new HashMap<>();
    private ResourceLocation armorTexture;

    public AddonArmorItem(ArmorMaterial armorMaterial, EquipmentSlot equipmentSlot, Properties properties) {
        super(armorMaterial, equipmentSlot, properties);

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            Multimap<Attribute, AttributeModifier> multimap = ArrayListMultimap.create();
            multimap.putAll(super.getDefaultAttributeModifiers(slot));
            this.attributeModifiers.put(slot, multimap);
        }
    }

    @Override
    public ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return this.armorTexture;
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
            ArmorMaterial armorMaterial = ArmorMaterialParser.getArmorMaterial(GsonUtil.getAsResourceLocation(json, "armor_material"));

            if (armorMaterial == null) {
                throw new JsonParseException("Unknown armor material '" + GsonUtil.getAsResourceLocation(json, "armor_material") + "'");
            }

            EquipmentSlot slot = EquipmentSlot.byName(GsonHelper.getAsString(json, "slot"));

            if (slot.getType() != EquipmentSlot.Type.ARMOR) {
                throw new JsonParseException("The given slot type must be for an armor item");
            }

            AddonArmorItem item = new AddonArmorItem(armorMaterial, slot, properties);
            item.armorTexture = GsonUtil.getAsResourceLocation(json, "armor_texture");


            GsonUtil.ifHasKey(json, "armor_model", jsonElement -> ArmorModelManager.register(item, new ArmorModelManager.Simple(GsonUtil.getAsModelLayerLocation(json, "armor_model"))));

            return item;
        }

        @Override
        public void generateDocumentation(JsonDocumentationBuilder builder) {
            builder.setTitle("Armor");

            builder.addProperty("slot", EquipmentSlot.class)
                    .description("The slot the item will fit in. Possible values: " + Arrays.toString(Arrays.stream(EquipmentSlot.values()).filter(slot -> slot.getType() == EquipmentSlot.Type.ARMOR).map(EquipmentSlot::getName).toArray()))
                    .required().exampleJson(new JsonPrimitive("chest"));

            builder.addProperty("armor_material", ArmorMaterial.class)
                    .description("Armor material, which defines certain characteristics about the armor. Open armor_materials.html for seeing how to make custom ones. Possible values: " + Arrays.toString(ArmorMaterialParser.getIds().toArray(new ResourceLocation[0])))
                    .required().exampleJson(new JsonPrimitive("minecraft:diamond"));

            builder.addProperty("armor_texture", ResourceLocation.class)
                    .description("Armor texture (rendered on the player when wearing it).")
                    .required().exampleJson(new JsonPrimitive("example:textures/models/armor/example_armor.png"));
        }

        @Override
        public ResourceLocation getId() {
            return new ResourceLocation(Palladium.MOD_ID, "armor");
        }
    }

}
