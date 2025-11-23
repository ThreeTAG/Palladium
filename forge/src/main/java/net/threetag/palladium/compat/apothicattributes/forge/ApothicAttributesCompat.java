package net.threetag.palladium.compat.apothicattributes.forge;

import dev.shadowsoffire.attributeslib.util.IEntityOwned;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
import net.threetag.palladium.entity.DualWieldingPlayerHandler;

public class ApothicAttributesCompat {

    @SuppressWarnings("unchecked")
    public static void init() {
        DualWieldingPlayerHandler.ATTRIBUTE_MAP_FACTORY = player -> {
            var map = new AttributeMap(DefaultAttributes.getSupplier((EntityType<? extends LivingEntity>) player.getType()));
            ((IEntityOwned) map).setOwner(player);
            return map;
        };
    }

}
