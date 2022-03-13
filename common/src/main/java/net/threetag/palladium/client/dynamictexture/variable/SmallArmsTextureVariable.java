package net.threetag.palladium.client.dynamictexture.variable;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.threetag.palladium.util.PlayerUtil;

public record SmallArmsTextureVariable(String normalName,
                                       String smallArmsName) implements ITextureVariable {

    @Override
    public Object get(LivingEntity entity) {
        boolean smallArms = entity instanceof Player player && PlayerUtil.hasSmallArms(player);
        if (smallArms)
            return smallArmsName != null ? smallArmsName : true;
        else
            return normalName != null ? normalName : true;
    }

}
