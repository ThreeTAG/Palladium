package net.threetag.palladium.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
import net.minecraft.world.entity.boss.EnderDragonPart;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.threetag.palladium.client.PalladiumKeyMappings;
import net.threetag.palladium.network.PalladiumNetwork;
import net.threetag.palladium.network.RightClickAttackMessage;

import java.util.Objects;
import java.util.UUID;

public class DualWieldingPlayerHandler {

    protected static final UUID BASE_ATTACK_DAMAGE_UUID = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF");
    private final Player player;
    private int attackStrengthTicker;
    private ItemStack lastItemInOffHand = ItemStack.EMPTY;

    public DualWieldingPlayerHandler(Player player) {
        this.player = player;
    }

    public void tick() {
        ++this.attackStrengthTicker;
        ItemStack itemStack = this.player.getOffhandItem();
        if (!ItemStack.matches(this.lastItemInOffHand, itemStack)) {
            if (!ItemStack.isSameItem(this.lastItemInOffHand, itemStack)) {
                this.resetAttackStrengthTicker();
            }

            this.lastItemInOffHand = itemStack.copy();
        }
    }

    public void swing() {
        player.swing(InteractionHand.OFF_HAND, true);
    }

    public void attackWithOffHand(Entity target) {
        if (target.isAttackable()) {
            if (!target.skipAttackInteraction(this.player)) {
                float f = (float) getOffHandAttackStrength(this.player);
                float g;
                if (target instanceof LivingEntity) {
                    g = EnchantmentHelper.getDamageBonus(this.player.getOffhandItem(), ((LivingEntity) target).getMobType());
                } else {
                    g = EnchantmentHelper.getDamageBonus(this.player.getOffhandItem(), MobType.UNDEFINED);
                }

                float h = getOffHandAttackStrengthScale(0.5F);
                f *= 0.2F + h * h * 0.8F;
                g *= h;
                this.resetAttackStrengthTicker();
                if (f > 0.0F || g > 0.0F) {
                    boolean bl = h > 0.9F;
                    boolean bl2 = false;
                    int i = 0;
                    i += EnchantmentHelper.getKnockbackBonus(this.player);
                    if (this.player.isSprinting() && bl) {
                        this.player.level().playSound(null, this.player.getX(), this.player.getY(), this.player.getZ(), SoundEvents.PLAYER_ATTACK_KNOCKBACK, this.player.getSoundSource(), 1.0F, 1.0F);
                        ++i;
                        bl2 = true;
                    }

                    boolean bl3 = bl
                            && this.player.fallDistance > 0.0F
                            && !this.player.onGround()
                            && !this.player.onClimbable()
                            && !this.player.isInWater()
                            && !this.player.hasEffect(MobEffects.BLINDNESS)
                            && !this.player.isPassenger()
                            && target instanceof LivingEntity;
                    bl3 = bl3 && !this.player.isSprinting();
                    if (bl3) {
                        f *= 1.5F;
                    }

                    f += g;
                    boolean bl4 = false;
                    double d = this.player.walkDist - this.player.walkDistO;
                    if (bl && !bl3 && !bl2 && this.player.onGround() && d < (double) this.player.getSpeed()) {
                        ItemStack itemStack = this.player.getItemInHand(InteractionHand.OFF_HAND);
                        if (itemStack.getItem() instanceof SwordItem) {
                            bl4 = true;
                        }
                    }

                    float j = 0.0F;
                    boolean bl5 = false;
                    int k = EnchantmentHelper.getFireAspect(this.player);
                    if (target instanceof LivingEntity) {
                        j = ((LivingEntity) target).getHealth();
                        if (k > 0 && !target.isOnFire()) {
                            bl5 = true;
                            target.setSecondsOnFire(1);
                        }
                    }

                    Vec3 vec3 = target.getDeltaMovement();
                    boolean bl6 = target.hurt(this.player.damageSources().playerAttack(this.player), f);
                    if (bl6) {
                        this.swing();

                        if (i > 0) {
                            if (target instanceof LivingEntity) {
                                ((LivingEntity) target)
                                        .knockback(
                                                (float) i * 0.5F,
                                                Mth.sin(this.player.getYRot() * (float) (Math.PI / 180.0)),
                                                -Mth.cos(this.player.getYRot() * (float) (Math.PI / 180.0))
                                        );
                            } else {
                                target.push(
                                        -Mth.sin(this.player.getYRot() * (float) (Math.PI / 180.0)) * (float) i * 0.5F,
                                        0.1,
                                        Mth.cos(this.player.getYRot() * (float) (Math.PI / 180.0)) * (float) i * 0.5F
                                );
                            }

                            this.player.setDeltaMovement(this.player.getDeltaMovement().multiply(0.6, 1.0, 0.6));
                            this.player.setSprinting(false);
                        }

                        if (bl4) {
                            float l = 1.0F + EnchantmentHelper.getSweepingDamageRatio(this.player) * f;

                            for (LivingEntity livingEntity : this.player.level().getEntitiesOfClass(LivingEntity.class, target.getBoundingBox().inflate(1.0, 0.25, 1.0))) {
                                if (livingEntity != this.player
                                        && livingEntity != target
                                        && !this.player.isAlliedTo(livingEntity)
                                        && (!(livingEntity instanceof ArmorStand) || !((ArmorStand) livingEntity).isMarker())
                                        && this.player.distanceToSqr(livingEntity) < 9.0) {
                                    livingEntity.knockback(
                                            0.4F, Mth.sin(this.player.getYRot() * (float) (Math.PI / 180.0)), -Mth.cos(this.player.getYRot() * (float) (Math.PI / 180.0))
                                    );
                                    livingEntity.hurt(this.player.damageSources().playerAttack(this.player), l);
                                }
                            }

                            this.player.level().playSound(null, this.player.getX(), this.player.getY(), this.player.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, this.player.getSoundSource(), 1.0F, 1.0F);
                            this.player.sweepAttack();
                        }

                        if (target instanceof ServerPlayer && target.hurtMarked) {
                            ((ServerPlayer) target).connection.send(new ClientboundSetEntityMotionPacket(target));
                            target.hurtMarked = false;
                            target.setDeltaMovement(vec3);
                        }

                        if (bl3) {
                            this.player.level().playSound(null, this.player.getX(), this.player.getY(), this.player.getZ(), SoundEvents.PLAYER_ATTACK_CRIT, this.player.getSoundSource(), 1.0F, 1.0F);
                            this.player.crit(target);
                        }

                        if (!bl3 && !bl4) {
                            if (bl) {
                                this.player.level().playSound(null, this.player.getX(), this.player.getY(), this.player.getZ(), SoundEvents.PLAYER_ATTACK_STRONG, this.player.getSoundSource(), 1.0F, 1.0F);
                            } else {
                                this.player.level().playSound(null, this.player.getX(), this.player.getY(), this.player.getZ(), SoundEvents.PLAYER_ATTACK_WEAK, this.player.getSoundSource(), 1.0F, 1.0F);
                            }
                        }

                        if (g > 0.0F) {
                            this.player.magicCrit(target);
                        }

                        this.player.setLastHurtMob(target);
                        if (target instanceof LivingEntity) {
                            EnchantmentHelper.doPostHurtEffects((LivingEntity) target, this.player);
                        }

                        EnchantmentHelper.doPostDamageEffects(this.player, target);
                        ItemStack itemStack2 = this.player.getOffhandItem();
                        Entity entity = target;
                        if (target instanceof EnderDragonPart) {
                            entity = ((EnderDragonPart) target).parentMob;
                        }

                        if (!this.player.level().isClientSide && !itemStack2.isEmpty() && entity instanceof LivingEntity) {
                            itemStack2.hurtEnemy((LivingEntity) entity, this.player);
                            if (itemStack2.isEmpty()) {
                                this.player.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
                            }
                        }

                        if (target instanceof LivingEntity) {
                            float m = j - ((LivingEntity) target).getHealth();
                            this.player.awardStat(Stats.DAMAGE_DEALT, Math.round(m * 10.0F));
                            if (k > 0) {
                                target.setSecondsOnFire(k * 4);
                            }

                            if (this.player.level() instanceof ServerLevel && m > 2.0F) {
                                int n = (int) ((double) m * 0.5);
                                ((ServerLevel) this.player.level()).sendParticles(ParticleTypes.DAMAGE_INDICATOR, target.getX(), target.getY(0.5), target.getZ(), n, 0.1, 0.0, 0.1, 0.2);
                            }
                        }

                        this.player.causeFoodExhaustion(0.1F);
                    } else {
                        this.player.level().playSound(null, this.player.getX(), this.player.getY(), this.player.getZ(), SoundEvents.PLAYER_ATTACK_NODAMAGE, this.player.getSoundSource(), 1.0F, 1.0F);
                        if (bl5) {
                            target.clearFire();
                        }
                    }
                }
            }
        }
    }

