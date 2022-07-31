package net.threetag.palladium.item;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public interface CurioTrinketRenderer {

    <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack,
                                                                   PoseStack poseStack,
                                                                   EntityModel<? extends LivingEntity> entityModel,
                                                                   LivingEntity entity,
                                                                   MultiBufferSource bufferSource,
                                                                   int light, float limbSwing,
                                                                   float limbSwingAmount,
                                                                   float partialTicks,
                                                                   float ageInTicks, float netHeadYaw,
                                                                   float headPitch);

}
