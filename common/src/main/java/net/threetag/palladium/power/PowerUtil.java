package net.threetag.palladium.power;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.entity.LivingEntity;

public class PowerUtil {

    @ExpectPlatform
    public static IPowerHolder getPowerHolder(LivingEntity entity) {
        throw new AssertionError();
    }

}
