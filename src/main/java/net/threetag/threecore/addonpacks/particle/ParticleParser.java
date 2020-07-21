package net.threetag.threecore.addonpacks.particle;

import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.client.Minecraft;
import net.minecraft.particles.ParticleType;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.addonpacks.AddonPackManager;

import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Objects;

public class ParticleParser {

	public static final int resourcePrefix = "particles/".length();
	public static final int resourceSuffix = ".json".length();

	@SubscribeEvent
	public void onIParticleTypeRegistration(RegistryEvent.Register<ParticleType<?>> event) { IResourceManager resourceManager = AddonPackManager.getInstance().getResourceManager();

		for (ResourceLocation resourcelocation : resourceManager.getAllResourceLocations("particles", (name) -> name.endsWith(".json") && !name.startsWith("_"))) {
			String s = resourcelocation.getPath();
			ResourceLocation id = new ResourceLocation(resourcelocation.getNamespace(), s.substring(resourcePrefix, s.length() - resourceSuffix));

			try (IResource iresource = resourceManager.getResource(resourcelocation)) {
				ParticleType<?> particleType = parse(JSONUtils.fromJson(AddonPackManager.GSON, new BufferedReader(new InputStreamReader(iresource.getInputStream(), StandardCharsets.UTF_8)), JsonObject.class));
				if (particleType != null) {
					particleType.setRegistryName(id);
					event.getRegistry().register(particleType);
					ThreeCore.LOGGER.info("Registered addonpack particleType {}!", id);
				}
			} catch (Throwable throwable) {
				ThreeCore.LOGGER.error("Couldn't read addonpack particleType {} from {}", id, resourcelocation, throwable);
			}
		}

	}

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public void onParticleFactoryRegistration(ParticleFactoryRegisterEvent event) {
		IResourceManager resourceManager = AddonPackManager.getInstance().getResourceManager();
		LinkedHashMap<ResourceLocation, JsonObject> particleFactories = Maps.newLinkedHashMap();

		for (ResourceLocation resourcelocation : resourceManager.getAllResourceLocations("particles", (name) -> name.endsWith(".json") && !name.startsWith("_"))) {
			String s = resourcelocation.getPath();
			ResourceLocation id = new ResourceLocation(resourcelocation.getNamespace(), s.substring(resourcePrefix, s.length() - resourceSuffix));

			try {
				Minecraft.getInstance().particles.registerFactory((ThreeParticleType) Objects.requireNonNull(ForgeRegistries.PARTICLE_TYPES.getValue(id)), ThreeParticle.ThreeParticleFactory::new);
			} catch (Throwable throwable) {
				ThreeCore.LOGGER.error("Couldn't read addonpack particle factory {} from {}", id, resourcelocation, throwable);
			}
		}
	}

	public static ParticleType<?> parse(JsonObject json) throws JsonParseException {
		boolean alwaysShow = JSONUtils.hasField(json, "always_show") && JSONUtils.getBoolean(json, "always_show");
		Color color = new Color(1.0f, 1.0f, 1.0f);
		if(JSONUtils.hasField(json, "color")){
			JsonArray array = JSONUtils.getJsonArray(json, "color");
			 color = new Color(array.get(0).getAsInt(), array.get(1).getAsInt(), array.get(2).getAsInt());
		}
		double diameter = JSONUtils.hasField(json, "diameter") ? JSONUtils.getFloat(json, "diameter") : 0.2;
		int maxAge = JSONUtils.hasField(json, "max_age") ? JSONUtils.getInt(json, "max_age") : 100;
		float alpha = JSONUtils.hasField(json, "alpha") ? JSONUtils.getFloat(json, "alpha") : 1.0f;
		boolean canCollide = JSONUtils.hasField(json, "can_collide") && JSONUtils.getBoolean(json, "can_collide");
		boolean deathOnCollide = JSONUtils.hasField(json, "death_on_collide") && JSONUtils.getBoolean(json, "death_on_collide");
		boolean randomTexture = !JSONUtils.hasField(json, "random_texture") || JSONUtils.getBoolean(json, "random_texture");
		ThreeParticleType type = new ThreeParticleType(alwaysShow, color, diameter, maxAge, alpha, canCollide, deathOnCollide, randomTexture);
		return Objects.requireNonNull(type);
	}

}
