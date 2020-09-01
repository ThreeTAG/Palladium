package net.threetag.threecore.client.renderer.entity.modellayer.predicates;

import net.minecraft.entity.LivingEntity;
import net.threetag.threecore.ability.Ability;
import net.threetag.threecore.ability.AbilityHelper;
import net.threetag.threecore.ability.AcceleratingFlightAbility;
import net.threetag.threecore.ability.FlightAbility;
import net.threetag.threecore.client.renderer.entity.modellayer.IModelLayerContext;

import java.util.List;

public class FlyingPredicate implements IModelLayerPredicate {

    @Override
    public boolean test(IModelLayerContext context) {
        if (context.getAsEntity() instanceof LivingEntity && !context.getAsEntity().isOnGround()) {
            List<Ability> abilityList = AbilityHelper.getAbilities((LivingEntity) context.getAsEntity());

            for (Ability ability : abilityList) {
                if (ability instanceof FlightAbility || ability instanceof AcceleratingFlightAbility) {
                    if (ability.getConditionManager().isEnabled()) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

}
