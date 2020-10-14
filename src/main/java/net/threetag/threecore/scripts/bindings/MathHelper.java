package net.threetag.threecore.scripts.bindings;

import net.minecraft.util.math.vector.Vector3d;
import net.threetag.threecore.scripts.accessors.Vector3dAccessor;

public class MathHelper {

    public Vector3dAccessor vec(double x, double y, double z) {
        return new Vector3dAccessor(new Vector3d(x, y, z));
    }

}
