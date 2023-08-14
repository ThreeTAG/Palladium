package net.threetag.palladium.client.model.animation;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.threetag.palladium.entity.FlightHandler;
import net.threetag.palladium.entity.PalladiumPlayerExtension;
import net.threetag.palladium.util.Easing;

@Environment(EnvType.CLIENT)
public class HoveringAnimation extends PalladiumAnimation {

    public HoveringAnimation(int priority) {
        super(priority);
    }

    @Override
    public void animate(Builder builder, AbstractClientPlayer player, HumanoidModel<?> model, FirstPersonContext firstPersonContext, float partialTicks) {
        boolean active = !firstPersonContext.firstPerson();

        if (active && player instanceof PalladiumPlayerExtension extension) {
            var flight = extension.palladium$getFlightHandler();
            float anim = flight.getHoveringAnimation(partialTicks);

            if (anim <= 0F) {
                return;
            }

            var type = FlightHandler.getAnimationType(player);

            if (type == FlightHandler.FlightAnimationType.HEROIC) {
                this.animateHeroic(builder, player, anim, partialTicks);
            } else {
                this.animateNormal(builder, player, anim, partialTicks);
            }

            this.addAttackAnimation(builder, player, model);
        }
    }

    public void animateNormal(Builder builder, AbstractClientPlayer player, float animationProgress, float partialTicks) {
        float hover = (float) Math.sin((player.tickCount + partialTicks) / 10F);

        builder.get(PlayerModelPart.RIGHT_ARM)
                .resetXRot()
                .resetYRot()
                .resetZRot()
                .setYRotDegrees(7.5F)
                .setZRotDegrees(7.5F - hover * 2.5F)
                .animate(Easing.OUTCIRC, animationProgress);

        builder.get(PlayerModelPart.LEFT_ARM)
                .resetXRot()
                .resetYRot()
                .resetZRot()
                .setYRotDegrees(-7.5F)
                .setZRotDegrees(-7.5F + hover * 2.5F)
                .animate(Easing.OUTCIRC, animationProgress);

        builder.get(PlayerModelPart.RIGHT_LEG)
                .setXRotDegrees(2.5F - hover * 5F)
                .setYRotDegrees(7.5F)
                .setZRotDegrees(2.5F)
                .animate(Easing.OUTCIRC, animationProgress);

        builder.get(PlayerModelPart.LEFT_LEG)
                .setXRotDegrees(-2.5F + hover * 5F)
                .setYRotDegrees(-7.5F)
                .setZRotDegrees(-2.5F)
                .animate(Easing.OUTCIRC, animationProgress);
    }

    public void animateHeroic(Builder builder, AbstractClientPlayer player, float animationProgress, float partialTicks) {
        float hover = (float) Math.sin((player.tickCount + partialTicks) / 10F);

        builder.get(PlayerModelPart.RIGHT_ARM)
                .resetXRot()
                .resetYRot()
                .resetZRot()
                .setXRotDegrees(-2.5F)
                .setYRotDegrees(10F)
                .setZRotDegrees(7.5F - hover * 2.5F)
                .animate(Easing.OUTCIRC, animationProgress);

        builder.get(PlayerModelPart.LEFT_ARM)
                .resetXRot()
                .resetYRot()
                .resetZRot()
                .setXRotDegrees(5F)
                .setYRotDegrees(-10F)
                .setZRotDegrees(-7.5F + hover * 2.5F)
                .animate(Easing.OUTCIRC, animationProgress);

        builder.get(PlayerModelPart.RIGHT_LEG)
                .setX(-2.9F)
                .setY(11F)
                .setZ(-2F)
                .setXRotDegrees(20F - hover * 5F)
                .setYRotDegrees(15F)
                .setZRotDegrees(-5F)
                .animate(Easing.OUTCIRC, animationProgress);

        builder.get(PlayerModelPart.LEFT_LEG)
                .setXRotDegrees(2.5F + hover * 5F)
                .setYRotDegrees(0F)
                .setZRotDegrees(-2F)
                .animate(Easing.OUTCIRC, animationProgress);
    }

    protected void addAttackAnimation(Builder builder, AbstractClientPlayer player, HumanoidModel<?> model) {
        if (!(model.attackTime <= 0.0F)) {
            HumanoidArm humanoidArm = this.getAttackArm(player);
            PartAnimationData modelPart = humanoidArm == HumanoidArm.RIGHT ? builder.get(PlayerModelPart.RIGHT_ARM) : builder.get(PlayerModelPart.LEFT_ARM);
            var bodyYRot = model.body.yRot;

            builder.get(PlayerModelPart.RIGHT_ARM)
                    .setX(-Mth.cos(bodyYRot) * 5.0F)
                    .setZ(Mth.sin(bodyYRot) * 5.0F)
                    .setYRot(bodyYRot);

            builder.get(PlayerModelPart.LEFT_ARM)
                    .setX(Mth.cos(bodyYRot) * 5.0F)
                    .setZ(-Mth.sin(bodyYRot) * 5.0F)
                    .setXRot(bodyYRot)
                    .setYRot(bodyYRot);

            float f = 1.0F - model.attackTime;
            f *= f;
            f *= f;
            f = 1.0F - f;
            float g = Mth.sin(f * (float) Math.PI);
            float h = Mth.sin(model.attackTime * (float) Math.PI) * -(model.head.xRot - 0.7F) * 0.75F;

            modelPart.rotateX(-(g * 1.2F + h)).rotateY(bodyYRot * 2F).rotateZ(Mth.sin(model.attackTime * (float) Math.PI) * -0.4F);
        }
    }

    private HumanoidArm getAttackArm(AbstractClientPlayer player) {
        HumanoidArm humanoidArm = player.getMainArm();
        return player.swingingArm == InteractionHand.MAIN_HAND ? humanoidArm : humanoidArm.getOpposite();
    }
}
