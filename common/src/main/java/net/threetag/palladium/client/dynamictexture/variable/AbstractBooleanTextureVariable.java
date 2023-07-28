package net.threetag.palladium.client.dynamictexture.variable;

import com.google.gson.JsonObject;
import net.minecraft.util.GsonHelper;
import net.threetag.palladium.util.context.DataContext;

public abstract class AbstractBooleanTextureVariable implements ITextureVariable {

    public final String trueValue;
    public final String falseValue;

    public AbstractBooleanTextureVariable(JsonObject json) {
        this.trueValue = GsonHelper.getAsString(json, "true_value", "true");
        this.falseValue = GsonHelper.getAsString(json, "false_value", "false");
    }

    public abstract boolean getBoolean(DataContext context);

    @Override
    public Object get(DataContext context) {
        return this.getBoolean(context) ? this.trueValue : this.falseValue;
    }
}
