package com.threetag.threecore.util.client;

import com.threetag.threecore.util.client.events.AnimationEvent;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.common.MinecraftForge;

public class ClientHooks {

    public static void renderBipedPre(BipedModel model, Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        if (entity == null)
            return;
        AnimationEvent.SetRotationAngles ev = new AnimationEvent.SetRotationAngles(entity, model, f, f1, f2, f3, f4, f5, AnimationEvent.ModelSetRotationAnglesEventType.PRE);
        MinecraftForge.EVENT_BUS.post(ev);
        if (!ev.isCanceled() && ev.getEntity() instanceof LivingEntity) {
            model.setRotationAngles((LivingEntity) ev.getEntity(), ev.limbSwing, ev.limbSwingAmount, ev.partialTicks, ev.ageInTicks, ev.netHeadYaw, ev.headPitch);
        }
    }

    public static void renderBipedPost(BipedModel model, Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        if (entity == null)
            return;
        AnimationEvent.SetRotationAngles ev = new AnimationEvent.SetRotationAngles(entity, model, f, f1, f2, f3, f4, f5, AnimationEvent.ModelSetRotationAnglesEventType.POST);
        MinecraftForge.EVENT_BUS.post(ev);
    }

}