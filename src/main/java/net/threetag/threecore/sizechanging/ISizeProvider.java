package net.threetag.threecore.sizechanging;

import net.minecraft.entity.Entity;
import net.minecraft.entity.Pose;
import net.minecraft.util.math.vector.Vector2f;

public interface ISizeProvider {

    Vector2f getSize(Entity entity, Pose pose);

}
