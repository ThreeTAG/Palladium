package net.threetag.threecore.util.modellayer.texture.variable;

import net.minecraft.entity.player.PlayerEntity;
import net.threetag.threecore.util.modellayer.IModelLayerContext;
import net.threetag.threecore.util.player.PlayerHelper;

public class SmallArmsTextureVariable implements ITextureVariable {

    private final String normalName;
    private final String smallArmsName;

    public SmallArmsTextureVariable(String normalName, String smallArmsName) {
        this.normalName = normalName;
        this.smallArmsName = smallArmsName;
    }

    @Override
    public Object get(IModelLayerContext context) {
        boolean smallArms = context.getAsEntity() instanceof PlayerEntity && PlayerHelper.hasSmallArms((PlayerEntity) context.getAsEntity());
        if (smallArms)
            return smallArmsName != null ? smallArmsName : true;
        else
            return normalName != null ? normalName : true;
    }

}
