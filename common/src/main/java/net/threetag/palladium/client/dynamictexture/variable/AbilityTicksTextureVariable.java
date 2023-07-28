package net.threetag.palladium.client.dynamictexture.variable;

import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.ability.AbilityEntry;
import net.threetag.palladium.power.ability.AbilityUtil;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladium.util.json.GsonUtil;

import java.util.List;

public class AbilityTicksTextureVariable extends AbstractIntegerTextureVariable {

    private final ResourceLocation powerId;
    private final String abilityId;

    public AbilityTicksTextureVariable(ResourceLocation powerId, String abilityId, List<Pair<Operation, Integer>> operations) {
        super(operations);
        this.powerId = powerId;
        this.abilityId = abilityId;
    }

    public AbilityTicksTextureVariable(JsonObject json) {
        super(json);
        this.powerId = GsonUtil.getAsResourceLocation(json, "power");
        this.abilityId = GsonHelper.getAsString(json, "ability");
    }

    @Override
    public int getNumber(DataContext context) {
        var livingEntity = context.getLivingEntity();
        if (livingEntity != null) {
            AbilityEntry entry = AbilityUtil.getEntry(livingEntity, this.powerId, this.abilityId);

            if (entry == null) {
                return 0;
            }

            return entry.getEnabledTicks();
        }
        return 0;
    }

}
