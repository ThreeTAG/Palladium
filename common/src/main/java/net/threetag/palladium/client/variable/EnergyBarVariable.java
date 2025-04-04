package net.threetag.palladium.client.variable;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.data.DataContext;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.power.energybar.EnergyBarReference;

public class EnergyBarVariable extends IntegerPathVariable {

    public static final MapCodec<EnergyBarVariable> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            EnergyBarReference.CODEC.fieldOf("energy_bar").forGetter(v -> v.reference),
            modifyFunctionCodec()
    ).apply(instance, EnergyBarVariable::new));

    private final EnergyBarReference reference;

    public EnergyBarVariable(EnergyBarReference reference, String molang) {
        super(molang);
        this.reference = reference;
    }

    @Override
    public int getInteger(DataContext context) {
        if (context.getEntity() instanceof LivingEntity living) {
            var bar = this.reference.getBar(living, context.getPowerHolder());

            if (bar != null) {
                return bar.get();
            }
        }

        return 0;
    }

    @Override
    public PathVariableSerializer<?> getSerializer() {
        return PathVariableSerializers.ENERGY_BAR;
    }

    public static class Serializer extends IntSerializer<EnergyBarVariable> {

        @Override
        public MapCodec<EnergyBarVariable> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<PathVariable, EnergyBarVariable> builder, HolderLookup.Provider provider) {
            builder.setName("Energy Bar").setDescription("Returns of the current value of a specific energy bar.")
                    .add("energy_bar", TYPE_ABILITY_REFERENCE, "The energy bar to look for. Defined in this syntax: <power_id>#<energy_bar_key>")
                    .setExampleObject(new EnergyBarVariable(EnergyBarReference.parse("namespace:example_power#energy_bar_key"), ""));
        }
    }
}
