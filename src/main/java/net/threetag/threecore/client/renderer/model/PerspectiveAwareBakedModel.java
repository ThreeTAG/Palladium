package net.threetag.threecore.client.renderer.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.datafixers.util.Pair;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.Item;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.threetag.threecore.ThreeCore;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = ThreeCore.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class PerspectiveAwareBakedModel implements IBakedModel {

    private static final Map<Supplier<ResourceLocation>, List<Pair<ItemCameraTransforms.TransformType, ResourceLocation>>> LOAD = Maps.newHashMap();

    private final IBakedModel base;
    public final List<Pair<ItemCameraTransforms.TransformType, ? extends IBakedModel>> variants = Lists.newArrayList();

    public PerspectiveAwareBakedModel(IBakedModel base) {
        this.base = base;
    }

    public static void register(ResourceLocation toReplace, Pair<ItemCameraTransforms.TransformType, ResourceLocation>... replacing) {
        LOAD.put(() -> toReplace, Arrays.asList(replacing));
    }

    public static void register(Item toReplace, Pair<ItemCameraTransforms.TransformType, ResourceLocation>... replacing) {
        register(new ModelResourceLocation(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(toReplace)), "inventory"), replacing);
    }

    public static void register(Supplier<Item> toReplace, Pair<ItemCameraTransforms.TransformType, ResourceLocation>... replacing) {
        LOAD.put(() -> new ModelResourceLocation(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(toReplace.get())), "inventory"), Arrays.asList(replacing));
    }

    @SubscribeEvent
    public static void onModelRegistry(ModelRegistryEvent e) {
        LOAD.forEach((rl, pairs) -> pairs.forEach(pair -> ModelLoader.addSpecialModel(pair.getSecond())));
    }

    @SubscribeEvent
    public static void onModelBake(ModelBakeEvent e) {
        LOAD.forEach((rl, pairs) -> {
            IBakedModel base = e.getModelRegistry().get(rl.get());
            if (base != null) {
                PerspectiveAwareBakedModel model = new PerspectiveAwareBakedModel(base);
                pairs.forEach(pair -> {
                    IBakedModel overridingModel = e.getModelRegistry().get(pair.getSecond());

                    if (overridingModel == null) {
                        ThreeCore.LOGGER.error("Perspective aware model could not find " + pair.getSecond() + " for " + rl + "!");
                    } else {
                        model.addVariant(pair.getFirst(), overridingModel);
                    }
                });
                e.getModelRegistry().put(rl.get(), model);
            }
        });
    }

    public PerspectiveAwareBakedModel addVariant(ItemCameraTransforms.TransformType transformType, IBakedModel override) {
        this.variants.add(Pair.of(transformType, override));
        return this;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, Random rand) {
        return this.base.getQuads(state, side, rand);
    }

    @Override
    public boolean isAmbientOcclusion() {
        return this.base.isAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return this.base.isGui3d();
    }

    @Override
    public boolean func_230044_c_() {
        return this.base.func_230044_c_();
    }

    @Override
    public boolean isBuiltInRenderer() {
        return this.base.isBuiltInRenderer();
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return this.base.getParticleTexture();
    }

    @Override
    public ItemOverrideList getOverrides() {
        return this.base.getOverrides();
    }

    @Override
    public IBakedModel handlePerspective(ItemCameraTransforms.TransformType cameraTransformType, MatrixStack mat) {
        for (Pair<ItemCameraTransforms.TransformType, ? extends IBakedModel> pair : this.variants) {
            if (pair.getFirst() == cameraTransformType) {
                return pair.getSecond().handlePerspective(cameraTransformType, mat);
            }
        }
        return this.base.handlePerspective(cameraTransformType, mat);
    }
}
