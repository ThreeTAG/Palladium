package net.threetag.palladium.client.util;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.CameraType;
import net.threetag.palladium.logic.condition.Condition;
import net.threetag.palladium.logic.condition.TrueCondition;
import net.threetag.palladium.logic.context.DataContext;

public record PerspectiveAwareConditions(Condition mainCondition, Condition thirdPersonCondition,
                                         Condition firstPersonCondition) {

    public static final PerspectiveAwareConditions EMPTY = new PerspectiveAwareConditions(TrueCondition.INSTANCE, TrueCondition.INSTANCE, TrueCondition.INSTANCE);

    private static final Codec<PerspectiveAwareConditions> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Condition.CODEC.fieldOf("main").forGetter(PerspectiveAwareConditions::mainCondition),
            Condition.CODEC.optionalFieldOf("third_person", TrueCondition.INSTANCE).forGetter(PerspectiveAwareConditions::thirdPersonCondition),
            Condition.CODEC.optionalFieldOf("first_person", TrueCondition.INSTANCE).forGetter(PerspectiveAwareConditions::firstPersonCondition)
    ).apply(instance, PerspectiveAwareConditions::new));

    public static final Codec<PerspectiveAwareConditions> CODEC = Codec.either(Condition.CODEC, DIRECT_CODEC).xmap(
            either -> either.map(
                    conditions -> new PerspectiveAwareConditions(conditions, TrueCondition.INSTANCE, TrueCondition.INSTANCE),
                    direct -> direct),
            conditions -> conditions.firstPersonCondition instanceof TrueCondition && conditions.thirdPersonCondition instanceof TrueCondition ? Either.left(conditions.mainCondition) : Either.right(conditions));

    public boolean test(DataContext context, Perspective perspective) {
        if (!this.mainCondition.test(context)) {
            return false;
        }

        if (perspective == Perspective.FIRST_PERSON && !this.firstPersonCondition.test(context)) {
            return false;
        }

        return perspective != Perspective.THIRD_PERSON || this.thirdPersonCondition.test(context);
    }

    public enum Perspective {
        THIRD_PERSON,
        FIRST_PERSON;

        public static Perspective fromCameraType(CameraType cameraType) {
            return cameraType.isFirstPerson() ? FIRST_PERSON : THIRD_PERSON;
        }
    }

}
