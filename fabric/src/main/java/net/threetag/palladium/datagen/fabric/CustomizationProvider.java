package net.threetag.palladium.datagen.fabric;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.customization.Customization;
import net.threetag.palladium.customization.BuiltinCustomization;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class CustomizationProvider extends FabricCodecDataProvider<Customization> {

    public CustomizationProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(dataOutput, registriesFuture, PalladiumRegistryKeys.CUSTOMIZATION, Customization.Codecs.SIMPLE_CODEC);
    }

    @Override
    protected void configure(BiConsumer<ResourceLocation, Customization> provider, HolderLookup.Provider lookup) {
        for (BuiltinCustomization.Type type : BuiltinCustomization.Type.values()) {
            provider.accept(Palladium.id(type.getSerializedName()), new BuiltinCustomization(type));
        }
    }

    @Override
    public String getName() {
        return "Accessories";
    }
}
