package net.threetag.threecore.sizechanging.capability;

import net.threetag.threecore.sizechanging.SizeChangeType;
import net.minecraft.entity.Entity;

import javax.annotation.Nullable;

public interface ISizeChanging {

    void tick();

    float getWidth();

    float getHeight();

    float getRenderWidth(float partialTicks);

    float getRenderHeight(float partialTicks);

    SizeChangeType getSizeChangeType();

    float getScale();

    void changeSizeChangeType(SizeChangeType type);

    boolean startSizeChange(@Nullable SizeChangeType type, float size);

    boolean setSizeDirectly(@Nullable SizeChangeType type, float size);

    void updateBoundingBox();

}
