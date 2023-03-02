package net.threetag.palladium.item;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.addonpack.log.AddonPackLog;
import net.threetag.palladium.addonpack.parser.ArmorMaterialParser;
import net.threetag.palladium.addonpack.parser.ItemParser;
import net.threetag.palladium.client.model.ArmorModelManager;
import net.threetag.palladium.client.dynamictexture.DynamicTexture;
import net.threetag.palladium.client.renderer.renderlayer.ModelLookup;
import net.threetag.palladium.documentation.JsonDocumentationBuilder;
import net.threetag.palladium.util.PlayerSlot;
import net.threetag.palladium.util.SkinTypedValue;
import net.threetag.palladium.util.json.GsonUtil;
import net.threetag.palladiumcore.util.Platform;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddonArmorItem extends ArmorItem implements IAddonItem, ExtendedArmor {

    private List<Component> tooltipLines;
    private SkinTypedValue<DynamicTexture> armorTexture;
    private RenderLayerContainer renderLayerContainer = null;
    private boolean hideSecondLayer = false;

    public AddonArmorItem(ArmorMaterial armorMaterial, EquipmentSlot equipmentSlot, Properties properties) {
        super(armorMaterial, equipmentSlot, properties);
    }

    public AddonArmorItem hideSecondLayer() {
        this.hideSecondLayer = true;
        return this;
    }

    @Override
    public boolean hideSecondPlayerLayer(Player player, ItemStack stack, EquipmentSlot slot) {
        return this.hideSecondLayer;
    }

    @Override
    public ResourceLocation getArmorTextureLocation(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return entity instanceof LivingEntity livingEntity ? this.armorTexture.get(livingEntity).getTexture(livingEntity) : null;
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
        return this.attributeContainer.get(PlayerSlot.get(slot), super.getDefaultAttributeModifiers(slot));
    }

    @Override
    public AddonAttributeContainer getAttributeContainer() {
        return this.attributeContainer;
    }

    @Override
    public void setTooltip(List<Component> lines) {
        this.tooltipLines = lines;
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
            ArmorMaterial armorMaterial = ArmorMaterialParser.getArmorMaterial(GsonUtil.getAsResourceLocation(json, "armor_material"));

            if (armorMaterial == null) {
                throw new JsonParseException("Unknown armor material '" + GsonUtil.getAsResourceLocation(json, "armor_material") + "'");
            }

            EquipmentSlot slot = EquipmentSlot.byName(GsonHelper.getAsString(json, "slot"));

            if (slot.getType() != EquipmentSlot.Type.ARMOR) {
                throw new JsonParseException("The given slot type must be for an armor item");
            }

            AddonArmorItem item = new AddonArmorItem(armorMaterial, slot, properties);

            if (Platform.isClient()) {
                item.armorTexture = SkinTypedValue.fromJSON(json.get("armor_texture"), DynamicTexture::parse);

                String modelTypeKey = "armor_model_type";

                if (!json.has(modelTypeKey) && json.has("armor_model")) {
                    AddonPackLog.warning("Deprecated use of 'armor_model' in render layer. Please switch to 'armor_model_type'!");
                    modelTypeKey = "armor_model";
                }

                String finalModelTypeKey = modelTypeKey;
                GsonUtil.ifHasKey(json, "armor_model_layer", jsonElement -> {
                    ArmorModelManager.register(item,
                            json.has(finalModelTypeKey) ? SkinTypedValue.fromJSON(json.get(finalModelTypeKey), jsonElement1 -> {
                                ResourceLocation modelId = new ResourceLocation(jsonElement1.getAsString());
                                ModelLookup.Model m = ModelLookup.get(modelId);

                                if (m == null) {
                                    throw new JsonParseException("Unknown model type '" + modelId + "'");
                                }

                                return m;
                            }) : new SkinTypedValue<>(ModelLookup.HUMANOID),
                            SkinTypedValue.fromJSON(jsonElement, jsonElement1 -> GsonUtil.convertToModelLayerLocation(jsonElement1, "armor_model_layer"))
                    );
                });

                if (GsonHelper.getAsBoolean(json, "hide_second_player_layer", false)) {
                    item.hideSecondLayer();
                }
            }

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

            builder.addProperty("armor_texture", DynamicTexture.class)
                    .description("Armor texture (rendered on the player when wearing it). Can be a dynamic one like in render layers")
                    .required().exampleJson(new JsonPrimitive("example:textures/models/armor/example_armor.png"));

            builder.addProperty("armor_model_type", ResourceLocation.class)
                    .description("Armor model type, defines the bones for the model layer. Ideally only use minecraft:humanoid for 1 layer or minecraft:player for 2 layers")
                    .fallbackObject(new ResourceLocation("minecraft:humanoid")).exampleJson(new JsonPrimitive("minecraft:humanoid"));

            builder.addProperty("armor_model_layer", ModelLayerLocation.class)
                    .description("Armor model layer, must have the body parts for a humanoid model (if not specified for another model type).")
                    .fallbackObject(null).exampleJson(new JsonPrimitive("palladium:humanoid#suit"));

            builder.addProperty("hide_second_player_layer", Boolean.class)
                    .description("If enabled, the second player layer will be hidden when worn (only on the corresponding body part)")
                    .fallback(false).exampleJson(new JsonPrimitive(true));
        }

        @Override
        public ResourceLocation getId() {
            return new ResourceLocation(Palladium.MOD_ID, "armor");
        }
    }

}
