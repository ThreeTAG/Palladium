package net.threetag.palladium.logic.value;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.power.ability.AbilityReference;

public class AbilityAnimationTimerValue extends IntegerValue {

    public static final MapCodec<AbilityAnimationTimerValue> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            AbilityReference.CODEC.fieldOf("ability").forGetter(v -> v.reference),
            modifyFunctionCodec()
    ).apply(instance, AbilityAnimationTimerValue::new));

    private final AbilityReference reference;

    public AbilityAnimationTimerValue(AbilityReference reference, String molang) {
        super(molang);
        this.reference = reference;
    }

    @Override
    public int getInteger(DataContext context) {
        if (context.getEntity() instanceof LivingEntity living) {
            var ability = this.reference.getInstance(living, context.getPowerInstance());

            if (ability != null && ability.getAnimationTimer() != null) {
                return ability.getAnimationTimer().value();
            }
        }

        return 0;
    }

    @Override
    public ValueSerializer<?> getSerializer() {
        return ValueSerializers.ABILITY_ANIMATION_TIMER_VALUE.get();
    }

    public static class Serializer extends IntSerializer<AbilityAnimationTimerValue> {

        @Override
        public MapCodec<AbilityAnimationTimerValue> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Value, AbilityAnimationTimerValue> builder, HolderLookup.Provider provider) {
            builder.setName("Ability Animation Timer Value").setDescription("Returns the value of the animation timer for the specified ability. Defaults to 0 if no ability or animation timer was found.")
                    .add("ability", TYPE_ABILITY_REFERENCE, "The ability to get the animation timer for. Defined in this syntax: <power_id>#<ability_key>")
                    .addExampleObject(new AbilityAnimationTimerValue(AbilityReference.parse("namespace:example_power#ability_key"), ""));
        }
    }
}
