package net.threetag.threecore.client.renderer.entity.modellayer.texture.variable;

import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.threetag.threecore.ability.AbilityHelper;
import net.threetag.threecore.ability.container.DefaultAbilityContainer;
import net.threetag.threecore.ability.container.IAbilityContainer;
import net.threetag.threecore.client.renderer.entity.modellayer.IModelLayerContext;

import java.util.List;

public class AbilityContainerTimeTextureVariable extends AbstractIntegerTextureVariable {

    private final ResourceLocation abilityContainer;

    public AbilityContainerTimeTextureVariable(List<Pair<Operation, Integer>> operations, ResourceLocation abilityContainer) {
        super(operations);
        this.abilityContainer = abilityContainer;
    }

    public AbilityContainerTimeTextureVariable(JsonObject json) {
        super(json);
        this.abilityContainer = new ResourceLocation(JSONUtils.getString(json, "ability_container"));
    }

    @Override
    public int getNumber(IModelLayerContext context) {
        if (context.getAsEntity() instanceof LivingEntity) {
            IAbilityContainer container = AbilityHelper.getAbilityContainerFromId((LivingEntity) context.getAsEntity(), this.abilityContainer);

            if (container instanceof DefaultAbilityContainer) {
                return ((DefaultAbilityContainer) container).getMaxLifetime() - ((DefaultAbilityContainer) container).getLifetime();
            }
        }
        return 0;
    }
}
