package net.threetag.palladium.logic.molang;

import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.attachment.PalladiumAttachments;
import net.threetag.palladium.entity.flight.EntityFlightHandler;
import net.threetag.palladium.entity.flight.SwingingFlightType;
import net.threetag.palladium.event.RegisterMoLangQueriesEvent;
import net.threetag.palladium.power.ability.AbilityReference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;
import team.unnamed.mocha.runtime.binding.Binding;
import team.unnamed.mocha.runtime.value.Function;
import team.unnamed.mocha.runtime.value.ObjectProperty;
import team.unnamed.mocha.runtime.value.ObjectValue;
import team.unnamed.mocha.runtime.value.Value;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static net.threetag.palladium.logic.molang.VanillaQueries.HEAD_X_ROTATION;
import static net.threetag.palladium.logic.molang.VanillaQueries.HEAD_Y_ROTATION;

@EventBusSubscriber(modid = Palladium.MOD_ID)
public class PalladiumQueries implements ObjectValue {

    @SubscribeEvent
    static void registerMoLang(RegisterMoLangQueriesEvent e) {
        e.register(Palladium.MOD_ID, PalladiumQueries.class, PalladiumQueries::new);
    }

    private final EntityContext context;
    private final Map<String, Supplier<Object>> functions = new HashMap<>();

    public PalladiumQueries(EntityContext context) {
        this.context = context;
        this.functions.put(HEAD_X_ROTATION, this::head_x_rotation);
        this.functions.put(HEAD_Y_ROTATION, this::head_y_rotation);
        this.functions.put("tick_count", this::tick_count);
        this.functions.put("horizontal_speed", this::horizontal_speed);
        this.functions.put("flight_pitch", this::flight_pitch);
        this.functions.put("flight_roll", this::flight_roll);
        this.functions.put("flight_yaw", this::flight_yaw);
        this.functions.put("flight_limb_pitch", this::flight_limb_pitch);
        this.functions.put("flight_limb_roll", this::flight_limb_roll);
        this.functions.put("flight_limb_yaw", this::flight_limb_yaw);
        this.functions.put("swinging_right_arm_pitch", this::swinging_right_arm_pitch);
        this.functions.put("swinging_left_arm_pitch", this::swinging_left_arm_pitch);
        this.functions.put("any_animation_finished", this::any_animation_finished);
        this.functions.put("gliding_tick_count", this::gliding_tick_count);
        this.functions.put("is_wall_climbing", this::is_wall_climbing);
        this.functions.put("pos_x", this::pos_x);
        this.functions.put("pos_y", this::pos_y);
        this.functions.put("pos_z", this::pos_z);
    }

    @Override
    public @NotNull Value get(@NonNull String name) {
        if (name.equals("get_animation_timer_eased")) {
            return (Function<?>) (ctx, args) -> Value.of(get_animation_timer_eased(args.next().toString()));
        } else if (name.equals("model_property")) {
            return (Function<?>) (ctx, args) -> Value.of(model_property(args.next().toString(), args.next().toString()));
        } else if (this.functions.containsKey(name)) {
            return Value.of(this.functions.get(name).get());
        }
        return Value.nil();
    }

    @Binding(HEAD_X_ROTATION)
    public double head_x_rotation() {
        return context.entity().getViewXRot(context.partialTick());
    }

    @Binding(HEAD_Y_ROTATION)
    public double head_y_rotation() {
        if (context.entity() instanceof LivingEntity living) {
            float f = Mth.rotLerp(context.partialTick(), living.yHeadRotO, living.yHeadRot);
            float bodyRot = LivingEntityRenderer.solveBodyRot(living, f, context.partialTick());
            return Mth.wrapDegrees(f - bodyRot);
        }

        return context.entity().getViewYRot(context.partialTick());
    }

    @Binding("tick_count")
    public double tick_count() {
        return context.entity().tickCount + context.partialTick();
    }

