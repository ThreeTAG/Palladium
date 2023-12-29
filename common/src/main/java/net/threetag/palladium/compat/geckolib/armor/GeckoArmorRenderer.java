package net.threetag.palladium.compat.geckolib.armor;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.client.dynamictexture.TextureReference;
import net.threetag.palladium.client.renderer.PalladiumRenderTypes;
import net.threetag.palladium.util.context.DataContext;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class GeckoArmorRenderer<T extends AddonGeoArmorItem> extends GeoArmorRenderer<T> implements CancelGeckoArmorBuffer {

    private static Entity CURRENT_ENTITY = null;
    private static EquipmentSlot CURRENT_SLOT = null;

    public GeckoArmorRenderer(AddonGeoArmorItem item) {
        super(createGeoModel(item));

//        addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }

    public static <R extends AddonGeoArmorItem> GeoModel<R> createGeoModel(AddonGeoArmorItem item) {
        var itemId = BuiltInRegistries.ITEM.getKey(item);
        return new GeoModel<>(itemId, item.modelPath, item.texturePath, item.animationsPath);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        CURRENT_ENTITY = this.currentEntity;
        CURRENT_SLOT = this.currentSlot;
        super.renderToBuffer(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public RenderType getRenderType(T animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return PalladiumRenderTypes.getArmorTranslucent(texture);
    }

    public static class GeoModel<T extends AddonGeoArmorItem> extends software.bernie.geckolib.model.GeoModel<T> {

        private ResourceLocation modelPath;
        private TextureReference texturePath;
        private ResourceLocation animationsPath;

        public GeoModel(ResourceLocation itemId, ResourceLocation modelPath, TextureReference texturePath, ResourceLocation animationsPath) {
            this.modelPath = modelPath;
            this.texturePath = texturePath;
            this.animationsPath = animationsPath;

            if (this.modelPath == null) {
                this.modelPath = new ResourceLocation(itemId.getNamespace(), "geo/armor/" + itemId.getPath() + ".geo.json");
            }

            if (this.texturePath == null) {
                this.texturePath = TextureReference.normal(new ResourceLocation(itemId.getNamespace(), "textures/armor/" + itemId.getPath() + ".png"));
            }

            if (this.animationsPath == null) {
                this.animationsPath = new ResourceLocation(itemId.getNamespace(), "animations/armor/" + itemId.getPath() + ".animation.json");
            }
        }

        @Override
        public ResourceLocation getModelResource(AddonGeoArmorItem animatable) {
            return this.modelPath;
        }

        @Override
        public ResourceLocation getTextureResource(AddonGeoArmorItem animatable) {
            return this.texturePath.getTexture(CURRENT_ENTITY instanceof LivingEntity entity ? DataContext.forArmorInSlot(entity, CURRENT_SLOT) : DataContext.create());
        }

        @Override
        public ResourceLocation getAnimationResource(AddonGeoArmorItem animatable) {
            return this.animationsPath;
        }

        @Override
        public RenderType getRenderType(T animatable, ResourceLocation texture) {
            return PalladiumRenderTypes.getArmorTranslucent(texture);
        }
    }

}
