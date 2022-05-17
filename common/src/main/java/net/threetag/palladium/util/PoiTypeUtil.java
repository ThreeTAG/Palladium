package net.threetag.palladium.util;

import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.Block;

public class PoiTypeUtil {

    public static PoiType fromBlock(String name, Block block, int maxTickets, int validRange) {
        return PoiType.registerBlockStates(new PoiType(name, PoiType.getBlockStates(block), maxTickets, validRange));
    }

    public static PoiType fromBlock(String name, Block block) {
        return fromBlock(name, block, 1, 1);
    }

}
