package net.threetag.palladium.datagen.internal;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.tag.PalladiumBlockTags;

import java.util.concurrent.CompletableFuture;

public class PalladiumBlockTagProvider extends BlockTagsProvider {

    public PalladiumBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, Palladium.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(PalladiumBlockTags.PREVENTS_INTANGIBILITY).add(Blocks.BEDROCK);
    }
}
