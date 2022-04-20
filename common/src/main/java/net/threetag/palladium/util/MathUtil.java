package net.threetag.palladium.util;

import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class MathUtil {

    public static Vec3 calculateViewVector(float pitch, float yaw) {
        float f = pitch * (float) (Math.PI / 180.0);
        float g = -yaw * (float) (Math.PI / 180.0);
        float h = Mth.cos(g);
        float i = Mth.sin(g);
        float j = Mth.cos(f);
        float k = Mth.sin(f);
        return new Vec3(i * j, -k, h * j);
    }

}
