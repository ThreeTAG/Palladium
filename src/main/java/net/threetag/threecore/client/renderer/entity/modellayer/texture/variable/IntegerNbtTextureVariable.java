package net.threetag.threecore.client.renderer.entity.modellayer.texture.variable;

import net.minecraft.nbt.CompoundNBT;
import net.threetag.threecore.client.renderer.entity.modellayer.IModelLayerContext;

public class IntegerNbtTextureVariable implements ITextureVariable {

    private final String nbtTag;

    public IntegerNbtTextureVariable(String nbtTag) {
        this.nbtTag = nbtTag;
    }

    @Override
    public Object get(IModelLayerContext context) {
        CompoundNBT nbt = context.getAsItem() == null || context.getAsItem().isEmpty() ? context.getAsEntity().getPersistentData() : context.getAsItem().getOrCreateTag();
        return nbt.getInt(this.nbtTag);
    }
}
