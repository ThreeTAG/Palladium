package net.threetag.palladium.addonpack.builder;

import com.google.gson.JsonParseException;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;

public class PoiTypeBuilder extends AddonBuilder<PoiType> {

    private ResourceLocation blockId;
    private int maxTickets = 1;
    private int validRange = 1;

    public PoiTypeBuilder(ResourceLocation id) {
        super(id);
    }

    public PoiTypeBuilder setBlockStates(ResourceLocation blockId) {
        this.blockId = blockId;
        return this;
    }

    public PoiTypeBuilder maxTickets(int maxTickets) {
        this.maxTickets = maxTickets;
        return this;
    }

    public PoiTypeBuilder validRange(int validRange) {
        this.validRange = validRange;
        return this;
    }

    @Override
    protected PoiType create() {
        if (this.blockId == null) {
            throw new JsonParseException("No blockstates set for POI type");
        }

        if (!BuiltInRegistries.BLOCK.containsKey(this.blockId)) {
            throw new JsonParseException("Unknown block used in poi type " + this.getId() + ": " + this.blockId);
        }

        return new PoiType(PoiTypes.getBlockStates(BuiltInRegistries.BLOCK.get(this.blockId)), this.maxTickets, this.validRange);
    }
}
