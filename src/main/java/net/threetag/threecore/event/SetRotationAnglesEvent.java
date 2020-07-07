package net.threetag.threecore.event;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingEvent;

public class SetRotationAnglesEvent extends LivingEvent
{
	public BipedModel model;
	public float limbSwing;
	public float limbSwingAmount;
	public float ageInTicks;
	public float netHeadYaw;
	public float headPitch;

	public SetRotationAnglesEvent(LivingEntity entity, BipedModel model, float f, float f1, float f2, float f3, float f4)
	{
		super(entity);
		this.model = model;
		this.limbSwing = f;
		this.limbSwingAmount = f1;
		this.ageInTicks = f2;
		this.netHeadYaw = f3;
		this.headPitch = f4;
	}
}
