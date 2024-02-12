package net.threetag.palladium.addonpack.parser;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.threetag.palladium.addonpack.builder.ParticleTypeBuilder;
import net.threetag.palladium.util.json.GsonUtil;
import net.threetag.palladiumcore.util.Platform;

public class ParticleTypeParser extends AddonParser<ParticleTypeBuilder> {

    public ParticleTypeParser() {
        super(GSON, "particle_types", Registries.PARTICLE_TYPE);
    }

    @Override
    public ParticleTypeBuilder parse(ResourceLocation id, JsonElement jsonElement) {
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

    @Override
    public void postRegister(ParticleTypeBuilder addonBuilder) {
        if (Platform.isClient()) {
            ParticleTypeParserClient.registerProvider(addonBuilder);
        }
    }
}
