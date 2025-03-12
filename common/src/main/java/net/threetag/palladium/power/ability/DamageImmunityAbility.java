package net.threetag.palladium.power.ability;

import com.google.gson.JsonPrimitive;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.documentation.SettingType;
import net.threetag.palladium.power.energybar.EnergyBarUsage;
import net.threetag.palladium.util.AdvancedHolderSet;

import java.util.List;

public class DamageImmunityAbility extends Ability {

    public static final MapCodec<DamageImmunityAbility> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    AdvancedHolderSet.codec(Registries.DAMAGE_TYPE).fieldOf("damage_types").forGetter(ab -> ab.damageTypes),
                    propertiesCodec(), stateCodec(), energyBarUsagesCodec()
            ).apply(instance, DamageImmunityAbility::new));

    public final AdvancedHolderSet<DamageType> damageTypes;

    public DamageImmunityAbility(AdvancedHolderSet<DamageType> damageTypes, AbilityProperties properties, AbilityStateManager conditions, List<EnergyBarUsage> energyBarUsages) {
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
            builder.setName("Damage Immunity")
                    .setDescription("Makes the entity immune against certain damage types.")
                    .add("damage_types", SettingType.listOrPrimitive(TYPE_DAMAGE_TYPE_ID_OR_TAG), "The damage types the entity is immune against.")
                    .addToExampleJson("type", new JsonPrimitive("palladium:damage_immunity"))
                    .addToExampleJson("damage_types", new JsonPrimitive("minecraft:cactus"));
        }
    }
}
