package net.threetag.threecore.scripts.events;

import net.minecraft.client.renderer.model.ModelRenderer;
import net.threetag.threecore.event.SetRotationAnglesEvent;
import net.threetag.threecore.scripts.ScriptParameterName;

/**
 * Created by Nictogen on 2020-07-07.
 */
public class SetRotationAnglesScriptEvent extends LivingScriptEvent
{
	public SetRotationAnglesEvent event;

	public SetRotationAnglesScriptEvent(SetRotationAnglesEvent event)
	{
		super(event.getEntityLiving());
		this.event = event;
	}

	@Override public boolean isCancelable()
	{
		return false;
	}

	public float getLimbSwing()
	{
		return event.limbSwing;
	}

	public float getLimbSwingAmount()
	{
		return event.limbSwingAmount;
	}

	public float getAgeInTicks()
	{
		return event.ageInTicks;
	}

	public float getNetHeadYaw()
	{
		return event.netHeadYaw;
	}

	public float getHeadPitch()
	{
		return event.headPitch;
	}

	public void setRotationAngle(@ScriptParameterName("part") String part, @ScriptParameterName("xyz") String xyz, @ScriptParameterName("angle") float angle)
	{
		ModelRenderer modelRenderer = null;
		switch (part)
		{
		case "head":
			modelRenderer = event.model.bipedHead;
			break;
		case "headwear":
			modelRenderer = event.model.bipedHeadwear;
			break;
		case "body":
			modelRenderer = event.model.bipedBody;
			break;
		case "rightArm":
			modelRenderer = event.model.bipedRightArm;
			break;
		case "leftArm":
			modelRenderer = event.model.bipedLeftArm;
			break;
		case "rightLeg":
			modelRenderer = event.model.bipedRightLeg;
			break;
		case "leftLeg":
			modelRenderer = event.model.bipedLeftLeg;
			break;
		}

		if (modelRenderer != null)
		{
			switch (xyz)
			{
			case "x":
				modelRenderer.rotateAngleX = angle;
				break;
			case "y":
				modelRenderer.rotateAngleY = angle;
				break;
			case "z":
				modelRenderer.rotateAngleZ = angle;
				break;
			}
		}
	}
}
