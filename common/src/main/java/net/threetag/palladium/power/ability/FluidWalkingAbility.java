package net.threetag.palladium.power.ability;

import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.threetag.palladium.util.property.FluidTagProperty;
import net.threetag.palladium.util.property.PalladiumProperty;

public class FluidWalkingAbility extends Ability {

    public static final PalladiumProperty<TagKey<Fluid>> FLUID_TAG = new FluidTagProperty("fluid_tag").configurable("Determines the tag for the fluid(s) you can walk on. You need tags because of each fluid there are 2 fluids actually: A still and a flowing one. Minecraft's two fluid tags are: minecraft:water & minecraft:lava");

    public FluidWalkingAbility() {
        this.withProperty(FLUID_TAG, FluidTags.LAVA);
    }

    @Override
    public String getDocumentationDescription() {
        return "Let's you define a fluid you can walk on.";
    }

    public static boolean canWalkOn(LivingEntity entity, FluidState fluid) {
        if (fluid.is(FluidTags.WATER) && AbilityUtil.isTypeEnabled(entity, Abilities.WATER_WALK.get())) {
            return true;
        } else {
            return AbilityUtil.getEnabledEntries(entity, Abilities.FLUID_WALKING.get()).stream().anyMatch(e -> fluid.is(e.getProperty(FLUID_TAG)));
        }
    }
}
