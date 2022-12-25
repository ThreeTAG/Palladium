package net.threetag.palladium.addonpack.builder;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class ParticleTypeBuilder extends AddonBuilder<ParticleType<?>> {

    private boolean overrideLimiter = false;
    private RenderType renderType = RenderType.PARTICLE_SHEET_OPAQUE;
    private int lifetime = 100;
    private boolean hasPhysics = true;
    private float gravity = 0.02F;
    private float quadSize = 1F;
    private int brightness = -1;

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

    public static class Particle extends TextureSheetParticle {

        private final ParticleTypeBuilder builder;

        protected Particle(ClientLevel clientLevel, double x, double y, double z, double xd, double yd, double zd, ParticleTypeBuilder builder) {
            super(clientLevel, x, y, z, xd, yd, zd);
            this.builder = builder;
            this.lifetime = this.builder.lifetime;
            this.hasPhysics = this.builder.hasPhysics;
            this.gravity = this.builder.gravity;
            this.quadSize = this.builder.quadSize;
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
            switch (this.builder.renderType) {
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
            var particle = new Particle(level, x, y, z, xSpeed, ySpeed, zSpeed, this.builder);
            particle.pickSprite(this.sprite);
            return particle;
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

}
