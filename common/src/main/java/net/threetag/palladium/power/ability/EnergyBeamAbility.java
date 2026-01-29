package net.threetag.palladium.power.ability;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.threetag.palladium.client.energybeam.EnergyBeamManager;
import net.threetag.palladium.entity.PalladiumDamageTypes;
import net.threetag.palladium.entity.effect.EnergyBeamEffect;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.util.EntityUtil;
import net.threetag.palladium.util.PalladiumBlockUtil;
import net.threetag.palladium.util.property.*;
import net.threetag.palladiumcore.util.Platform;

import java.util.Collections;
import java.util.Objects;

public class EnergyBeamAbility extends Ability implements AnimationTimer, CommandSource {

    public static final PalladiumProperty<ResourceLocation> BEAM = new ResourceLocationProperty("energy_beam").configurable("Configuration for the look of the beam. Check wiki for information.");
    public static final PalladiumProperty<ResourceLocation> DAMAGE_TYPE = new ResourceLocationProperty("damage_type").configurable("Damage type which is applied when an entity is hit. Uses normal player/mob attack if null/unchanged.");
    public static final PalladiumProperty<Float> DAMAGE = new FloatProperty("damage").configurable("The damage dealt with aiming for entities (per tick)");
    public static final PalladiumProperty<Float> MAX_DISTANCE = new FloatProperty("max_distance").configurable("The maximum distance you can reach with your heat vision");
    public static final PalladiumProperty<Float> SPEED = new FloatProperty("speed").configurable("Speed at which the energy beam extends from the player. Use 0 for instant extension.");
    public static final PalladiumProperty<Integer> SET_ON_FIRE_SECONDS = new IntegerProperty("set_on_fire_seconds").configurable("You can use this to set targeted entities on fire. If set to 0 it will not cause any.");
    public static final PalladiumProperty<Boolean> CAUSE_FIRE = new BooleanProperty("cause_fire").configurable("If enabled, targeted blocks will start to burn (fire will be placed).");
    public static final PalladiumProperty<Boolean> SMELT_BLOCKS = new BooleanProperty("smelt_blocks").configurable("If enabled, targeted blocks will turn into their smelting result (e.g. sand will turn into glass).");
    public static final PalladiumProperty<CommandFunctionProperty.CommandFunctionParsing> COMMANDS_ON_BLOCK_HIT = new CommandFunctionProperty("commands_on_block_hit").sync(SyncType.NONE).disablePersistence().configurable("Sets the commands which get executed when the beam hits an object");
    public static final PalladiumProperty<CommandFunctionProperty.CommandFunctionParsing> COMMANDS_ON_ENTITY_HIT = new CommandFunctionProperty("commands_on_entity_hit").sync(SyncType.NONE).disablePersistence().configurable("Sets the commands which get executed when the beam hits an object");

    public static final PalladiumProperty<Vec3> TARGET = new Vec3Property("distance").sync(SyncType.NONE);
    public static final PalladiumProperty<Float> VALUE = new FloatProperty("value").sync(SyncType.NONE).disablePersistence();
    public static final PalladiumProperty<Float> PREV_VALUE = new FloatProperty("prev_value").sync(SyncType.NONE).disablePersistence();

    public EnergyBeamAbility() {
        this.withProperty(BEAM, new ResourceLocation("example:energy_beam"))
                .withProperty(DAMAGE_TYPE, null)
                .withProperty(DAMAGE, 5F)
                .withProperty(MAX_DISTANCE, 30F)
                .withProperty(SPEED, 0.5F)
                .withProperty(SET_ON_FIRE_SECONDS, 0)
                .withProperty(CAUSE_FIRE, false)
                .withProperty(SMELT_BLOCKS, false)
                .withProperty(COMMANDS_ON_BLOCK_HIT, new CommandFunctionProperty.CommandFunctionParsing(Collections.emptyList()))
                .withProperty(COMMANDS_ON_ENTITY_HIT, new CommandFunctionProperty.CommandFunctionParsing(Collections.emptyList()));
        ;
    }

    @Override
    public void registerUniqueProperties(PropertyManager manager) {
        manager.register(TARGET, Vec3.ZERO);
        manager.register(VALUE, 0F);
        manager.register(PREV_VALUE, 0F);
    }

    @Override
    public void firstTick(LivingEntity entity, AbilityInstance entry, IPowerHolder holder, boolean enabled) {
        if (enabled && entity instanceof Player player && entry.getProperty(VALUE) <= 0F && Platform.isClient()) {
            EnergyBeamEffect.start(player, entry.getReference());
        }
    }

