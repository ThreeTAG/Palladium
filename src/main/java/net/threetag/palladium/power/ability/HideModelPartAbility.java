package net.threetag.palladium.power.ability;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.documentation.SettingType;
import net.threetag.palladium.power.energybar.EnergyBarUsage;
import net.threetag.palladium.util.PalladiumCodecs;

import java.util.List;

public class HideModelPartAbility extends Ability {

    public static final MapCodec<HideModelPartAbility> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    PalladiumCodecs.listOrPrimitive(Codec.STRING).fieldOf("body_parts").forGetter(ab -> ab.modelParts),
                    Codec.BOOL.optionalFieldOf("affects_first_person", true).forGetter(ab -> ab.affectsFirstPerson),
                    propertiesCodec(), stateCodec(), energyBarUsagesCodec()
            ).apply(instance, HideModelPartAbility::new));

    public final List<String> modelParts;
    public final boolean affectsFirstPerson;

    public HideModelPartAbility(List<String> modelParts, boolean affectsFirstPerson, AbilityProperties properties, AbilityStateManager conditions, List<EnergyBarUsage> energyBarUsages) {
        super(properties, conditions, energyBarUsages);
        this.modelParts = modelParts;
        this.affectsFirstPerson = affectsFirstPerson;
    }

    @Override
    public AbilitySerializer<HideModelPartAbility> getSerializer() {
        return AbilitySerializers.HIDE_MODEL_PART.get();
    }

    public static class Serializer extends AbilitySerializer<HideModelPartAbility> {

        @Override
        public MapCodec<HideModelPartAbility> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Ability, HideModelPartAbility> builder, HolderLookup.Provider provider) {
            builder.setDescription("Hides the specified body parts of the entity.")
                    .add("body_parts", SettingType.listOrPrimitive(TYPE_STRING), "The body parts to hide.")
                    .addOptional("affects_first_person", TYPE_BOOLEAN, "Determines if the first person arm should disappear as well (if it's disabled).")
                    .addExampleObject(new HideModelPartAbility(List.of("head", "body"), false, AbilityProperties.BASIC, AbilityStateManager.EMPTY, List.of()));
        }
    }
}
