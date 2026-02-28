package net.threetag.palladium.client;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderHandEvent;
import net.neoforged.neoforge.client.event.RenderLivingEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;
import net.neoforged.neoforge.common.CommonHooks;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.attachment.PalladiumAttachments;
import net.threetag.palladium.client.renderer.entity.state.PalladiumRenderStateKeys;
import net.threetag.palladium.power.ability.AbilitySerializers;
import net.threetag.palladium.power.ability.AbilityUtil;
import net.threetag.palladium.power.ability.IntangibilityAbility;
import net.threetag.palladium.util.PlayerUtil;

@EventBusSubscriber(modid = Palladium.MOD_ID, value = Dist.CLIENT)
public class AbilityClientEventHandler {

    public static float OVERRIDDEN_OPACITY = 1F;
    public static int OVERRIDDEN_TINT = -1;
    private static int WALL_JUMP_TICKS = 0;
    private static BlockPos WALL_JUMP_DIRECTION = null;
    private static boolean CACHED_JUMP_KEY_PRESSED = false;

    @SubscribeEvent
    static void preRenderLiving(RenderLivingEvent.Pre<?, ?, ?> e) {
        float opacity = e.getRenderState().getRenderDataOrDefault(PalladiumRenderStateKeys.OPACITY, 1F);

        if (opacity <= 0F) {
            e.setCanceled(true);
            resetColorOverrides();
        } else {
            OVERRIDDEN_OPACITY = opacity;
            OVERRIDDEN_TINT = e.getRenderState().getRenderDataOrDefault(PalladiumRenderStateKeys.TINT, -1);
        }
    }

    @SubscribeEvent
    static void postRenderLiving(RenderLivingEvent.Post<?, ?, ?> e) {
        resetColorOverrides();
    }

    @SubscribeEvent
    static void renderHand(RenderHandEvent e) {
        float opacity = 1F - AbilityUtil.getHighestAnimationTimerProgress(Minecraft.getInstance().player, AbilitySerializers.INVISIBILITY.get(), e.getPartialTick());

        if (opacity <= 0F) {
            e.setCanceled(true);
            resetColorOverrides();
        } else {
            OVERRIDDEN_OPACITY = opacity;
        }
    }

    public static void resetColorOverrides() {
        OVERRIDDEN_OPACITY = 1F;
        OVERRIDDEN_TINT = -1;
    }

    @SubscribeEvent
    static void renderFog(ViewportEvent.RenderFog e) {
        if (e.getCamera().entity() instanceof LivingEntity living) {
            if (AbilityUtil.isTypeEnabled(living, AbilitySerializers.INTANGIBILITY.get())) {
                if (IntangibilityAbility.getInWallBlockState(living) != null) {
                    e.setFarPlaneDistance(5F);
                    e.setNearPlaneDistance(1F);
                }
            }
        }
    }

    @SubscribeEvent
    static void fogColor(ViewportEvent.ComputeFogColor e) {
        if (e.getCamera().entity() instanceof LivingEntity living) {
            if (AbilityUtil.isTypeEnabled(living, AbilitySerializers.INTANGIBILITY.get())) {
                if (IntangibilityAbility.getInWallBlockState(living) != null) {
                    e.setRed(0F);
                    e.setGreen(0F);
                    e.setBlue(0F);
                }
            }
        }
    }

    @SubscribeEvent
    static void wallClimbing(ClientTickEvent.Post e) {
        var mc = Minecraft.getInstance();

        if (mc.player != null) {
            if (mc.player.getData(PalladiumAttachments.IS_CLIMBING.get()) && WALL_JUMP_TICKS <= 0) {
                mc.player.level().findSupportingBlock(mc.player, mc.player.getBoundingBox().inflate(0.2F, -0.2F, 0.2F)).ifPresent(blockPos -> {
                    var direction = mc.player.blockPosition().subtract(blockPos);
                    var toWallRot = (float) -Mth.wrapDegrees(Math.toDegrees(Math.atan2(direction.getX(), direction.getZ())) + 180F);
                    mc.player.setYBodyRot(toWallRot);

                    float f = Mth.wrapDegrees(mc.player.getYRot() - toWallRot);
                    float f1 = Mth.clamp(f, -105.0F, 105.0F);
                    mc.player.yRotO += f1 - f;
                    mc.player.setYRot( mc.player.getYRot() + f1 - f);
                    mc.player.setYHeadRot(mc.player.getYRot());
                });
            }

            if (CACHED_JUMP_KEY_PRESSED != mc.options.keyJump.isDown()) {
                if (!CACHED_JUMP_KEY_PRESSED && mc.player.getData(PalladiumAttachments.IS_CLIMBING.get())) {
                    float f = mc.player.getJumpPower(1F);
                    if (!(f <= 1.0E-5F)) {
                        mc.player.level().findSupportingBlock(mc.player, mc.player.getBoundingBox().inflate(0.2F, -0.2F, 0.2F)).ifPresent(blockPos -> {
                            WALL_JUMP_DIRECTION = mc.player.blockPosition().subtract(blockPos);
                            WALL_JUMP_TICKS = 10;
                            var jumpAngle = (float) -Mth.wrapDegrees(Math.toDegrees(Math.atan2(WALL_JUMP_DIRECTION.getX(), WALL_JUMP_DIRECTION.getZ())));

                            mc.player.setYRot(jumpAngle);
                            mc.options.keyUp.consumeClick();
                            mc.player.needsSync = true;
                            CommonHooks.onLivingJump(mc.player);
                        });
                    }
                }
                CACHED_JUMP_KEY_PRESSED = !CACHED_JUMP_KEY_PRESSED;
            }


            if (!PlayerUtil.isFlying(mc.player) && WALL_JUMP_TICKS > 0) {
                float f = mc.player.getJumpPower(1F);
                float scale = WALL_JUMP_TICKS / 80F;
                mc.player.addDeltaMovement(new Vec3(WALL_JUMP_DIRECTION.getX() * f * scale, f * scale, WALL_JUMP_DIRECTION.getZ() * f * scale));
                WALL_JUMP_TICKS--;
            } else {
                WALL_JUMP_TICKS = 0;
                WALL_JUMP_DIRECTION = null;
            }
        }
    }

}
