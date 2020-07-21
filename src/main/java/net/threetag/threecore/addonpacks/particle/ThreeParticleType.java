package net.threetag.threecore.addonpacks.particle;

import net.minecraft.particles.BasicParticleType;

import java.awt.*;

public class ThreeParticleType extends BasicParticleType
{
	boolean canCollide, deathOnCollide, randomTexture;
	Color color;
	double diameter;
	int maxAge;
	float alpha;

	public ThreeParticleType(boolean alwaysShow, Color color, double diameter, int maxAge, float alpha, boolean canCollide, boolean deathOnCollide, boolean randomTexture)
	{
		super(alwaysShow);
		this.color = color;
		this.diameter = diameter;
		this.maxAge = maxAge;
		this.alpha = alpha;
		this.canCollide = canCollide;
		this.deathOnCollide = deathOnCollide;
		this.randomTexture = randomTexture;
	}
}
