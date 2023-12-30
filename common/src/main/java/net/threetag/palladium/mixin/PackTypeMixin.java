package net.threetag.palladium.mixin;

import net.minecraft.server.packs.PackType;
import net.threetag.palladium.addonpack.AddonPackManager;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Arrays;

@Mixin(PackType.class)
@Unique
public class PackTypeMixin {

    @Shadow
    @Final
    @Mutable
    private static PackType[] $VALUES;

    @Invoker(value = "<init>")
    private static PackType create(String name, int ordinal, String directoryName) {
        throw new AssertionError();
    }

    static {
        var entry = create("ADDON", $VALUES.length, "addon");

        AddonPackManager.PACK_TYPE = entry;

        $VALUES = Arrays.copyOf($VALUES, $VALUES.length + 1);
        $VALUES[$VALUES.length - 1] = entry;
    }

}
