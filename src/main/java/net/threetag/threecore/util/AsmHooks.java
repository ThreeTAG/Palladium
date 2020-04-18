package net.threetag.threecore.util;

import com.google.common.base.MoreObjects;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.network.play.NetworkPlayerInfo;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec2f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.threetag.threecore.accessoires.Accessoire;
import net.threetag.threecore.capability.CapabilityAccessoires;
import net.threetag.threecore.capability.CapabilitySizeChanging;
import net.threetag.threecore.client.renderer.entity.PlayerSkinHandler;

import java.util.concurrent.atomic.AtomicReference;

public class AsmHooks {

    @OnlyIn(Dist.CLIENT)
    public static ResourceLocation getLocationSkin(NetworkPlayerInfo playerInfo) {
        playerInfo.loadPlayerTextures();
        return PlayerSkinHandler.getCurrentSkin(playerInfo.gameProfile, MoreObjects.firstNonNull(playerInfo.playerTextures.get(MinecraftProfileTexture.Type.SKIN), DefaultPlayerSkin.getDefaultSkin(playerInfo.gameProfile.getId())));
    }

    public static void registerNativeImage(String url, NativeImage nativeImage) {
        System.out.println(url + " - " + nativeImage);
//        MinecraftForgeClient.registerImageLayerSupplier(resourceLocation, () -> nativeImage);
    }

    public static EntitySize getOverridenSize(EntitySize entitySize, Entity entity, Pose pose) {
        Vec2f vec = getSize(entity, pose);
        return new EntitySize(entitySize.width * vec.x, entitySize.height * vec.y, entitySize.fixed);
    }

    public static Vec2f getSize(Entity entity, Pose pose) {
        AtomicReference<Float> width = new AtomicReference<>(1F);
        AtomicReference<Float> height = new AtomicReference<>(1F);
        entity.getCapability(CapabilitySizeChanging.SIZE_CHANGING).ifPresent(s -> {
            width.set(s.getWidth());
            height.set(s.getHeight());
        });
        return new Vec2f(width.get(), height.get());
    }

    @OnlyIn(Dist.CLIENT)
    public static void preRenderCallback(Entity entityIn, MatrixStack matrixStackIn, float partialTicks) {
        matrixStackIn.push();
        entityIn.getCapability(CapabilitySizeChanging.SIZE_CHANGING).ifPresent(sizeChanging -> {
            float width = sizeChanging.getRenderWidth(RenderUtil.renderTickTime);
            float height = sizeChanging.getRenderHeight(RenderUtil.renderTickTime);
            matrixStackIn.scale(width, height, width);
        });
    }

    public static void entityTick(Entity entity) {
        entity.getCapability(CapabilitySizeChanging.SIZE_CHANGING).ifPresent(s -> s.tick());
    }

    @OnlyIn(Dist.CLIENT)
    public static void renderInInvCallback(LivingEntity entity) {
        entity.getCapability(CapabilitySizeChanging.SIZE_CHANGING).ifPresent(sizeChanging -> {
            float width = 1F / sizeChanging.getRenderWidth(RenderUtil.renderTickTime);
            RenderSystem.scalef(width, 1F / sizeChanging.getRenderHeight(RenderUtil.renderTickTime), width);
        });
    }

    @OnlyIn(Dist.CLIENT)
    public static void postRenderCallback(Entity entityIn, MatrixStack matrixStackIn, float partialTicks) {
        matrixStackIn.pop();
    }

    @OnlyIn(Dist.CLIENT)
    public static void postRotationAnglesCallback(LivingRenderer<? extends LivingEntity, ? extends EntityModel> renderer, LivingEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        entityIn.getCapability(CapabilityAccessoires.ACCESSOIRES).ifPresent(accessoireHolder -> {
            for (Accessoire accessoire : accessoireHolder.getActiveAccessoires()) {
                if (accessoire.getPlayerPart() != null) {
                    accessoire.getPlayerPart().setVisibility((PlayerModel) renderer.getEntityModel(), false);
                }
            }
        });
    }
}
