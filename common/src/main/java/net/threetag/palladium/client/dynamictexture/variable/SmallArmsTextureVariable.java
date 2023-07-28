package net.threetag.palladium.client.dynamictexture.variable;

import com.google.gson.JsonObject;
import net.minecraft.world.entity.player.Player;
import net.threetag.palladium.util.PlayerUtil;
import net.threetag.palladium.util.context.DataContext;

public class SmallArmsTextureVariable extends AbstractBooleanTextureVariable {

    public SmallArmsTextureVariable(JsonObject json) {
        super(json);
    }

    @Override
    public boolean getBoolean(DataContext context) {
        return context.getEntity() instanceof Player player && PlayerUtil.hasSmallArms(player);
    }

}
