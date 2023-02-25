package net.threetag.palladium.compat.geckolib;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.threetag.palladium.addonpack.parser.ArmorMaterialParser;
import net.threetag.palladium.addonpack.parser.ItemParser;
import net.threetag.palladium.client.renderer.renderlayer.PackRenderLayerManager;
import net.threetag.palladium.documentation.JsonDocumentationBuilder;
import net.threetag.palladium.item.IAddonItem;
import net.threetag.palladium.util.json.GsonUtil;
import net.threetag.palladiumcore.util.Platform;
import software.bernie.geckolib3.GeckoLib;

import java.util.Arrays;

public class GeckoLibCompat {

    public static void init() {
        PackRenderLayerManager.registerParser(new ResourceLocation(GeckoLib.ModID, "default"), GeckoRenderLayer::parse);
        ItemParser.registerTypeSerializer(new ArmorParser());
    }

    @ExpectPlatform
    public static ArmorItem createArmorItem(ArmorMaterial armorMaterial, EquipmentSlot slot, Item.Properties properties, boolean hideSecondLayer) {
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

            if (Platform.isClient() && item instanceof PackGeckoArmorItem armor) {
                armor.setGeckoLocations(GsonUtil.getAsResourceLocation(json, "armor_model"),
                        GsonUtil.getAsResourceLocation(json, "armor_texture"),
                        GsonUtil.getAsResourceLocation(json, "armor_animation", null),
                        GsonHelper.getAsString(json, "armor_animation_name", "animation.armor.loop"));
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
                    .fallbackObject(null).exampleJson(new JsonPrimitive("palladium:geo/test_model.geo.json"));

            builder.addProperty("armor_animation", ResourceLocation.class)
                    .description("Path to geckolib model animation file.")
                    .fallbackObject(null).exampleJson(new JsonPrimitive("palladium:animations/test_model.animation.json"));

            builder.addProperty("armor_animation_name", String.class)
                    .description("Name of the looping animation, in BlockBench it's the animation you create on the right.")
                    .fallbackObject("animation.armor.loop").exampleJson(new JsonPrimitive("animation.armor.loop"));

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
