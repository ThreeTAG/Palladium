package net.threetag.threecore.sizechanging;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.util.math.Vec2f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.threetag.threecore.capability.CapabilitySizeChanging;
import net.threetag.threecore.util.RenderUtil;

import java.util.concurrent.atomic.AtomicReference;

public class SizeManager {

    public static final IAttribute SIZE_WIDTH = (new RangedAttribute(null, "threecore.sizeWidth", 1D, 0.1D, 32D)).setShouldWatch(true);
    public static final IAttribute SIZE_HEIGHT = (new RangedAttribute(null, "threecore.sizeHeight", 1D, 0.1D, 32D)).setShouldWatch(true);

    public static Vec2f getSize(Entity entity, Pose pose) {
        AtomicReference<Float> width = new AtomicReference<>(1F);
        AtomicReference<Float> height = new AtomicReference<>(1F);
        entity.getCapability(CapabilitySizeChanging.SIZE_CHANGING).ifPresent(s -> {
            width.set(s.getWidth());
            height.set(s.getHeight());
        });
        return new Vec2f(width.get(), height.get());
    }

    public static void entityTick(Entity entity) {
        entity.getCapability(CapabilitySizeChanging.SIZE_CHANGING).ifPresent(s -> s.tick());
    }

    public static EntitySize getOverridenSize(EntitySize entitySize, Entity entity, Pose pose) {
        Vec2f vec = getSize(entity, pose);
        return new EntitySize(entitySize.width * vec.x, entitySize.height * vec.y, entitySize.fixed);
    }

    @OnlyIn(Dist.CLIENT)
    public static void scaleEntity(Entity entity, double x, double y, double z) {
        entity.getCapability(CapabilitySizeChanging.SIZE_CHANGING).ifPresent(sizeChanging -> {
            float width = sizeChanging.getRenderWidth(RenderUtil.renderTickTime);
            float height = sizeChanging.getRenderHeight(RenderUtil.renderTickTime);
            GlStateManager.scalef(width, height, width);
            GlStateManager.translated((x / width) - x, (y / height) - y, (z / width) - z);
        });
    }

    @OnlyIn(Dist.CLIENT)
    public static void renderInInvCallback(LivingEntity entity) {
        entity.getCapability(CapabilitySizeChanging.SIZE_CHANGING).ifPresent(sizeChanging -> {
            float width = 1F / sizeChanging.getRenderWidth(RenderUtil.renderTickTime);
            GlStateManager.scalef(width, 1F / sizeChanging.getRenderHeight(RenderUtil.renderTickTime), width);
        });
    }
}
