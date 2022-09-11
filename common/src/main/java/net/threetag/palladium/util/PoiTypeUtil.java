package net.threetag.palladium.util;

import dev.architectury.platform.Platform;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.level.block.Block;

public class PoiTypeUtil {

    public static PoiType fromBlock(Block block, int maxTickets, int validRange) {
        var states = PoiTypes.getBlockStates(block);
        PoiType type = new PoiType(states, maxTickets, validRange);
        PoiTypes.registerBlockStates(Holder.direct(type));

        if(Platform.isFabric()) {
            PoiTypes.ALL_STATES.addAll(states);
        }

        return type;
    }

    public static PoiType fromBlock(Block block) {
        return fromBlock(block, 1, 1);
    }

}
