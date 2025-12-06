package net.threetag.palladium.data.forge;

import com.mojang.serialization.JsonOps;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.damagesource.DamageType;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.JsonCodecProvider;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.entity.PalladiumDamageTypes;

import java.util.Map;

public class PalladiumDamageTypeProvider extends JsonCodecProvider<DamageType> {

    public PalladiumDamageTypeProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, existingFileHelper, Palladium.MOD_ID, JsonOps.INSTANCE, PackType.SERVER_DATA, "damage_type", DamageType.CODEC, getEntries());
    }

    private static Map<ResourceLocation, DamageType> getEntries() {
        return Map.of(PalladiumDamageTypes.ENERGY_BEAM.location(), new DamageType("palladium.energy_beam", 0.1F));
    }
}
