package net.threetag.palladium.addonpack.parser;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.threetag.palladium.addonpack.builder.AddonBuilder;
import net.threetag.palladium.addonpack.builder.ParticleTypeBuilder;
import net.threetag.palladium.util.json.GsonUtil;
import net.threetag.palladiumcore.registry.client.ParticleProviderRegistry;
import net.threetag.palladiumcore.util.Platform;

import java.util.function.Supplier;

public class ParticleTypeParser extends AddonParser<ParticleType<?>> {

    public ParticleTypeParser() {
        super(GSON, "particle_types", Registry.PARTICLE_TYPE_REGISTRY);
    }

    @Override
    public AddonBuilder<ParticleType<?>> parse(ResourceLocation id, JsonElement jsonElement) {
        JsonObject json = GsonHelper.convertToJsonObject(jsonElement, "$");
        var builder = new ParticleTypeBuilder(id);

        GsonUtil.ifHasKey(json, "override_limiter", el -> builder.enableOverrideLimiter(GsonHelper.convertToBoolean(el, "override_limiter")));

        if (Platform.isClient()) {
            GsonUtil.ifHasKey(json, "render_type", el -> {
                String renderTypeS = GsonHelper.convertToString(el, "render_type");
                ParticleTypeBuilder.RenderType renderType = ParticleTypeBuilder.RenderType.byName(renderTypeS);

                if (renderType == null) {
                    throw new JsonParseException("Unknown particle render type '" + renderTypeS + "'");
                }

                builder.renderType(renderType);
            });

            GsonUtil.ifHasKey(json, "texture_type", el -> {
                String textureTypeS = GsonHelper.convertToString(el, "texture_type");
                ParticleTypeBuilder.TextureType textureType = ParticleTypeBuilder.TextureType.byName(textureTypeS);

                if (textureType == null) {
                    throw new JsonParseException("Unknown particle texture type '" + textureTypeS + "'");
                }

                builder.textureType(textureType);
            });

            GsonUtil.ifHasKey(json, "lifetime", el -> builder.lifetime(GsonHelper.convertToInt(el, "lifetime")));
            GsonUtil.ifHasKey(json, "has_physics", el -> builder.hasPhysics(GsonHelper.convertToBoolean(el, "has_physics")));
            GsonUtil.ifHasKey(json, "gravity", el -> builder.gravity(GsonHelper.convertToFloat(el, "gravity")));
            GsonUtil.ifHasKey(json, "quad_size", el -> builder.quadSize(GsonHelper.convertToFloat(el, "quad_size")));
            GsonUtil.ifHasKey(json, "brightness", el -> builder.brightness(GsonHelper.convertToInt(el, "brightness")));
        }

        return builder;
    }

    @SuppressWarnings({"UnnecessaryLocalVariable", "rawtypes", "unchecked"})
    @Override
    @Environment(EnvType.CLIENT)
    public void postRegister(AddonBuilder<ParticleType<?>> addonBuilder) {
        if (Platform.isClient() && addonBuilder instanceof ParticleTypeBuilder typeBuilder) {
            Supplier supplier = addonBuilder;
            ParticleEngine.SpriteParticleRegistration provider = spriteSet -> new ParticleTypeBuilder.Provider(typeBuilder, spriteSet);
            ParticleProviderRegistry.register(supplier, provider);
        }
    }
}
