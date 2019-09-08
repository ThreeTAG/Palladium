package net.threetag.threecore.sizechanging.capability;

import net.threetag.threecore.sizechanging.SizeChangeType;
import net.minecraft.entity.Entity;

import javax.annotation.Nullable;

public interface ISizeChanging {

    void tick(Entity entity);

    float getWidth();

    float getHeight();

    float getRenderWidth(float partialTicks);

    float getRenderHeight(float partialTicks);

    SizeChangeType getSizeChangeType();

    float getScale();

    void changeSizeChangeType(SizeChangeType type);

    boolean startSizeChange(Entity entity, @Nullable SizeChangeType type, float size);

    boolean setSizeDirectly(Entity entity, @Nullable SizeChangeType type, float size);

    void updateBoundingBox(Entity entity);

    void sync(Entity entity);

}
