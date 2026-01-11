package net.threetag.palladium.power.ability;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.client.renderer.entity.effect.EnergyBeamEffectRenderer;
import net.threetag.palladium.component.PalladiumDataComponents;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.power.energybar.EnergyBarUsage;
import net.threetag.palladium.util.EntityUtil;
import net.threetag.palladium.util.PalladiumCodecs;

import java.util.List;

public class BeamAbility extends Ability {

    public static final MapCodec<BeamAbility> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Identifier.CODEC.fieldOf("beam_renderer").forGetter(ab -> ab.beamId),
                    Codec.floatRange(0, Float.MAX_VALUE).optionalFieldOf("damage", 0F).forGetter(ab -> ab.damage),
                    Codec.floatRange(0, Float.MAX_VALUE).optionalFieldOf("max_distance", 30F).forGetter(ab -> ab.maxDistance),
                    PalladiumCodecs.TIME.optionalFieldOf("set_on_fire_ticks", 0).forGetter(ab -> ab.setOnFireSeconds),
                    Codec.BOOL.optionalFieldOf("cause_fire", false).forGetter(ab -> ab.causeFire),
                    Codec.BOOL.optionalFieldOf("smelt_blocks", false).forGetter(ab -> ab.smeltBlocks),
                    propertiesCodec(), stateCodec(), energyBarUsagesCodec()
            ).apply(instance, BeamAbility::new));

    public final Identifier beamId;
    public final float damage, maxDistance;
    public final int setOnFireSeconds;
    public final boolean causeFire, smeltBlocks;

    public BeamAbility(Identifier beamId, float damage, float maxDistance, int setOnFireSeconds, boolean causeFire, boolean smeltBlocks, AbilityProperties properties, AbilityStateManager state, List<EnergyBarUsage> energyBarUsages) {
        super(properties, state, energyBarUsages);
        this.beamId = beamId;
        this.damage = damage;
        this.maxDistance = maxDistance;
        this.setOnFireSeconds = setOnFireSeconds;
        this.causeFire = causeFire;
        this.smeltBlocks = smeltBlocks;
    }

    @Override
    public AbilitySerializer<BeamAbility> getSerializer() {
        return AbilitySerializers.BEAM.get();
    }

    @Override
    public void registerDataComponents(DataComponentMap.Builder components) {
        components.set(PalladiumDataComponents.Abilities.ENERGY_BEAM_TARGET.get(), null);
    }

    @Override
    public void firstTick(LivingEntity entity, AbilityInstance<?> instance) {
        var timer = instance.getAnimationTimer();
        if (entity instanceof Player player && (timer == null || timer.value() <= 0F) && entity.level().isClientSide()) {
            EnergyBeamEffectRenderer.start(player, instance.getReference());
        }
    }

    @Override
    public void tick(LivingEntity entity, AbilityInstance<?> instance, boolean enabled) {
        HitResult hit = null;
        var timer = instance.getAnimationTimerValueEased(1F);
        boolean hitTarget = timer >= 1F;

        if (hitTarget) {
            hit = updateTargetPos(entity, instance, 1F);
        }

        if (hitTarget) {
            if (hit instanceof EntityHitResult entityHitResult) {
                if (entity.level() instanceof ServerLevel serverLevel) {
                    if (this.setOnFireSeconds > 0) {
                        entityHitResult.getEntity().setRemainingFireTicks(this.setOnFireSeconds);
                    }

                    if (this.damage > 0) {
                        var damageSrc = entity instanceof Player player ? entity.level().damageSources().playerAttack(player) : entity.damageSources().mobAttack(entity);
                        entityHitResult.getEntity().hurtServer(serverLevel, damageSrc, this.damage);
                    }
                }

                Palladium.PROXY.spawnEnergyBeamParticles(entity.level(), hit.getLocation(), this.beamId);
            } else if (hit instanceof BlockHitResult blockHitResult) {
                BlockState blockState = entity.level().getBlockState(blockHitResult.getBlockPos());

                if (!blockState.isAir()) {
                    if (entity.level() instanceof ServerLevel level) {
                        if (this.smeltBlocks) {
                            SingleRecipeInput simpleContainer = new SingleRecipeInput(new ItemStack(blockState.getBlock()));
                            level.recipeAccess().getRecipeFor(RecipeType.SMELTING, simpleContainer, entity.level()).ifPresent(recipe -> {
                                ItemStack result = recipe.value().assemble(simpleContainer, entity.level().registryAccess());

                                if (!result.isEmpty() && Block.byItem(result.getItem()) != Blocks.AIR) {
                                    entity.level().setBlockAndUpdate(blockHitResult.getBlockPos(), Block.byItem(result.getItem()).defaultBlockState());
                                }
                            });

                            blockState = entity.level().getBlockState(blockHitResult.getBlockPos());
                        }

                        if (this.causeFire && blockState.isFlammable(level, blockHitResult.getBlockPos(), blockHitResult.getDirection())) {
                            BlockPos pos = blockHitResult.getBlockPos().offset(blockHitResult.getDirection().getUnitVec3i());
                            if (entity.level().isEmptyBlock(pos)) {
                                entity.level().setBlockAndUpdate(pos, Blocks.FIRE.defaultBlockState());
                            }
                        }
                    }

                    Palladium.PROXY.spawnEnergyBeamParticles(entity.level(), hit.getLocation(), this.beamId);
                }
            }
        }
    }

    public HitResult updateTargetPos(LivingEntity living, AbilityInstance<?> instance, float partialTick) {
        var start = living.getEyePosition(partialTick);
        var end = start.add(EntityUtil.getLookVector(living, partialTick).scale(this.maxDistance));
        HitResult endHit = EntityUtil.rayTraceWithEntities(living, start, end, start.distanceTo(end), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, en -> true);
        instance.setSilently(PalladiumDataComponents.Abilities.ENERGY_BEAM_TARGET.get(), endHit.getLocation());
        return endHit;
    }

    public float beamLengthMultiplier(AbilityInstance<?> instance, float partialTick) {
        return instance.getAnimationTimerValueEased(partialTick);
    }

    public static class Serializer extends AbilitySerializer<BeamAbility> {

        @Override
        public MapCodec<BeamAbility> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Ability, BeamAbility> builder, HolderLookup.Provider provider) {
            builder.setDescription("Shoots an beam in the direction the player is looking at.")
                    .add("beam_renderer", TYPE_RESOURCE_LOCATION, "The id of the beam renderer to use")
                    .addOptional("damage", TYPE_FLOAT, "The damage the beam deals to entities", 0F)
                    .addOptional("max_distance", TYPE_FLOAT, "The maximum distance the beam can travel", 30F)
                    .addOptional("set_on_fire_ticks", TYPE_INT, "The amount of ticks the hit entity is set on fire", 0)
                    .addOptional("cause_fire", TYPE_BOOLEAN, "If the beam should cause fire on blocks", false)
                    .addOptional("smelt_blocks", TYPE_BOOLEAN, "If the beam should smelt hit blocks", false)
                    .setExampleObject(new BeamAbility(
                            Identifier.fromNamespaceAndPath("example", "beam_renderer_id"),
                            5F,
                            25F,
                            20,
                            false,
                            false,
                            AbilityProperties.BASIC,
                            AbilityStateManager.EMPTY,
                            List.of()
                    ));
        }
    }
}
