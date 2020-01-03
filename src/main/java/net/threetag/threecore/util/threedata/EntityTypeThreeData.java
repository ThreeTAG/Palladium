package net.threetag.threecore.util.threedata;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class EntityTypeThreeData extends ThreeData<EntityType<?>> {

    public EntityTypeThreeData(String key) {
        super(key);
    }

    @Override
    public EntityType<?> parseValue(JsonObject jsonObject, EntityType<?> defaultValue) {
        if (!JSONUtils.hasField(jsonObject, this.jsonKey))
            return defaultValue;
        EntityType<?> entityType = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(JSONUtils.getString(jsonObject, this.jsonKey)));
        if (entityType == null)
            throw new JsonSyntaxException("Entity type " + JSONUtils.getString(jsonObject, this.jsonKey) + " does not exist!");
        return entityType;
    }

    @Override
    public void writeToNBT(CompoundNBT nbt, EntityType<?> value) {
        nbt.putString(this.key, Objects.requireNonNull(value.getRegistryName()).toString());
    }

    @Override
    public EntityType<?> readFromNBT(CompoundNBT nbt, EntityType<?> defaultValue) {
        if (!nbt.contains(this.key))
            return defaultValue;
        return ForgeRegistries.ENTITIES.getValue(new ResourceLocation(nbt.getString(this.key)));
    }

    @Override
    public String getDisplay(EntityType<?> value) {
        return ForgeRegistries.ENTITIES.getKey(value).toString();
    }

    @Override
    public boolean displayAsString(EntityType<?> value) {
        return true;
    }

}