    @SuppressWarnings("ConstantValue")
    @Override
    public void tick(LivingEntity entity, AbilityInstance entry, IPowerHolder holder, boolean enabled) {
        float speed = entry.getProperty(SPEED);
        float value = entry.getProperty(VALUE);
        entry.setUniqueProperty(PREV_VALUE, value);
        HitResult hit = null;
        boolean active = enabled;

        if (speed > 0F) {
            hit = updateTargetPos(entity, entry, 1F);

            if (entry.isEnabled() && value < 1F) {
                entry.setUniqueProperty(VALUE, value = Math.min(value + speed, 1F));
            } else if (!entry.isEnabled() && value > 0F) {
                entry.setUniqueProperty(VALUE, value = Math.max(value - speed, 0F));
            }

            active = value >= 1F;
        }

        if (active) {
            if (hit instanceof EntityHitResult entityHitResult) {
                var fireSecs = entry.getProperty(SET_ON_FIRE_SECONDS);

                if (fireSecs > 0) {
                    entityHitResult.getEntity().setSecondsOnFire(fireSecs);
                }

                var dmg = entry.getProperty(DAMAGE);
                if (dmg > 0) {
                    var dmgSources = entity.level().damageSources();
                    var customType = entry.getProperty(DAMAGE_TYPE);
                    var damageSrc = dmgSources.source(customType != null ? ResourceKey.create(Registries.DAMAGE_TYPE, customType) : PalladiumDamageTypes.ENERGY_BEAM, entity, entity);
                    entityHitResult.getEntity().hurt(damageSrc, dmg);
                }

                if (Platform.isClient()) {
                    this.spawnParticles(entity.level(), hit.getLocation(), entry);
                }

                if (entity.level() instanceof ServerLevel serverLevel) {
                    var source = this.createCommandSourceStack(entity, serverLevel, hit.getLocation());
                    var function = entry.getProperty(COMMANDS_ON_ENTITY_HIT).getCommandFunction(entity.level().getServer());

                    if (function != null) {
                        Objects.requireNonNull(entity.level().getServer()).getFunctions().execute(function, source.withSuppressedOutput().withMaximumPermission(2));
                    }
                }
            } else if (hit instanceof BlockHitResult blockHitResult) {
                BlockState blockState = entity.level().getBlockState(blockHitResult.getBlockPos());

                if (!blockState.isAir()) {
                    if (entry.getProperty(SMELT_BLOCKS)) {
                        SimpleContainer simpleContainer = new SimpleContainer(new ItemStack(blockState.getBlock()));
                        entity.level().getRecipeManager().getRecipeFor(RecipeType.SMELTING, simpleContainer, entity.level()).ifPresent(recipe -> {
                            ItemStack result = recipe.assemble(simpleContainer, entity.level().registryAccess());

                            if (!result.isEmpty() && Block.byItem(result.getItem()) != Blocks.AIR) {
                                entity.level().setBlockAndUpdate(blockHitResult.getBlockPos(), Block.byItem(result.getItem()).defaultBlockState());
                            }
                        });

                        blockState = entity.level().getBlockState(blockHitResult.getBlockPos());
                    }

                    if (entry.getProperty(CAUSE_FIRE) && PalladiumBlockUtil.canBurn(blockState, entity.level(), blockHitResult.getBlockPos(), blockHitResult.getDirection())) {
                        BlockPos pos = blockHitResult.getBlockPos().offset(blockHitResult.getDirection().getNormal());
                        if (entity.level().isEmptyBlock(pos)) {
                            entity.level().setBlockAndUpdate(pos, Blocks.FIRE.defaultBlockState());
                        }
                    }

                    if (Platform.isClient()) {
                        this.spawnParticles(entity.level(), hit.getLocation(), entry);
                    }

                    if (entity.level() instanceof ServerLevel serverLevel) {
                        var source = this.createCommandSourceStack(entity, serverLevel, hit.getLocation());
                        var function = entry.getProperty(COMMANDS_ON_BLOCK_HIT).getCommandFunction(entity.level().getServer());

                        if (function != null) {
                            Objects.requireNonNull(entity.level().getServer()).getFunctions().execute(function, source.withSuppressedOutput().withMaximumPermission(2));
                        }
                    }
                }
            }
        }
    }

    public CommandSourceStack createCommandSourceStack(LivingEntity entity, ServerLevel serverLevel, Vec3 position) {
        return new CommandSourceStack(this, position, entity.getRotationVector(),
                serverLevel, 2, entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity)
                .withSuppressedOutput();
    }

    @Environment(EnvType.CLIENT)
    public void spawnParticles(Level level, Vec3 pos, AbilityInstance entry) {
        var beam = EnergyBeamManager.INSTANCE.get(entry.getProperty(BEAM));

        if (beam != null) {
            beam.spawnParticles(level, pos);
        }
    }

    public static HitResult updateTargetPos(LivingEntity living, AbilityInstance entry, float partialTick) {
        try {
            var start = living.getEyePosition(partialTick);
            var end = start.add(EntityUtil.getLookVector(living, partialTick).scale(entry.getProperty(MAX_DISTANCE)));
            HitResult endHit = EntityUtil.rayTraceWithEntities(living, start, end, start.distanceTo(end), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, en -> true);
            entry.setUniqueProperty(TARGET, endHit.getLocation());
            return endHit;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public float getAnimationValue(AbilityInstance entry, float partialTick) {
        return Mth.lerp(partialTick, entry.getProperty(PREV_VALUE), entry.getProperty(VALUE));
    }

    @Override
    public float getAnimationTimer(AbilityInstance entry, float partialTick, boolean maxedOut) {
        if (maxedOut) {
            return 1F;
        }
        return Mth.lerp(partialTick, entry.getProperty(PREV_VALUE), entry.getProperty(VALUE));
    }

    @Override
    public void sendSystemMessage(Component component) {

    }

    @Override
    public boolean acceptsSuccess() {
        return false;
    }

    @Override
    public boolean acceptsFailure() {
        return false;
    }

    @Override
    public boolean shouldInformAdmins() {
        return false;
    }
}
