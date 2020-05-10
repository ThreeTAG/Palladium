package net.threetag.threecore.ability;

import net.threetag.threecore.util.threedata.EnumSync;
import net.threetag.threecore.util.threedata.ThreeData;
import net.threetag.threecore.util.threedata.FloatThreeData;
import net.threetag.threecore.util.PlayerUtil;
import net.threetag.threecore.util.icon.ItemIcon;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class TeleportAbility extends Ability {

    public static final ThreeData<Float> DISTANCE = new FloatThreeData("distance").setSyncType(EnumSync.NONE)
            .enableSetting("distance", "The maximum amount of blocks you can teleport to");

    public TeleportAbility() {
        super(AbilityType.TELEPORT);
    }

    @Override
    public void registerData() {
        super.registerData();
        this.dataManager.register(DISTANCE, 5F);
        this.dataManager.register(ICON, new ItemIcon(Items.ENDER_PEARL));
    }

    @Override
    public void action(LivingEntity entity) {
        Vec3d lookVec = entity.getLookVec().scale(this.dataManager.get(DISTANCE));
        RayTraceResult rtr = entity.world.rayTraceBlocks(new RayTraceContext(new Vec3d(entity.getPosX(), entity.getPosY() + entity.getEyeHeight(), entity.getPosZ()), new Vec3d(entity.getPosX() + lookVec.x, entity.getPosY() + entity.getEyeHeight() + lookVec.y, entity.getPosZ() + lookVec.z), RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.NONE, entity));

        PlayerUtil.playSoundToAll(entity.world, entity.getPosX(), entity.getPosY(), entity.getPosZ(), 50, SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS);
        entity.setPositionAndUpdate(rtr.getHitVec().x, rtr.getHitVec().y, rtr.getHitVec().z);
        PlayerUtil.playSoundToAll(entity.world, entity.getPosX(), entity.getPosY(), entity.getPosZ(), 50, SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS);

        for (int i = 0; i < 30; ++i) {
            PlayerUtil.spawnParticleForAll(
                    entity.world, 50, ParticleTypes.PORTAL, true, (float) entity.getPosX(),
                    (float) entity.getPosY() + entity.world.rand.nextFloat() * entity.getHeight(), (float) entity.getPosZ(), (
                            entity.world.rand.nextFloat() - 0.5F) * 2.0F, -entity.world.rand.nextFloat(), (entity.world.rand.nextFloat() - 0.5F) * 2.0F, 1,
                    10);
        }
    }
}
