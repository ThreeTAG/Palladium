package net.threetag.palladium.util;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.level.block.Block;

public class PoiTypeUtil {

    public static PoiType fromBlock(String name, Block block, int maxTickets, int validRange) {
        PoiType type = new PoiType(PoiTypes.getBlockStates(block), maxTickets, validRange);
        PoiTypes.registerBlockStates(Holder.direct(type));
        return type;
    }

    public static PoiType fromBlock(String name, Block block) {
        return fromBlock(name, block, 1, 1);
    }

}
