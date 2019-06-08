package com.threetag.threecore.abilities;

import com.threetag.threecore.abilities.data.ThreeData;
import com.threetag.threecore.abilities.data.ThreeDataFloat;
import com.threetag.threecore.abilities.data.EnumSync;
import com.threetag.threecore.util.helper.PlayerHelper;
import com.threetag.threecore.util.render.ItemIcon;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.init.Particles;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class AbilityTeleport extends AbilityAction {

    public static final ThreeData<Float> DISTANCE = new ThreeDataFloat("distance").setSyncType(EnumSync.NONE).enableSetting("distance", "The maximum amount of blocks you can teleport to");

    public AbilityTeleport() {
        super(AbilityType.TELEPORT);
    }

    @Override
    public void registerData() {
        super.registerData();
        this.dataManager.register(DISTANCE, 5F);
        this.dataManager.register(ICON, new ItemIcon(Items.ENDER_PEARL));
    }

    @Override
    public boolean action(EntityLivingBase entity) {
        Vec3d lookVec = entity.getLookVec().scale(this.dataManager.get(DISTANCE));
        RayTraceResult rtr = entity.world.rayTraceBlocks(new Vec3d(entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ),
                new Vec3d(entity.posX + lookVec.x, entity.posY + entity.getEyeHeight() + lookVec.y, entity.posZ + lookVec.z));

        if (rtr != null && rtr.hitVec != null) {
            PlayerHelper.playSoundToAll(entity.world, entity.posX, entity.posY, entity.posZ, 50, SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS);
            entity.setPositionAndUpdate(rtr.hitVec.x, rtr.hitVec.y, rtr.hitVec.z);
            PlayerHelper.playSoundToAll(entity.world, entity.posX, entity.posY, entity.posZ, 50, SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS);
            for (int i = 0; i < 30; ++i) {
                PlayerHelper.spawnParticleForAll(
                        entity.world, 50, Particles.PORTAL, true, (float) entity.posX,
                        (float) entity.posY + entity.world.rand.nextFloat() * (float) entity.height, (float) entity.posZ, (
                                entity.world.rand.nextFloat() - 0.5F) * 2.0F, -entity.world.rand.nextFloat(), (entity.world.rand.nextFloat() - 0.5F) * 2.0F, 1,
                        10);
            }
            return true;
        }
        return false;

    }
}
