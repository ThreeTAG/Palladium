package net.threetag.palladium.logic.molang;

import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.threetag.palladium.Palladium;
import org.jetbrains.annotations.Nullable;

public interface EntityContext {

    Entity entity();

    @Nullable
    default Avatar player() {
        var entity = this.entity();
        return entity instanceof Avatar avatar ? avatar : null;
    }

    default Level level() {
        return this.entity().level();
    }

    default float partialTick() {
        return Palladium.PROXY.getCurrentPartialTick();
    }

}
