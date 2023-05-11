package net.threetag.palladium.client.model;

import net.minecraft.world.entity.Entity;

public interface ExtraAnimatedModel<T extends Entity> {

    void extraAnimations(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float partialTicks);

}
