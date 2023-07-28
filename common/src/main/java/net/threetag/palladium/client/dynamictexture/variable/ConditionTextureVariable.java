package net.threetag.palladium.client.dynamictexture.variable;

import com.google.gson.JsonObject;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.condition.Condition;
import net.threetag.palladium.condition.ConditionEnvironment;
import net.threetag.palladium.condition.ConditionSerializer;
import net.threetag.palladium.util.context.DataContext;

import java.util.List;

public class ConditionTextureVariable extends AbstractBooleanTextureVariable {

    private final List<Condition> conditions;

    public ConditionTextureVariable(JsonObject json) {
        super(json);

        this.conditions = ConditionSerializer.listFromJSON(json.get("conditions"), ConditionEnvironment.ASSETS);
    }

    @Override
    public boolean getBoolean(DataContext context) {
        for (Condition condition : this.conditions) {
            if (!condition.active(context)) {
                return false;
            }
        }
        return false;
    }
}
