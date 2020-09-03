package net.threetag.threecore.scripts.accessors;

import net.minecraft.block.BlockState;
import net.minecraft.state.Property;
import net.minecraft.util.math.BlockPos;
import net.threetag.threecore.scripts.ScriptParameterName;

public class BlockStateAccessor extends ScriptAccessor<BlockState> {

    protected BlockStateAccessor(BlockState value) {
        super(value);
    }

    public String getBlock() {
        return this.value.getBlock().getRegistryName().toString();
    }

    public MaterialAccessor getMaterial() {
        return new MaterialAccessor(this.value.getMaterial());
    }

    public boolean isAir(@ScriptParameterName("world") WorldAccessor world, @ScriptParameterName("x") int x, @ScriptParameterName("y") int y, @ScriptParameterName("z") int z) {
        return this.value.isAir(world.value, new BlockPos(x, y, z));
    }

    public Property getPropertyByName(@ScriptParameterName("name") String name) {
        for (Property property : this.value.getProperties()) {
            if (property.getName().equalsIgnoreCase(name)) {
                return property;
            }
        }

        return null;
    }

    public <T extends Comparable<T>> BlockStateAccessor withProperty(@ScriptParameterName("property") Object property, @ScriptParameterName("value") T value) {
        Property<T> p = property instanceof Property ? (Property<T>) property : (property instanceof String ? this.getPropertyByName((String) property) : null);
        return p == null ? this : new BlockStateAccessor(this.value.with(p, value));
    }

    public Object getProperty(@ScriptParameterName("property") Object property) {
        Property<?> p = property instanceof Property ? (Property<?>) property : (property instanceof String ? this.getPropertyByName((String) property) : null);
        return p == null ? null : this.value.get(p);
    }

}
