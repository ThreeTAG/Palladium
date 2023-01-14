package net.threetag.palladium.client.dynamictexture.variable;

import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.ability.AbilityEntry;
import net.threetag.palladium.power.ability.AbilityUtil;
import net.threetag.palladium.util.json.GsonUtil;
import net.threetag.palladium.util.property.IntegerProperty;
import net.threetag.palladium.util.property.PalladiumProperty;

import java.util.List;

public class AbilityIntegerPropertyVariable extends AbstractIntegerTextureVariable {

    private final ResourceLocation powerId;
    private final String abilityId;
    private final String propertyKey;

    public AbilityIntegerPropertyVariable(ResourceLocation powerId, String abilityId, String propertyKey, List<Pair<Operation, Integer>> operations) {
        super(operations);
        this.powerId = powerId;
        this.abilityId = abilityId;
        this.propertyKey = propertyKey;
    }

    public AbilityIntegerPropertyVariable(JsonObject json) {
        super(json);
        this.powerId = GsonUtil.getAsResourceLocation(json, "power");
        this.abilityId = GsonHelper.getAsString(json, "ability");
        this.propertyKey = GsonHelper.getAsString(json, "property");
    }

    @Override
    public int getNumber(LivingEntity entity) {
        AbilityEntry entry = AbilityUtil.getEntry(entity, this.powerId, this.abilityId);

        if (entry == null) {
            return 0;
        }

        PalladiumProperty<?> property = entry.getEitherPropertyByKey(this.propertyKey);

        if (property instanceof IntegerProperty integerProperty) {
            return entry.getProperty(integerProperty);
        }

        return 0;
    }
}
