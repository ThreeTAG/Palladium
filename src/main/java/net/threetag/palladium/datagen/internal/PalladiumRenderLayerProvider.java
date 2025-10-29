package net.threetag.palladium.datagen.internal;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.client.renderer.RenderTypeRegistry;
import net.threetag.palladium.client.renderer.entity.layer.pack.DefaultPackRenderLayer;
import net.threetag.palladium.client.renderer.entity.layer.pack.PackRenderLayer;
import net.threetag.palladium.client.renderer.entity.layer.pack.PackRenderLayerAnimation;
import net.threetag.palladium.client.renderer.entity.layer.pack.PackRenderLayerTexture;
import net.threetag.palladium.client.util.PerspectiveAwareConditions;
import net.threetag.palladium.customization.BuiltinCustomization;
import net.threetag.palladium.datagen.RenderLayerProvider;
import net.threetag.palladium.entity.SkinTypedValue;

import java.util.concurrent.CompletableFuture;

public class PalladiumRenderLayerProvider extends RenderLayerProvider {

    public PalladiumRenderLayerProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, Palladium.MOD_ID);
    }

    @Override
    protected void gather() {
        for (BuiltinCustomization.Type type : BuiltinCustomization.Type.values()) {
            this.unconditional(type.getRenderLayerId(), this.makeRenderLayer(type));
        }
    }

    public PackRenderLayer<?> makeRenderLayer(BuiltinCustomization.Type type) {
        boolean glowing = type == BuiltinCustomization.Type.HEROBRINE_EYES || type == BuiltinCustomization.Type.LUCRAFT_ARC_REACTOR;

        return new DefaultPackRenderLayer(
                type.model,
                this.getTexture(type),
                RenderTypeRegistry.ENTITY_TRANSLUCENT,
                glowing ? 15 : 0,
                PackRenderLayerAnimation.EMPTY,
                PerspectiveAwareConditions.EMPTY
        );
    }

    public SkinTypedValue<PackRenderLayerTexture> getTexture(BuiltinCustomization.Type type) {
        var texture = Palladium.id("textures/customization/" + type.key + ".png");

        if (type == BuiltinCustomization.Type.STRAWHAT) {
            texture = ResourceLocation.withDefaultNamespace("textures/entity/villager/profession/farmer.png");
        }

        if (type == BuiltinCustomization.Type.WINTER_SOLDIER_ARM) {
            return new SkinTypedValue<>(
                    new PackRenderLayerTexture(Palladium.id("textures/customization/" + type.key + ".png")),
                    new PackRenderLayerTexture(Palladium.id("textures/customization/" + type.key + "_slim.png"))
            );
        }

        return new SkinTypedValue<>(new PackRenderLayerTexture(texture));
    }
}
