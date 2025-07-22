package net.threetag.palladium.customization;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.Util;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.client.model.ModelLayerLocationCodec;
import net.threetag.palladium.client.renderer.entity.layer.*;
import net.threetag.palladium.condition.PerspectiveAwareConditions;
import net.threetag.palladium.entity.BodyPart;
import net.threetag.palladium.entity.SkinTypedValue;

import java.util.Collections;
import java.util.List;

public class BuiltinCustomization extends Customization {

    public static final MapCodec<BuiltinCustomization> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Type.CODEC.fieldOf("builtin_type").forGetter(a -> a.type)
    ).apply(instance, BuiltinCustomization::new));

    private final Type type;

    public BuiltinCustomization(Type type) {
        this.type = type;
    }

    @Override
    public CustomizationSerializer<?> getSerializer() {
        return CustomizationSerializers.BUILTIN.get();
    }

    @Override
    public ResourceKey<CustomizationCategory> getSlot() {
        return this.type.slot;
    }

    @Override
    public Component getTitle(RegistryAccess registryAccess) {
        return this.type.title;
    }

    @Override
    public ResourceLocation getRenderLayerId(RegistryAccess registryAccess) {
        return this.type.getRenderLayerId();
    }

    @Override
    public List<BodyPart> getHiddenBodyParts() {
        return this.type.getHiddenBodyParts();
    }

    @Override
    public UnlockedBy unlockedBy() {
        return UnlockedBy.REWARD;
    }

    public static class Serializer extends CustomizationSerializer<BuiltinCustomization> {

        @Override
        public MapCodec<BuiltinCustomization> codec() {
            return CODEC;
        }
    }

    public enum Type implements StringRepresentable {

        // Hats
        FEZ("fez", CustomizationCategories.HAT, "fez"),
        ANTENNA("antenna", CustomizationCategories.HAT, "antenna"),
        OWCA_FEDORA("owca_fedora", CustomizationCategories.HAT, "fedora"),
        KRUSTY_KRAB_HAT("krusty_krab_hat", CustomizationCategories.HAT, "krusty_krab_hat"),
        STRAWHAT("strawhat", CustomizationCategories.HAT, "strawhat"),
        ELTON_HAT("elton_hat", CustomizationCategories.HAT, "fedora"),

        // Heads
        HEROBRINE_EYES("herobrine_eyes", CustomizationCategories.HEAD),
        FACE_MASK("face_mask", CustomizationCategories.HEAD),

        // Chest
        LUCRAFT_ARC_REACTOR("lucraft_arc_reactor", CustomizationCategories.CHEST),

        // Arms
        WINTER_SOLDIER_ARM("winter_soldier_arm", CustomizationCategories.ARMS);

        public static final Codec<Type> CODEC = StringRepresentable.fromEnum(Type::values);

        private final String key;
        private final ResourceKey<CustomizationCategory> slot;
        private final Component title;
        private final SkinTypedValue<ModelLayerLocationCodec> model;
        private final ResourceLocation renderLayerId;

        Type(String key, ResourceKey<CustomizationCategory> slot) {
            this.key = key;
            this.slot = slot;
            this.title = Component.translatable(Util.makeDescriptionId("customization", Palladium.id(this.key)));
            this.model = new SkinTypedValue<>(
                    new ModelLayerLocationCodec(ResourceLocation.withDefaultNamespace("player"), "main"),
                    new ModelLayerLocationCodec(ResourceLocation.withDefaultNamespace("player_slim"), "main")
            );
            this.renderLayerId = Palladium.id("customization/" + this.key);
        }

        Type(String key, ResourceKey<CustomizationCategory> slot, String model) {
            this.key = key;
            this.slot = slot;
            this.title = Component.translatable(Util.makeDescriptionId("customization", Palladium.id(this.key)));
            this.model = new SkinTypedValue<>(new ModelLayerLocationCodec(Palladium.id("customization/" + model), "main"));
            this.renderLayerId = Palladium.id("customization/" + this.key);
        }

        @Override
        public String getSerializedName() {
            return this.key;
        }

        public ResourceLocation getRenderLayerId() {
            return this.renderLayerId;
        }

        public List<BodyPart> getHiddenBodyParts() {
            if (this == WINTER_SOLDIER_ARM) {
                return Collections.singletonList(BodyPart.LEFT_ARM);
            }
            return Collections.emptyList();
        }

        @Environment(EnvType.CLIENT)
        public SkinTypedValue<PackRenderLayerTexture> getTexture() {
            var texture = Palladium.id("textures/customization/" + this.key + ".png");

            if (this == STRAWHAT) {
                texture = ResourceLocation.withDefaultNamespace("textures/entity/villager/profession/farmer.png");
            }

            if (this == WINTER_SOLDIER_ARM) {
                return new SkinTypedValue<>(
                        new PackRenderLayerTexture(Palladium.id("textures/customization/" + this.key + ".png")),
                        new PackRenderLayerTexture(Palladium.id("textures/customization/" + this.key + "_slim.png"))
                );
            }

            return new SkinTypedValue<>(new PackRenderLayerTexture(texture));
        }

        @Environment(EnvType.CLIENT)
        public PackRenderLayer<?> makeRenderLayer() {
            boolean glowing = this == HEROBRINE_EYES || this == LUCRAFT_ARC_REACTOR;

            return new DefaultPackRenderLayer(
                    this.model,
                    this.getTexture(),
                    RenderTypeFunctions.SOLID,
                    glowing ? 15 : 0,
                    PackRenderLayerAnimations.EMPTY,
                    PerspectiveAwareConditions.EMPTY
            );
        }
    }
}
