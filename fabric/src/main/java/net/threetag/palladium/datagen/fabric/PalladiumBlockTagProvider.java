package net.threetag.palladium.datagen.fabric;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagEntry;
import net.minecraft.world.level.block.Blocks;
import net.threetag.palladium.tag.PalladiumBlockTags;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class PalladiumBlockTagProvider extends FabricTagProvider.BlockTagProvider {

    public PalladiumBlockTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {
        this.builder(PalladiumBlockTags.PREVENTS_INTANGIBILITY).add(BuiltInRegistries.BLOCK.getResourceKey(Blocks.BEDROCK).orElseThrow());
    }

    @Override
    public @NotNull String getName() {
        return "Palladium " + super.getName();
    }
}
