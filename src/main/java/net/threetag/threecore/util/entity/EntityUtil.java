package net.threetag.threecore.util.entity;

import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityUtil {

    public static void spawnXP(World world, double x, double y, double z, int amount, float value) {
        int i;
        if (value == 0.0F) {
            amount = 0;
        } else if (value < 1.0F) {
            i = MathHelper.floor((float) amount * value);
            if (i < MathHelper.ceil((float) amount * value) && Math.random() < (double) ((float) amount * value - (float) i)) {
                ++i;
            }

            amount = i;
        }

        while (amount > 0) {
            i = ExperienceOrbEntity.getXPSplit(amount);
            amount -= i;
            world.addEntity(new ExperienceOrbEntity(world, x, y, z, i));
        }
    }

}
