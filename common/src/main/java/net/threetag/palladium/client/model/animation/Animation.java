package net.threetag.palladium.client.model.animation;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.LivingEntity;

@Environment(EnvType.CLIENT)
public abstract class Animation {

    public abstract int getPriority();

    public abstract boolean active(LivingEntity entity);

    public void setupRotations(PlayerRenderer playerRenderer, AbstractClientPlayer player, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTicks) {

    }

    public abstract void setupAnimation(HumanoidModel<?> model, LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch);

}
