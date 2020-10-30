package net.threetag.threecore.loot.function;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootFunction;
import net.minecraft.loot.LootFunctionType;
import net.minecraft.loot.LootParameter;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.functions.CopyName;
import net.minecraft.util.JSONUtils;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;

import java.util.Set;

public class CopyEnergyFunction extends LootFunction
{

    private final CopyName.Source source;

    protected CopyEnergyFunction(ILootCondition[] conditionsIn, CopyName.Source sourceIn) {
        super(conditionsIn);
        this.source = sourceIn;
    }

    @Override
    public Set<LootParameter<?>> getRequiredParameters() {
        return ImmutableSet.of(this.source.parameter);
    }

    @Override
    protected ItemStack doApply(ItemStack stack, LootContext context) {
        Object object = context.get(this.source.parameter);
        if (object instanceof CapabilityProvider) {
            CapabilityProvider provider = (CapabilityProvider) object;
            provider.getCapability(CapabilityEnergy.ENERGY).ifPresent(energyStorage -> {
                stack.getOrCreateTag().putInt("Energy", energyStorage.getEnergyStored());
            });
        }

        return stack;
    }

    public static Builder<?> builder(CopyName.Source sourceIn) {
        return builder((conditions) -> {
            return new CopyEnergyFunction(conditions, sourceIn);
        });
    }

    @Override public LootFunctionType getFunctionType()
    {
        return TCLootFunctions.COPY_ENERGY;
    }

    public static class Serializer extends LootFunction.Serializer<CopyEnergyFunction> {

        @Override public void serialize(JsonObject object, CopyEnergyFunction function, JsonSerializationContext serializationContext)
        {
            super.serialize(object, function, serializationContext);
            object.addProperty("source", function.source.name);
        }

        public CopyEnergyFunction deserialize(JsonObject object, JsonDeserializationContext deserializationContext, ILootCondition[] conditionsIn) {
            CopyName.Source copyname$source = CopyName.Source.byName(JSONUtils.getString(object, "source"));
            return new CopyEnergyFunction(conditionsIn, copyname$source);
        }
    }
}
