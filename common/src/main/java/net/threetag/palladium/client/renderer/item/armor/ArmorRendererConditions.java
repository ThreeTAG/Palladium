package net.threetag.palladium.client.renderer.item.armor;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.util.GsonHelper;
import net.threetag.palladium.condition.Condition;
import net.threetag.palladium.condition.ConditionEnvironment;
import net.threetag.palladium.condition.ConditionSerializer;
import net.threetag.palladium.item.Openable;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladium.util.context.DataContextType;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;

public class ArmorRendererConditions {

    public static final String BASE_TEXTURE = "default";
    public static final String OPENED_TEXTURE = "open";

    public final List<ConditionedTextureKey> conditions = new LinkedList<>();

    public String getTexture(DataContext context, ArmorTextureData textures) {
        String key = BASE_TEXTURE;

        var stack = context.getItem();
        if (!stack.isEmpty() && stack.getItem() instanceof Openable openable) {
            var max = openable.getOpeningTime(stack);
            if (max <= 0 && openable.isOpen(stack)) {
                key = OPENED_TEXTURE;
            } else if (max > 0) {
                key = OPENED_TEXTURE + "_" + openable.getOpeningProgress(context.getItem());

                if (!textures.has(key)) {
                    key = OPENED_TEXTURE;
                }
            }
        }

        if (!textures.has(key)) {
            key = BASE_TEXTURE;
        }

        for (ConditionedTextureKey condition : this.conditions) {
            if (condition.textureKey != null && condition.isActive(context)) {
                key = condition.textureKey;
            }
        }

        return key;
    }

    public String getModelLayer(DataContext context, ArmorModelData models) {
        String key = BASE_TEXTURE;

        if (context.has(DataContextType.ITEM) && context.getItem().getItem() instanceof Openable openable && openable.getOpeningProgress(context.getItem()) > 0) {
            if (openable.getOpeningTime(context.getItem()) <= 0) {
                key = OPENED_TEXTURE;
            } else {
                key = OPENED_TEXTURE + "_" + openable.getOpeningProgress(context.getItem());

                if (!models.has(key)) {
                    key = OPENED_TEXTURE;
                }
            }
        }

        if (!models.has(key)) {
            key = BASE_TEXTURE;
        }

        for (ConditionedTextureKey condition : this.conditions) {
            if (condition.modelKey != null && condition.isActive(context)) {
                key = condition.modelKey;
            }
        }

        return key;
    }

    public static ArmorRendererConditions fromJson(@Nullable JsonArray jsonArray) {
        if (jsonArray == null) {
            return new ArmorRendererConditions();
        }

        var cond = new ArmorRendererConditions();

        for (JsonElement jsonElement : jsonArray) {
            JsonObject json = GsonHelper.convertToJsonObject(jsonElement, "conditions.$");
            String textureKey = GsonHelper.getAsString(json, "texture", null);
            String modelKey = GsonHelper.getAsString(json, "model_layer", null);
            cond.conditions.add(0, new ConditionedTextureKey(textureKey, modelKey, ConditionSerializer.listFromJSON(json.get("if"), ConditionEnvironment.ASSETS)));
        }
        return cond;
    }

    private record ConditionedTextureKey(String textureKey, String modelKey, List<Condition> conditions) {

        public boolean isActive(DataContext context) {
            for (Condition condition : this.conditions) {
                if (!condition.active(context)) {
                    return false;
                }
            }

            return true;
        }

    }

}
