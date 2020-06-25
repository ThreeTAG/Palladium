package net.threetag.threecore.client.renderer.entity.modellayer.texture.variable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.JSONUtils;
import net.threetag.threecore.ability.Ability;
import net.threetag.threecore.ability.AbilityHelper;
import net.threetag.threecore.client.renderer.entity.modellayer.IModelLayerContext;
import net.threetag.threecore.util.threedata.ThreeData;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nictogen on 2020-06-24.
 */
public class AbilityDataTextureVariable implements ITextureVariable
{
	private HashMap<String, String> textureMap = new HashMap<>();
	private String defaultTexture;
	private String abilityID;
	private String dataID;

	public AbilityDataTextureVariable(JsonObject object){
		for (Map.Entry<String, JsonElement> values : JSONUtils.getJsonObject(object, "values").entrySet())
			textureMap.put(values.getKey(), values.getValue().getAsString());
		defaultTexture = JSONUtils.getString(object, "defaultTexture");
		abilityID = JSONUtils.getString(object, "abilityID");
		dataID = JSONUtils.getString(object, "dataID");
	}

	@Override public Object get(IModelLayerContext context)
	{
		if(context.getAsEntity() instanceof LivingEntity){
			for (Ability ability : AbilityHelper.getAbilities((LivingEntity) context.getAsEntity()))
				if(ability.getId().equals(abilityID))
					for (ThreeData<?> datum : ability.getDataManager().getData())
						if(datum.getKey().equals(dataID))
						{
							String dataValue = ability.getDataManager().get(datum).toString();
							if(textureMap.get(dataValue) != null)
								return textureMap.get(dataValue);
						}

		}
		return defaultTexture;
	}
}
