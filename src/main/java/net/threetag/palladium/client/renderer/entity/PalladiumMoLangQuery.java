package net.threetag.palladium.client.renderer.entity;

import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.entity.flight.EntityFlightHandler;
import net.threetag.palladium.entity.flight.SwingingFlightType;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.power.ability.AbilityReference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.unnamed.mocha.runtime.binding.Binding;
import team.unnamed.mocha.runtime.value.*;

@Binding("query")
public class PalladiumMoLangQuery implements ObjectValue {

    public static final PalladiumMoLangQuery INSTANCE = new PalladiumMoLangQuery();
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
        return switch (name) {
            case "get_age" -> ObjectProperty.property(Value.of(get_age()), false).value();
            case "get_animation_timer_eased" -> (Function<?>) (ctx, args) -> {
                var arg = args.next().eval();
                return NumberValue.of(get_animation_timer_eased(arg != null ? arg.getAsString() : null));
            };
            case "horizontal_speed" -> ObjectProperty.property(Value.of(horizontal_speed()), false).value();
            case "flight_pitch" -> ObjectProperty.property(Value.of(flight_pitch()), false).value();
            case "flight_roll" -> ObjectProperty.property(Value.of(flight_roll()), false).value();
            case "flight_yaw" -> ObjectProperty.property(Value.of(flight_yaw()), false).value();
            case "flight_limb_pitch" -> ObjectProperty.property(Value.of(flight_limb_pitch()), false).value();
            case "flight_limb_roll" -> ObjectProperty.property(Value.of(flight_limb_roll()), false).value();
            case "flight_limb_yaw" -> ObjectProperty.property(Value.of(flight_limb_yaw()), false).value();
            case "swinging_right_arm_pitch" ->
                    ObjectProperty.property(Value.of(swinging_right_arm_pitch()), false).value();
            case "swinging_left_arm_pitch" ->
                    ObjectProperty.property(Value.of(swinging_left_arm_pitch()), false).value();
            default -> Value.nil();
        };
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

    @Binding("horizontal_speed")
    public double horizontal_speed() {
        var entity = CONTEXT.getEntity();

        if (entity == null) {
            return 0D;
        }

        return entity.position().subtract(entity.oldPosition()).length();
    }

    @Binding("flight_pitch")
    public double flight_pitch() {
        var entity = CONTEXT.getLivingEntity();

        if (entity != null) {
            var animation = EntityFlightHandler.get(entity).getAnimationHandler();
            return animation != null ? animation.getPitch(partialTick) : 0;
        }

        return 0F;
    }

    @Binding("flight_roll")
    public double flight_roll() {
        var entity = CONTEXT.getLivingEntity();

        if (entity != null) {
            var animation = EntityFlightHandler.get(entity).getAnimationHandler();
            return animation != null ? animation.getRoll(partialTick) : 0;
        }

        return 0F;
    }

    @Binding("flight_yaw")
    public double flight_yaw() {
        var entity = CONTEXT.getLivingEntity();

        if (entity != null) {
            var animation = EntityFlightHandler.get(entity).getAnimationHandler();
            return animation != null ? animation.getYaw(partialTick) : 0;
        }

        return 0F;
    }

    @Binding("flight_limb_pitch")
    public double flight_limb_pitch() {
        var entity = CONTEXT.getLivingEntity();

        if (entity != null) {
            var animation = EntityFlightHandler.get(entity).getAnimationHandler();
            return animation != null ? animation.getLimbPitch(partialTick) : 0;
        }

        return 0F;
    }

    @Binding("flight_limb_roll")
    public double flight_limb_roll() {
        var entity = CONTEXT.getLivingEntity();

        if (entity != null) {
            var animation = EntityFlightHandler.get(entity).getAnimationHandler();
            return animation != null ? animation.getLimbRoll(partialTick) : 0;
        }

        return 0F;
    }

    @Binding("flight_limb_yaw")
    public double flight_limb_yaw() {
        var entity = CONTEXT.getLivingEntity();

        if (entity != null) {
            var animation = EntityFlightHandler.get(entity).getAnimationHandler();
            return animation != null ? animation.getLimbYaw(partialTick) : 0;
        }

        return 0F;
    }

    @Binding("swinging_right_arm_pitch")
    public double swinging_right_arm_pitch() {
        var entity = CONTEXT.getLivingEntity();

        if (entity != null) {
            var animation = EntityFlightHandler.get(entity).getAnimationHandler();
            return animation instanceof SwingingFlightType.AnimationHandler controller ? controller.getRightArmPitch(partialTick) : 0;
        }

        return 0F;
    }

    @Binding("swinging_left_arm_pitch")
    public double swinging_left_arm_pitch() {
        var entity = CONTEXT.getLivingEntity();

        if (entity != null) {
            var animation = EntityFlightHandler.get(entity).getAnimationHandler();
            return animation instanceof SwingingFlightType.AnimationHandler controller ? controller.getLeftArmPitch(partialTick) : 0;
        }

        return 0F;
    }


}
