package net.threetag.threecore.abilities.superpower;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.abilities.AbilityGenerator;
import net.threetag.threecore.abilities.AbilityType;
import net.threetag.threecore.abilities.capability.CapabilityAbilityContainer;
import net.threetag.threecore.abilities.network.SendSuperpowerToastMessage;
import net.threetag.threecore.util.render.IIcon;
import net.threetag.threecore.util.render.IconSerializer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.resources.IResourceManagerReloadListener;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.network.NetworkDirection;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class SuperpowerManager implements IResourceManagerReloadListener {

    private static Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private static SuperpowerManager INSTANCE;
    public static final int resourcePrefix = "superpowers/".length();
    public static final int resourceSuffix = ".json".length();

    public SuperpowerManager() {
        INSTANCE = this;
    }

    public Map<ResourceLocation, Superpower> registeredSuperpowers = Maps.newHashMap();

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        this.registeredSuperpowers.clear();
        for (ResourceLocation resourcelocation : resourceManager.getAllResourceLocations("superpowers", (name) -> name.endsWith(".json"))) {
            String s = resourcelocation.getPath();
            ResourceLocation resourcelocation1 = new ResourceLocation(resourcelocation.getNamespace(), s.substring(resourcePrefix, s.length() - resourceSuffix));
            try (IResource iresource = resourceManager.getResource(resourcelocation)) {
                Superpower superpower = parseSuperpower(resourcelocation1, JSONUtils.fromJson(GSON, new BufferedReader(new InputStreamReader(iresource.getInputStream(), StandardCharsets.UTF_8)), JsonObject.class));
                if (superpower != null) {
                    ThreeCore.LOGGER.info("Registered superpower {}!", resourcelocation1);
                    this.registeredSuperpowers.put(resourcelocation1, superpower);
                }
            } catch (Throwable throwable) {
                ThreeCore.LOGGER.error("Couldn't read superpower {} from {}", resourcelocation1, resourcelocation, throwable);
            }
        }
    }

    public Superpower parseSuperpower(ResourceLocation resourceLocation, JsonObject json) throws Exception {
        ITextComponent name = ITextComponent.Serializer.fromJson(JSONUtils.getJsonObject(json, "name").toString());
        IIcon icon = IconSerializer.deserialize(JSONUtils.getJsonObject(json, "icon"));
        List<AbilityGenerator> abilityGenerators = Lists.newArrayList();
        if (JSONUtils.hasField(json, "abilities")) {
            JsonObject abilities = JSONUtils.getJsonObject(json, "abilities");
            abilities.entrySet().forEach((e) -> {
                if (e.getValue() instanceof JsonObject) {
                    JsonObject o = (JsonObject) e.getValue();
                    AbilityType type = AbilityType.REGISTRY.getValue(new ResourceLocation(JSONUtils.getString(o, "ability")));
                    if (type == null)
                        throw new JsonSyntaxException("Expected 'ability' to be an ability, was unknown string '" + JSONUtils.getString(o, "ability") + "'");
                    abilityGenerators.add(new AbilityGenerator(e.getKey(), type, o));
                }
            });
        }

        return new Superpower(resourceLocation, name, icon, abilityGenerators);
    }

    public Collection<Superpower> getSuperpowers() {
        return this.registeredSuperpowers.values();
    }

    public Superpower getSuperpower(ResourceLocation id) {
        return this.registeredSuperpowers.get(id);
    }

    public static SuperpowerManager getInstance() {
        return INSTANCE;
    }

    public static void setSuperpower(LivingEntity entity, Superpower superpower) {
        try {
            entity.getCapability(CapabilityAbilityContainer.ABILITY_CONTAINER).ifPresent(abilityContainer -> {
                abilityContainer.clearAbilities(entity, ability -> ability.getAdditionalData().getBoolean("IsFromSuperpower"));
                abilityContainer.addAbilities(entity, superpower);
                if (entity instanceof ServerPlayerEntity)
                    ThreeCore.NETWORK_CHANNEL.sendTo(new SendSuperpowerToastMessage(superpower.getName(), superpower.getIcon()), ((ServerPlayerEntity) entity).connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
