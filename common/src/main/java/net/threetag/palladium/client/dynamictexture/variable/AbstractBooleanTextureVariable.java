package net.threetag.palladium.client.dynamictexture.variable;

import com.google.gson.JsonObject;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.LivingEntity;

public abstract class AbstractBooleanTextureVariable implements ITextureVariable {

    public final String trueValue;
    public final String falseValue;

    public AbstractBooleanTextureVariable(JsonObject json) {
        this.trueValue = GsonHelper.getAsString(json, "true_value", "true");
        this.falseValue = GsonHelper.getAsString(json, "false_value", "false");
    }

    public abstract boolean getBoolean(LivingEntity entity);

    @Override
    public Object get(LivingEntity entity) {
        return this.getBoolean(entity) ? this.trueValue : this.falseValue;
    }
}
