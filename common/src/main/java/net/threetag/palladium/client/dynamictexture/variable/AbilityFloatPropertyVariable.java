package net.threetag.palladium.client.dynamictexture.variable;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.ability.AbilityEntry;
import net.threetag.palladium.power.ability.AbilityUtil;
import net.threetag.palladium.util.json.GsonUtil;
import net.threetag.palladium.util.property.FloatProperty;
import net.threetag.palladium.util.property.PalladiumProperty;

import java.util.List;

public class AbilityFloatPropertyVariable extends AbstractFloatTextureVariable {

    private final ResourceLocation powerId;
    private final String abilityId;
    private final String propertyKey;

    public AbilityFloatPropertyVariable(ResourceLocation powerId, String abilityId, String propertyKey, List<Pair<Operation, JsonPrimitive>> operations) {
        super(operations);
        this.powerId = powerId;
        this.abilityId = abilityId;
        this.propertyKey = propertyKey;
    }

    public AbilityFloatPropertyVariable(JsonObject json) {
        super(json);
        this.powerId = GsonUtil.getAsResourceLocation(json, "power");
        this.abilityId = GsonHelper.getAsString(json, "ability");
        this.propertyKey = GsonHelper.getAsString(json, "property");
    }

    @Override
    public float getNumber(Entity entity) {
        if (entity instanceof LivingEntity livingEntity) {
            AbilityEntry entry = AbilityUtil.getEntry(livingEntity, this.powerId, this.abilityId);

            if (entry == null) {
                return 0F;
            }

            PalladiumProperty<?> property = entry.getEitherPropertyByKey(this.propertyKey);

            if (property instanceof FloatProperty floatProperty) {
                return entry.getProperty(floatProperty);
            }
        }

        return 0F;
    }
}
