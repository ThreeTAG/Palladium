package net.threetag.palladium.compat.geckolib.armor;

import com.google.gson.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.threetag.palladium.addonpack.parser.ArmorMaterialParser;
import net.threetag.palladium.addonpack.parser.ItemParser;
import net.threetag.palladium.client.dynamictexture.TextureReference;
import net.threetag.palladium.compat.geckolib.GeckoLibCompat;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddonGeoArmorItem extends AddonArmorItem implements GeoItem {

    public List<ParsedAnimationController<GeoItem>> animationControllers;
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public ResourceLocation modelPath;
    public TextureReference texturePath;
    public ResourceLocation animationsPath;

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

    @Override
    public boolean hasCustomRenderer() {
        return false;
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

            var item = GeckoLibCompat.createArmorItem(armorMaterial, type, properties);
            item.modelPath = GsonUtil.getAsResourceLocation(json, "armor_model", null);
            item.texturePath = GsonUtil.getAsTextureReference(json, "armor_texture", null);
            item.animationsPath = GsonUtil.getAsResourceLocation(json, "armor_animations", null);
            item.animationControllers = json.has("animation_controller") ? GsonUtil.fromListOrPrimitive(json.get("animation_controller"), el -> ParsedAnimationController.controllerFromJson(el.getAsJsonObject())) : new ArrayList<>();

            return item;
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

            builder.addProperty("armor_model", ResourceLocation.class)
                    .description("Path to geckolib model file. Required bones: [armorHead, armorBody, armorRightArm, armorLeftArm, armorRightLeg, armorLeftLeg, armorRightBoot, armorLeftBoot].")
                    .fallback(null).exampleJson(new JsonPrimitive("palladium:test_model.geo.json"));

            builder.addProperty("armor_texture", TextureReference.class)
                    .description("Location of the armor texture. Can also use a dynamic texture using #.")
                    .fallback(null).exampleJson(new JsonPrimitive("example:textures/models/armor/example_armor.png"));

            builder.addProperty("armor_animations", ResourceLocation.class)
                    .description("ID of the animations that will be used.")
                    .fallback(null).exampleJson(new JsonPrimitive("palladium:animations/test_animation.animation.json"));

            var animationsExample = new JsonArray();
            var extendedC = new JsonObject();
            extendedC.addProperty("name", "controller_name");
            extendedC.addProperty("animation", "animation_name");
            extendedC.addProperty("transition_tick_time", 10);
            var triggers = new JsonObject();
            triggers.addProperty("trigger_name", "animation_name");
            extendedC.add("triggers", triggers);
            animationsExample.add(extendedC);
            builder.addProperty("armor_animation_controllers", List.class)
                    .description("Names of controllers for the animation.")
                    .fallbackObject(null).exampleJson(animationsExample);

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
