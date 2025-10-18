package net.threetag.palladium.entity.flight;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Mth;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.client.beam.PalladiumBeams;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.documentation.SettingType;
import net.threetag.palladium.entity.SwingAnchor;
import net.threetag.palladium.network.PalladiumNetwork;
import net.threetag.palladium.network.SyncSwingAnchorPacket;
import net.threetag.palladium.util.PalladiumCodecs;
import net.threetag.palladium.util.PlayerUtil;

import java.util.Objects;

public class SwingingFlightType extends FlightType {

    private static final Codec<Vec2> RADIUS_CODEC = Codec.either(Codec.FLOAT, Vec2.CODEC).xmap(
            either -> either.map(
                    f -> new Vec2(f, f),
                    vec2 -> vec2
            ),
            vec2 -> vec2.x == vec2.y ? Either.left(vec2.x) : Either.right(vec2)
    );

    public static final MapCodec<SwingingFlightType> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("max_anchor_height").forGetter(f -> f.maxHeight),
            MaxHeightType.CODEC.optionalFieldOf("max_anchor_height_type", MaxHeightType.RELATIVE).forGetter(f -> f.maxHeightType),
            RADIUS_CODEC.fieldOf("radius").forGetter(f -> f.radiusRange),
            PalladiumCodecs.TIME.optionalFieldOf("swing_interval", 40).forGetter(f -> f.swingInterval),
            AnimationSettings.CODEC.optionalFieldOf("animation", AnimationSettings.DEFAULT).forGetter(f -> f.animationSettings)
    ).apply(instance, SwingingFlightType::new));

    private final int maxHeight;
    private final MaxHeightType maxHeightType;
    private final Vec2 radiusRange;
    private final int swingInterval;
    public final AnimationSettings animationSettings;

    public SwingingFlightType(int maxHeight, MaxHeightType maxHeightType, Vec2 radiusRange, int swingInterval, AnimationSettings animationSettings) {
        this.maxHeight = maxHeight;
        this.maxHeightType = maxHeightType;
        this.radiusRange = new Vec2(Math.min(radiusRange.x, radiusRange.y), Math.max(radiusRange.x, radiusRange.y));
        this.swingInterval = swingInterval;
        this.animationSettings = animationSettings;
    }

    public float getAvgRadius() {
        return (this.radiusRange.x + this.radiusRange.y) / 2F;
    }

    @Override
    public FlightTypeSerializer<?> getSerializer() {
        return FlightTypeSerializers.SWINGING.get();
    }

    @Override
    public FlightController<?> createController() {
        return new Controller();
    }

    @Override
    public FlightAnimationHandler<?> createAnimationHandler() {
        return new AnimationHandler(this.animationSettings);
    }

    public enum MaxHeightType implements StringRepresentable {

        ABSOLUTE("absolute"),
        RELATIVE("relative");

        public static final Codec<MaxHeightType> CODEC = StringRepresentable.fromEnum(MaxHeightType::values);

        private final String name;

        MaxHeightType(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }
    }

    public static class Controller extends FlightController<SwingingFlightType> {

        private BlockPos anchor;
        private double ropeLength = -1;
        private double swingTicks = 0;
        public boolean rightArm = true;

        @Override
        public void tick(LivingEntity entity, SwingingFlightType flightType) {
            super.tick(entity, flightType);

            if (entity.isUsingItem()) {
                this.stopFlight(entity);
                return;
            }

            if (this.anchor != null && entity instanceof Player player) {
                var input = PlayerUtil.getMovementInput(player);

                if (input != null) {
                    if (input.jump()) {
                        this.ropeLength = Mth.clamp(this.ropeLength - 0.1F, flightType.radiusRange.x, flightType.radiusRange.y);
                    } else if (input.shift()) {
                        this.ropeLength = Mth.clamp(this.ropeLength + 0.1F, flightType.radiusRange.x, flightType.radiusRange.y);
                    }

                    Vec3 pos = entity.position();
                    Vec3 diff = pos.subtract(this.anchor.getCenter());
                    double distance = diff.length();

                    if (distance > this.ropeLength) {
                        Vec3 corrected = this.anchor.getCenter().add(diff.scale(this.ropeLength / distance));
                        var currentPos = entity.position();
                        entity.setPos(Mth.lerp(0.05F, currentPos.x, corrected.x), Mth.lerp(0.05F, currentPos.y, corrected.y), Mth.lerp(0.05F, currentPos.z, corrected.z));

                        Vec3 vel = entity.getDeltaMovement();
                        Vec3 radial = diff.normalize().scale(vel.dot(diff.normalize()));
                        Vec3 tangential = vel.subtract(radial);
                        entity.setDeltaMovement(tangential);
                    }

                    if (input.forward()) {
                        Vec3 forward = entity.getLookAngle().normalize();
                        var movement = entity.getDeltaMovement().add(forward.scale(0.05));
                        entity.setDeltaMovement(movement);
                        this.swingTicks++;
                    } else {
                        this.swingTicks = 0;
                    }

                    if (!entity.level().isClientSide() && this.swingTicks >= flightType.swingInterval) {
                        this.start(entity, flightType);
                    }
                }
            }
        }

        @Override
        public void start(LivingEntity entity, SwingingFlightType flightType) {
            this.ropeLength = flightType.getAvgRadius();
            this.swingTicks = 0;

            if (!entity.level().isClientSide) {
                this.setAnchor(entity, this.findNewAnchor(entity, this.ropeLength, flightType), flightType);
            }
        }

        public void setAnchor(LivingEntity entity, BlockPos pos, SwingingFlightType flightType) {
            this.anchor = pos;
            this.ropeLength = this.ropeLength > 0 ? this.ropeLength : this.anchor.getCenter().distanceTo(entity.position());
            this.rightArm = !this.rightArm;

            if (!entity.level().isClientSide) {
                PalladiumNetwork.sendToTrackingAndSelf(entity, new SyncSwingAnchorPacket(entity.getId(), this.anchor));
                var anchorEntity = new SwingAnchor(entity, pos, flightType.animationSettings.beamRendererId.get(this.rightArm));
                entity.level().addFreshEntity(anchorEntity);
            }
        }

        public BlockPos findNewAnchor(LivingEntity entity, double radius, SwingingFlightType flightType) {
            if (this.anchor == null) {
                var movement = entity.getDeltaMovement().normalize().scale(radius);
                var y = (int) Math.ceil(entity.getY() + (entity.onGround() ? entity.getBbHeight() : 0) + radius);
                var pos = entity.position().add(movement);
                return clampMaxHeight(entity.level(), new BlockPos((int) pos.x, y, (int) pos.z), flightType);
            } else {
                var lookAngle = entity.getLookAngle().normalize();
                var y = (int) (this.anchor.getY() + lookAngle.scale(2).y);
                lookAngle = lookAngle.scale(radius);
                var pos = entity.position().add(lookAngle);
                return clampMaxHeight(entity.level(), new BlockPos((int) pos.x, y, (int) pos.z), flightType);
            }
        }

        private BlockPos clampMaxHeight(Level level, BlockPos pos, SwingingFlightType flightType) {
            if (flightType.maxHeightType == MaxHeightType.ABSOLUTE) {
                return new BlockPos(pos.getX(), Math.min(pos.getY(), flightType.maxHeight), pos.getZ());
            } else {
                var maxPos = new BlockPos(pos.getX(), level.getMaxY(), pos.getZ());

                while (level.isEmptyBlock(maxPos) && level.getFluidState(maxPos).isEmpty() && maxPos.getY() > level.getMinY()) {
                    maxPos = maxPos.below();
                }

                if (maxPos.getY() >= pos.getY()) {
                    return pos;
                } else {
                    return new BlockPos(pos.getX(), (int) Mth.lerp(0.25F, pos.getY(), maxPos.getZ()), pos.getZ());
                }
            }
        }

        public BlockPos getAnchor() {
            return this.anchor;
        }
    }

    public record AnimationSettings(ResourceLocation assetId, BeamRendererValue beamRendererId, float maxLean,
                                    float bodyStiffness, float limbStiffness) {

        public static final ResourceLocation DEFAULT_ASSET_ID = Palladium.id("flight/swinging");
        public static final BeamRendererValue DEFAULT_BEAM = new BeamRendererValue(PalladiumBeams.SWINGING_WEB_RIGHT, PalladiumBeams.SWINGING_WEB_LEFT);
        public static final float DEFAULT_MAX_LEAN = 35F;
        public static final float DEFAULT_BODY_STIFFNESS = 12F;
        public static final float DEFAULT_LIMB_STIFFNESS = 8F;

        public static final AnimationSettings DEFAULT = new AnimationSettings(DEFAULT_ASSET_ID, DEFAULT_BEAM, DEFAULT_MAX_LEAN, DEFAULT_BODY_STIFFNESS, DEFAULT_LIMB_STIFFNESS);

        public static final Codec<AnimationSettings> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ResourceLocation.CODEC.optionalFieldOf("asset_id", DEFAULT_ASSET_ID).forGetter(AnimationSettings::assetId),
                BeamRendererValue.CODEC.optionalFieldOf("beam_renderer", DEFAULT_BEAM).forGetter(AnimationSettings::beamRendererId),
                ExtraCodecs.NON_NEGATIVE_FLOAT.optionalFieldOf("max_lean", DEFAULT_MAX_LEAN).forGetter(AnimationSettings::maxLean),
                ExtraCodecs.NON_NEGATIVE_FLOAT.optionalFieldOf("body_stiffness", DEFAULT_BODY_STIFFNESS).forGetter(AnimationSettings::bodyStiffness),
                ExtraCodecs.NON_NEGATIVE_FLOAT.optionalFieldOf("limb_stiffness", DEFAULT_LIMB_STIFFNESS).forGetter(AnimationSettings::limbStiffness)
        ).apply(instance, AnimationSettings::new));

        public static final Codec<AnimationSettings> CODEC = Codec.either(DIRECT_CODEC, ResourceLocation.CODEC)
                .xmap(either -> either.map(
                                animationSettings -> animationSettings,
                                resourceLocation -> new AnimationSettings(resourceLocation, DEFAULT_BEAM, DEFAULT_MAX_LEAN, DEFAULT_BODY_STIFFNESS, DEFAULT_LIMB_STIFFNESS)),
                        animationSettings ->
                                animationSettings.equals(DEFAULT) ?
                                        Either.right(animationSettings.assetId) : Either.left(animationSettings));

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof AnimationSettings that)) return false;
            return Float.compare(maxLean, that.maxLean) == 0 && Float.compare(bodyStiffness, that.bodyStiffness) == 0 && Float.compare(limbStiffness, that.limbStiffness) == 0 && Objects.equals(assetId, that.assetId) && Objects.equals(beamRendererId, that.beamRendererId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(assetId, beamRendererId, maxLean, bodyStiffness, limbStiffness);
        }
    }

    public record BeamRendererValue(ResourceLocation rightArm, ResourceLocation leftArm) {

        public static final Codec<BeamRendererValue> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ResourceLocation.CODEC.fieldOf("right_arm").forGetter(BeamRendererValue::rightArm),
                ResourceLocation.CODEC.fieldOf("left_arm").forGetter(BeamRendererValue::leftArm)
        ).apply(instance, BeamRendererValue::new));

        public static final Codec<BeamRendererValue> CODEC = Codec.either(DIRECT_CODEC, ResourceLocation.CODEC)
                .xmap(either -> either.map(
                                beamRendererValue -> beamRendererValue,
                                resourceLocation -> new BeamRendererValue(resourceLocation, resourceLocation)),
                        beamRendererValue ->
                                beamRendererValue.rightArm.equals(beamRendererValue.leftArm) ?
                                        Either.right(beamRendererValue.rightArm) : Either.left(beamRendererValue));

        public ResourceLocation get(boolean rightArm) {
            return rightArm ? this.rightArm : this.leftArm;
        }

    }

    public static class AnimationHandler extends FlightAnimationHandler<SwingingFlightType> {

        public final AnimationSettings animationSettings;
        public float pitchVel, rollVel;
        public float limbPitchVel, limbRollVel;
        public float rightArmPitch, prevRightArmPitch, rightArmVel;
        public float leftArmPitch, prevLeftArmPitch, leftArmVel;

        public AnimationHandler(AnimationSettings animationSettings) {
            this.animationSettings = animationSettings;
        }

        @Override
        public ResourceLocation getAnimationAssetId() {
            return this.animationSettings.assetId();
        }

        @Override
        public void tick(LivingEntity entity, boolean flightStopped) {
            super.tick(entity, flightStopped);

            var anchor = entity.level().getEntitiesOfClass(
                    SwingAnchor.class,
                    AABB.ofSize(entity.position(), 50, 50, 50),
                    a -> a.getOwner() != null && a.getOwner().getUUID().equals(entity.getUUID())
            ).stream().findFirst().orElse(null);

            if (anchor != null) {
                Vec3 rope = anchor.blockPosition().getCenter().subtract(entity.position()).normalize();

                Vec3 vel = entity.getDeltaMovement();
                if (vel.lengthSqr() < 1.0E-4) vel = Vec3.ZERO;

                Vec3 worldUp = new Vec3(0, 1, 0);
                Vec3 right = worldUp.cross(rope).normalize();
                if (right.lengthSqr() < 1.0E-4) {
                    right = new Vec3(1, 0, 0);
                }
                Vec3 forward = right.cross(rope).normalize();

                double forwardComp = vel.dot(forward);
                double sideComp = vel.dot(right);

                var maxLean = this.animationSettings.maxLean;
                float targetPitch = (float) (-forwardComp * -maxLean);
                float targetRoll = (float) (-sideComp * -maxLean);

                float dt = 1f / 20F;
                float stiffness = this.animationSettings.bodyStiffness;
                float damping = 2f * (float) Math.sqrt(stiffness);

                float pitchError = targetPitch - this.pitch;
                float accPitch = stiffness * pitchError - damping * this.pitchVel;
                this.pitchVel += accPitch * dt;
                this.pitch += this.pitchVel * dt;

                float rollError = targetRoll - this.roll;
                float accRoll = stiffness * rollError - damping * this.rollVel;
                this.rollVel += accRoll * dt;
                this.roll += this.rollVel * dt;
            } else {
                this.pitch *= 0.8F;
                this.roll *= 0.8F;
                this.pitchVel = 0;
                this.rollVel = 0;
            }

            var flightController = EntityFlightHandler.get(entity);
            boolean rightArm = flightController != null && flightController.getController() instanceof SwingingFlightType.Controller controller && controller.rightArm;
            this.updateLimbs(this.pitch, this.roll, anchor != null && rightArm, anchor != null && !rightArm);
        }

        private void updateLimbs(float targetPitch, float targetRoll, boolean rightArm, boolean leftArm) {
            float deltaTime = 1F / 20F;
            float stiffness = this.animationSettings.limbStiffness * 2F;
            float damping = 2F * (float) Math.sqrt(stiffness);

            float acc = stiffness * (targetPitch - this.limbPitch) - damping * this.limbPitchVel;
            this.limbPitchVel += acc * deltaTime;
            this.limbPitch += this.limbPitchVel * deltaTime;

            acc = stiffness * (targetRoll - this.limbRoll) - damping * this.limbRollVel;
            this.limbRollVel += acc * deltaTime;
            this.limbRoll += this.limbRollVel * deltaTime;

            this.prevRightArmPitch = this.rightArmPitch;
            this.prevLeftArmPitch = this.leftArmPitch;
            var rightArmTarget = rightArm ? -160 : targetPitch;
            var leftArmTarget = leftArm ? -160 : targetPitch;

            acc = stiffness * (rightArmTarget - this.rightArmPitch) - damping * this.rightArmVel;
            this.rightArmVel += acc * deltaTime;
            this.rightArmPitch += this.rightArmVel * deltaTime;

            acc = stiffness * (leftArmTarget - this.leftArmPitch) - damping * this.leftArmVel;
            this.leftArmVel += acc * deltaTime;
            this.leftArmPitch += this.leftArmVel * deltaTime;
        }

        public float getRightArmPitch(float partialTick) {
            return Mth.lerp(partialTick, this.prevRightArmPitch, this.rightArmPitch);
        }

        public float getLeftArmPitch(float partialTick) {
            return Mth.lerp(partialTick, this.prevLeftArmPitch, this.leftArmPitch);
        }
    }

    public static class Serializer extends FlightTypeSerializer<SwingingFlightType> {

        @Override
        public MapCodec<SwingingFlightType> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<FlightType, SwingingFlightType> builder, HolderLookup.Provider provider) {
            builder.setName("Swinging")
                    .setDescription("Allows the entity to swing from anchor points, similar to a grappling hook.")
                    .add("max_anchor_height", TYPE_INT, "The maximum height for anchor points.")
                    .addOptional("max_anchor_height_type", TYPE_SWINGING_HEIGHT_TYPE, "Determines if the maximum height for anchors are relative to the ground, or a fixed y-level.", MaxHeightType.RELATIVE)
                    .add("radius", SettingType.combined(TYPE_FLOAT, TYPE_VECTOR2), "The range of distances from the anchor point that the entity can swing at. It can either be a single, fixed number, or an array of 2 numbers that the player can adjust between while swinging.")
                    .addOptional("swing_interval", TYPE_TIME, "The time (in ticks) the player must be moving forward before a new anchor point is created.", 40)
                    .addOptional("animation", TYPE_SWINGING_ANIMATION, "Settings for the swinging flight animation.")
                    .setExampleObject(new SwingingFlightType(
                            20,
                            MaxHeightType.RELATIVE,
                            new Vec2(5F, 10F),
                            60,
                            new AnimationSettings(
                                    ResourceLocation.fromNamespaceAndPath("namespace", "animation_id"),
                                    new BeamRendererValue(
                                            ResourceLocation.fromNamespaceAndPath("namespace", "beam_right_id"),
                                            ResourceLocation.fromNamespaceAndPath("namespace", "beam_left_id")
                                    ),
                                    50F,
                                    15F,
                                    10F
                            )
                    ));
        }
    }
}
