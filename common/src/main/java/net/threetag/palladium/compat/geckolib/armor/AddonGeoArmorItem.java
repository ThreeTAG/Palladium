package net.threetag.palladium.compat.geckolib.armor;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.threetag.palladium.addonpack.parser.ArmorMaterialParser;
import net.threetag.palladium.addonpack.parser.ItemParser;
import net.threetag.palladium.compat.geckolib.playeranimator.ParsedAnimationController;
import net.threetag.palladium.documentation.JsonDocumentationBuilder;
import net.threetag.palladium.item.AddonArmorItem;
import net.threetag.palladium.item.IAddonItem;
import net.threetag.palladium.util.json.GsonUtil;
import software.bernie.geckolib.GeckoLib;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Arrays;
import java.util.List;

public class AddonGeoArmorItem extends AddonArmorItem implements GeoItem {

    public List<ParsedAnimationController<GeoItem>> animationControllers;
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public AddonGeoArmorItem(ArmorMaterial materialIn, ArmorItem.Type type, Properties builder) {
        super(materialIn, type, builder);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        for (ParsedAnimationController<GeoItem> controller : this.animationControllers) {
            controllerRegistrar.add(controller.createController(this));
        }
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    public static class Parser implements ItemParser.ItemTypeSerializer {

        @Override
        public IAddonItem parse(JsonObject json, Item.Properties properties) {
            ArmorMaterial armorMaterial = ArmorMaterialParser.getArmorMaterial(GsonUtil.getAsResourceLocation(json, "armor_material"));

            if (armorMaterial == null) {
                throw new JsonParseException("Unknown armor material '" + GsonUtil.getAsResourceLocation(json, "armor_material") + "'");
            }

            ArmorItem.Type type = AddonArmorItem.Parser.getArmorType(GsonHelper.getAsString(json, "slot"));

            if (type == null) {
                throw new JsonParseException("Armor slot must be one of the following: " + Arrays.toString(Arrays.stream(ArmorItem.Type.values()).map(ArmorItem.Type::getName).toArray()));
            }

            return new AddonGeoArmorItem(armorMaterial, type, properties);
        }

        @Override
        public void generateDocumentation(JsonDocumentationBuilder builder) {
            builder.setTitle("GeckoLib Armor");

            builder.addProperty("slot", ArmorItem.Type.class)
                    .description("The slot the item will fit in. Possible values: " + Arrays.toString(Arrays.stream(ArmorItem.Type.values()).map(ArmorItem.Type::getName).toArray()))
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
            return new ResourceLocation(GeckoLib.MOD_ID, "armor");
        }
    }
}
