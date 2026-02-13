package net.threetag.palladium.logic.molang;

import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.event.RegisterMoLangQueriesEvent;
import net.threetag.palladium.util.EntityUtil;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

@EventBusSubscriber(modid = Palladium.MOD_ID)
public class VanillaQueries {

    public static final String ACTOR_COUNT = "actor_count";
    public static final String BLOCKING = "blocking";
    public static final String BODY_X_ROTATION = "body_x_rotation";
    public static final String BODY_Y_ROTATION = "body_y_rotation";
    public static final String CARDINAL_FACING = "cardinal_facing";
    public static final String CARDINAL_FACING_2D = "cardinal_facing_2d";
    public static final String CARDINAL_PLAYER_FACING = "cardinal_player_facing";
    public static final String DAY = "day";
    public static final String DEATH_TICKS = "death_ticks";
    public static final String DISTANCE_FROM_CAMERA = "distance_from_camera";
    public static final String EQUIPMENT_COUNT = "equipment_count";
    public static final String FRAME_ALPHA = "frame_alpha";
    public static final String GET_ACTOR_INFO_ID = "get_actor_info_id";
    public static final String GROUND_SPEED = "ground_speed";
    public static final String HAS_CAPE = "has_cape";
    public static final String HAS_COLLISION = "has_collision";
    public static final String HAS_GRAVITY = "has_gravity";
    public static final String HAS_HEAD_GEAR = "has_head_gear";
    public static final String HAS_OWNER = "has_owner";
    public static final String HAS_PLAYER_RIDER = "has_player_rider";
    public static final String HAS_RIDER = "has_rider";
    public static final String HEAD_X_ROTATION = "head_x_rotation";
    public static final String HEAD_Y_ROTATION = "head_y_rotation";
    public static final String HEALTH = "health";
    public static final String HURT_TIME = "hurt_time";
    public static final String INVULNERABLE_TICKS = "invulnerable_ticks";
    public static final String IS_ALIVE = "is_alive";
    public static final String IS_ANGRY = "is_angry";
    public static final String IS_BABY = "is_baby";
    public static final String IS_BREATHING = "is_breathing";
    public static final String IS_FIRE_IMMUNE = "is_fire_immune";
    public static final String IS_FIRST_PERSON = "is_first_person";
    public static final String IS_IN_CONTACT_WITH_WATER = "is_in_contact_with_water";
    public static final String IS_IN_LAVA = "is_in_lava";
    public static final String IS_IN_WATER = "is_in_water";
    public static final String IS_IN_WATER_OR_RAIN = "is_in_water_or_rain";
    public static final String IS_INVISIBLE = "is_invisible";
    public static final String IS_LEASHED = "is_leashed";
    public static final String IS_MOVING = "is_moving";
    public static final String IS_ON_FIRE = "is_on_fire";
    public static final String IS_ON_GROUND = "is_on_ground";
    public static final String IS_RIDING = "is_riding";
    public static final String IS_SADDLED = "is_saddled";
    public static final String IS_SILENT = "is_silent";
    public static final String IS_SLEEPING = "is_sleeping";
    public static final String IS_SNEAKING = "is_sneaking";
    public static final String IS_SPRINTING = "is_sprinting";
    public static final String IS_SWIMMING = "is_swimming";
    public static final String IS_USING_ITEM = "is_using_item";
    public static final String IS_WALL_CLIMBING = "is_wall_climbing";
    public static final String LIFE_TIME = "life_time";
    public static final String LIMB_SWING = "limb_swing";
    public static final String LIMB_SWING_AMOUNT = "limb_swing_amount";
    public static final String MAIN_HAND_ITEM_MAX_DURATION = "main_hand_item_max_duration";
    public static final String MAIN_HAND_ITEM_USE_DURATION = "main_hand_item_use_duration";
    public static final String MAX_HEALTH = "max_health";
    public static final String MOON_BRIGHTNESS = "moon_brightness";
    public static final String MOON_PHASE = "moon_phase";
    public static final String MOVEMENT_DIRECTION = "movement_direction";
    public static final String PLAYER_LEVEL = "player_level";
    public static final String RIDER_BODY_X_ROTATION = "rider_body_x_rotation";
    public static final String RIDER_BODY_Y_ROTATION = "rider_body_y_rotation";
    public static final String RIDER_HEAD_X_ROTATION = "rider_head_x_rotation";
    public static final String RIDER_HEAD_Y_ROTATION = "rider_head_y_rotation";
    public static final String SCALE = "scale";
    public static final String SLEEP_ROTATION = "sleep_rotation";
    public static final String TIME_OF_DAY = "time_of_day";
    public static final String TIME_STAMP = "time_stamp";
    public static final String VERTICAL_SPEED = "vertical_speed";
    public static final String YAW_SPEED = "yaw_speed";

