package net.threetag.palladium.compat.kubejs;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.latvian.mods.kubejs.KubeJS;
import dev.latvian.mods.kubejs.event.EventJS;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.client.model.animation.Animation;
import net.threetag.palladium.util.PoseStackUtil;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;

public class RegisterAnimationsEventJS extends EventJS {

    public final BiConsumer<ResourceLocation, Animation> registry;

    public RegisterAnimationsEventJS(BiConsumer<ResourceLocation, Animation> registry) {
        this.registry = registry;
    }

    public AnimationImpl register(String id, int priority, Active active, Animate animate) {
        var animation = new AnimationImpl(priority, active, null, animate, null);
        this.registry.accept(KubeJS.id(id), animation);
        return animation;
    }

    public AnimationImpl register(String id, int priority, Active active) {
        var animation = new AnimationImpl(priority, active, null, null, null);
        this.registry.accept(KubeJS.id(id), animation);
        return animation;
    }

    public interface Active {

        boolean active(LivingEntity entity);

    }

    public interface Animate {

        void setupAnimation(HumanoidModel<?> model, LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float partialTicks);

    }

    public interface FirstPersonAnimate {

        void setupFirstPersonAnimation(PoseStackUtil poseStack, AbstractClientPlayer player, boolean rightArm, float partialTicks);

    }

    public interface SetupRotations {

        void setupRotations(PlayerRenderer playerRenderer, AbstractClientPlayer player, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTicks);

    }

    public static class AnimationImpl extends Animation {

        private final int priority;
        private final Active active;
        @Nullable
        private SetupRotations setupRotations;
        @Nullable
        private Animate animate;
        @Nullable
        private FirstPersonAnimate firstPersonAnimate;

        public AnimationImpl(int priority, Active active, @Nullable SetupRotations setupRotations, @Nullable Animate animate, @Nullable FirstPersonAnimate firstPersonAnimate) {
            this.priority = priority;
            this.active = active;
            this.setupRotations = setupRotations;
            this.animate = animate;
            this.firstPersonAnimate = firstPersonAnimate;
        }

        @Override
        public int getPriority() {
            return this.priority;
        }

        @Override
        public boolean active(LivingEntity entity) {
            return this.active.active(entity);
        }

        public AnimationImpl setupRotations(SetupRotations setupRotations) {
            this.setupRotations = setupRotations;
            return this;
        }

        public AnimationImpl animate(Animate animate) {
            this.animate = animate;
            return this;
        }

        public AnimationImpl animateFirstPerson(FirstPersonAnimate firstPersonAnimate) {
            this.firstPersonAnimate = firstPersonAnimate;
            return this;
        }

        @Override
        public void setupRotations(PlayerRenderer playerRenderer, AbstractClientPlayer player, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTicks) {
            if (this.setupRotations != null) {
                this.setupRotations.setupRotations(playerRenderer, player, poseStack, ageInTicks, rotationYaw, partialTicks);
            }
        }

        @Override
        public void setupAnimation(HumanoidModel<?> model, LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float partialTicks) {
            if (this.animate != null) {
                this.animate.setupAnimation(model, entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, partialTicks);
            }
        }

        @Override
        public void setupFirstPersonAnimation(PoseStack poseStack, AbstractClientPlayer player, boolean rightArm, float partialTicks) {
            if (this.firstPersonAnimate != null) {
                this.firstPersonAnimate.setupFirstPersonAnimation(new PoseStackUtil(poseStack), player, rightArm, partialTicks);
            }
        }
    }

}
