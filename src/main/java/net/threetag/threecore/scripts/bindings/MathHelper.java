package net.threetag.threecore.scripts.bindings;

import net.minecraft.util.math.vector.Vector3d;
import net.threetag.threecore.scripts.ScriptParameterName;
import net.threetag.threecore.scripts.accessors.Vector3dAccessor;

public class MathHelper {

    public Vector3dAccessor vec(@ScriptParameterName("x") double x, @ScriptParameterName("y") double y, @ScriptParameterName("z") double z) {
        return new Vector3dAccessor(new Vector3d(x, y, z));
    }

}
