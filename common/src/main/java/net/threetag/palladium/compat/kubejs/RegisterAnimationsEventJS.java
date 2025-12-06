package net.threetag.palladium.compat.kubejs;

import dev.latvian.mods.kubejs.KubeJS;
import dev.latvian.mods.kubejs.event.EventJS;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.client.model.animation.PalladiumAnimation;
import net.threetag.palladium.power.PowerUtil;

import java.util.function.BiConsumer;

public class RegisterAnimationsEventJS extends EventJS {

    public final BiConsumer<ResourceLocation, PalladiumAnimation> registry;

    public RegisterAnimationsEventJS(BiConsumer<ResourceLocation, PalladiumAnimation> registry) {
        this.registry = registry;
    }

    public AnimationImpl register(String id, int priority, Animate animate) {
        var animation = new AnimationImpl(priority, null, animate);
        this.registry.accept(KubeJS.id(id), animation);
        return animation;
    }

    public AnimationImpl registerForPower(String id, ResourceLocation powerId, int priority, Animate animate) {
        var animation = new AnimationImpl(priority, powerId, animate);
        this.registry.accept(KubeJS.id(id), animation);
        return animation;
    }

    public interface Animate {

        void animate(AnimationBuilder builder);

    }

    public static class AnimationImpl extends PalladiumAnimation {

        private final Animate animate;
        private final ResourceLocation powerId;

        public AnimationImpl(int priority, ResourceLocation powerId, Animate animate) {
            super(priority);
            this.powerId = powerId;
            this.animate = animate;
        }

        @Override
        public void animate(Builder builder, AbstractClientPlayer player, HumanoidModel<?> model, FirstPersonContext firstPersonContext, float partialTicks) {
            if (this.powerId == null || PowerUtil.hasPower(player, this.powerId)) {
                this.animate.animate(new AnimationBuilder(builder, player, model, firstPersonContext, partialTicks));
            }
        }
    }

    public static class AnimationBuilder {

        private final PalladiumAnimation.Builder builder;
        private final AbstractClientPlayer player;
        private final HumanoidModel<?> model;
        private final PalladiumAnimation.FirstPersonContext firstPersonContext;
        private final float partialTicks;

        public AnimationBuilder(PalladiumAnimation.Builder builder, AbstractClientPlayer player, HumanoidModel<?> model, PalladiumAnimation.FirstPersonContext firstPersonContext, float partialTicks) {
            this.builder = builder;
            this.player = player;
            this.model = model;
            this.firstPersonContext = firstPersonContext;
            this.partialTicks = partialTicks;
        }

        public PalladiumAnimation.PartAnimationData get(PalladiumAnimation.PlayerModelPart part) {
            return this.builder.get(part);
        }

        public AbstractClientPlayer getPlayer() {
            return player;
        }

        public HumanoidModel<?> getModel() {
            return model;
        }

        public PalladiumAnimation.FirstPersonContext getFirstPersonContext() {
            return firstPersonContext;
        }

        public float getPartialTicks() {
            return partialTicks;
        }

        public boolean isFirstPerson() {
            return this.firstPersonContext.firstPerson();
        }

        public boolean isRightArmRendering() {
            return this.firstPersonContext.rightArm();
        }

        public boolean isLeftArmRendering() {
            return this.firstPersonContext.leftArm();
        }

        public boolean isMainArmRendering() {
            return this.firstPersonContext.mainArm();
        }

        public boolean isOffArmRendering() {
            return this.firstPersonContext.offArm();
        }
    }

}
