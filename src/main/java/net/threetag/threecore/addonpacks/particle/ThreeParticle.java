package net.threetag.threecore.addonpacks.particle;

import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.awt.*;

@OnlyIn(Dist.CLIENT)
public class ThreeParticle extends SpriteTexturedParticle {

    public IAnimatedSprite sprites;
    public boolean deathOnCollide, randomTexture;

    public ThreeParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, Color tint, double diameter,
                         int maxAge, float alpha, boolean canCollide, boolean deathOnCollide, boolean randomTexture, IAnimatedSprite sprites) {
        super(world, x, y, z, velocityX, velocityY, velocityZ);
        this.sprites = sprites;

        setColor(tint.getRed() / 255.0F, tint.getGreen() / 255.0F, tint.getBlue() / 255.0F);
        setSize((float) diameter, (float) diameter);    // the size (width, height) of the collision box.

        final float PARTICLE_SCALE_FOR_ONE_METRE = 0.5F; //  if the particleScale is 0.5, the texture will be rendered as 1 metre high
        particleScale = PARTICLE_SCALE_FOR_ONE_METRE * (float) diameter; // sets the rendering size of the particle for a TexturedParticle.

        this.maxAge = maxAge;  // lifetime in ticks: 100 ticks = 5 seconds

        this.particleAlpha = alpha;

        //the vanilla Particle constructor added random variation to our starting velocity.  Undo it!
        this.motionX = velocityX;
        this.motionY = velocityY;
        this.motionZ = velocityZ;

        this.canCollide = canCollide;
        this.deathOnCollide = deathOnCollide;
        this.randomTexture = randomTexture;
    }

    @Override
    public void tick() {
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;

        move(motionX, motionY, motionZ);

        if (!randomTexture)
            this.selectSpriteWithAge(this.sprites);

        if ((onGround && deathOnCollide) || (prevPosY == posY && motionY > 0 && deathOnCollide) || (this.age++ >= this.maxAge))
            this.setExpired();
    }

    @Override
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @OnlyIn(Dist.CLIENT)
    public static class ThreeParticleFactory implements IParticleFactory<BasicParticleType> {
        @Nullable
        @Override
        public Particle makeParticle(BasicParticleType data, ClientWorld world, double xPos, double yPos, double zPos, double xVelocity, double yVelocity,
                                     double zVelocity) {
            ThreeParticleType type = (ThreeParticleType) data;

            ThreeParticle newParticle = new ThreeParticle(world, xPos, yPos, zPos, xVelocity, yVelocity, zVelocity, type.color, type.diameter, type.maxAge,
                    type.alpha, type.canCollide, type.deathOnCollide, type.randomTexture, sprites);
            if (type.randomTexture)
                newParticle.selectSpriteRandomly(sprites);
            else
                newParticle.selectSpriteWithAge(sprites);
            return newParticle;
        }

        private final IAnimatedSprite sprites;

        public ThreeParticleFactory(IAnimatedSprite sprite) {
            this.sprites = sprite;
        }

    }
}
