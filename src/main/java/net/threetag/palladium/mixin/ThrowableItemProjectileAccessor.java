package net.threetag.palladium.mixin;

import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ThrowableItemProjectile.class)
public interface ThrowableItemProjectileAccessor {

    @Invoker("getDefaultItem")
    Item palladium$getDefaultItem();

}