    @Binding("get_animation_timer_eased")
    public double get_animation_timer_eased(String abilityRef) {
        if (context.entity() instanceof LivingEntity living && abilityRef != null) {
            var ability = AbilityReference.parse(abilityRef).getInstance(living);

            if (ability != null) {
                return ability.getAnimationTimerProgressEased(context.partialTick());
            }
        }

        return 0F;
    }

    @Binding("horizontal_speed")
    public double horizontal_speed() {
        return context.entity().position().subtract(context.entity().oldPosition()).length();
    }

    @Binding("model_property")
    public double model_property(String bone, String type) {
        return this.context.getModelValue(bone, type);
    }

    @Binding("flight_pitch")
    public double flight_pitch() {
        var animation = context.entity() instanceof LivingEntity living
                ? EntityFlightHandler.get(living).getAnimationHandler()
                : null;
        return animation != null ? animation.getPitch(context.partialTick()) : 0;
    }

    @Binding("flight_roll")
    public double flight_roll() {
        var animation = context.entity() instanceof LivingEntity living
                ? EntityFlightHandler.get(living).getAnimationHandler()
                : null;
        return animation != null ? animation.getRoll(context.partialTick()) : 0;
    }

    @Binding("flight_yaw")
    public double flight_yaw() {
        var animation = context.entity() instanceof LivingEntity living
                ? EntityFlightHandler.get(living).getAnimationHandler()
                : null;
        return animation != null ? animation.getYaw(context.partialTick()) : 0;
    }

    @Binding("flight_limb_pitch")
    public double flight_limb_pitch() {
        var animation = context.entity() instanceof LivingEntity living
                ? EntityFlightHandler.get(living).getAnimationHandler()
                : null;
        return animation != null ? animation.getLimbPitch(context.partialTick()) : 0;
    }

    @Binding("flight_limb_roll")
    public double flight_limb_roll() {
        var animation = context.entity() instanceof LivingEntity living
                ? EntityFlightHandler.get(living).getAnimationHandler()
                : null;
        return animation != null ? animation.getLimbRoll(context.partialTick()) : 0;
    }

    @Binding("flight_limb_yaw")
    public double flight_limb_yaw() {
        var animation = context.entity() instanceof LivingEntity living
                ? EntityFlightHandler.get(living).getAnimationHandler()
                : null;
        return animation != null ? animation.getLimbYaw(context.partialTick()) : 0;
    }

    @Binding("swinging_right_arm_pitch")
    public double swinging_right_arm_pitch() {
        var animation = context.entity() instanceof LivingEntity living
                ? EntityFlightHandler.get(living).getAnimationHandler()
                : null;
        return animation instanceof SwingingFlightType.AnimationHandler swinging
                ? swinging.getRightArmPitch(context.partialTick())
                : 0;
    }

    @Binding("swinging_left_arm_pitch")
    public double swinging_left_arm_pitch() {
        var animation = context.entity() instanceof LivingEntity living
                ? EntityFlightHandler.get(living).getAnimationHandler()
                : null;
        return animation instanceof SwingingFlightType.AnimationHandler swinging
                ? swinging.getLeftArmPitch(context.partialTick())
                : 0;
    }

    @Binding("any_animation_finished")
    public boolean any_animation_finished() {
        return this.context.hasAnimationFinished();
    }

    @Binding("gliding_tick_count")
    public double gliding_tick_count() {
        return this.context.entity() instanceof LivingEntity living ? living.getFallFlyingTicks() : 0;
    }

    @Binding("is_wall_climbing")
    public boolean is_wall_climbing() {
        return this.context.entity() instanceof Player player && player.getData(PalladiumAttachments.IS_CLIMBING.get());
    }

    @Binding("pos_x")
    public double pos_x() {
        return this.context.entity().getX();
    }

    @Binding("pos_y")
    public double pos_y() {
        return this.context.entity().getY();
    }

    @Binding("pos_z")
    public double pos_z() {
        return this.context.entity().getZ();
    }

    @Override
    public @Nullable ObjectProperty getProperty(@NotNull String name) {
        return null;
    }
}
