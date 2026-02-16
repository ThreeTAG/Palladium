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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.unnamed.mocha.runtime.binding.Binding;
import team.unnamed.mocha.runtime.value.ObjectProperty;
import team.unnamed.mocha.runtime.value.ObjectValue;
import team.unnamed.mocha.runtime.value.Value;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

@EventBusSubscriber(modid = Palladium.MOD_ID)
public class VanillaQueries implements ObjectValue {

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
        e.register("query", VanillaQueries.class, VanillaQueries::new);
    }

    private final EntityContext context;
    private final Map<String, Supplier<Object>> functions = new HashMap<>();

    public VanillaQueries(EntityContext context) {
        this.context = context;

        this.functions.put(ACTOR_COUNT, this::actor_count);
        this.functions.put(BLOCKING, this::blocking);
        this.functions.put(BODY_X_ROTATION, this::body_x_rotation);
        this.functions.put(BODY_Y_ROTATION, this::body_y_rotation);
        this.functions.put(CARDINAL_FACING, this::cardinal_facing);
        this.functions.put(CARDINAL_FACING_2D, this::cardinal_facing_2d);
        this.functions.put(CARDINAL_PLAYER_FACING, this::cardinal_player_facing);
        this.functions.put(DAY, this::day);
        this.functions.put(DEATH_TICKS, this::death_ticks);
        this.functions.put(DISTANCE_FROM_CAMERA, this::distance_from_camera);
        this.functions.put(EQUIPMENT_COUNT, this::equipment_count);
        this.functions.put(FRAME_ALPHA, this::frame_alpha);
        this.functions.put(GET_ACTOR_INFO_ID, this::get_actor_info_id);
        this.functions.put(GROUND_SPEED, this::ground_speed);
        this.functions.put(HAS_CAPE, this::has_cape);
        this.functions.put(HAS_COLLISION, this::has_collision);
        this.functions.put(HAS_GRAVITY, this::has_gravity);
        this.functions.put(HAS_HEAD_GEAR, this::has_head_gear);
        this.functions.put(HAS_OWNER, this::has_owner);
        this.functions.put(HAS_PLAYER_RIDER, this::has_player_rider);
        this.functions.put(HAS_RIDER, this::has_rider);
        this.functions.put(HEAD_X_ROTATION, this::head_x_rotation);
        this.functions.put(HEAD_Y_ROTATION, this::head_y_rotation);
        this.functions.put(HEALTH, this::health);
        this.functions.put(HURT_TIME, this::hurt_time);
        this.functions.put(INVULNERABLE_TICKS, this::invulnerable_ticks);
        this.functions.put(IS_ALIVE, this::is_alive);
        this.functions.put(IS_ANGRY, this::is_angry);
        this.functions.put(IS_BABY, this::is_baby);
        this.functions.put(IS_BREATHING, this::is_breathing);
        this.functions.put(IS_FIRE_IMMUNE, this::is_fire_immune);
        this.functions.put(IS_FIRST_PERSON, this::is_first_person);
        this.functions.put(IS_IN_CONTACT_WITH_WATER, this::is_in_contact_with_water);
        this.functions.put(IS_IN_LAVA, this::is_in_lava);
        this.functions.put(IS_IN_WATER, this::is_in_water);
        this.functions.put(IS_IN_WATER_OR_RAIN, this::is_in_water_or_rain);
        this.functions.put(IS_INVISIBLE, this::is_invisible);
        this.functions.put(IS_LEASHED, this::is_leashed);
        this.functions.put(IS_MOVING, this::is_moving);
        this.functions.put(IS_ON_FIRE, this::is_on_fire);
        this.functions.put(IS_ON_GROUND, this::is_on_ground);
        this.functions.put(IS_RIDING, this::is_riding);
        this.functions.put(IS_SADDLED, this::is_saddled);
        this.functions.put(IS_SILENT, this::is_silent);
        this.functions.put(IS_SLEEPING, this::is_sleeping);
        this.functions.put(IS_SNEAKING, this::is_sneaking);
        this.functions.put(IS_SPRINTING, this::is_sprinting);
        this.functions.put(IS_SWIMMING, this::is_swimming);
        this.functions.put(IS_USING_ITEM, this::is_using_item);
        this.functions.put(IS_WALL_CLIMBING, this::is_wall_climbing);
        this.functions.put(LIFE_TIME, this::life_time);
        this.functions.put(LIMB_SWING, this::limb_swing);
        this.functions.put(LIMB_SWING_AMOUNT, this::limb_swing_amount);
        this.functions.put(MAIN_HAND_ITEM_MAX_DURATION, this::main_hand_item_max_duration);
        this.functions.put(MAIN_HAND_ITEM_USE_DURATION, this::main_hand_item_use_duration);
        this.functions.put(MAX_HEALTH, this::max_health);
        this.functions.put(MOON_BRIGHTNESS, this::moon_brightness);
        this.functions.put(MOON_PHASE, this::moon_phase);
        this.functions.put(MOVEMENT_DIRECTION, this::movement_direction);
        this.functions.put(PLAYER_LEVEL, this::player_level);
        this.functions.put(RIDER_BODY_X_ROTATION, this::rider_body_x_rotation);
        this.functions.put(RIDER_BODY_Y_ROTATION, this::rider_body_y_rotation);
        this.functions.put(RIDER_HEAD_X_ROTATION, this::rider_head_x_rotation);
        this.functions.put(RIDER_HEAD_Y_ROTATION, this::rider_head_y_rotation);
        this.functions.put(SCALE, this::scale);
        this.functions.put(SLEEP_ROTATION, this::sleep_rotation);
        this.functions.put(TIME_OF_DAY, this::time_of_day);
        this.functions.put(TIME_STAMP, this::time_stamp);
        this.functions.put(VERTICAL_SPEED, this::vertical_speed);
        this.functions.put(YAW_SPEED, this::yaw_speed);
    }

    @Override
    public @NotNull Value get(@NotNull String name) {
        if (this.functions.containsKey(name)) {
            return Value.of(this.functions.get(name).get());
        }
        return Value.nil();
    }

    @Override
    public @Nullable ObjectProperty getProperty(@NotNull String name) {
        return null;
    }

    @Binding(ACTOR_COUNT)
    public double actor_count() {
        return 1;
    }

    @Binding(CARDINAL_PLAYER_FACING)
    public double cardinal_player_facing() {
        return context.entity().getDirection().ordinal();
    }

    @Binding(DAY)
    public double day() {
        return context.level().getGameTime() / 24000D;
    }

    @Binding(FRAME_ALPHA)
    public double frame_alpha() {
        return context.partialTick();
    }

    @Binding(HAS_CAPE)
    public boolean has_cape() {
        return Palladium.PROXY.hasCape(context.entity());
    }

    @Binding(IS_FIRST_PERSON)
    public boolean is_first_person() {
        return Palladium.PROXY.isFirstPerson(context.entity());
    }

    @Binding(LIFE_TIME)
    public double life_time() {
        return 0D;
    }

    @Binding(MOON_BRIGHTNESS)
    public double moon_brightness() {
        return context.level().environmentAttributes().getValue(EnvironmentAttributes.STAR_BRIGHTNESS,
                context.entity().position());
    }

    @Binding(MOON_PHASE)
    public double moon_phase() {
        return context.level().environmentAttributes()
                .getValue(EnvironmentAttributes.MOON_PHASE, context.entity().position()).index();
    }

    @Binding(PLAYER_LEVEL)
    public double player_level() {
        return context.entity() instanceof Player player ? player.experienceLevel : 0D;
    }

    @Binding(TIME_OF_DAY)
    public double time_of_day() {
        return context.level().getDayTime() / 24000D;
    }

    @Binding(TIME_STAMP)
    public double time_stamp() {
        return context.level().getGameTime();
    }

    @Binding(BODY_X_ROTATION)
    public double body_x_rotation() {
        return context.entity().getViewXRot(context.partialTick());
    }

    @Binding(BODY_Y_ROTATION)
    public double body_y_rotation() {
        return context.entity() instanceof LivingEntity living
                ? Mth.lerp(context.partialTick(), living.yBodyRotO, living.yBodyRot)
                : context.entity().getViewYRot(context.partialTick());
    }

    @Binding(CARDINAL_FACING)
    public double cardinal_facing() {
        return context.entity().getDirection().get3DDataValue();
    }

    @Binding(CARDINAL_FACING_2D)
    public double cardinal_facing_2d() {
        int direction = context.entity().getDirection().get3DDataValue();
        return direction < 2 ? 6 : direction;
    }

    @Binding(DISTANCE_FROM_CAMERA)
    public double distance_from_camera() {
        return 0D;
    }

    @Binding(GET_ACTOR_INFO_ID)
    public double get_actor_info_id() {
        return context.entity().getId();
    }

    @Binding(EQUIPMENT_COUNT)
    public double equipment_count() {
        return context.entity() instanceof EquipmentUser armorable ? Arrays.stream(EquipmentSlot.values())
                .filter(EquipmentSlot::isArmor).filter(slot -> !armorable.getItemBySlot(slot).isEmpty()).count() : 0;
    }

    @Binding(HAS_COLLISION)
    public boolean has_collision() {
        return !context.entity().noPhysics;
    }

    @Binding(HAS_GRAVITY)
    public boolean has_gravity() {
        return !context.entity().isNoGravity();
    }

    @Binding(HAS_OWNER)
    public boolean has_owner() {
        return context.entity() instanceof OwnableEntity ownable && ownable.getOwnerReference() != null;
    }

    @Binding(HAS_PLAYER_RIDER)
    public boolean has_player_rider() {
        return context.entity().hasPassenger(Player.class::isInstance);
    }

    @Binding(HAS_RIDER)
    public boolean has_rider() {
        return context.entity().isVehicle();
    }

    @Binding(IS_ALIVE)
    public boolean is_alive() {
        return context.entity().isAlive();
    }

    @Binding(IS_ANGRY)
    public boolean is_angry() {
        return context.entity() instanceof NeutralMob neutralMob && neutralMob.isAngry();
    }

    @Binding(IS_BREATHING)
    public boolean is_breathing() {
        return context.entity().getAirSupply() >= context.entity().getMaxAirSupply();
    }

    @Binding(IS_FIRE_IMMUNE)
    public boolean is_fire_immune() {
        return context.entity().getType().fireImmune();
    }

    @Binding(IS_INVISIBLE)
    public boolean is_invisible() {
        return context.entity().isInvisible();
    }

    @Binding(IS_IN_CONTACT_WITH_WATER)
    public boolean is_in_contact_with_water() {
        return context.entity().isInWaterOrRain();
    }

    @Binding(IS_IN_LAVA)
    public boolean is_in_lava() {
        return context.entity().isInLava();
    }

    @Binding(IS_IN_WATER)
    public boolean is_in_water() {
        return context.entity().isInWater();
    }

    @Binding(IS_IN_WATER_OR_RAIN)
    public boolean is_in_water_or_rain() {
        return context.entity().isInWaterOrRain();
    }

    @Binding(IS_LEASHED)
    public boolean is_leashed() {
        return context.entity() instanceof Leashable leashable && leashable.isLeashed();
    }

    @Binding(IS_MOVING)
    public boolean is_moving() {
        return EntityUtil.isMoving(context.entity());
    }

    @Binding(IS_ON_FIRE)
    public boolean is_on_fire() {
        return context.entity().isOnFire();
    }

    @Binding(IS_ON_GROUND)
    public boolean is_on_ground() {
        return context.entity().onGround();
    }

    @Binding(IS_RIDING)
    public boolean is_riding() {
        return context.entity().isPassenger();
    }

    @Binding(IS_SADDLED)
    public boolean is_saddled() {
        return context.entity() instanceof EquipmentUser equipmentUser
                && !equipmentUser.getItemBySlot(EquipmentSlot.SADDLE).isEmpty();
    }

    @Binding(IS_SILENT)
    public boolean is_silent() {
        return context.entity().isSilent();
    }

    @Binding(IS_SNEAKING)
    public boolean is_sneaking() {
        return context.entity().isCrouching();
    }

    @Binding(IS_SPRINTING)
    public boolean is_sprinting() {
        return context.entity().isSprinting();
    }

    @Binding(IS_SWIMMING)
    public boolean is_swimming() {
        return context.entity().isSwimming();
    }

    @Binding(MOVEMENT_DIRECTION)
    public double movement_direction() {
        return EntityUtil.isMoving(context.entity())
                ? Direction.getApproximateNearest(context.entity().getDeltaMovement()).get3DDataValue()
                : 6;
    }

    @Binding(RIDER_BODY_X_ROTATION)
    public double rider_body_x_rotation() {
        return context.entity().isVehicle() ? context.entity().getFirstPassenger() instanceof LivingEntity ? 0
                : Objects.requireNonNull(context.entity().getFirstPassenger()).getViewXRot(context.partialTick()) : 0;
    }

    @Binding(RIDER_BODY_Y_ROTATION)
    public double rider_body_y_rotation() {
        return context.entity().isVehicle() ? context.entity().getFirstPassenger() instanceof LivingEntity living
                ? Mth.lerp(context.partialTick(), living.yBodyRotO, living.yBodyRot)
                : Objects.requireNonNull(context.entity().getFirstPassenger()).getViewYRot(context.partialTick()) : 0;
    }

    @Binding(RIDER_HEAD_X_ROTATION)
    public double rider_head_x_rotation() {
        return context.entity().getFirstPassenger() instanceof LivingEntity living
                ? living.getViewXRot(context.partialTick())
                : 0;
    }

    @Binding(RIDER_HEAD_Y_ROTATION)
    public double rider_head_y_rotation() {
        return context.entity().getFirstPassenger() instanceof LivingEntity living
                ? living.getViewYRot(context.partialTick())
                : 0;
    }

    @Binding(VERTICAL_SPEED)
    public double vertical_speed() {
        return context.entity().getDeltaMovement().y();
    }

    @Binding(YAW_SPEED)
    public double yaw_speed() {
        return context.entity().getYRot(context.partialTick()) - context.entity().yRotO;
    }

    @Binding(BLOCKING)
    public boolean blocking() {
        return context.entity() instanceof LivingEntity living && living.isBlocking();
    }

    @Binding(DEATH_TICKS)
    public double death_ticks() {
        return context.entity() instanceof LivingEntity living
                ? living.deathTime == 0 ? 0 : living.deathTime + context.partialTick()
                : 0;
    }

    @Binding(GROUND_SPEED)
    public double ground_speed() {
        return context.entity().getDeltaMovement().horizontalDistance();
    }

    @Binding(HAS_HEAD_GEAR)
    public boolean has_head_gear() {
        return context.entity() instanceof EquipmentUser equipmentUser
                && !equipmentUser.getItemBySlot(EquipmentSlot.HEAD).isEmpty();
    }

    @Binding(HEAD_X_ROTATION)
    public double head_x_rotation() {
        return context.entity().getViewXRot(context.partialTick());
    }

    @Binding(HEAD_Y_ROTATION)
    public double head_y_rotation() {
        return context.entity().getViewYRot(context.partialTick());
    }

    @Binding(HEALTH)
    public double health() {
        return context.entity() instanceof LivingEntity living ? living.getHealth() : 0;
    }

    @Binding(HURT_TIME)
    public double hurt_time() {
        return context.entity() instanceof LivingEntity living
                ? living.hurtTime == 0 ? 0 : living.hurtTime - context.partialTick()
                : 0;
    }

    @Binding(INVULNERABLE_TICKS)
    public double invulnerable_ticks() {
        return context.entity() instanceof LivingEntity living
                ? living.invulnerableTime == 0 ? 0 : living.invulnerableTime - context.partialTick()
                : 0;
    }

    @Binding(IS_BABY)
    public boolean is_baby() {
        return context.entity() instanceof LivingEntity living && living.isBaby();
    }

    @Binding(IS_SLEEPING)
    public boolean is_sleeping() {
        return context.entity() instanceof LivingEntity living && living.isSleeping();
    }

    @Binding(IS_USING_ITEM)
    public boolean is_using_item() {
        return context.entity() instanceof LivingEntity living && living.isUsingItem();
    }

    @Binding(IS_WALL_CLIMBING)
    public boolean is_wall_climbing() {
        return context.entity() instanceof LivingEntity living && living.onClimbable();
    }

    @Binding(LIMB_SWING)
    public double limb_swing() {
        return context.entity() instanceof LivingEntity living ? living.walkAnimation.position(context.partialTick()) : 0;
    }

    @Binding(LIMB_SWING_AMOUNT)
    public double limb_swing_amount() {
        return context.entity() instanceof LivingEntity living ? living.walkAnimation.speed(context.partialTick()) : 0;
    }

    @Binding(MAIN_HAND_ITEM_MAX_DURATION)
    public double main_hand_item_max_duration() {
        return context.entity() instanceof LivingEntity living ? living.getMainHandItem().getUseDuration(living) : 0;
    }

    @Binding(MAIN_HAND_ITEM_USE_DURATION)
    public double main_hand_item_use_duration() {
        return context.entity() instanceof LivingEntity living && living.getUsedItemHand() == InteractionHand.MAIN_HAND
                ? (living.getTicksUsingItem() / 20D) + context.partialTick()
                : 0;
    }

    @Binding(MAX_HEALTH)
    public double max_health() {
        return context.entity() instanceof LivingEntity living ? living.getMaxHealth() : 0D;
    }

    @Binding(SCALE)
    public double scale() {
        return context.entity() instanceof LivingEntity living ? living.getScale() : 1D;
    }

    @Binding(SLEEP_ROTATION)
    public double sleep_rotation() {
        return Optional.ofNullable(context.entity() instanceof LivingEntity living ? living.getBedOrientation() : null)
                .map(Direction::toYRot).orElse(0f);
    }
}
