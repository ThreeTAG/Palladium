package net.threetag.palladium.power.ability;

import com.mojang.blaze3d.shaders.FogShape;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FogType;
import net.threetag.palladiumcore.event.EventResult;
import net.threetag.palladiumcore.event.ViewportEvents;

import java.util.concurrent.atomic.AtomicReference;

public class AbilityClientEventHandler implements ViewportEvents.RenderFog, ViewportEvents.ComputeFogColor {

    public static void init() {
        AbilityClientEventHandler instance = new AbilityClientEventHandler();
        ViewportEvents.RENDER_FOG.register(instance);
        ViewportEvents.COMPUTE_FOG_COLOR.register(instance);
    }

    @Override
    public EventResult renderFog(GameRenderer gameRenderer, Camera camera, double partialTick, FogRenderer.FogMode fogMode, FogType fogType, AtomicReference<Float> farPlaneDistance, AtomicReference<Float> nearPlaneDistance, AtomicReference<FogShape> fogShape) {
        if (camera.getEntity() instanceof LivingEntity living) {
            if (AbilityUtil.isTypeEnabled(living, Abilities.INTANGIBILITY.get())) {
                if (getInWallBlockState(living) != null) {
                    fogShape.set(FogShape.SPHERE);
                    farPlaneDistance.set(5F);
                    nearPlaneDistance.set(1F);
                    return EventResult.cancel();
                }
            }
        }
        return EventResult.pass();
    }

    public static BlockState getInWallBlockState(LivingEntity playerEntity) {
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

        for (int i = 0; i < 8; ++i) {
            double d = playerEntity.getX() + (double) (((float) ((i) % 2) - 0.5F) * playerEntity.getBbWidth() * 0.8F);
            double e = playerEntity.getEyeY() + (double) (((float) ((i >> 1) % 2) - 0.5F) * 0.1F);
            double f = playerEntity.getZ() + (double) (((float) ((i >> 2) % 2) - 0.5F) * playerEntity.getBbWidth() * 0.8F);
            mutable.set(d, e, f);
            BlockState blockState = playerEntity.level.getBlockState(mutable);
            if (blockState.getRenderShape() != RenderShape.INVISIBLE && blockState.isViewBlocking(playerEntity.level, mutable)) {
                return blockState;
            }
        }

        return null;
    }

    @Override
    public void computeFogColor(GameRenderer gameRenderer, Camera camera, double partialTick, AtomicReference<Float> red, AtomicReference<Float> green, AtomicReference<Float> blue) {
        if (camera.getEntity() instanceof LivingEntity living) {
            if (AbilityUtil.isTypeEnabled(living, Abilities.INTANGIBILITY.get())) {
                if (getInWallBlockState(living) != null) {
                    red.set(0F);
                    green.set(0F);
                    blue.set(0F);
                }
            }
        }
    }
}
