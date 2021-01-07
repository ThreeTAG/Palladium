package net.threetag.threecore.client.renderer.entity.modellayer;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.LazyValue;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.NonNullFunction;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.client.renderer.entity.modellayer.predicates.IModelLayerPredicate;
import net.threetag.threecore.util.documentation.IDocumentationSettings;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
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

    public static class Parser implements NonNullFunction<JsonObject, IModelLayer>, IDocumentationSettings {

        public static final ResourceLocation ID = new ResourceLocation(ThreeCore.MODID, "compound");

        @Nonnull
        @Override
        public IModelLayer apply(@Nonnull JsonObject json) {
            List<LazyValue<IModelLayer>> modelLayers = Lists.newLinkedList();
            JsonArray jsonArray = JSONUtils.getJsonArray(json, "layers");

            for (int i = 0; i < jsonArray.size(); i++) {
                int finalI = i;
                modelLayers.add(new LazyValue<>(() -> ModelLayerManager.parseLayer(jsonArray.get(finalI))));
            }

            return new CompoundModelLayer(modelLayers);
        }

        @Override
        public ResourceLocation getId() {
            return ID;
        }

        @Override
        public List<String> getColumns() {
            return Arrays.asList("Setting", "Type", "Description", "Required", "Fallback Value");
        }

        @Override
        public List<Iterable<?>> getRows() {
            List<Iterable<?>> rows = new ArrayList<>();
            rows.add(Arrays.asList("layers", IModelLayer[].class, "Array of model layers that you want to merge together", true, null));
            return rows;
        }

        @Override
        public JsonElement getExampleJson() {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("type", ID.toString());
            JsonArray layers = new JsonArray();
            layers.add(new ModelLayer.Parser().getExampleJson());
            layers.add(new CapeModelLayer.Parser().getExampleJson());
            jsonObject.add("layers", layers);
            return jsonObject;
        }
    }
}
