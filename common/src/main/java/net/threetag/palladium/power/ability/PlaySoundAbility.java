package net.threetag.palladium.power.ability;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.sound.AbilitySound;
import net.threetag.palladium.util.PlayerUtil;
import net.threetag.palladium.util.property.BooleanProperty;
import net.threetag.palladium.util.property.FloatProperty;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.ResourceLocationProperty;
import net.threetag.palladiumcore.util.Platform;

public class PlaySoundAbility extends Ability {

    public static final PalladiumProperty<ResourceLocation> SOUND = new ResourceLocationProperty("sound").configurable("Sound ID that is being played");
    public static final PalladiumProperty<Float> VOLUME = new FloatProperty("volume").configurable("Volume for the played sound");
    public static final PalladiumProperty<Float> PITCH = new FloatProperty("pitch").configurable("Pitch for the played sound");
    public static final PalladiumProperty<Boolean> LOOPING = new BooleanProperty("looping").configurable("Whether or not the sound should loop during the time the ability is enabled");
    public static final PalladiumProperty<Boolean> PLAY_SELF = new BooleanProperty("play_self").configurable("Whether or not the sound should be played to just the player executing the ability, or to all players");
    public static final PalladiumProperty<Boolean> PLAY_OTHERS = new BooleanProperty("play_others").configurable("If 'play_self' is disabled, this determines whether or not the sound should be played to the players around the player executing the ability");

    public PlaySoundAbility() {
        this.withProperty(SOUND, new ResourceLocation("item.elytra.flying"))
                .withProperty(VOLUME, 1F)
                .withProperty(PITCH, 1F)
                .withProperty(LOOPING, false)
                .withProperty(PLAY_SELF, false)
                .withProperty(PLAY_OTHERS, false);
    }

    @Override
    public String getDocumentationDescription() {
        return "Plays a sound when being enabled.";
    }

    @Override
    public boolean isEffect() {
        return true;
    }

    @Override
    public void firstTick(LivingEntity entity, AbilityInstance entry, IPowerHolder holder, boolean enabled) {
        if (enabled) {
            if (entry.getProperty(LOOPING)) {
                if (Platform.isClient() && entity.level().isClientSide) {
                    this.startSound(entity, entry);
                }
            } else if (!entity.level().isClientSide) {
                if (entry.getProperty(PLAY_SELF)) {
                    if (entity instanceof Player player) {
                        PlayerUtil.playSound(player, entity.getX(), entity.getEyeY(), entity.getZ(), entry.getProperty(SOUND), entity.getSoundSource(), entry.getProperty(VOLUME), entry.getProperty(PITCH));
                    }
                } else {
                    if (entry.getProperty(PLAY_OTHERS)) {
                        PlayerUtil.playSoundToAll(entity.level(), entity.getX(), entity.getEyeY(), entity.getZ(), 100, entry.getProperty(SOUND), entity.getSoundSource(), entry.getProperty(VOLUME), entry.getProperty(PITCH), player -> {
                            return player != entity;
                        });
                    } else {
                        PlayerUtil.playSoundToAll(entity.level(), entity.getX(), entity.getEyeY(), entity.getZ(), 100, entry.getProperty(SOUND), entity.getSoundSource(), entry.getProperty(VOLUME), entry.getProperty(PITCH));
                    }
                }
            }
        }
    }

    @Environment(EnvType.CLIENT)
    public void startSound(LivingEntity entity, AbilityInstance entry) {
        boolean play;

        if (entry.getProperty(PLAY_SELF)) {
            play = entity == Minecraft.getInstance().player;
        } else {
            if (entry.getProperty(PLAY_OTHERS)) {
                play = entity != Minecraft.getInstance().player;
            } else {
                play = true;
            }
        }

        if (play) {
            Minecraft.getInstance().getSoundManager().play(new AbilitySound(entry.getReference(), entity, entry.getProperty(SOUND), entity.getSoundSource(), entry.getProperty(VOLUME), entry.getProperty(PITCH)));
        }
    }
}
