package net.threetag.palladium.mixin.fabric;

import net.minecraft.server.packs.PackType;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.ArrayList;
import java.util.Arrays;

@Mixin(PackType.class)
@Unique
public class PackTypeMixin {

    @Shadow
    @Final
    @Mutable
    private static PackType[] $VALUES;

    private static final PackType ADDON = addVariant("ADDON", "addon", com.mojang.bridge.game.PackType.DATA);

    @Invoker("<init>")
    public static PackType invokeInit(String internalName, int internalId, String directory, com.mojang.bridge.game.PackType bridgeType) {
        throw new AssertionError();
    }

    private static PackType addVariant(String internalName, String directory, com.mojang.bridge.game.PackType bridgeType) {
        ArrayList<PackType> types = new ArrayList<>(Arrays.asList($VALUES));
        PackType addon = invokeInit(internalName, types.size(), directory, bridgeType);
        types.add(addon);
        $VALUES = types.toArray(new PackType[0]);
        return addon;
    }

}
