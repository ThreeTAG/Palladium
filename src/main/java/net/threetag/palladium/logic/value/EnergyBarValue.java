package net.threetag.palladium.logic.value;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.power.energybar.EnergyBarReference;

public class EnergyBarValue extends IntegerValue {

    public static final MapCodec<EnergyBarValue> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            EnergyBarReference.CODEC.fieldOf("energy_bar").forGetter(v -> v.reference),
            modifyFunctionCodec()
    ).apply(instance, EnergyBarValue::new));

    private final EnergyBarReference reference;

    public EnergyBarValue(EnergyBarReference reference, String molang) {
        super(molang);
        this.reference = reference;
    }

    @Override
    public int getInteger(DataContext context) {
        if (context.getEntity() instanceof LivingEntity living) {
            var bar = this.reference.getBar(living, context.getPowerInstance());

            if (bar != null) {
                return bar.get();
            }
        }

        return 0;
    }

    @Override
    public ValueSerializer<?> getSerializer() {
        return ValueSerializers.ENERGY_BAR.get();
    }

    public static class Serializer extends IntSerializer<EnergyBarValue> {

        @Override
        public MapCodec<EnergyBarValue> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Value, EnergyBarValue> builder, HolderLookup.Provider provider) {
            builder.setName("Energy Bar").setDescription("Returns of the current value of a specific energy bar.")
                    .add("energy_bar", TYPE_ABILITY_REFERENCE, "The energy bar to look for. Defined in this syntax: &lt;power_namespace&gt;:&lt;power_name&gt;#&lt;energy_bar_key&gt;")
                    .addExampleObject(new EnergyBarValue(EnergyBarReference.parse("namespace:example_power#energy_bar_key"), ""));
        }
    }
}
