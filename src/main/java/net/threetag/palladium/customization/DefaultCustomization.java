package net.threetag.palladium.customization;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

import java.util.Objects;
import java.util.Optional;

public class DefaultCustomization extends Customization {

    public static final MapCodec<DefaultCustomization> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ComponentSerialization.CODEC.optionalFieldOf("name").forGetter(a -> Optional.ofNullable(a.customTitle)),
            ResourceKey.codec(PalladiumRegistryKeys.CUSTOMIZATION_CATEGORY).fieldOf("category").forGetter(a -> a.category),
            PreviewableRenderLayerIdentifier.CODEC.optionalFieldOf("render_layer").forGetter(a -> Optional.ofNullable(a.renderLayer)),
            Codec.BOOL.optionalFieldOf("unlockable", false).forGetter(a -> a.unlockable)
    ).apply(instance, (t, s, r, u) ->
            new DefaultCustomization(t.orElse(null), s, r.orElse(null), u)));

    private final Component customTitle;
    private final ResourceKey<CustomizationCategory> category;
    private final PreviewableRenderLayerIdentifier renderLayer;
    private final boolean unlockable;
    private Identifier cachedRenderLayerId;

    public DefaultCustomization(Component customTitle, ResourceKey<CustomizationCategory> category, PreviewableRenderLayerIdentifier renderLayer, boolean unlockable) {
        this.customTitle = customTitle;
        this.category = category;
        this.renderLayer = renderLayer;
        this.unlockable = unlockable;
    }

    @Override
    public CustomizationSerializer<?> getSerializer() {
        return CustomizationSerializers.DEFAULT.get();
    }

    @Override
    public Component getTitle(RegistryAccess registryAccess) {
        if (this.customTitle != null) {
            return this.customTitle;
        } else {
            return super.getTitle(registryAccess);
        }
    }

    @Override
    public ResourceKey<CustomizationCategory> getCategoryKey() {
        return this.category;
    }

    public Identifier getRenderLayerId(RegistryAccess registryAccess, boolean preview) {
        if (this.renderLayer != null) {
            return this.renderLayer.get(preview);
        } else {
            if (this.cachedRenderLayerId == null) {
                var id = registryAccess.lookupOrThrow(PalladiumRegistryKeys.CUSTOMIZATION).getKey(this);
                this.cachedRenderLayerId = Objects.requireNonNull(id).withPrefix("customization/");
            }

            return this.cachedRenderLayerId;
        }
    }

    @Override
    public UnlockedBy unlockedBy() {
        return this.unlockable ? UnlockedBy.COMMAND : UnlockedBy.NONE;
    }

    public static class Serializer extends CustomizationSerializer<DefaultCustomization> {

        @Override
        public MapCodec<DefaultCustomization> codec() {
            return CODEC;
        }
    }
}
