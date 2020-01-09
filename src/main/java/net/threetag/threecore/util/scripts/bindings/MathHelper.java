package net.threetag.threecore.util.scripts.bindings;

import net.minecraft.util.math.Vec3d;
import net.threetag.threecore.util.scripts.accessors.Vec3dAccessor;

public class MathHelper {

    public Vec3dAccessor vec(double x, double y, double z) {
        return new Vec3dAccessor(new Vec3d(x, y, z));
    }

}
