package net.threetag.palladium.mixin;

import net.minecraft.world.item.Item;
import net.threetag.palladium.item.PalladiumItemExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Item.class)
public class ItemMixin implements PalladiumItemExtension {

    @Unique
    private Item.Properties palladium$properties;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void init(Item.Properties properties, CallbackInfo ci) {
        this.palladium$properties = properties;
    }

    @Override
    @Unique
    public Item.Properties palladium$getProperties() {
        return this.palladium$properties;
    }
}
