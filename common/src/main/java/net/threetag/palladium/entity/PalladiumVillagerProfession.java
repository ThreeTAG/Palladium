package net.threetag.palladium.entity;

import com.google.common.collect.ImmutableSet;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class PalladiumVillagerProfession extends VillagerProfession {

    private final Supplier<PoiType> poiTypeSupplier;

    @SuppressWarnings("ConstantConditions")
    public PalladiumVillagerProfession(String string, Supplier<PoiType> poiTypeSupplier, ImmutableSet<Item> immutableSet, ImmutableSet<Block> immutableSet2, @Nullable SoundEvent soundEvent) {
        super(string, null, immutableSet, immutableSet2, soundEvent);
        this.poiTypeSupplier = poiTypeSupplier;
    }

    @Override
    public PoiType getJobPoiType() {
        return this.poiTypeSupplier.get();
    }

}
