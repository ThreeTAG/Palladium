package net.threetag.threecore.ability;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.threetag.threecore.sound.FlightSound;
import net.threetag.threecore.util.icon.TexturedIcon;
import net.threetag.threecore.util.threedata.BooleanThreeData;
import net.threetag.threecore.util.threedata.EnumSync;
import net.threetag.threecore.util.threedata.FloatThreeData;
import net.threetag.threecore.util.threedata.ThreeData;

public class FlightAbility extends Ability implements FlightSound.IFlyingAbility {

    public static final ThreeData<Float> SPEED = new FloatThreeData("speed").setSyncType(EnumSync.SELF).enableSetting("speed", "Sets the speed multiplier for flying when you are NOT sprinting");
    public static final ThreeData<Float> SPRINT_SPEED = new FloatThreeData("sprint_speed").setSyncType(EnumSync.SELF).enableSetting("sprint_speed", "Sets the speed multiplier for flying when you are sprinting");
    public static final ThreeData<Boolean> ROTATE_ARMS = new BooleanThreeData("rotate_arms").enableSetting("rotate_arms", "If enabled the players arms will face in your direction (like Superman)");

    public Object sound;

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
    public void action(LivingEntity entity) {
        if (!entity.func_233570_aj_()) {
            if (entity.moveForward > 0F) {
                this.startSound(entity);
                Vector3d vec = entity.getLookVec();
                double speed = entity.isSprinting() ? this.dataManager.get(SPRINT_SPEED) : this.dataManager.get(SPEED);
                // TODO multiply fly speed by size
                entity.setMotion(vec.x * speed, vec.y * speed - (entity.isCrouching() ? entity.getHeight() * 0.2F : 0), vec.z * speed);
            } else if (entity.isCrouching()) {
                entity.setMotion(new Vector3d(entity.getMotion().x, entity.getHeight() * -0.2F, entity.getMotion().z));
            } else {
                entity.setMotion(new Vector3d(entity.getMotion().x, Math.sin(entity.ticksExisted / 10F) / 100F, entity.getMotion().z));
            }
        }
    }

    public void startSound(LivingEntity entity) {
        if (this.sound == null)
            DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
                Minecraft.getInstance().getSoundHandler().play((ISound) (this.sound = new FlightSound(this, entity, SoundEvents.ITEM_ELYTRA_FLYING, SoundCategory.PLAYERS)));
            });
    }

    @Override
    public void onFinishedPlayingSound() {
        this.sound = null;
    }
}
