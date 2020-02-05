package net.threetag.threecore.client.renderer.entity.modellayer;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.LazyLoadBase;
import net.threetag.threecore.client.renderer.entity.modellayer.predicates.IModelLayerPredicate;

import java.util.Arrays;
import java.util.List;

public class CompoundModelLayer implements IModelLayer {

    private List<LazyLoadBase<IModelLayer>> modelLayerList;
    public final List<IModelLayerPredicate> predicateList = Lists.newLinkedList();

    public CompoundModelLayer(List<LazyLoadBase<IModelLayer>> modelLayerList) {
        this.modelLayerList = modelLayerList;
    }

    public CompoundModelLayer(LazyLoadBase<IModelLayer>... layers) {
        this.modelLayerList = Arrays.asList(layers);
    }

    @Override
    public void render(IModelLayerContext context, IEntityRenderer entityRenderer, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        for (LazyLoadBase<IModelLayer> layers : this.modelLayerList) {
            if (layers.getValue() != null) {
                if (layers.getValue().isActive(context)) {
                    layers.getValue().render(context, entityRenderer, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
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
        List<LazyLoadBase<IModelLayer>> modelLayers = Lists.newLinkedList();
        JsonArray jsonArray = JSONUtils.getJsonArray(json, "layers");

        for (int i = 0; i < jsonArray.size(); i++) {
            int finalI = i;
            modelLayers.add(new LazyLoadBase<>(() -> ModelLayerManager.parseLayer(jsonArray.get(finalI))));
        }

        return new CompoundModelLayer(modelLayers);
    }
}
