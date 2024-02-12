package net.threetag.palladium.addonpack.builder;

import com.google.gson.JsonParseException;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;

public class PoiTypeBuilder extends AddonBuilder<PoiType, PoiTypeBuilder> {

    private ResourceLocation blockId;
    private Integer maxTickets;
    private Integer validRange;

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
        ResourceLocation blockId = this.getValue(b -> b.blockId);

        if (blockId == null) {
            throw new JsonParseException("No blockstates set for POI type");
        }

        if (!BuiltInRegistries.BLOCK.containsKey(blockId)) {
            throw new JsonParseException("Unknown block used in poi type " + this.getId() + ": " + blockId);
        }

        return new PoiType(PoiTypes.getBlockStates(BuiltInRegistries.BLOCK.get(blockId)), this.getValue(b -> b.maxTickets, 1), this.getValue(b -> b.validRange, 1));
    }
}
