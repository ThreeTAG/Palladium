package net.threetag.palladium.entity.flight;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.power.ability.AnimationTimer;
import net.threetag.palladium.power.ability.AnimationTimerSetting;
import net.threetag.palladium.util.Easing;
import net.threetag.palladium.util.PlayerUtil;
import org.jetbrains.annotations.Nullable;

public class DefaultFlightType extends FlightType {

    public static final MapCodec<DefaultFlightType> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ExtraCodecs.NON_NEGATIVE_FLOAT.fieldOf("speed").forGetter(f -> f.speed),
            ExtraCodecs.NON_NEGATIVE_FLOAT.optionalFieldOf("sprint_speed", -1F).forGetter(f -> f.sprintSpeed),
            AnimationSettings.CODEC.optionalFieldOf("animation", AnimationSettings.DEFAULT).forGetter(f -> f.animation)
    ).apply(instance, DefaultFlightType::new));

    private final float speed, sprintSpeed;
    private final AnimationSettings animation;

    public DefaultFlightType(float speed, float sprintSpeed, AnimationSettings animation) {
        this.speed = speed;
        this.sprintSpeed = Math.max(sprintSpeed, speed);
        this.animation = animation;
    }

    @Override
    public FlightTypeSerializer<?> getSerializer() {
        return FlightTypeSerializers.FLIGHT.get();
    }

    @Override
    public FlightController<?> createController() {
        return new Controller();
    }

    public boolean allowPropulsion() {
        return this.sprintSpeed > this.speed;
    }

    @Override
    public @Nullable FlightAnimationHandler<?> createAnimationHandler() {
        return new AnimationHandler(this.animation, this.allowPropulsion());
    }

    public static class Controller extends FlightController<DefaultFlightType> {

        public Vec3 flightVector = Vec3.ZERO;

        @Override
        public void tick(LivingEntity entity, DefaultFlightType flightType) {
            if (entity instanceof Player player) {
                var input = PlayerUtil.getMovementInput(player);

                if (input == null) {
                    return;
                }

                var lookAngle = player.getLookAngle().normalize();

                if (input.forward() || input.backward() || input.left() || input.right() || input.jump() || input.shift()) {
                    var moveVector = input.forward() && input.backward() ? Vec3.ZERO :
                            input.forward() ? lookAngle :
                                    input.backward() ? lookAngle.scale(-1) : Vec3.ZERO;
                    moveVector = moveVector.add(0, input.jump() && input.shift() ? 0 : input.jump() ? 1 : input.shift() ? -1 : 0, 0);

                    if (input.right() || input.left()) {
                        var sideVec = lookAngle.yRot((float) Math.toRadians(
                                input.left() && input.right() ? 0 : input.left() ? 90F : -90F
                        ));
                        moveVector = moveVector.add(sideVec.x, 0, sideVec.z);
                    }

                    var max = moveVector.normalize()
                            .scale((player.isSprinting() && !entity.isUsingItem() ? flightType.sprintSpeed : flightType.speed) * 0.5F);
                    var diff = max.subtract(this.flightVector);
                    this.flightVector = this.flightVector.add(diff.scale(0.2F));
                } else if (this.flightVector.length() > 0.001F) {
                    this.flightVector = this.flightVector.scale(0.8);
                } else {
                    this.flightVector = Vec3.ZERO;
                }

                player.setDeltaMovement(new Vec3(this.flightVector.x, this.flightVector.y + Math.sin(player.tickCount / 10F) / 100F, this.flightVector.z));
            }

            entity.fallDistance = 0F;
            super.tick(entity, flightType);
        }

        @Override
        public void start(LivingEntity entity, DefaultFlightType flightType) {
            this.flightVector = entity.getDeltaMovement();
        }

        @Override
        public void clampRotation(LivingEntity entity, DefaultFlightType flightType) {
            if (entity.isSprinting() && flightType.allowPropulsion()
                    && entity instanceof Player player && PlayerUtil.getMovementInput(player) != null) {
                float yaw = (float) (Mth.atan2(-this.flightVector.x, this.flightVector.z) * (180F / Math.PI));
                float f = Mth.wrapDegrees(entity.getYRot() - yaw);
                float g = Mth.clamp(f, -45.0F, 45.0F);
                entity.setYRot(entity.getYRot() + g - f);
                entity.setYHeadRot(entity.getYRot());
            }
        }
    }

    public record AnimationSettings(Identifier assetId, float maxLean, float bodyStiffness, float limbStiffness) {

        public static final AnimationSettings DEFAULT = new AnimationSettings(Palladium.id("flight/default"), 35F, 12F, 8F);

        public static final Codec<AnimationSettings> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Identifier.CODEC.fieldOf("asset_id").forGetter(AnimationSettings::assetId),
                ExtraCodecs.NON_NEGATIVE_FLOAT.optionalFieldOf("max_lean", 35F).forGetter(AnimationSettings::maxLean),
                ExtraCodecs.NON_NEGATIVE_FLOAT.optionalFieldOf("body_stiffness", 12F).forGetter(AnimationSettings::bodyStiffness),
                ExtraCodecs.NON_NEGATIVE_FLOAT.optionalFieldOf("limb_stiffness", 8F).forGetter(AnimationSettings::limbStiffness)
        ).apply(instance, AnimationSettings::new));

        public static final Codec<AnimationSettings> CODEC = Codec.either(DIRECT_CODEC, Identifier.CODEC)
                .xmap(either -> either.map(
                                animationSettings -> animationSettings,
                                Identifier -> new AnimationSettings(Identifier, 35F, 12, 8)),
                        animationSettings ->
                                animationSettings.maxLean == 35F && animationSettings.bodyStiffness == 12F && animationSettings.limbStiffness == 8F ?
                                        Either.right(animationSettings.assetId) : Either.left(animationSettings));

    }

    public static class AnimationHandler extends FlightAnimationHandler<DefaultFlightType> {

        public final AnimationSettings animationSettings;
        public final boolean allowPropulsion;
        public float pitchVel, rollVel;
        public float limbPitchVel, limbRollVel;
        public AnimationTimer propulsionScale = new AnimationTimerSetting(0, 20, Easing.INOUTCUBIC).create();

        public AnimationHandler(AnimationSettings animationSettings, boolean allowPropulsion) {
            this.animationSettings = animationSettings;
            this.allowPropulsion = allowPropulsion;
        }

        @Override
        public Identifier getAnimationAssetId() {
            return this.animationSettings.assetId();
        }

        @Override
        public void tick(LivingEntity entity, boolean flightStopped) {
            super.tick(entity, flightStopped);

            float targetPitch = 0F;
            float targetRoll = 0F;
            boolean propulsion = !flightStopped && entity.isSprinting() && this.allowPropulsion && !entity.isUsingItem();
            this.propulsionScale.tickAndUpdate(propulsion);

            if (!flightStopped) {
                Vec3 v = entity.getDeltaMovement();
                double horizSpeed = Math.sqrt(v.x * v.x + v.z * v.z);

                if (v.lengthSqr() > 1.0E-3 && horizSpeed > 0.05D) {
                    v = v.normalize();
                    var lookAngle = entity.getLookAngle();
                    var f = lookAngle.subtract(0, lookAngle.y, 0);

                    var vHoriz = new Vec3(v.x, 0, v.z).normalize();
                    var fHoriz = new Vec3(f.x, 0, f.z).normalize();

                    var right = fHoriz.cross(new Vec3(0, 1, 0)).normalize();
                    double side = vHoriz.dot(right);

                    double forward = vHoriz.dot(fHoriz);
                    var maxLean = this.animationSettings.maxLean;

                    if (propulsion) {
                        targetPitch = 90F + entity.getXRot();
                    } else {
                        targetPitch = (float) Mth.clamp(forward * 80F * horizSpeed, -maxLean, maxLean);
                    }

                    targetRoll = (float) Mth.clamp(-side * 80F * horizSpeed, -maxLean, maxLean);
                }
            }

            float deltaTime = 1F / 20F;
            float stiffness = this.animationSettings.bodyStiffness;
            float damping = 2F * (float) Math.sqrt(stiffness);

            float accPitch = stiffness * (targetPitch - this.pitch) - damping * this.pitchVel;
            this.pitchVel += accPitch * deltaTime;
            this.pitch += this.pitchVel * deltaTime;

            float accRoll = stiffness * (targetRoll - this.roll) - damping * this.rollVel;
            this.rollVel += accRoll * deltaTime;
            this.roll += this.rollVel * deltaTime;

            this.updateLimbs(propulsion ? 0F : this.pitch, this.roll);
        }

        private void updateLimbs(float targetPitch, float targetRoll) {
            float deltaTime = 1F / 20F;
            float stiffness = this.animationSettings.limbStiffness;
            float damping = 2F * (float) Math.sqrt(stiffness);

            float acc = stiffness * (targetPitch - this.limbPitch) - damping * this.limbPitchVel;
            this.limbPitchVel += acc * deltaTime;
            this.limbPitch += this.limbPitchVel * deltaTime;

            acc = stiffness * (targetRoll - this.limbRoll) - damping * this.limbRollVel;
            this.limbRollVel += acc * deltaTime;
            this.limbRoll += this.limbRollVel * deltaTime;
        }

        public float getPropulsionScale(float partialTick) {
            return this.propulsionScale.eased(partialTick);
        }
    }

    public static class Serializer extends FlightTypeSerializer<DefaultFlightType> {

        @Override
        public MapCodec<DefaultFlightType> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<FlightType, DefaultFlightType> builder, HolderLookup.Provider provider) {
            builder.setName("Default Flight")
                    .setDescription("Default flying type. If the sprint speed is equal or less than the normal speed, this flight will be levitation-only.")
                    .add("speed", TYPE_FLOAT, "The normal flight speed.")
                    .addOptional("sprint_speed", TYPE_FLOAT, "The sprinting flight speed. If not set, it will be equal to the normal speed.")
                    .addOptional("animation", TYPE_DEFAULT_FLIGHT_ANIMATION, "The animation settings for this flight type.")
                    .addExampleObject(new DefaultFlightType(1F, 2F, new AnimationSettings(
                            Identifier.fromNamespaceAndPath("namespace", "animation_id"), 35F, 20F, 12F
                    )));

        }
    }
}
