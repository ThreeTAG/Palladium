package net.threetag.palladium.compat.kubejs;

import dev.latvian.mods.kubejs.KubeJS;
import dev.latvian.mods.kubejs.event.EventJS;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.client.model.animation.Animation;

import java.util.function.BiConsumer;

public class RegisterAnimationsEventJS extends EventJS {

    public final BiConsumer<ResourceLocation, Animation> registry;

    public RegisterAnimationsEventJS(BiConsumer<ResourceLocation, Animation> registry) {
        this.registry = registry;
    }

    public void register(String id, int priority, Active active, Animate animate) {
        this.registry.accept(KubeJS.id(id), new AnimationImpl(priority, active, animate));
    }

    public interface Active {

        boolean active(LivingEntity entity);

    }

    public interface Animate {

        void setupAnimation(HumanoidModel<?> model, LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float partialTicks);

    }

    public static class AnimationImpl extends Animation {

        private final int priority;
        private final Active active;
        private final Animate animate;

        public AnimationImpl(int priority, Active active, Animate animate) {
            this.priority = priority;
            this.active = active;
            this.animate = animate;
        }

        @Override
        public int getPriority() {
            return this.priority;
        }

        @Override
        public boolean active(LivingEntity entity) {
            return this.active.active(entity);
        }

        @Override
        public void setupAnimation(HumanoidModel<?> model, LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float partialTicks) {
            this.animate.setupAnimation(model, entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, partialTicks);
        }
    }

}
