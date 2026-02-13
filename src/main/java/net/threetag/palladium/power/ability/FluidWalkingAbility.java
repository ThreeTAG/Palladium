package net.threetag.palladium.power.ability;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.documentation.SettingType;
import net.threetag.palladium.power.energybar.EnergyBarUsage;
import net.threetag.palladium.util.PalladiumCodecs;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FluidWalkingAbility extends Ability {

    public static final MapCodec<FluidWalkingAbility> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    PalladiumCodecs.listOrPrimitive(TagKey.hashedCodec(Registries.FLUID)).optionalFieldOf("fluid_tag", Collections.singletonList(FluidTags.WATER)).forGetter(ab -> ab.fluidTags),
                    propertiesCodec(), stateCodec(), energyBarUsagesCodec()
            ).apply(instance, FluidWalkingAbility::new));

    public final List<TagKey<Fluid>> fluidTags;

    public FluidWalkingAbility(List<TagKey<Fluid>> fluidTags, AbilityProperties properties, AbilityStateManager conditions, List<EnergyBarUsage> energyBarUsages) {
        super(properties, conditions, energyBarUsages);
        this.fluidTags = fluidTags;
    }

    public static boolean canWalkOn(LivingEntity entity, FluidState fluid) {
        return AbilityUtil.getEnabledInstances(entity, AbilitySerializers.FLUID_WALKING.get())
                .stream()
                .anyMatch(e -> e.getAbility().fluidTags.stream().anyMatch(fluid::is));
    }

    @Override
    public AbilitySerializer<FluidWalkingAbility> getSerializer() {
        return AbilitySerializers.FLUID_WALKING.get();
    }

    public static class Serializer extends AbilitySerializer<FluidWalkingAbility> {

        @Override
        public MapCodec<FluidWalkingAbility> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Ability, FluidWalkingAbility> builder, HolderLookup.Provider provider) {
            builder.setDescription("Allows the entity to walk on a specific fluid.")
                    .addOptional("fluid_tag", SettingType.listOrPrimitive(TYPE_FLUID_TAG), "The fluid tag(s) the entity can walk on.", FluidTags.WATER.location().toString())
                    .addExampleObject(new FluidWalkingAbility(Arrays.asList(FluidTags.WATER, FluidTags.LAVA), AbilityProperties.BASIC, AbilityStateManager.EMPTY, List.of()));
        }
    }
}
