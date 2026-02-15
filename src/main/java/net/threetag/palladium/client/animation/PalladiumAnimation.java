package net.threetag.palladium.client.animation;

import com.mojang.serialization.Codec;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Entity;
import net.threetag.palladium.client.util.ModelUtil;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.logic.context.DataContextKeys;
import net.threetag.palladium.logic.molang.EntityContext;
import net.threetag.palladium.logic.molang.MoLangQueryRegistry;
import net.threetag.palladium.util.molang.ModifyFloatFunction;
import org.jetbrains.annotations.NotNull;
import team.unnamed.mocha.MochaEngine;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class PalladiumAnimation implements EntityContext {

    public static final Codec<PalladiumAnimation> CODEC = Codec.unboundedMap(Codec.STRING, PartAnimation.CODEC)
            .xmap(PalladiumAnimation::new, palladiumAnimation -> palladiumAnimation.animations);
    public static final PalladiumAnimation EMPTY = new PalladiumAnimation(Map.of());
    private final Map<String, PartAnimation> animations;
    private Entity cachedEntity;

    public PalladiumAnimation(Map<String, PartAnimation> animations) {
        this.animations = animations;

        if (!this.animations.isEmpty()) {
            MochaEngine<?> mocha = MoLangQueryRegistry.create(this);
            this.animations.values().forEach(partAnimation -> partAnimation.build(mocha));
        }
    }

    public void animate(Model<?> model, DataContext context, EntityRenderState renderState, float partialTick) {
        if (!this.animations.isEmpty()) {
            this.cachedEntity = context.getEntity();

            if (renderState instanceof AvatarRenderState state) {
                context.with(DataContextKeys.CAPE_X_ROT, -(6.0F + state.capeLean / 2.0F + state.capeFlap));
                context.with(DataContextKeys.CAPE_Y_ROT, 180.0F - state.capeLean2 / 2.0F);
                context.with(DataContextKeys.CAPE_Z_ROT, state.capeLean2 / -2.0F);
            }

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

    public Map<String, PartAnimation> animations() {
        return animations;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (PalladiumAnimation) obj;
        return Objects.equals(this.animations, that.animations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(animations);
    }

    @Override
    public String toString() {
        return "PalladiumAnimation[" +
                "animations=" + animations + ']';
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
                this.animations.forEach((type, value) -> {
                    float original = switch (type) {
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

                    float val = value.modify(original);

                    switch (type) {
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
                });
            }
        }
    }

}
