package net.threetag.threecore.ability;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.threetag.threecore.sound.FlightSound;
import net.threetag.threecore.util.icon.TexturedIcon;
import net.threetag.threecore.util.threedata.EnumSync;
import net.threetag.threecore.util.threedata.FloatThreeData;
import net.threetag.threecore.util.threedata.ThreeData;

public class AcceleratingFlightAbility extends Ability implements FlightSound.IFlyingAbility {

    public static final ThreeData<Float> BASE_SPEED = new FloatThreeData("base_speed").setSyncType(EnumSync.NONE).enableSetting("Speed you start with when starting to fly");
    public static final ThreeData<Float> MAX_SPEED = new FloatThreeData("max_speed").setSyncType(EnumSync.NONE).enableSetting("Maximum flight speed you can reach");
    public static final ThreeData<Float> ACCELERATION = new FloatThreeData("acceleration").setSyncType(EnumSync.NONE).enableSetting("Acceleration: amount of speed per tick");

    public float speed;
    public Object sound;

    public AcceleratingFlightAbility() {
        super(AbilityType.ACCELERATING_FLIGHT);
    }

    @Override
    public void registerData() {
        super.registerData();
        this.register(ICON, new TexturedIcon(TexturedIcon.ICONS_TEXTURE, 0, 16, 16, 16));
        this.register(BASE_SPEED, 0.25F);
        this.register(MAX_SPEED, 10F);
        this.register(ACCELERATION, 0.01F);
        this.register(FlightAbility.ROTATE_ARMS, true);
    }

    @Override
    public void firstTick(LivingEntity entity) {
        DistExecutor.runWhenOn(Dist.CLIENT, () -> new Runnable() {
            @Override
            public void run() {
                Minecraft.getInstance().getSoundHandler().play(new FlightSound(AcceleratingFlightAbility.this, entity, SoundEvents.ITEM_ELYTRA_FLYING, SoundCategory.PLAYERS));
            }
        });
    }

    @Override
    public void action(LivingEntity entity) {
        if (!entity.onGround) {

//            if (!entity.world.isRemote && this.prevMotion.distanceTo(motion) >= 5F && this.prevMotion.length() > motion.length()) {
//                entity.world.createExplosion(null, entity.posX, entity.posY + entity.size.height / 2D, entity.posZ, (float) motion.length(), Explosion.Mode.DESTROY);
//            }

            if (entity.moveForward > 0F) {
                this.startSound(entity);
                Vec3d vec = entity.getLookVec();
                this.speed = MathHelper.clamp(this.speed + this.get(ACCELERATION), this.get(BASE_SPEED), this.get(MAX_SPEED));
                // TODO multiply fly speed by size
                entity.setMotion(vec.x * speed, vec.y * speed - (entity.isSneaking() ? entity.getHeight() * 0.2F : 0), vec.z * speed);

            } else {
                if (entity.getMotion().length() <= 1D) {
                    this.speed = 0;
                } else {
                    this.speed = MathHelper.clamp(this.speed - this.get(ACCELERATION), this.get(BASE_SPEED), this.get(MAX_SPEED));
                }

                if (entity.isSneaking()) {
                    entity.setMotion(new Vec3d(entity.getMotion().x, entity.getHeight() * -0.2F, entity.getMotion().z));
                } else {
                    entity.setMotion(new Vec3d(entity.getMotion().x, Math.sin(entity.ticksExisted / 10F) / 100F, entity.getMotion().z));
                }
            }
        } else {
            this.speed = 0;
        }
    }

    public void startSound(LivingEntity entity) {
        if (this.sound == null)
            DistExecutor.runWhenOn(Dist.CLIENT, () -> new Runnable() {
                @Override
                public void run() {
                    Minecraft.getInstance().getSoundHandler().play((ISound) (sound = new FlightSound(AcceleratingFlightAbility.this, entity, SoundEvents.ITEM_ELYTRA_FLYING, SoundCategory.PLAYERS)));
                }
            });
    }

    @Override
    public void onFinishedPlayingSound() {
        this.sound = null;
    }
}
