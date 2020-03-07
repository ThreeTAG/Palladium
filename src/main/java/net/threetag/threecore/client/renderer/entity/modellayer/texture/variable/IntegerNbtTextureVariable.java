package net.threetag.threecore.client.renderer.entity.modellayer.texture.variable;

import net.minecraft.nbt.CompoundNBT;
import net.threetag.threecore.client.renderer.entity.modellayer.IModelLayerContext;

public class IntegerNbtTextureVariable implements ITextureVariable {

    private final String nbtTag;
    private final int addValue;

    public IntegerNbtTextureVariable(String nbtTag, int addValue) {
        this.nbtTag = nbtTag;
        this.addValue = addValue;
    }

    @Override
    public Object get(IModelLayerContext context) {
        CompoundNBT nbt = context.getAsItem() == null || context.getAsItem().isEmpty() ? context.getAsEntity().getPersistentData() : context.getAsItem().getOrCreateTag();
        return addValue + nbt.getInt(this.nbtTag);
    }
}
