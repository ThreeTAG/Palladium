package net.threetag.palladium.power.ability;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.documentation.SettingType;
import net.threetag.palladium.entity.ArmSetting;
import net.threetag.palladium.mixin.ThrowableItemProjectileAccessor;
import net.threetag.palladium.power.energybar.EnergyBarUsage;

import java.util.Collections;
import java.util.List;

public class ProjectileAbility extends Ability {

    public static final MapCodec<ProjectileAbility> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    BuiltInRegistries.ENTITY_TYPE.byNameCodec().optionalFieldOf("entity_type", EntityType.SNOWBALL).forGetter(ab -> ab.entityType),
                    CompoundTag.CODEC.optionalFieldOf("entity_data", new CompoundTag()).forGetter(ab -> ab.entityData),
                    ExtraCodecs.NON_NEGATIVE_FLOAT.optionalFieldOf("inaccuracy", 0F).forGetter(ab -> ab.inaccuracy),
                    ExtraCodecs.NON_NEGATIVE_FLOAT.optionalFieldOf("velocity", 1.5F).forGetter(ab -> ab.velocity),
                    ArmSetting.CODEC.optionalFieldOf("swinging_arm", ArmSetting.MAIN_ARM).forGetter(ab -> ab.swingingArm),
                    Codec.BOOL.optionalFieldOf("ignore_player_movement", false).forGetter(ab -> ab.ignorePlayerMovement),
                    propertiesCodec(), stateCodec(), energyBarUsagesCodec()
            ).apply(instance, ProjectileAbility::new));

    public final EntityType<?> entityType;
    public final CompoundTag entityData;
    public final float inaccuracy, velocity;
    public final ArmSetting swingingArm;
    public final boolean ignorePlayerMovement;

    public ProjectileAbility(EntityType<?> entityType, CompoundTag entityData, float inaccuracy, float velocity, ArmSetting swingingArm, boolean ignorePlayerMovement, AbilityProperties properties, AbilityStateManager stateManager, List<EnergyBarUsage> energyBarUsages) {
        super(properties, stateManager, energyBarUsages);
        this.entityType = entityType;
        this.entityData = entityData;
        this.inaccuracy = inaccuracy;
        this.velocity = velocity;
        this.swingingArm = swingingArm;
        this.ignorePlayerMovement = ignorePlayerMovement;
    }

    @Override
    public AbilitySerializer<ProjectileAbility> getSerializer() {
        return AbilitySerializers.PROJECTILE.get();
    }

    @Override
    public boolean tick(LivingEntity entity, AbilityInstance<?> abilityInstance, boolean enabled) {
        if (!entity.level().isClientSide() && enabled) {
            CompoundTag compound = this.entityData == null ? new CompoundTag() : this.entityData;
            compound.putString("id", BuiltInRegistries.ENTITY_TYPE.getKey(this.entityType).toString());
            ServerLevel level = (ServerLevel) entity.level();

            return EntityType.loadEntityRecursive(compound, level, EntitySpawnReason.COMMAND, (en) -> {
                if (en instanceof Projectile projectile) {
                    projectile.setOwner(entity);
                    projectile.setPos(entity.getX(), entity.getEyeY() - (double)0.1F, entity.getZ());

                    if (this.ignorePlayerMovement) {
                        Vec3 vec3 = entity.getDeltaMovement();
                        projectile.setDeltaMovement(projectile.getDeltaMovement().subtract(vec3.x, entity.onGround() ? 0.0 : vec3.y, vec3.z));
                    }

                    for (InteractionHand hand : this.swingingArm.getHand(entity)) {
                        entity.swing(hand, true);
                    }

                    var item = ItemStack.EMPTY;

                    if (projectile instanceof ThrowableItemProjectileAccessor itemProjectile) {
                        item = itemProjectile.palladium$getDefaultItem().getDefaultInstance();
                    }

                    return Projectile.spawnProjectileFromRotation((serverLevel, livingEntity, itemStack) -> projectile, level, item, entity, 0.0F, this.velocity, this.inaccuracy);
                }

                return null;
            }) != null;
        }

        return enabled;
    }

    public static class Serializer extends AbilitySerializer<ProjectileAbility> {

        @Override
        public MapCodec<ProjectileAbility> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Ability, ProjectileAbility> builder, HolderLookup.Provider provider) {
            builder.setDescription("Shoots a projectile entity")
                    .add("entity_type", TYPE_ENTITY_TYPE, "ID of an entity type (must be a projectile).")
                    .addOptional("entity_data", TYPE_NBT, "Additional NBT data for the projectile entity.")
                    .addOptional("inaccuracy", TYPE_NON_NEGATIVE_FLOAT, "Inaccurary when shooting the projectile", 0)
                    .addOptional("velocity", TYPE_NON_NEGATIVE_FLOAT, "Velocity with which the projectile is being shot", 1.5F)
                    .addOptional("swinging_arm", TYPE_ARM_SETTING, "The arm which will swing when the projectile is being shot", ArmSetting.MAIN_ARM)
                    .addOptional("ignore_player_movement", TYPE_BOOLEAN, "Usually, the current velocity of player will be added to the projectile's velocity. This setting can disable that.", false)
                    .addExampleObject(new ProjectileAbility(
                            EntityType.SNOWBALL,
                            new CompoundTag(),
                            0F,
                            1.5F,
                            ArmSetting.MAIN_ARM,
                            false,
                            AbilityProperties.BASIC, AbilityStateManager.EMPTY, Collections.emptyList()
                    ))
                    .addExampleObject(new ProjectileAbility(
                            EntityType.ENDER_PEARL,
                            new CompoundTag(),
                            0.2F,
                            2F,
                            ArmSetting.BOTH,
                            true,
                            AbilityProperties.BASIC, AbilityStateManager.EMPTY, Collections.emptyList()
                    ));
        }
    }
}
