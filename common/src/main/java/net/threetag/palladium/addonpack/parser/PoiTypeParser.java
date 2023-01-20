package net.threetag.palladium.addonpack.parser;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.threetag.palladium.addonpack.builder.AddonBuilder;
import net.threetag.palladium.addonpack.builder.PoiTypeBuilder;
import net.threetag.palladium.util.json.GsonUtil;
import net.threetag.palladiumcore.registry.DeferredRegister;
import net.threetag.palladiumcore.registry.RegistrySupplier;
import net.threetag.palladiumcore.util.Platform;

public class PoiTypeParser extends AddonParser<PoiType> {

    public PoiTypeParser() {
        super(GSON, "poi_types", Registry.POINT_OF_INTEREST_TYPE_REGISTRY);
    }

    @Override
    public AddonBuilder<PoiType> parse(ResourceLocation id, JsonElement jsonElement) {
        JsonObject json = GsonHelper.convertToJsonObject(jsonElement, "$");
        PoiTypeBuilder builder = new PoiTypeBuilder(id);

        builder.setBlockStates(GsonUtil.getAsResourceLocation(json, "block"));
        builder.maxTickets(GsonHelper.getAsInt(json, "max_tickets", 1));
        builder.validRange(GsonHelper.getAsInt(json, "valid_range", 1));

        return builder;
    }

    @Override
    public void postRegister(AddonBuilder<PoiType> addonBuilder) {
        if (Platform.isFabric()) {
            DeferredRegister.POI_TYPES_TO_FIX.add(new RegistrySupplier<>(addonBuilder.getId(), addonBuilder));
        }
    }
}
