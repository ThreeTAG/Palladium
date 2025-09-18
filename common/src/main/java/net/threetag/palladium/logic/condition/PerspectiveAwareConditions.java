package net.threetag.palladium.logic.condition;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.CameraType;
import net.threetag.palladium.logic.context.DataContext;

import java.util.Collections;
import java.util.List;

public class PerspectiveAwareConditions {

    public static final PerspectiveAwareConditions EMPTY = new PerspectiveAwareConditions(Collections.emptyList(), Collections.emptyList(), Collections.emptyList());

    private static final Codec<PerspectiveAwareConditions> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Condition.LIST_CODEC.fieldOf("main").forGetter(conditions -> conditions.conditions),
            Condition.LIST_CODEC.optionalFieldOf("third_person", Collections.emptyList()).forGetter(conditions -> conditions.thirdPersonConditions),
            Condition.LIST_CODEC.optionalFieldOf("first_person", Collections.emptyList()).forGetter(conditions -> conditions.firstPersonConditions)
    ).apply(instance, PerspectiveAwareConditions::new));

    public static final Codec<PerspectiveAwareConditions> CODEC = Codec.either(Condition.LIST_CODEC, DIRECT_CODEC).xmap(
            either -> either.map(
                    conditions -> new PerspectiveAwareConditions(conditions, Collections.emptyList(), Collections.emptyList()),
                    direct -> direct),
            conditions -> conditions.firstPersonConditions.isEmpty() && conditions.thirdPersonConditions.isEmpty() ? Either.left(conditions.conditions) : Either.right(conditions));

    protected final List<Condition> conditions;
    protected final List<Condition> thirdPersonConditions;
    protected final List<Condition> firstPersonConditions;

    public PerspectiveAwareConditions(List<Condition> conditions, List<Condition> thirdPersonConditions, List<Condition> firstPersonConditions) {
        this.conditions = conditions;
        this.thirdPersonConditions = thirdPersonConditions;
        this.firstPersonConditions = firstPersonConditions;
    }

    public boolean test(DataContext context, Perspective perspective) {
        if (!Condition.checkConditions(this.conditions, context)) {
            return false;
        }

        if (perspective == Perspective.FIRST_PERSON && !Condition.checkConditions(this.firstPersonConditions, context)) {
            return false;
        }

        return perspective != Perspective.THIRD_PERSON || Condition.checkConditions(this.thirdPersonConditions, context);
    }

    public enum Perspective {
        THIRD_PERSON,
        FIRST_PERSON;

        @Environment(EnvType.CLIENT)
        public static Perspective fromCameraType(CameraType cameraType) {
            return cameraType.isFirstPerson() ? FIRST_PERSON : THIRD_PERSON;
        }
    }

}
