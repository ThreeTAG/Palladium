package net.threetag.palladium.addonpack.builder;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class ParticleTypeBuilder extends AddonBuilder<ParticleType<?>, ParticleTypeBuilder> {

    private boolean overrideLimiter = false;
    private RenderType renderType; // default: RenderType.PARTICLE_SHEET_OPAQUE
    private TextureType textureType; // default: TextureType.RANDOM
    private Integer lifetime; // default: 100
    private Boolean hasPhysics; // default: true
    private Float gravity; // default: 0.02F
    private Float quadSize; // default: 1F
    private Integer brightness; // default: -1

    public ParticleTypeBuilder(ResourceLocation id) {
        super(id);
    }

    @Override
    protected ParticleType<?> create() {
        return new SimpleParticleType(this.overrideLimiter);
    }

    public ParticleTypeBuilder enableOverrideLimiter(boolean overrideLimiter) {
        this.overrideLimiter = overrideLimiter;
        return this;
    }

    public ParticleTypeBuilder renderType(RenderType renderType) {
        this.renderType = renderType;
        return this;
    }

    public ParticleTypeBuilder textureType(TextureType textureType) {
        this.textureType = textureType;
        return this;
    }

    public ParticleTypeBuilder lifetime(int lifetime) {
        this.lifetime = lifetime;
        return this;
    }

    public ParticleTypeBuilder hasPhysics(boolean hasPhysics) {
        this.hasPhysics = hasPhysics;
        return this;
    }

    public ParticleTypeBuilder gravity(float gravity) {
        this.gravity = gravity;
        return this;
    }

    public ParticleTypeBuilder quadSize(float quadSize) {
        this.quadSize = quadSize;
        return this;
    }

    public ParticleTypeBuilder brightness(int brightness) {
        this.brightness = brightness;
        return this;
    }

    @Environment(EnvType.CLIENT)
    public static class Particle extends TextureSheetParticle {

        private final ParticleTypeBuilder builder;
        private final SpriteSet spriteSet;

        protected Particle(ClientLevel clientLevel, SpriteSet spriteSet, double x, double y, double z, double xd, double yd, double zd, ParticleTypeBuilder builder) {
            super(clientLevel, x, y, z, xd, yd, zd);
            this.builder = builder;
            this.spriteSet = spriteSet;
            this.lifetime = this.builder.getValue(b -> b.lifetime, 100);
            this.hasPhysics = this.builder.getValue(b -> b.hasPhysics, true);
            this.gravity = this.builder.getValue(b -> b.gravity, 0.02F);
            this.quadSize = this.builder.getValue(b -> b.quadSize, 1F);

            if (this.gravity == 0F) {
                this.xd = 0F;
                this.yd = 0F;
                this.zd = 0F;
            }

            if (this.builder.getValue(b -> b.textureType, TextureType.RANDOM) == TextureType.RANDOM) {
                this.pickSprite(this.spriteSet);
            } else if (this.builder.getValue(b -> b.textureType, TextureType.RANDOM) == TextureType.AGING) {
                this.setSpriteFromAge(this.spriteSet);
            }
        }

        @Override
        public void tick() {
            super.tick();
            if (this.builder.textureType == TextureType.AGING) {
                this.setSpriteFromAge(this.spriteSet);
            }
        }

        @Override
        public int getLightColor(float partialTick) {
            if (this.builder.brightness < 0) {
                return super.getLightColor(partialTick);
            }
            int i = super.getLightColor(partialTick);
            int k = i >> 16 & 255;
            return this.builder.brightness | k << 16;
        }

        @Override
        public ParticleRenderType getRenderType() {
            switch (this.builder.getValue(b -> b.renderType, RenderType.PARTICLE_SHEET_OPAQUE)) {
                case NO_RENDER -> {
                    return ParticleRenderType.NO_RENDER;
                }
                case PARTICLE_SHEET_LIT -> {
                    return ParticleRenderType.PARTICLE_SHEET_LIT;
                }
                case PARTICLE_SHEET_OPAQUE -> {
                    return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
                }
                case PARTICLE_SHEET_TRANSLUCENT -> {
                    return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
                }
                case TERRAIN_SHEET -> {
                    return ParticleRenderType.TERRAIN_SHEET;
                }
            }
            return ParticleRenderType.NO_RENDER;
        }
    }

    @Environment(EnvType.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {

        private final ParticleTypeBuilder builder;
        private final SpriteSet sprite;

        public Provider(ParticleTypeBuilder builder, SpriteSet sprite) {
            this.builder = builder;
            this.sprite = sprite;
        }

        @Nullable
        @Override
        public net.minecraft.client.particle.Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new Particle(level, this.sprite, x, y, z, xSpeed, ySpeed, zSpeed, this.builder);
        }
    }

    public enum RenderType {

        TERRAIN_SHEET("terrain_sheet"),
        PARTICLE_SHEET_OPAQUE("opaque"),
        PARTICLE_SHEET_TRANSLUCENT("translucent"),
        PARTICLE_SHEET_LIT("lit"),
        NO_RENDER("no_render");

        private final String name;

        RenderType(String name) {
            this.name = name;
        }

        public static RenderType byName(String name) {
            for (RenderType value : values()) {
                if (value.name.equalsIgnoreCase(name)) {
                    return value;
                }
            }

            return null;
        }
    }

    public enum TextureType {

        RANDOM("random"),
        AGING("aging");

        private final String name;

        TextureType(String name) {
            this.name = name;
        }

        public static TextureType byName(String name) {
            for (TextureType value : values()) {
                if (value.name.equalsIgnoreCase(name)) {
                    return value;
                }
            }

            return null;
        }
    }

}
