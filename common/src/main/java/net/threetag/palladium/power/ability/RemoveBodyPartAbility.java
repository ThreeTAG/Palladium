package net.threetag.palladium.power.ability;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.documentation.SettingType;
import net.threetag.palladium.entity.BodyPart;
import net.threetag.palladium.power.energybar.EnergyBarUsage;
import net.threetag.palladium.util.CodecExtras;

import java.util.List;

public class RemoveBodyPartAbility extends Ability {

    // TODO

    public static final MapCodec<RemoveBodyPartAbility> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    CodecExtras.listOrPrimitive(BodyPart.CODEC).fieldOf("body_parts").forGetter(ab -> ab.bodyParts),
                    Codec.BOOL.optionalFieldOf("affects_first_person", true).forGetter(ab -> ab.affectsFirstPerson),
                    propertiesCodec(), stateCodec(), energyBarUsagesCodec()
            ).apply(instance, RemoveBodyPartAbility::new));

    public final List<BodyPart> bodyParts;
    public final boolean affectsFirstPerson;

    public RemoveBodyPartAbility(List<BodyPart> bodyParts, boolean affectsFirstPerson, AbilityProperties properties, AbilityStateManager conditions, List<EnergyBarUsage> energyBarUsages) {
        super(properties, conditions, energyBarUsages);
        this.bodyParts = bodyParts;
        this.affectsFirstPerson = affectsFirstPerson;
    }

    @Override
    public AbilitySerializer<RemoveBodyPartAbility> getSerializer() {
        return AbilitySerializers.REMOVE_BODY_PART.get();
    }

    public static class Serializer extends AbilitySerializer<RemoveBodyPartAbility> {

        @Override
        public MapCodec<RemoveBodyPartAbility> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Ability, RemoveBodyPartAbility> builder, HolderLookup.Provider provider) {
            builder.setDescription("Removes the specified body parts of the entity.")
                    .add("body_parts", SettingType.enumList(BodyPart.values()), "The body parts to remove.")
                    .addOptional("affects_first_person", TYPE_BOOLEAN, "Determines if the first person arm should disappear as well (if it's disabled).")
                    .setExampleObject(new RemoveBodyPartAbility(List.of(BodyPart.HEAD, BodyPart.CHEST), false, AbilityProperties.BASIC, AbilityStateManager.EMPTY, List.of()));
        }
    }
}
