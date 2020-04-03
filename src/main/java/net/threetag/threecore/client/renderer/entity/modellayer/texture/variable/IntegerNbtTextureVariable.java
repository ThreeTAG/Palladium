package net.threetag.threecore.client.renderer.entity.modellayer.texture.variable;

import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import net.minecraft.nbt.CompoundNBT;
import net.threetag.threecore.client.renderer.entity.modellayer.IModelLayerContext;

import java.util.List;

public class IntegerNbtTextureVariable extends AbstractIntegerTextureVariable {

    private final String nbtTag;

    public IntegerNbtTextureVariable(String nbtTag, List<Pair<Operation, Integer>> operations) {
        super(operations);
        this.nbtTag = nbtTag;
    }

    public IntegerNbtTextureVariable(String nbtTag, JsonObject json) {
        super(json);
        this.nbtTag = nbtTag;
    }


    @Override
    public int getNumber(IModelLayerContext context) {
        CompoundNBT nbt = context.getAsItem() == null || context.getAsItem().isEmpty() ? context.getAsEntity().getPersistentData() : context.getAsItem().getOrCreateTag();
        return nbt.getInt(this.nbtTag);
    }
}
