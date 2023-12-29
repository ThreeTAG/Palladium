package net.threetag.palladium.addonpack.parser;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleType;
import net.threetag.palladium.addonpack.builder.AddonBuilder;
import net.threetag.palladium.addonpack.builder.ParticleTypeBuilder;
import net.threetag.palladiumcore.registry.client.ParticleProviderRegistry;
import net.threetag.palladiumcore.util.Platform;

import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public class ParticleTypeParserClient {

    @SuppressWarnings({"UnnecessaryLocalVariable", "rawtypes", "unchecked"})
    public static void registerProvider(AddonBuilder<ParticleType<?>> addonBuilder) {
        if (Platform.isClient() && addonBuilder instanceof ParticleTypeBuilder typeBuilder) {
            Supplier supplier = addonBuilder;
            ParticleEngine.SpriteParticleRegistration provider = spriteSet -> new ParticleTypeBuilder.Provider(typeBuilder, spriteSet);
            ParticleProviderRegistry.register(supplier, provider);
        }
    }

}
