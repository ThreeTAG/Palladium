package net.threetag.threecore.sizechanging;

import net.minecraft.entity.Entity;
import net.minecraft.entity.Pose;
import net.minecraft.util.math.Vec2f;

public interface ISizeProvider {

    Vec2f getSize(Entity entity, Pose pose);

}
