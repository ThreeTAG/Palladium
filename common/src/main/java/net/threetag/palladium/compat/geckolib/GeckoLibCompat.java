package net.threetag.palladium.compat.geckolib;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.threetag.palladium.addonpack.parser.ArmorMaterialParser;
import net.threetag.palladium.addonpack.parser.ItemParser;
import net.threetag.palladium.client.renderer.renderlayer.PackRenderLayerManager;
import net.threetag.palladium.client.renderer.renderlayer.RenderLayerStates;
import net.threetag.palladium.documentation.JsonDocumentationBuilder;
import net.threetag.palladium.item.IAddonItem;
import net.threetag.palladium.util.json.GsonUtil;
import software.bernie.geckolib3.GeckoLib;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings({"rawtypes"})
public class GeckoLibCompat {

    public static void init() {
        ItemParser.registerTypeSerializer(new ArmorParser());
    }

    @Environment(EnvType.CLIENT)
    public static void initClient() {
        PackRenderLayerManager.registerParser(new ResourceLocation(GeckoLib.ModID, "default"), GeckoRenderLayer::parse);
        PackRenderLayerManager.registerStateFunction(layer -> layer instanceof GeckoRenderLayer g ? new GeckoLayerState(g) : new RenderLayerStates.State());
    }

    @ExpectPlatform
    public static ArmorItem createArmorItem(ArmorMaterial armorMaterial, EquipmentSlot slot, Item.Properties properties, boolean hideSecondLayer) {
        throw new AssertionError();
    }

    @Environment(EnvType.CLIENT)
    @ExpectPlatform
    public static void renderFirstPerson(AbstractClientPlayer player, ItemStack stack, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, ModelPart rendererArm, boolean rightArm) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static GeoArmorRenderer getArmorRenderer(Class<? extends ArmorItem> clazz, Entity entity) {
        throw new AssertionError();
    }

    public static class ArmorParser implements ItemParser.ItemTypeSerializer {

        @Override
        public IAddonItem parse(JsonObject json, Item.Properties properties) {
            ArmorMaterial armorMaterial = ArmorMaterialParser.getArmorMaterial(GsonUtil.getAsResourceLocation(json, "armor_material"));

            if (armorMaterial == null) {
                throw new JsonParseException("Unknown armor material '" + GsonUtil.getAsResourceLocation(json, "armor_material") + "'");
            }

            EquipmentSlot slot = EquipmentSlot.byName(GsonHelper.getAsString(json, "slot"));

            if (slot.getType() != EquipmentSlot.Type.ARMOR) {
                throw new JsonParseException("The given slot type must be for an armor item");
            }

            ArmorItem item = createArmorItem(armorMaterial, slot, properties, GsonHelper.getAsBoolean(json, "hide_second_player_layer", false));

            if (item instanceof PackGeckoArmorItem armor) {
                armor.setGeckoLocations(GsonUtil.getAsResourceLocation(json, "armor_model"),
                        GsonUtil.getAsResourceLocation(json, "armor_texture"),
                        GsonUtil.getAsResourceLocation(json, "armor_animation_file", null),
                        ParsedAnimationController.getAsList(json, "armor_animation_controllers"));
            }

            return (IAddonItem) item;
        }

        @Override
        public void generateDocumentation(JsonDocumentationBuilder builder) {
            builder.setTitle("GeckoLib Armor");

            builder.addProperty("slot", EquipmentSlot.class)
                    .description("The slot the item will fit in. Possible values: " + Arrays.toString(Arrays.stream(EquipmentSlot.values()).filter(slot -> slot.getType() == EquipmentSlot.Type.ARMOR).map(EquipmentSlot::getName).toArray()))
                    .required().exampleJson(new JsonPrimitive("chest"));

            builder.addProperty("armor_material", ArmorMaterial.class)
                    .description("Armor material, which defines certain characteristics about the armor. Open armor_materials.html for seeing how to make custom ones. Possible values: " + Arrays.toString(ArmorMaterialParser.getIds().toArray(new ResourceLocation[0])))
                    .required().exampleJson(new JsonPrimitive("minecraft:diamond"));

            builder.addProperty("armor_texture", ResourceLocation.class)
                    .description("Armor texture (rendered on the player when wearing it). Simple texture file required")
                    .required().exampleJson(new JsonPrimitive("example:textures/models/armor/example_armor.png"));

            builder.addProperty("armor_model", ResourceLocation.class)
                    .description("Path to geckolib model file. Required bones: [armorHead, armorBody, armorRightArm, armorLeftArm, armorRightLeg, armorLeftLeg, armorRightBoot, armorLeftBoot]")
                    .required().exampleJson(new JsonPrimitive("palladium:geo/test_model.geo.json"));

            builder.addProperty("armor_animation_file", ResourceLocation.class)
                    .description("Path to geckolib model animation file.")
                    .fallbackObject(null).exampleJson(new JsonPrimitive("palladium:animations/test_model.animation.json"));

            var animationsExample = new JsonArray();
            animationsExample.add("main");
            var extendedC = new JsonObject();
            extendedC.addProperty("name", "second_controller");
            extendedC.addProperty("initial_animation", "animation_name");
            extendedC.addProperty("transition_ticks", 1);
            animationsExample.add(extendedC);
            builder.addProperty("armor_animation_controllers", List.class)
                    .description("Names of controllers for the animation. Leave it empty to just have one main one. Add multiple to play multiple animations at the same time.")
                    .fallbackObject("main").exampleJson(animationsExample);

            builder.addProperty("hide_second_player_layer", Boolean.class)
                    .description("If enabled, the second player layer will be hidden when worn (only on the corresponding body part)")
                    .fallback(false).exampleJson(new JsonPrimitive(true));
        }

        @Override
        public ResourceLocation getId() {
            return new ResourceLocation(GeckoLib.ModID, "armor");
        }
    }

}
