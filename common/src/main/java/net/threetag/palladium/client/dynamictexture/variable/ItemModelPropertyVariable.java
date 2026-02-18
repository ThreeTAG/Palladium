package net.threetag.palladium.client.dynamictexture.variable;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.documentation.JsonDocumentationBuilder;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladium.util.json.GsonUtil;

import java.util.List;

public class ItemModelPropertyVariable extends AbstractFloatTextureVariable {
    private final ResourceLocation modelProperty;
    private final float fallback;

    public ItemModelPropertyVariable(ResourceLocation modelProperty, float fallback, List<Pair<Operation, JsonPrimitive>> operations) {
        super(operations);
        this.modelProperty = modelProperty;
        this.fallback = fallback;
    }

    @Override
    public float getNumber(DataContext context) {
        var item = context.getItem();

        if (!item.isEmpty()) {
            ItemPropertyFunction itemProperty = ItemProperties.getProperty(item.getItem(), modelProperty);
            if (itemProperty == null) {
                return this.fallback;
            }
            return itemProperty.call(item, (ClientLevel) context.getLevel(), context.getLivingEntity(), 0);
        }

        return this.fallback;
    }

    public static class Serializer implements ITextureVariableSerializer {

        @Override
        public ITextureVariable parse(JsonObject json) {
            return new ItemModelPropertyVariable(GsonUtil.getAsResourceLocation(json, "model_property"), GsonHelper.getAsFloat(json, "fallback", 0), AbstractFloatTextureVariable.parseOperations(json));
        }

        @Override
        public void addDocumentationFields(JsonDocumentationBuilder builder) {
            builder.setTitle("Item Model Property");

            builder.addProperty("model_property", ResourceLocation.class)
                    .description("ID of the item model property to get.")
                    .required().exampleJson(new JsonPrimitive("minecraft:damage"));

            builder.addProperty("fallback", Float.class)
                    .description("If the item model property is not found on the item, this value will be used instead.")
                    .fallback(0f)
                    .exampleJson(new JsonPrimitive(0.0f));

            AbstractIntegerTextureVariable.addDocumentationFields(builder);
        }

        @Override
        public String getDocumentationDescription() {
            return "When used in an item-context, this returns the value of the specified item model property, such as \"custom_model_data\" or \"damage\".";
        }

        @Override
        public ResourceLocation getId() {
            return Palladium.id("item_model_property");
        }
    }
}
