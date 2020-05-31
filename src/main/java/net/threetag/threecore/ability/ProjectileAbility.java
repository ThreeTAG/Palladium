package net.threetag.threecore.ability;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.server.ServerWorld;
import net.threetag.threecore.util.icon.ItemIcon;
import net.threetag.threecore.util.threedata.*;

public class ProjectileAbility extends Ability {

    public static final ThreeData<EntityType<?>> ENTITY_TYPE = new EntityTypeThreeData("entity_type").setSyncType(EnumSync.NONE).enableSetting("Determines the entity that will be spawned");
    public static final ThreeData<CompoundNBT> ENTITY_DATA = new CompoundNBTThreeData("entity_data").setSyncType(EnumSync.NONE).enableSetting("NBT tag for the entity that will be spawned");
    public static final ThreeData<Float> INACCURACY = new FloatThreeData("inaccuracy").setSyncType(EnumSync.NONE).enableSetting("Determines the inaccuracy when shooting the projectile");
    public static final ThreeData<Float> VELOCITY = new FloatThreeData("velocity").setSyncType(EnumSync.NONE).enableSetting("Determines the velocity when shooting the projectile");

    public ProjectileAbility() {
        super(AbilityType.PROJECTILE);
    }

    @Override
    public void registerData() {
        super.registerData();
        this.dataManager.register(ENTITY_TYPE, EntityType.SNOWBALL);
        this.dataManager.register(ENTITY_DATA, new CompoundNBT());
        this.dataManager.register(INACCURACY, 0F);
        this.dataManager.register(VELOCITY, 1.5F);
        this.dataManager.register(ICON, new ItemIcon(Items.SNOWBALL));
    }

    @Override
    public void action(LivingEntity entity) {
        if (!entity.world.isRemote) {
            CompoundNBT compound = this.dataManager.get(ENTITY_DATA).copy();
            compound.putString("id", this.dataManager.get(ENTITY_TYPE).getRegistryName().toString());

            ServerWorld world = (ServerWorld) entity.world;
            EntityType.loadEntityAndExecute(compound, world, projectile -> {
                if (!(projectile instanceof IProjectile))
                    return null;

                projectile.setLocationAndAngles(entity.getPosX(), entity.getPosY() + entity.getEyeHeight() - 0.1D, entity.getPosZ(), projectile.rotationYaw, projectile.rotationPitch);
                float pitchOffset = 0;
                float velocity = this.dataManager.get(VELOCITY);
                float inaccuracy = this.dataManager.get(INACCURACY);
                float f = -MathHelper.sin(entity.rotationYaw * ((float) Math.PI / 180F)) * MathHelper.cos(entity.rotationPitch * ((float) Math.PI / 180F));
                float f1 = -MathHelper.sin((entity.rotationPitch + pitchOffset) * ((float) Math.PI / 180F));
                float f2 = MathHelper.cos(entity.rotationYaw * ((float) Math.PI / 180F)) * MathHelper.cos(entity.rotationPitch * ((float) Math.PI / 180F));
                ((IProjectile) projectile).shoot(f, f1, f2, velocity, inaccuracy);
                Vec3d vec3d = entity.getMotion();
                projectile.setMotion(projectile.getMotion().add(vec3d.x, entity.onGround ? 0.0D : vec3d.y, vec3d.z));

                return !world.summonEntity(projectile) ? null : projectile;
            });
        }
    }
}
