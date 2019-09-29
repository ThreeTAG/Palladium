package net.threetag.threecore.util.loot.function;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootFunction;
import net.minecraft.world.storage.loot.LootParameter;
import net.minecraft.world.storage.loot.conditions.ILootCondition;
import net.minecraft.world.storage.loot.functions.CopyName;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.threetag.threecore.ThreeCore;

import java.util.Set;

public class CopyEnergyFunction extends LootFunction {

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

    public static LootFunction.Builder<?> builder(CopyName.Source sourceIn) {
        return builder((conditions) -> {
            return new CopyEnergyFunction(conditions, sourceIn);
        });
    }

    public static class Serializer extends LootFunction.Serializer<CopyEnergyFunction> {
        public Serializer() {
            super(new ResourceLocation(ThreeCore.MODID, "copy_energy"), CopyEnergyFunction.class);
        }

        public void serialize(JsonObject object, CopyEnergyFunction functionClazz, JsonSerializationContext serializationContext) {
            super.serialize(object, functionClazz, serializationContext);
            object.addProperty("source", functionClazz.source.name);
        }

        public CopyEnergyFunction deserialize(JsonObject object, JsonDeserializationContext deserializationContext, ILootCondition[] conditionsIn) {
            CopyName.Source copyname$source = CopyName.Source.byName(JSONUtils.getString(object, "source"));
            return new CopyEnergyFunction(conditionsIn, copyname$source);
        }
    }
}
