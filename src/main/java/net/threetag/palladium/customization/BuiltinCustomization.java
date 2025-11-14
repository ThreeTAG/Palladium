package net.threetag.palladium.customization;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.Util;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.client.model.ModelLayerLocationCodec;
import net.threetag.palladium.entity.SkinTypedValue;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

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
    public ResourceKey<CustomizationCategory> getCategoryKey() {
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

        // Chest
        LUCRAFT_ARC_REACTOR("lucraft_arc_reactor", CustomizationCategories.CHEST);

        public static final Codec<Type> CODEC = StringRepresentable.fromEnum(Type::values);

        public final String key;
        private final ResourceKey<CustomizationCategory> slot;
        private final Component title;
        public final SkinTypedValue<ModelLayerLocationCodec> model;
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

        Type(String key, ResourceKey<CustomizationCategory> slot, String model, boolean slimVariant) {
            this.key = key;
            this.slot = slot;
            this.title = Component.translatable(Util.makeDescriptionId("customization", Palladium.id(this.key)));
            this.renderLayerId = Palladium.id("customization/" + this.key);

            if (slimVariant) {
                this.model = new SkinTypedValue<>(
                        new ModelLayerLocationCodec(Palladium.id("customization/" + model), "main"),
                        new ModelLayerLocationCodec(Palladium.id("customization/" + model + "_slim"), "main")
                );
            } else {
                this.model = new SkinTypedValue<>(new ModelLayerLocationCodec(Palladium.id("customization/" + model), "main"));
            }
        }

        @Override
        public String getSerializedName() {
            return this.key;
        }

        public ResourceLocation getRenderLayerId() {
            return this.renderLayerId;
        }

        public ResourceKey<Customization> getResourceKey() {
            return ResourceKey.create(PalladiumRegistryKeys.CUSTOMIZATION, Palladium.id(this.key));
        }
    }
}
