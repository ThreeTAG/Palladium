package net.threetag.palladium.mixin.forge;

import net.minecraft.server.packs.PackType;
import net.minecraftforge.common.IExtensibleEnum;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PackType.class)
public class PackTypeMixin implements IExtensibleEnum {

    private static PackType create(String name, String directoryName, com.mojang.bridge.game.PackType packType) {
        throw new IllegalStateException("Enum not extended");
    }

}
