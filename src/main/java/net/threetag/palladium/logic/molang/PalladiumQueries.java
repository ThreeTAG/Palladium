package net.threetag.palladium.logic.molang;

import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.entity.flight.EntityFlightHandler;
import net.threetag.palladium.entity.flight.SwingingFlightType;
import net.threetag.palladium.event.RegisterMoLangQueriesEvent;
import net.threetag.palladium.power.ability.AbilityReference;

@EventBusSubscriber(modid = Palladium.MOD_ID)
public class PalladiumQueries {

    @SubscribeEvent
    static void registerMoLang(RegisterMoLangQueriesEvent e) {
        e.getPalladiumQuery()
                .setDouble("tick_count", (ctx, args) -> ctx.entity().tickCount + ctx.partialTick())
                .setDouble("cape_x_rot", (ctx, args) -> 0)
                .setDouble("cape_y_rot", (ctx, args) -> 0)
                .setDouble("cape_z_rot", (ctx, args) -> 0)
                .setDouble("get_animation_timer_eased", (ctx, args) -> {
                    var arg = args.next().eval();
                    if (ctx.entity() instanceof LivingEntity living && arg != null) {
                        var ability = AbilityReference.parse(arg.getAsString()).getInstance(living);

                        if (ability != null) {
                            return ability.getAnimationTimerProgressEased(ctx.partialTick());
                        }
                    }

                    return 0F;
                })
                .setDouble("horizontal_speed", (ctx, args) -> ctx.entity().position().subtract(ctx.entity().oldPosition()).length())
                .setDouble("flight_pitch", (ctx, args) -> {
                    var animation = ctx.entity() instanceof LivingEntity living ? EntityFlightHandler.get(living).getAnimationHandler() : null;
                    return animation != null ? animation.getPitch(ctx.partialTick()) : 0;
                })
                .setDouble("flight_roll", (ctx, args) -> {
                    var animation = ctx.entity() instanceof LivingEntity living ? EntityFlightHandler.get(living).getAnimationHandler() : null;
                    return animation != null ? animation.getRoll(ctx.partialTick()) : 0;
                })
                .setDouble("flight_yaw", (ctx, args) -> {
                    var animation = ctx.entity() instanceof LivingEntity living ? EntityFlightHandler.get(living).getAnimationHandler() : null;
                    return animation != null ? animation.getYaw(ctx.partialTick()) : 0;
                })
                .setDouble("flight_limb_pitch", (ctx, args) -> {
                    var animation = ctx.entity() instanceof LivingEntity living ? EntityFlightHandler.get(living).getAnimationHandler() : null;
                    return animation != null ? animation.getLimbPitch(ctx.partialTick()) : 0;
                })
                .setDouble("flight_limb_roll", (ctx, args) -> {
                    var animation = ctx.entity() instanceof LivingEntity living ? EntityFlightHandler.get(living).getAnimationHandler() : null;
                    return animation != null ? animation.getLimbRoll(ctx.partialTick()) : 0;
                })
                .setDouble("flight_limb_yaw", (ctx, args) -> {
                    var animation = ctx.entity() instanceof LivingEntity living ? EntityFlightHandler.get(living).getAnimationHandler() : null;
                    return animation != null ? animation.getLimbYaw(ctx.partialTick()) : 0;
                })
                .setDouble("swinging_right_arm_pitch", (ctx, args) -> {
                    var animation = ctx.entity() instanceof LivingEntity living ? EntityFlightHandler.get(living).getAnimationHandler() : null;
                    return animation instanceof SwingingFlightType.AnimationHandler swinging ? swinging.getRightArmPitch(ctx.partialTick()) : 0;
                })
                .setDouble("swinging_left_arm_pitch", (ctx, args) -> {
                    var animation = ctx.entity() instanceof LivingEntity living ? EntityFlightHandler.get(living).getAnimationHandler() : null;
                    return animation instanceof SwingingFlightType.AnimationHandler swinging ? swinging.getLeftArmPitch(ctx.partialTick()) : 0;
                });
    }
}
