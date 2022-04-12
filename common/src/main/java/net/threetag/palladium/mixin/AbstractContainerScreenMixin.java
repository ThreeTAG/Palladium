package net.threetag.palladium.mixin;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractContainerScreen.class)
public interface AbstractContainerScreenMixin {

    @Accessor
    int getLeftPos();

    @Accessor
    int getTopPos();

}
