package net.threetag.palladium.client.dynamictexture.variable;

import com.google.gson.JsonObject;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.condition.Condition;
import net.threetag.palladium.condition.ConditionContextType;
import net.threetag.palladium.condition.ConditionSerializer;

import java.util.List;

public class ConditionTextureVariable extends AbstractBooleanTextureVariable {

    private final List<Condition> conditions;

    public ConditionTextureVariable(JsonObject json) {
        super(json);

        this.conditions = ConditionSerializer.listFromJSON(json.get("conditions"), ConditionContextType.RENDER_LAYERS);
    }

    @Override
    public boolean getBoolean(Entity entity) {
        if(entity instanceof LivingEntity living) {
            for (Condition condition : this.conditions) {
                if(!condition.active(living, null, null, null)) {
                    return false;
                }
            }

            return true;
        }
        return false;
    }
}
