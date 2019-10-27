package net.threetag.threecore.util.modellayer.texture.variable;

import net.minecraft.entity.player.PlayerEntity;
import net.threetag.threecore.util.modellayer.IModelLayerContext;
import net.threetag.threecore.util.player.PlayerHelper;

public class SmallArmsTextureVariable implements ITextureVariable {

    private final String name;

    public SmallArmsTextureVariable(String name) {
        this.name = name;
    }

    @Override
    public Object get(IModelLayerContext context) {
        boolean smallArms = context.getAsEntity() instanceof PlayerEntity && PlayerHelper.hasSmallArms((PlayerEntity) context.getAsEntity());
        return name != null && !name.isEmpty() ? (smallArms ? name : "") : smallArms;
    }

}
