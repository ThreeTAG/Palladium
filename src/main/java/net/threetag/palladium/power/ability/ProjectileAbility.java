// TODO

//package net.threetag.palladium.power.ability;
//
//import com.mojang.serialization.Codec;
//import com.mojang.serialization.MapCodec;
//import com.mojang.serialization.codecs.RecordCodecBuilder;
//import net.minecraft.core.Holder;
//import net.minecraft.core.registries.BuiltInRegistries;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.server.level.ServerLevel;
//import net.minecraft.world.InteractionHand;
//import net.minecraft.world.entity.EntityType;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.entity.ai.attributes.Attributes;
//import net.minecraft.world.entity.projectile.Projectile;
//import net.minecraft.world.phys.Vec3;
//import net.threetag.palladium.entity.CustomProjectile;
//import net.threetag.palladium.entity.PalladiumEntityTypes;
//import net.threetag.palladium.power.PowerHolder;
//import net.threetag.palladium.power.energybar.EnergyBarUsage;
//import net.threetag.palladium.entity.ArmSetting;
//import net.threetag.palladium.util.CodecExtras;
//
//import java.util.List;
//
//public class ProjectileAbility extends Ability {
//
//    public static final MapCodec<ProjectileAbility> CODEC = RecordCodecBuilder.mapCodec(instance ->
//            instance.group(
//                    BuiltInRegistries.ENTITY_TYPE.holderByNameCodec().optionalFieldOf("entity_type", PalladiumEntityTypes.CUSTOM_PROJECTILE).forGetter(ab -> ab.entityType),
//                    CompoundTag.CODEC.optionalFieldOf("entity_data", new CompoundTag()).forGetter(ab -> ab.entityData),
//                    CodecExtras.NON_NEGATIVE_FLOAT.optionalFieldOf("inaccuracy", 0F).forGetter(ab -> ab.inaccuracy),
//                    CodecExtras.NON_NEGATIVE_FLOAT.optionalFieldOf("velocity", 1.5F).forGetter(ab -> ab.velocity),
//                    ArmSetting.CODEC.optionalFieldOf("swinging_arm", ArmSetting.MAIN_ARM).forGetter(ab -> ab.swingingArm),
//                    Codec.BOOL.optionalFieldOf("damage_from_player", false).forGetter(ab -> ab.damageFromPlayer),
//                    Codec.BOOL.optionalFieldOf("ignore_player_movement", false).forGetter(ab -> ab.ignorePlayerMovement),
//                    propertiesCodec(), conditionsCodec(), energyBarUsagesCodec()
//            ).apply(instance, ProjectileAbility::new));
//
//    public final Holder<EntityType<?>> entityType;
//    public final CompoundTag entityData;
//    public final float inaccuracy, velocity;
//    public final ArmSetting swingingArm;
//    public final boolean damageFromPlayer, ignorePlayerMovement;
//
//    public ProjectileAbility(Holder<EntityType<?>> entityType, CompoundTag entityData, float inaccuracy, float velocity, ArmSetting swingingArm, boolean damageFromPlayer, boolean ignorePlayerMovement, AbilityProperties properties, AbilityConditions conditions, List<EnergyBarUsage> energyBarUsages) {
//        super(properties, conditions, energyBarUsages);
//        this.entityType = entityType;
//        this.entityData = entityData;
//        this.inaccuracy = inaccuracy;
//        this.velocity = velocity;
//        this.swingingArm = swingingArm;
//        this.damageFromPlayer = damageFromPlayer;
//        this.ignorePlayerMovement = ignorePlayerMovement;
//    }
//
//    @Override
//    public AbilitySerializer<ProjectileAbility> getSerializer() {
//        return AbilitySerializers.PROJECTILE.get();
//    }
//
//    @Override
//    public void tick(LivingEntity entity, AbilityInstance entry, PowerHolder holder, boolean enabled) {
//        if (!entity.level().isClientSide && enabled) {
//            CompoundTag compound = this.entityData == null ? new CompoundTag() : this.entityData;
//            var entityId = this.entityType.unwrapKey();
//            entityId.ifPresent(entityTypeResourceKey -> compound.putString("id", entityTypeResourceKey.location().toString()));
//
//            ServerLevel world = (ServerLevel) entity.level();
//            EntityType.loadEntityRecursive(compound, world, (en) -> {
//                if (!(en instanceof Projectile projectile))
//                    return null;
//
//                projectile.moveTo(entity.getX(), entity.getY() + entity.getEyeHeight() - 0.1D, entity.getZ(), projectile.getYRot(), projectile.getXRot());
//                projectile.shootFromRotation(entity, entity.getXRot(), entity.getYRot(), 0, this.velocity, this.inaccuracy);
//                if (this.ignorePlayerMovement) {
//                    Vec3 vec3 = entity.getDeltaMovement();
//                    projectile.setDeltaMovement(projectile.getDeltaMovement().subtract(vec3.x, entity.onGround() ? 0.0 : vec3.y, vec3.z));
//                }
//                projectile.setOwner(entity);
//
//                for (InteractionHand hand : this.swingingArm.getHand(entity)) {
//                    entity.swing(hand, true);
//                }
//
//                if (this.damageFromPlayer && projectile instanceof CustomProjectile customProjectile) {
//                    customProjectile.damage = (float) entity.getAttributeValue(Attributes.ATTACK_DAMAGE);
//                }
//
//                return !world.addWithUUID(projectile) ? null : projectile;
//            });
//        }
//    }
//
//    public static class Serializer extends AbilitySerializer<ProjectileAbility> {
//
//        @Override
//        public MapCodec<ProjectileAbility> codec() {
//            return CODEC;
//        }
//    }
//}
