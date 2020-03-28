package net.threetag.threecore.client.renderer.entity.modellayer;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.LazyValue;
import net.threetag.threecore.client.renderer.entity.modellayer.predicates.IModelLayerPredicate;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class CompoundModelLayer implements IModelLayer {

    private List<LazyValue<IModelLayer>> modelLayerList;
    public final List<IModelLayerPredicate> predicateList = Lists.newLinkedList();

    public CompoundModelLayer(List<LazyValue<IModelLayer>> modelLayerList) {
        this.modelLayerList = modelLayerList;
    }

    public CompoundModelLayer(LazyValue<IModelLayer>... layers) {
        this.modelLayerList = Arrays.asList(layers);
    }

    @Override
    public void render(IModelLayerContext context, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int i, @Nullable IEntityRenderer<? extends Entity, ? extends EntityModel<?>> entityRenderer, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        for (LazyValue<IModelLayer> layers : this.modelLayerList) {
            if (layers.getValue() != null) {
                if (layers.getValue().isActive(context)) {
                    layers.getValue().render(context, matrixStack, renderTypeBuffer, i, entityRenderer, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
                }
            }
        }
    }

    @Override
    public boolean isActive(IModelLayerContext context) {
        return ModelLayerManager.arePredicatesFulFilled(this.predicateList, context);
    }

    @Override
    public CompoundModelLayer addPredicate(IModelLayerPredicate predicate) {
        this.predicateList.add(predicate);
        return this;
    }

    public static CompoundModelLayer parse(JsonObject json) {
        List<LazyValue<IModelLayer>> modelLayers = Lists.newLinkedList();
        JsonArray jsonArray = JSONUtils.getJsonArray(json, "layers");

        for (int i = 0; i < jsonArray.size(); i++) {
            int finalI = i;
            modelLayers.add(new LazyValue<>(() -> ModelLayerManager.parseLayer(jsonArray.get(finalI))));
        }

        return new CompoundModelLayer(modelLayers);
    }
}
