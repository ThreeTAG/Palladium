package com.threetag.threecore.abilities;

import com.threetag.threecore.abilities.data.BooleanThreeData;
import com.threetag.threecore.abilities.data.EnumSync;
import com.threetag.threecore.abilities.data.FloatThreeData;
import com.threetag.threecore.abilities.data.ThreeData;
import com.threetag.threecore.util.render.TexturedIcon;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class FlightAbility extends Ability {

    public static final ThreeData<Float> SPEED = new FloatThreeData("speed").setSyncType(EnumSync.SELF).enableSetting("speed", "Sets the speed multiplier for flying when you are NOT sprinting");
    public static final ThreeData<Float> SPRINT_SPEED = new FloatThreeData("sprint_speed").setSyncType(EnumSync.SELF).enableSetting("sprint_speed", "Sets the speed multiplier for flying when you are sprinting");
    public static final ThreeData<Boolean> ROTATE_ARMS = new BooleanThreeData("rotate_arms").enableSetting("rotate_arms", "If enabled the players arms will face in your direction (like Superman)");

    public FlightAbility() {
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
    public void updateTick(LivingEntity entity) {
        if (entity.onGround && ticks > 20)
            this.getConditionManager().disableKeybounds();

        if (entity.moveForward > 0F && !entity.onGround) {
            Vec3d vec = entity.getLookVec();
            double speed = entity.isSprinting() ? this.dataManager.get(SPRINT_SPEED) : this.dataManager.get(SPEED);
            // TODO multiply fly speed by size
            entity.setMotion(vec.x * speed, vec.y * speed, vec.z * speed);

            if (!entity.world.isRemote) {
                entity.fallDistance = 0.0F;
            }
        } else {
            float motionY = 0F;
            if (ticks < 20) {
                int lowestY = entity.getPosition().getY();

                while (lowestY > 0 && !entity.world.isBlockPresent(new BlockPos(entity.posX, lowestY, entity.posZ))) {
                    lowestY--;
                }

                if (entity.getPosition().getY() - lowestY < 5) {
                    motionY += 1;
                }
            }

            motionY += Math.sin(entity.ticksExisted / 10F) / 100F;
            entity.fallDistance = 0F;
            Vec3d motion = new Vec3d(entity.getMotion().x, motionY, entity.getMotion().z);
            entity.setMotion(motion);
        }

    }
}
