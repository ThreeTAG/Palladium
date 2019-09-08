package net.threetag.threecore.abilities.data;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.Effect;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class PotionThreeData extends ThreeData<Effect>
{

    public PotionThreeData(String key) {
        super(key);
    }

    @Override
    public Effect parseValue(JsonObject jsonObject, Effect defaultValue) {
        if (!JSONUtils.hasField(jsonObject, this.jsonKey))
            return defaultValue;
        String potionKey = JSONUtils.getString(jsonObject, this.jsonKey);
        Effect potion = ForgeRegistries.POTIONS.getValue(new ResourceLocation(potionKey));
        if (potion == null)
            throw new JsonSyntaxException("Potion " + potionKey + " does not exist!");
        return potion;
    }

    @Override
    public void writeToNBT(CompoundNBT nbt, Effect value) {
        nbt.putString(this.key, ForgeRegistries.POTIONS.getKey(value).toString());
    }

    @Override
    public Effect readFromNBT(CompoundNBT nbt, Effect defaultValue) {
        if (!nbt.contains(this.key))
            return defaultValue;
        Effect potion = ForgeRegistries.POTIONS.getValue(new ResourceLocation(nbt.getString(this.key)));
        if (potion == null)
            return defaultValue;
        return potion;
    }

    @Override
    public String getDisplay(Effect value) {
        return ForgeRegistries.POTIONS.getKey(value).toString();
    }

    @Override
    public boolean displayAsString(Effect value) {
        return true;
    }
}
