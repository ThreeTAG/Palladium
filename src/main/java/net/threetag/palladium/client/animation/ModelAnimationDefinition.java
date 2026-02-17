package net.threetag.palladium.client.animation;

import com.mojang.serialization.Codec;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Entity;
import net.threetag.palladium.client.util.ModelUtil;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.logic.molang.EntityContext;
import net.threetag.palladium.logic.molang.MoLangQueryRegistry;
import net.threetag.palladium.util.molang.ModifyFloatFunction;
import org.jetbrains.annotations.NotNull;
import team.unnamed.mocha.MochaEngine;

import java.util.HashMap;
import java.util.Map;

public final class ModelAnimationDefinition implements EntityContext {

    public static final Codec<ModelAnimationDefinition> CODEC = Codec.unboundedMap(Codec.STRING, PartAnimation.CODEC)
            .xmap(ModelAnimationDefinition::new, modelAnimationDefinition -> modelAnimationDefinition.animations);
    public static final ModelAnimationDefinition EMPTY = new ModelAnimationDefinition(Map.of());
    private final Map<String, PartAnimation> animations;
    private Entity cachedEntity;
    private float cachedPartialTicks;
    private Model<?> cachedModel;

    public ModelAnimationDefinition(Map<String, PartAnimation> animations) {
        this.animations = animations;

        if (!this.animations.isEmpty()) {
            MochaEngine<ModelAnimationDefinition> mocha = MoLangQueryRegistry.create(this);
            this.animations.values().forEach(partAnimation -> partAnimation.build(mocha));
        }
    }

    public void animate(Model<?> model, DataContext context, float partialTick) {
        if (!this.animations.isEmpty()) {
            this.cachedEntity = context.getEntity();
            this.cachedPartialTicks = partialTick;
            this.cachedModel = model;

            this.animations.forEach((bone, animation) -> {
                var part = ModelUtil.getPartFromModel(model, bone);

                if (part != null) {
                    animation.animate(part);
                }
            });
        }
    }

    @Override
    public Entity entity() {
        return this.cachedEntity;
    }

    @Override
    public float partialTick() {
        return this.cachedPartialTicks;
    }

    @Override
    public float getModelValue(String boneName, String type) {
        if (this.cachedModel != null) {
            var bone = ModelUtil.getPartFromModel(this.cachedModel, boneName);

            if (bone != null) {
                if (type.equalsIgnoreCase("x")) {
                    return bone.x;
                } else if (type.equalsIgnoreCase("y")) {
                    return bone.y;
                } else if (type.equalsIgnoreCase("z")) {
                    return bone.z;
                } else if (type.equalsIgnoreCase("x_rot")) {
                    return bone.xRot;
                } else if (type.equalsIgnoreCase("y_rot")) {
                    return bone.yRot;
                } else if (type.equalsIgnoreCase("z_rot")) {
                    return bone.zRot;
                } else if (type.equalsIgnoreCase("x_rot_degrees")) {
                    return (float) Math.toDegrees(bone.xRot);
                } else if (type.equalsIgnoreCase("y_rot_degrees")) {
                    return (float) Math.toDegrees(bone.yRot);
                } else if (type.equalsIgnoreCase("z_rot_degrees")) {
                    return (float) Math.toDegrees(bone.zRot);
                } else if (type.equalsIgnoreCase("x_scale")) {
                    return bone.xScale;
                } else if (type.equalsIgnoreCase("y_scale")) {
                    return bone.yScale;
                } else if (type.equalsIgnoreCase("z_scale")) {
                    return bone.zScale;
                }
            }
        }

        return 0F;
    }

    public enum PartAnimationType implements StringRepresentable {

        X("x"),
        Y("y"),
        Z("z"),
        X_ROT("x_rot"),
        Y_ROT("y_rot"),
        Z_ROT("z_rot"),
        X_SCALE("x_scale"),
        Y_SCALE("y_scale"),
        Z_SCALE("z_scale");

        public static final Codec<PartAnimationType> CODEC = StringRepresentable.fromEnum(PartAnimationType::values);
        private final String name;

        PartAnimationType(String name) {
            this.name = name;
        }

        @Override
        public @NotNull String getSerializedName() {
            return this.name;
        }
    }

    public static class PartAnimation {

        public static final Codec<PartAnimation> CODEC = Codec.unboundedMap(PartAnimationType.CODEC, Codec.STRING)
                .xmap(PartAnimation::new, partAnimation -> partAnimation.animationsRaw);

        private final Map<PartAnimationType, String> animationsRaw;
        private Map<PartAnimationType, ModifyFloatFunction> animations;

        public PartAnimation(Map<PartAnimationType, String> animationsRaw) {
            this.animationsRaw = animationsRaw;
        }

        public void build(MochaEngine<?> mocha) {
            this.animations = new HashMap<>();
            this.animationsRaw.forEach((type, value) -> {
                this.animations.put(type, mocha.compile(value, ModifyFloatFunction.class));
            });
        }

        public void animate(ModelPart part) {
            if (this.animations != null && !this.animations.isEmpty()) {
                for (Map.Entry<PartAnimationType, ModifyFloatFunction> e : this.animations.entrySet()) {
                    float original = switch (e.getKey()) {
                        case X -> part.x;
                        case Y -> part.y;
                        case Z -> part.z;
                        case X_ROT -> (float) Math.toDegrees(part.xRot);
                        case Y_ROT -> (float) Math.toDegrees(part.yRot);
                        case Z_ROT -> (float) Math.toDegrees(part.zRot);
                        case X_SCALE -> part.xScale;
                        case Y_SCALE -> part.yScale;
                        case Z_SCALE -> part.zScale;
                    };

                    float val = e.getValue().modify(original);

                    switch (e.getKey()) {
                        case X -> part.x = val;
                        case Y -> part.y = val;
                        case Z -> part.z = val;
                        case X_ROT -> part.xRot = (float) Math.toRadians(val);
                        case Y_ROT -> part.yRot = (float) Math.toRadians(val);
                        case Z_ROT -> part.zRot = (float) Math.toRadians(val);
                        case X_SCALE -> part.xScale = val;
                        case Y_SCALE -> part.yScale = val;
                        case Z_SCALE -> part.zScale = val;
                    }
                }
            }
        }
    }
}