    public float getOffHandAttackStrengthScale(float adjustTicks) {
        return Mth.clamp(((float) this.attackStrengthTicker + adjustTicks) / player.getCurrentItemAttackStrengthDelay(), 0.0F, 1.0F);
    }

    public void resetAttackStrengthTicker() {
        this.attackStrengthTicker = 0;
    }

    @SuppressWarnings("unchecked")
    public static double getOffHandAttackStrength(Player player) {
        var attributeMap = new AttributeMap(DefaultAttributes.getSupplier((EntityType<? extends LivingEntity>) player.getType()));
        var attackDamage = Objects.requireNonNull(attributeMap.getInstance(Attributes.ATTACK_DAMAGE));

        if (player.getAttributes().hasAttribute(Attributes.ATTACK_DAMAGE)) {
            for (AttributeModifier modifier : Objects.requireNonNull(player.getAttributes().getInstance(Attributes.ATTACK_DAMAGE)).getModifiers()) {
                if (!modifier.getId().equals(BASE_ATTACK_DAMAGE_UUID)) {
                    attackDamage.addTransientModifier(modifier);
                }
            }
        }

        var stack = player.getOffhandItem();
        var itemAttributes = stack.getAttributeModifiers(EquipmentSlot.MAINHAND);

        if (itemAttributes.containsKey(Attributes.ATTACK_DAMAGE)) {
            for (AttributeModifier modifier : itemAttributes.get(Attributes.ATTACK_DAMAGE)) {
                attackDamage.addTransientModifier(modifier);
            }
        }


        return attackDamage.getValue();
    }

    @Environment(EnvType.CLIENT)
    public static void attackClient() {
        if(!PalladiumKeyMappings.DUAL_WIELDING_RIGHT_CLICK) {
            PalladiumKeyMappings.DUAL_WIELDING_RIGHT_CLICK = true;
            var mc = Minecraft.getInstance();
            var hitResult = mc.hitResult;

            if (Objects.requireNonNull(hitResult).getType() == HitResult.Type.ENTITY && mc.player instanceof PalladiumPlayerExtension ext) {
                var target = ((EntityHitResult) hitResult).getEntity();
                PalladiumNetwork.NETWORK.sendToServer(new RightClickAttackMessage(target.getId()));
                ext.palladium$getDualWieldingHandler().attackWithOffHand(target);
                ext.palladium$getDualWieldingHandler().resetAttackStrengthTicker();
            } else {
                PalladiumNetwork.NETWORK.sendToServer(new RightClickAttackMessage(-1));
            }
        }
    }

}
