package net.threetag.palladium.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.addonpack.log.AddonPackLog;

@EventBusSubscriber(modid = Palladium.MOD_ID, value = Dist.CLIENT)
public class WatcherRenderer {

    public static final WatcherRenderer INSTANCE = new WatcherRenderer();
    private static final ResourceLocation TEXTURE = Palladium.id("textures/environment/watcher.png");
    private static final int OCCURRENCE_INTERVAL = 24000; // full Minecraft day
    private static final float OCCURRENCE_CHANCE = 0.01F; // 1% chance per day
    private static final int OCCURRENCE_DURATION = 20 * 60; // 1 minute
    private static final int OCCURRENCE_FADE = 40; // 2 seconds

    private int ticksTilOccurrence = OCCURRENCE_INTERVAL;
    private int visibleTicks = 0;
    private int visibility = 0;
    private int prevVisibility = 0;

    public WatcherRenderer() {

    }

    public void render(PoseStack poseStack, MultiBufferSource.BufferSource bufferSource, float visibility) {
//        if (visibility > 0F) {
//            var easing = this.visibleTicks > 0 ? Easing.INSINE : Easing.OUTSINE;
//            visibility = easing.apply(visibility);
//
//            poseStack.pushPose();
//            poseStack.mulPose(Axis.YP.rotationDegrees(-135.0F));
//            poseStack.mulPose(Axis.XP.rotationDegrees(60));
//            float f = 40.0F;
//            float g = 100.0F;
//            VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.celestial(TEXTURE));
//            int i = ARGB.white(visibility);
//            Matrix4f matrix4f = poseStack.last().pose();
//            vertexConsumer.addVertex(matrix4f, -f, g, -f).setUv(1.0F, 0.0F).setColor(i);
//            vertexConsumer.addVertex(matrix4f, f, g, -f).setUv(0.0F, 0.0F).setColor(i);
//            vertexConsumer.addVertex(matrix4f, f, g, f).setUv(0.0F, 1.0F).setColor(i);
//            vertexConsumer.addVertex(matrix4f, -f, g, f).setUv(1.0F, 1.0F).setColor(i);
//            bufferSource.endBatch();
//            poseStack.popPose();
//        }
    }

    @SubscribeEvent
    static void clientTick(ClientTickEvent.Post e) {
        INSTANCE.prevVisibility = INSTANCE.visibility;

        if (INSTANCE.ticksTilOccurrence > 0) {
            INSTANCE.ticksTilOccurrence--;

            if (INSTANCE.ticksTilOccurrence <= 0) {
                if (Math.random() < OCCURRENCE_CHANCE) {
                    INSTANCE.visibleTicks = OCCURRENCE_DURATION;
                    AddonPackLog.info("Someone is watching...");
                }

                INSTANCE.ticksTilOccurrence = (int) (OCCURRENCE_INTERVAL * (1 + Math.random()));
            }
        }

        if (INSTANCE.visibleTicks > 0) {
            INSTANCE.visibleTicks--;

            if (INSTANCE.visibility < OCCURRENCE_FADE) {
                INSTANCE.visibility++;
            }
        } else if (INSTANCE.visibility > 0) {
            INSTANCE.visibility--;
        }
    }

    public float getVisibility(float partialTick) {
        return Mth.lerp(partialTick, this.prevVisibility, this.visibility) / (float) OCCURRENCE_FADE;
    }
}
