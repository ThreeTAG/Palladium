package net.threetag.palladium.logic.value;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.power.ability.AbilityReference;

public class AbilityTickCountValue extends IntegerValue {

    public static final MapCodec<AbilityTickCountValue> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            AbilityReference.CODEC.fieldOf("ability").forGetter(v -> v.reference),
            modifyFunctionCodec()
    ).apply(instance, AbilityTickCountValue::new));

    private final AbilityReference reference;

    public AbilityTickCountValue(AbilityReference reference, String molang) {
        super(molang);
        this.reference = reference;
    }

    @Override
    public int getInteger(DataContext context) {
        if (context.getEntity() instanceof LivingEntity living) {
            var ability = this.reference.getInstance(living, context.getPowerInstance());

            if (ability != null) {
                return ability.getEnabledTicks();
            }
        }

        return 0;
    }

    @Override
    public ValueSerializer<?> getSerializer() {
        return ValueSerializers.ABILITY_TICK_COUNT.get();
    }

    public static class Serializer extends IntSerializer<AbilityTickCountValue> {

        @Override
        public MapCodec<AbilityTickCountValue> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Value, AbilityTickCountValue> builder, HolderLookup.Provider provider) {
            builder.setName("Ability Tick Count").setDescription("Returns the ticks the ability in the context has been enabled for.")
                    .add("ability", TYPE_ABILITY_REFERENCE, "The ability to get the ticks from. Defined in this syntax: &lt;power_namespace&gt;:&lt;power_name&gt;#&lt;ability_key&gt;")
                    .addExampleObject(new AbilityTickCountValue(AbilityReference.parse("namespace:example_power#ability_key"), ""));
        }
    }
}
