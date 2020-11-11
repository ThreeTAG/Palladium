package net.threetag.threecore.client.renderer.entity.modellayer;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.accessoires.Accessoires;
import net.threetag.threecore.accessoires.SupporterCloakAccessoire;
import net.threetag.threecore.capability.CapabilityAccessoires;
import net.threetag.threecore.util.SupporterHandler;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

@Mod.EventBusSubscriber(modid = ThreeCore.MODID, value = Dist.CLIENT)
public class ModelLayerRenderer<T extends LivingEntity, M extends BipedModel<T>, A extends BipedModel<T>> extends LayerRenderer<T, M> {

    private static ArrayList<LivingRenderer> entitiesWithLayer = new ArrayList<>();

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void renderEntityPre(RenderLivingEvent.Pre e) {
        if (!entitiesWithLayer.contains(e.getRenderer())) {
            e.getRenderer().addLayer(new ModelLayerRenderer(e.getRenderer()));
            entitiesWithLayer.add(e.getRenderer());
        }
    }

    public ModelLayerRenderer(IEntityRenderer<T, M> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int packedLightIn, T entityIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        AtomicBoolean cape = new AtomicBoolean(false);
        ModelLayerManager.forEachLayer(entityIn, (layer, context) -> {
            if (layer.isActive(context)) {
                layer.render(context, matrixStack, renderTypeBuffer, packedLightIn, this.entityRenderer, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);

                if (layer instanceof CapeModelLayer) {
                    cape.set(true);
                }
            }
        });

        if (!cape.get() && SupporterHandler.getPlayerData(entityIn.getUniqueID()).getCloakTexture() != null) {
            entityIn.getCapability(CapabilityAccessoires.ACCESSOIRES).ifPresent(accessoires -> {
                if (accessoires.getActiveAccessoires().contains(Accessoires.SUPPORTER_CLOAK.get())) {
                    SupporterCloakAccessoire.MODEL_LAYER.render(new ModelLayerContext(entityIn), matrixStack, renderTypeBuffer, packedLightIn, this.entityRenderer, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
                }
            });
        }
    }
}
