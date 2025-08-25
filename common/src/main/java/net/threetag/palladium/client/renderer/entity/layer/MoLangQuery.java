package net.threetag.palladium.client.renderer.entity.layer;

import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.data.DataContext;
import net.threetag.palladium.power.ability.AbilityReference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.unnamed.mocha.runtime.binding.Binding;
import team.unnamed.mocha.runtime.value.*;

@Binding("query")
public class MoLangQuery implements ObjectValue {

    public static final MoLangQuery INSTANCE = new MoLangQuery();
    private static DataContext CONTEXT = null;
    private static float partialTick = 0F;

    public static void setContext(DataContext context, float pTick) {
        CONTEXT = context;
        partialTick = pTick;
    }

    @Override
    public @Nullable ObjectProperty getProperty(@NotNull String s) {
        return null;
    }

    @Override
    public @NotNull Value get(@NotNull String name) {
        if (name.equals("get_age")) {
            return ObjectProperty.property(Value.of(get_age()), false).value();
        } else if (name.equals("get_animation_timer_eased")) {
            return (Function<?>) (ctx, args) -> {
                var arg = args.next().eval();
                return NumberValue.of(get_animation_timer_eased(arg != null ? arg.getAsString() : null));
            };
        }

        return Value.nil();
    }

    @Binding("get_age")
    public double get_age() {
        var entity = CONTEXT.getEntity();
        return entity != null ? entity.tickCount + partialTick : 0;
    }

    @Binding("get_animation_timer_eased")
    public double get_animation_timer_eased(String abilityKey) {
        if (CONTEXT.getEntity() instanceof LivingEntity living) {
            var ability = abilityKey == null || abilityKey.isEmpty() ? CONTEXT.getAbility() : AbilityReference.parse(abilityKey).getInstance(living, CONTEXT.getPowerHolder());

            if (ability != null) {
                return ability.getAnimationTimerValueEased(partialTick);
            }
        }

        return 0F;
    }

}
