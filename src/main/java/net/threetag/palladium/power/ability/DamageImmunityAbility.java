package net.threetag.palladium.power.ability;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.power.energybar.EnergyBarUsage;
import net.threetag.palladium.util.MixedHolderSet;

import java.util.List;

public class DamageImmunityAbility extends Ability {

    public static final MapCodec<DamageImmunityAbility> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    MixedHolderSet.codec(Registries.DAMAGE_TYPE).fieldOf("damage_type").forGetter(ab -> ab.damageTypes),
                    propertiesCodec(), stateCodec(), energyBarUsagesCodec()
            ).apply(instance, DamageImmunityAbility::new));

    public final MixedHolderSet<DamageType> damageTypes;

    public DamageImmunityAbility(MixedHolderSet<DamageType> damageTypes, AbilityProperties properties, AbilityStateManager conditions, List<EnergyBarUsage> energyBarUsages) {
        super(properties, conditions, energyBarUsages);
        this.damageTypes = damageTypes;
    }

    public static boolean isImmuneAgainst(AbilityInstance<DamageImmunityAbility> ability, DamageSource source) {
        return ability.isEnabled() && ability.getAbility().damageTypes.contains(source.typeHolder());
    }

    @Override
    public AbilitySerializer<DamageImmunityAbility> getSerializer() {
        return AbilitySerializers.DAMAGE_IMMUNITY.get();
    }

    public static class Serializer extends AbilitySerializer<DamageImmunityAbility> {

        @Override
        public MapCodec<DamageImmunityAbility> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Ability, DamageImmunityAbility> builder, HolderLookup.Provider provider) {
            JsonObject example = new JsonObject();
            example.addProperty("type", AbilitySerializers.DAMAGE_IMMUNITY.getId().toString());
            example.add("damage_type", new JsonPrimitive("minecraft:cactus"));

            builder.setName("Damage Immunity")
                    .setDescription("Makes the entity immune against certain damage types.")
                    .add("damage_type", TYPE_DAMAGE_TYPE_HOLDER_SET, "The damage types the entity is immune against.")
                    .addExampleJson(example);
        }
    }
}
