package com.threetag.threecore.abilities;

import com.threetag.threecore.abilities.data.ThreeData;
import com.threetag.threecore.abilities.data.ThreeDataBoolean;
import com.threetag.threecore.abilities.data.ThreeDataFloat;
import com.threetag.threecore.abilities.data.EnumSync;
import com.threetag.threecore.util.render.TexturedIcon;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class AbilityFlight extends AbilityToggle {

    public static final ThreeData<Float> SPEED = new ThreeDataFloat("speed").setSyncType(EnumSync.SELF).enableSetting("speed", "Sets the speed multiplier for flying when you are NOT sprinting");
    public static final ThreeData<Float> SPRINT_SPEED = new ThreeDataFloat("sprint_speed").setSyncType(EnumSync.SELF).enableSetting("sprint_speed", "Sets the speed multiplier for flying when you are sprinting");
    public static final ThreeData<Boolean> ROTATE_ARMS = new ThreeDataBoolean("rotate_arms").enableSetting("rotate_arms", "If enabled the players arms will face in your direction (like Superman)");

    public AbilityFlight() {
        super(AbilityType.FLIGHT);
    }

    @Override
    public void registerData() {
        super.registerData();
        this.dataManager.register(ICON, new TexturedIcon(TexturedIcon.ICONS_TEXTURE, 0, 16, 16, 16));
        this.dataManager.register(SPEED, 1F);
        this.dataManager.register(SPRINT_SPEED, 3F);
        this.dataManager.register(ROTATE_ARMS, true);
    }

    @Override
    public void updateTick(EntityLivingBase entity) {
        if (entity.onGround && ticks > 20)
            this.dataManager.set(ENABLED, false);

        if (entity.moveForward > 0F && !entity.onGround) {
            Vec3d vec = entity.getLookVec();
            double speed = entity.isSprinting() ? this.dataManager.get(SPRINT_SPEED) : this.dataManager.get(SPEED);
            // TODO multiply fly speed by size
            entity.motionX = vec.x * speed;
            entity.motionY = vec.y * speed;
            entity.motionZ = vec.z * speed;

            if (!entity.world.isRemote) {
                entity.fallDistance = 0.0F;
            }
        } else {
            float motionY = 0F;
            if (ticks < 20) {
                int lowestY = entity.getPosition().getY();

                while (lowestY > 0 && !entity.world.isBlockFullCube(new BlockPos(entity.posX, lowestY, entity.posZ))) {
                    lowestY--;
                }

                if (entity.getPosition().getY() - lowestY < 5) {
                    motionY += 1;
                }
            }

            motionY += Math.sin(entity.ticksExisted / 10F) / 100F;
            entity.fallDistance = 0F;
            entity.motionY = motionY;
        }

    }
}
