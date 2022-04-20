package net.threetag.palladium.power.ability;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.threetag.palladium.client.renderer.PalladiumRenderTypes;
import net.threetag.palladium.client.renderer.renderlayer.AbilityEffectsRenderLayer;
import net.threetag.palladium.entity.effect.EnergyBlastEffect;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.sound.EnergyBlastSound;
import net.threetag.palladium.sound.PalladiumSoundEvents;
import net.threetag.palladium.util.EntityUtil;
import net.threetag.palladium.util.property.*;

import java.awt.*;
import java.util.Random;

public class EnergyBlastAbility extends Ability {

    public static final PalladiumProperty<EnergyBlastOriginProperty.EnergyBlastOrigin> ORIGIN = new EnergyBlastOriginProperty("origin").configurable("Defines the origin point of the energy blast");
    public static final PalladiumProperty<Color> COLOR = new ColorProperty("color").configurable("Defines the color of the blast");
    public static final PalladiumProperty<Float> DAMAGE = new FloatProperty("damage").configurable("The damage dealt with aiming for entities (per tick)");
    public static final PalladiumProperty<Float> MAX_DISTANCE = new FloatProperty("max_distance").configurable("The maximum distance you can reach with your heat vision");
    public static final PalladiumProperty<SoundEvent> SOUND_EVENT = new SoundEventProperty("sound_event").configurable("The sound you want to have played. Can be null");

    public static final PalladiumProperty<Integer> ANIMATION_TIMER = new IntegerProperty("animation_timer").sync(SyncType.NONE);
    public static final PalladiumProperty<Double> DISTANCE = new DoubleProperty("distance").sync(SyncType.NONE);

    public EnergyBlastAbility() {
        this.withProperty(ORIGIN, EnergyBlastOriginProperty.EnergyBlastOrigin.EYES);
        this.withProperty(COLOR, Color.RED);
        this.withProperty(DAMAGE, 1F);
        this.withProperty(MAX_DISTANCE, 30F);
        this.withProperty(SOUND_EVENT, PalladiumSoundEvents.HEAT_VISION.get());
    }

    @Override
    public void registerUniqueProperties(PropertyManager manager) {
        manager.register(ANIMATION_TIMER, 0);
        manager.register(DISTANCE, 0D);
    }

    @Override
    public void firstTick(LivingEntity entity, AbilityEntry entry, IPowerHolder holder, boolean enabled) {
        EnergyBlastEffect.start(entity, holder.getPower(), entry);
        if(Platform.getEnvironment() == Env.CLIENT && entry.getProperty(SOUND_EVENT) != null) {
            Minecraft.getInstance().getSoundManager().play(new EnergyBlastSound(entry.getProperty(SOUND_EVENT), entity.getSoundSource(), entity, holder.getPower().getId(), entry.id));
        }
    }

    @Override
    public void tick(LivingEntity entity, AbilityEntry entry, IPowerHolder holder, boolean enabled) {
        if (enabled) {
            if (entity.level.isClientSide && entry.getProperty(ANIMATION_TIMER) < 5) {
                entry.setOwnProperty(ANIMATION_TIMER, entry.getProperty(ANIMATION_TIMER) + 1);
            }

            EnergyBlastOriginProperty.EnergyBlastOrigin origin = entry.getProperty(ORIGIN);

            if(origin == EnergyBlastOriginProperty.EnergyBlastOrigin.CHEST) {
                entity.yBodyRotO = entity.yHeadRotO;
                entity.yBodyRot = entity.yHeadRot;
            }

            Vec3 startVec = origin.getOriginVector(entity);
            Vec3 endVec = origin.getEndVector(entity, startVec, entry.getProperty(MAX_DISTANCE));
            HitResult hitResult = EntityUtil.rayTraceWithEntities(entity, startVec, endVec, entry.getProperty(MAX_DISTANCE), ClipContext.Block.COLLIDER, ClipContext.Fluid.ANY, target -> !target.isInvulnerable());
            entry.setOwnProperty(DISTANCE, hitResult.getLocation().distanceTo(startVec));

            if (entry.getEnabledTicks() >= 5) {
                if (!entity.level.isClientSide()) {
                    if (hitResult.getType() == HitResult.Type.ENTITY) {
                        EntityHitResult entityHitResult = (EntityHitResult) hitResult;
                        entityHitResult.getEntity().setSecondsOnFire(5);
                        if (entity instanceof Player player)
                            entityHitResult.getEntity().hurt(DamageSource.playerAttack(player), entry.getProperty(DAMAGE));
                        else
                            entityHitResult.getEntity().hurt(DamageSource.mobAttack(entity), entry.getProperty(DAMAGE));
                    } else if (hitResult.getType() == HitResult.Type.BLOCK) {
                        BlockHitResult blockHitResult = (BlockHitResult) hitResult;
                        BlockState blockState = entity.level.getBlockState(blockHitResult.getBlockPos());

                        SimpleContainer simpleContainer = new SimpleContainer(new ItemStack(blockState.getBlock()));
                        entity.level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, simpleContainer, entity.level).ifPresent(recipe -> {
                            ItemStack result = recipe.assemble(simpleContainer);

                            if (!result.isEmpty() && Block.byItem(result.getItem()) != Blocks.AIR) {
                                entity.level.setBlockAndUpdate(blockHitResult.getBlockPos(), Block.byItem(result.getItem()).defaultBlockState());
                            }
                        });

                        blockState = entity.level.getBlockState(blockHitResult.getBlockPos());

                        if (blockState.getMaterial().isFlammable()) {
                            BlockPos pos = blockHitResult.getBlockPos().offset(blockHitResult.getDirection().getNormal());
                            if (entity.level.isEmptyBlock(pos)) {
                                entity.level.setBlockAndUpdate(pos, Blocks.FIRE.defaultBlockState());
                            }
                        }
                    }
                } else {
                    Vec3 direction = new Vec3(startVec.x() - hitResult.getLocation().x(), startVec.y() - hitResult.getLocation().y(), startVec.z() - hitResult.getLocation().z()).normalize().scale(0.1D);
                    entity.level.addParticle(new Random().nextBoolean() ? ParticleTypes.SMOKE : ParticleTypes.FLAME, hitResult.getLocation().x(), hitResult.getLocation().y(), hitResult.getLocation().z(), direction.x(), direction.y(), direction.z());
                }
            }
        } else if (entity.level.isClientSide && entry.getProperty(ANIMATION_TIMER) > 0) {
            entry.setOwnProperty(ANIMATION_TIMER, entry.getProperty(ANIMATION_TIMER) - 1);
        }
    }

    @Environment(EnvType.CLIENT)
    public static void animation(AbilityEffectsRenderLayer layer, AbilityEntry entry, PoseStack poseStack, MultiBufferSource buffer, int packedLight, LivingEntity livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        int timer = entry.getProperty(ANIMATION_TIMER);

        if (timer > 0) {
            double distance = entry.getProperty(DISTANCE) * (timer / 5F);
            Color origColor = entry.getProperty(COLOR);
            Color color = new Color(origColor.getRed(), origColor.getGreen(), origColor.getBlue(), (int) (timer / 5F * 255F));
            var builder = buffer.getBuffer(PalladiumRenderTypes.LASER);

            poseStack.pushPose();
            entry.getProperty(ORIGIN).render(poseStack, builder, livingEntity, color, layer, distance, partialTicks);
            poseStack.popPose();
        }
    }
}
