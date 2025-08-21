package net.threetag.palladium.customization;

import com.mojang.serialization.Codec;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.entity.BodyPart;
import net.threetag.palladium.registry.PalladiumRegistries;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

import java.util.Collections;
import java.util.List;

public abstract class Customization {

    private Component title;

    public abstract CustomizationSerializer<?> getSerializer();

    public Component getTitle(RegistryAccess registryAccess) {
        if (this.title == null) {
            this.title = Component.translatable(makeDescriptionId(this, registryAccess));
        }
        return this.title;
    }

    public static String makeDescriptionId(Customization customization, RegistryAccess registryAccess) {
        return Util.makeDescriptionId("customization", registryAccess.lookupOrThrow(PalladiumRegistryKeys.CUSTOMIZATION).getKey(customization));
    }

    public static String makeDescriptionId(ResourceKey<Customization> key) {
        return Util.makeDescriptionId("customization", key.location());
    }

    public static String makeDescriptionId(ResourceLocation id) {
        return Util.makeDescriptionId("customization", id);
    }

    public abstract ResourceKey<CustomizationCategory> getCategoryKey();

    public Holder<CustomizationCategory> getCategory(RegistryAccess registryAccess) {
        return registryAccess.lookupOrThrow(PalladiumRegistryKeys.CUSTOMIZATION_CATEGORY).get(this.getCategoryKey()).orElseThrow();
    }

    public ResourceLocation getRenderLayerId(RegistryAccess registryAccess) {
        return null;
    }

    public List<BodyPart> getHiddenBodyParts() {
        return Collections.emptyList();
    }

    public UnlockedBy unlockedBy() {
        return UnlockedBy.NONE;
    }

    public static class Codecs {

        public static final Codec<Holder<Customization>> HOLDER_CODEC = RegistryFixedCodec.create(PalladiumRegistryKeys.CUSTOMIZATION);

        public static final Codec<Customization> CODEC = PalladiumRegistries.CUSTOMIZATION_SERIALIZERS.byNameCodec().dispatch(Customization::getSerializer, CustomizationSerializer::codec);

        public static final Codec<Customization> SIMPLE_CODEC = Codec.withAlternative(
                CODEC,
                DefaultCustomization.CODEC.codec()
        );

    }

    public enum UnlockedBy {

        NONE,
        COMMAND,
        REWARD

    }
}
