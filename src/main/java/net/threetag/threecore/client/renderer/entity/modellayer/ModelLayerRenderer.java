package net.threetag.threecore.client.renderer.entity.modellayer;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.ability.Ability;
import net.threetag.threecore.ability.AbilityHelper;

import java.util.ArrayList;

@Mod.EventBusSubscriber(modid = ThreeCore.MODID, value = Dist.CLIENT)
public class ModelLayerRenderer<T extends LivingEntity, M extends BipedModel<T>, A extends BipedModel<T>> extends LayerRenderer<T, M> {

    private static ArrayList<Class<? extends LivingEntity>> entitiesWithLayer = new ArrayList<>();

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void renderEntityPre(RenderLivingEvent.Pre e) {
        if (!entitiesWithLayer.contains(e.getEntity().getClass())) {
            e.getRenderer().addLayer(new ModelLayerRenderer(e.getRenderer()));
            entitiesWithLayer.add(e.getEntity().getClass());
        }
    }

    public ModelLayerRenderer(IEntityRenderer<T, M> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int packedLightIn, T entityIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        renderItemLayers(matrixStack, renderTypeBuffer, packedLightIn, entityIn, EquipmentSlotType.HEAD, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
        renderItemLayers(matrixStack, renderTypeBuffer, packedLightIn, entityIn, EquipmentSlotType.CHEST, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
        renderItemLayers(matrixStack, renderTypeBuffer, packedLightIn, entityIn, EquipmentSlotType.LEGS, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
        renderItemLayers(matrixStack, renderTypeBuffer, packedLightIn, entityIn, EquipmentSlotType.FEET, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);

        ModelLayerContext context = new ModelLayerContext(entityIn);
        for (Ability ability : AbilityHelper.getAbilities(entityIn)) {
            if (ability instanceof IModelLayerProvider && ability.getConditionManager().isEnabled()) {
                renderLayers(matrixStack, renderTypeBuffer, packedLightIn, (IModelLayerProvider) ability, context, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
            }
        }
    }

    public void renderItemLayers(MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int packedLightIn, T entity, EquipmentSlotType slot, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        ItemStack stack = entity.getItemStackFromSlot(slot);
        ModelLayerContext context = new ModelLayerContext(entity, stack, slot);

        if (stack.getItem() instanceof IModelLayerProvider) {
            for (IModelLayer layer : ((IModelLayerProvider) stack.getItem()).getModelLayers(context)) {
                if (layer.isActive(context)) {
                    layer.render(context, matrixStack, renderTypeBuffer, packedLightIn, this.entityRenderer, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
                }
            }
        }
    }

    public void renderLayers(MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int i, IModelLayerProvider provider, IModelLayerContext context, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        for (IModelLayer layer : provider.getModelLayers(context)) {
            if (layer.isActive(context)) {
                layer.render(context, matrixStack, renderTypeBuffer, i, this.entityRenderer, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
            }
        }
    }
}
