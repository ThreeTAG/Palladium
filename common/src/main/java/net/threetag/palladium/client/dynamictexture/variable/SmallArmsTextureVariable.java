package net.threetag.palladium.client.dynamictexture.variable;

import com.google.gson.JsonObject;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.threetag.palladium.util.PlayerUtil;

public class SmallArmsTextureVariable extends AbstractBooleanTextureVariable {

    public SmallArmsTextureVariable(JsonObject json) {
        super(json);
    }

    @Override
    public boolean getBoolean(LivingEntity entity) {
        return entity instanceof Player player && PlayerUtil.hasSmallArms(player);
    }

}
