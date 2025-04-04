package net.threetag.palladium.client.variable;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.data.DataContext;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.power.ability.AbilityReference;

public class AnimationTimerVariable extends IntegerPathVariable {

    public static final MapCodec<AnimationTimerVariable> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            AbilityReference.CODEC.fieldOf("ability").forGetter(v -> v.reference),
            modifyFunctionCodec()
    ).apply(instance, AnimationTimerVariable::new));

    private final AbilityReference reference;

    public AnimationTimerVariable(AbilityReference reference, String molang) {
        super(molang);
        this.reference = reference;
    }

    @Override
    public int getInteger(DataContext context) {
        if (context.getEntity() instanceof LivingEntity living) {
            var ability = this.reference.getInstance(living, context.getPowerHolder());

            if (ability != null && ability.getAnimationTimer() != null) {
                return ability.getAnimationTimer().value();
            }
        }

        return 0;
    }

    @Override
    public PathVariableSerializer<?> getSerializer() {
        return PathVariableSerializers.ANIMATION_TIMER;
    }

    public static class Serializer extends IntSerializer<AnimationTimerVariable> {

        @Override
        public MapCodec<AnimationTimerVariable> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<PathVariable, AnimationTimerVariable> builder, HolderLookup.Provider provider) {
            builder.setName("Animation Timer").setDescription("Returns the value of the animation timer for the specified ability. Defaults to 0 if no ability or animation timer was found.")
                    .add("ability", TYPE_ABILITY_REFERENCE, "The ability to get the animation timer for. Defined in this syntax: <power_id>#<ability_key>")
                    .setExampleObject(new AnimationTimerVariable(AbilityReference.parse("namespace:example_power#ability_key"), ""));
        }
    }
}
