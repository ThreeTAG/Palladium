package net.threetag.threecore.util.scripts.accessors;

import net.minecraft.block.BlockState;
import net.minecraft.state.IProperty;
import net.threetag.threecore.util.scripts.ScriptParameterName;

public class BlockStateAccessor extends ScriptAccessor<BlockState> {

    protected BlockStateAccessor(BlockState value) {
        super(value);
    }

    public String getBlock() {
        return this.value.getBlock().getRegistryName().toString();
    }

    public IProperty getPropertyByName(@ScriptParameterName("name") String name) {
        for (IProperty property : this.value.getProperties()) {
            if (property.getName().equalsIgnoreCase(name)) {
                return property;
            }
        }

        return null;
    }

    public <T extends Comparable<T>> BlockStateAccessor withProperty(@ScriptParameterName("property") Object property, @ScriptParameterName("value") T value) {
        IProperty<T> p = property instanceof IProperty ? (IProperty<T>) property : (property instanceof String ? this.getPropertyByName((String) property) : null);
        return p == null ? this : new BlockStateAccessor(this.value.with(p, value));
    }

    public Object getProperty(@ScriptParameterName("property") Object property) {
        IProperty<?> p = property instanceof IProperty ? (IProperty<?>) property : (property instanceof String ? this.getPropertyByName((String) property) : null);
        return p == null ? null : this.value.get(p);
    }

}