    @SubscribeEvent
    static void registerMoLang(RegisterMoLangQueriesEvent e) {
        e.getVanillaQuery()
                .setDouble(ACTOR_COUNT, (ctx, args) -> 1)
                .setDouble(CARDINAL_PLAYER_FACING, (ctx, args) -> ctx.entity().getDirection().ordinal())
                .setDouble(DAY, (ctx, args) -> ctx.level().getGameTime() / 24000D)
                .setDouble(FRAME_ALPHA, (ctx, args) -> ctx.partialTick())
                .setBoolean(HAS_CAPE, (ctx, args) -> Palladium.PROXY.hasCape(ctx.entity()))
                .setBoolean(IS_FIRST_PERSON, (ctx, args) -> Palladium.PROXY.isFirstPerson(ctx.entity()))
                .setDouble(LIFE_TIME, (ctx, args) -> 0D)
                .setDouble(MOON_BRIGHTNESS, (ctx, args) -> ctx.level().environmentAttributes().getValue(EnvironmentAttributes.STAR_BRIGHTNESS, ctx.entity().position()))
                .setDouble(MOON_PHASE, (ctx, args) -> ctx.level().environmentAttributes().getValue(EnvironmentAttributes.MOON_PHASE, ctx.entity().position()).index())
                .setDouble(PLAYER_LEVEL, (ctx, args) -> ctx.entity() instanceof Player player ? player.experienceLevel : 0D)
                .setDouble(TIME_OF_DAY, (ctx, args) -> ctx.level().getDayTime() / 24000D)
                .setDouble(TIME_STAMP, (ctx, args) -> ctx.level().getGameTime())
                .setDouble(BODY_X_ROTATION, (ctx, args) -> ctx.entity().getViewXRot(ctx.partialTick()))
                .setDouble(BODY_Y_ROTATION, (ctx, args) -> ctx.entity() instanceof LivingEntity living ? Mth.lerp(ctx.partialTick(), living.yBodyRotO, living.yBodyRot) : ctx.entity().getViewYRot(ctx.partialTick()))
                .setDouble(CARDINAL_FACING, (ctx, args) -> ctx.entity().getDirection().get3DDataValue())
                .setDouble(CARDINAL_FACING_2D, (ctx, args) -> {
                    int direction = ctx.entity().getDirection().get3DDataValue();
                    return direction < 2 ? 6 : direction;
                })
                .setDouble(DISTANCE_FROM_CAMERA, (ctx, args) -> 0D)
                .setDouble(GET_ACTOR_INFO_ID, (ctx, args) -> ctx.entity().getId())
                .setDouble(EQUIPMENT_COUNT, (ctx, args) -> ctx.entity() instanceof EquipmentUser armorable ? Arrays.stream(EquipmentSlot.values()).filter(EquipmentSlot::isArmor).filter(slot -> !armorable.getItemBySlot(slot).isEmpty()).count() : 0)
                .setBoolean(HAS_COLLISION, (ctx, args) -> !ctx.entity().noPhysics)
                .setBoolean(HAS_GRAVITY, (ctx, args) -> !ctx.entity().isNoGravity())
                .setBoolean(HAS_OWNER, (ctx, args) -> ctx.entity() instanceof OwnableEntity ownable && ownable.getOwnerReference() != null)
                .setBoolean(HAS_PLAYER_RIDER, (ctx, args) -> ctx.entity().hasPassenger(Player.class::isInstance))
                .setBoolean(HAS_RIDER, (ctx, args) -> ctx.entity().isVehicle())
                .setBoolean(IS_ALIVE, (ctx, args) -> ctx.entity().isAlive())
                .setBoolean(IS_ANGRY, (ctx, args) -> ctx.entity() instanceof NeutralMob neutralMob && neutralMob.isAngry())
                .setBoolean(IS_BREATHING, (ctx, args) -> ctx.entity().getAirSupply() >= ctx.entity().getMaxAirSupply())
                .setBoolean(IS_FIRE_IMMUNE, (ctx, args) -> ctx.entity().getType().fireImmune())
                .setBoolean(IS_INVISIBLE, (ctx, args) -> ctx.entity().isInvisible())
                .setBoolean(IS_IN_CONTACT_WITH_WATER, (ctx, args) -> ctx.entity().isInWaterOrRain())
                .setBoolean(IS_IN_LAVA, (ctx, args) -> ctx.entity().isInLava())
                .setBoolean(IS_IN_WATER, (ctx, args) -> ctx.entity().isInWater())
                .setBoolean(IS_IN_WATER_OR_RAIN, (ctx, args) -> ctx.entity().isInWaterOrRain())
                .setBoolean(IS_LEASHED, (ctx, args) -> ctx.entity() instanceof Leashable leashable && leashable.isLeashed())
                .setBoolean(IS_MOVING, (ctx, args) -> EntityUtil.isMoving(ctx.entity()))
                .setBoolean(IS_ON_FIRE, (ctx, args) -> ctx.entity().isOnFire())
                .setBoolean(IS_ON_GROUND, (ctx, args) -> ctx.entity().onGround())
                .setBoolean(IS_RIDING, (ctx, args) -> ctx.entity().isPassenger())
                .setBoolean(IS_SADDLED, (ctx, args) -> ctx.entity() instanceof EquipmentUser equipmentUser && !equipmentUser.getItemBySlot(EquipmentSlot.SADDLE).isEmpty())
                .setBoolean(IS_SILENT, (ctx, args) -> ctx.entity().isSilent())
                .setBoolean(IS_SNEAKING, (ctx, args) -> ctx.entity().isCrouching())
                .setBoolean(IS_SPRINTING, (ctx, args) -> ctx.entity().isSprinting())
                .setBoolean(IS_SWIMMING, (ctx, args) -> ctx.entity().isSwimming())
                .setDouble(MOVEMENT_DIRECTION, (ctx, args) -> EntityUtil.isMoving(ctx.entity()) ? Direction.getApproximateNearest(ctx.entity().getDeltaMovement()).get3DDataValue() : 6)
                .setDouble(RIDER_BODY_X_ROTATION, (ctx, args) -> ctx.entity().isVehicle() ? ctx.entity().getFirstPassenger() instanceof LivingEntity ? 0 : Objects.requireNonNull(ctx.entity().getFirstPassenger()).getViewXRot(ctx.partialTick()) : 0)
                .setDouble(RIDER_BODY_Y_ROTATION, (ctx, args) -> ctx.entity().isVehicle() ? ctx.entity().getFirstPassenger() instanceof LivingEntity living ? Mth.lerp(ctx.partialTick(), living.yBodyRotO, living.yBodyRot) : Objects.requireNonNull(ctx.entity().getFirstPassenger()).getViewYRot(ctx.partialTick()) : 0)
                .setDouble(RIDER_HEAD_X_ROTATION, (ctx, args) -> ctx.entity().getFirstPassenger() instanceof LivingEntity living ? living.getViewXRot(ctx.partialTick()) : 0)
                .setDouble(RIDER_HEAD_Y_ROTATION, (ctx, args) -> ctx.entity().getFirstPassenger() instanceof LivingEntity living ? living.getViewYRot(ctx.partialTick()) : 0)
                .setDouble(VERTICAL_SPEED, (ctx, args) -> ctx.entity().getDeltaMovement().y())
                .setDouble(YAW_SPEED, (ctx, args) -> ctx.entity().getYRot(ctx.partialTick()) - ctx.entity().yRotO)

                .setBoolean(BLOCKING, (ctx, args) -> ctx.entity() instanceof LivingEntity living && living.isBlocking())
                .setDouble(DEATH_TICKS, (ctx, args) -> ctx.entity() instanceof LivingEntity living ? living.deathTime == 0 ? 0 : living.deathTime + ctx.partialTick() : 0)
                .setDouble(GROUND_SPEED, (ctx, args) -> ctx.entity().getDeltaMovement().horizontalDistance())
                .setBoolean(HAS_HEAD_GEAR, (ctx, args) -> ctx.entity() instanceof EquipmentUser equipmentUser && !equipmentUser.getItemBySlot(EquipmentSlot.HEAD).isEmpty())
                .setDouble(HEAD_X_ROTATION, (ctx, args) -> ctx.entity().getViewXRot(ctx.partialTick()))
                .setDouble(HEAD_Y_ROTATION, (ctx, args) -> ctx.entity().getViewYRot(ctx.partialTick()))
                .setDouble(HEALTH, (ctx, args) -> ctx.entity() instanceof LivingEntity living ? living.getHealth() : 0)
                .setDouble(HURT_TIME, (ctx, args) -> ctx.entity() instanceof LivingEntity living ? living.hurtTime == 0 ? 0 : living.hurtTime - ctx.partialTick() : 0)
                .setDouble(INVULNERABLE_TICKS, (ctx, args) -> ctx.entity() instanceof LivingEntity living ? living.invulnerableTime == 0 ? 0 : living.invulnerableTime - ctx.partialTick() : 0)
                .setBoolean(IS_BABY, (ctx, args) -> ctx.entity() instanceof LivingEntity living && living.isBaby())
                .setBoolean(IS_SLEEPING, (ctx, args) -> ctx.entity() instanceof LivingEntity living && living.isSleeping())
                .setBoolean(IS_USING_ITEM, (ctx, args) -> ctx.entity() instanceof LivingEntity living && living.isUsingItem())
                .setBoolean(IS_WALL_CLIMBING, (ctx, args) -> ctx.entity() instanceof LivingEntity living && living.onClimbable())
                .setDouble(LIMB_SWING, (ctx, args) -> ctx.entity() instanceof LivingEntity living ? living.walkAnimation.position() : 0)
                .setDouble(LIMB_SWING_AMOUNT, (ctx, args) -> ctx.entity() instanceof LivingEntity living ? living.walkAnimation.speed(ctx.partialTick()) : 0)
                .setDouble(MAIN_HAND_ITEM_MAX_DURATION, (ctx, args) -> ctx.entity() instanceof LivingEntity living ? living.getMainHandItem().getUseDuration(living) : 0)
                .setDouble(MAIN_HAND_ITEM_USE_DURATION, (ctx, args) -> ctx.entity() instanceof LivingEntity living && living.getUsedItemHand() == InteractionHand.MAIN_HAND ? (living.getTicksUsingItem() / 20D) + ctx.partialTick() : 0)
                .setDouble(MAX_HEALTH, (ctx, args) -> ctx.entity() instanceof LivingEntity living ? living.getMaxHealth() : 0D)
                .setDouble(SCALE, (ctx, args) -> ctx.entity() instanceof LivingEntity living ? living.getScale() : 1D)
                .setDouble(SLEEP_ROTATION, (ctx, args) -> Optional.ofNullable(ctx.entity() instanceof LivingEntity living ? living.getBedOrientation() : null).map(Direction::toYRot).orElse(0f));
    }

}
