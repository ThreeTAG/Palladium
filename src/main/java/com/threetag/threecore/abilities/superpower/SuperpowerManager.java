package com.threetag.threecore.abilities.superpower;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.threetag.threecore.ThreeCore;
import com.threetag.threecore.abilities.AbilityGenerator;
import com.threetag.threecore.abilities.AbilityType;
import com.threetag.threecore.abilities.capability.CapabilityAbilityContainer;
import com.threetag.threecore.abilities.network.MessageSendSuperpowerToast;
import com.threetag.threecore.util.render.IIcon;
import com.threetag.threecore.util.render.IconSerializer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.resource.IResourceType;
import net.minecraftforge.resource.ISelectiveResourceReloadListener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class SuperpowerManager implements ISelectiveResourceReloadListener {

    private static Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private static SuperpowerManager INSTANCE;
    public static final int resourcePrefix = "superpowers/".length();
    public static final int resourceSuffix = ".json".length();

    public SuperpowerManager() {
        INSTANCE = this;
    }

    public Map<ResourceLocation, Superpower> registeredSuperpowers = Maps.newHashMap();

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager, Predicate<IResourceType> resourcePredicate) {
        this.registeredSuperpowers.clear();
        for (ResourceLocation resourcelocation : resourceManager.getAllResourceLocations("superpowers", (name) -> name.endsWith(".json"))) {
            String s = resourcelocation.getPath();
            ResourceLocation resourcelocation1 = new ResourceLocation(resourcelocation.getNamespace(), s.substring(resourcePrefix, s.length() - resourceSuffix));
            try (IResource iresource = resourceManager.getResource(resourcelocation)) {
                Superpower superpower = parseSuperpower(resourcelocation1, JsonUtils.fromJson(GSON, new BufferedReader(new InputStreamReader(iresource.getInputStream(), StandardCharsets.UTF_8)), JsonObject.class));
                if (superpower != null) {
                    this.registeredSuperpowers.put(resourcelocation1, superpower);
                }
            } catch (Throwable throwable) {
                ThreeCore.LOGGER.error("Couldn't read superpower {} from {}", resourcelocation1, resourcelocation, throwable);
            }
        }
    }

    public Superpower parseSuperpower(ResourceLocation resourceLocation, JsonObject json) throws Exception {
        ITextComponent name = ITextComponent.Serializer.fromJson(JsonUtils.getJsonObject(json, "name").toString());
        IIcon icon = IconSerializer.deserialize(JsonUtils.getJsonObject(json, "icon"));
        List<AbilityGenerator> abilityGenerators = Lists.newArrayList();
        if (JsonUtils.hasField(json, "abilities")) {
            JsonObject abilities = JsonUtils.getJsonObject(json, "abilities");
            abilities.entrySet().forEach((e) -> {
                if (e.getValue() instanceof JsonObject) {
                    JsonObject o = (JsonObject) e.getValue();
                    AbilityType type = AbilityType.REGISTRY.getValue(new ResourceLocation(JsonUtils.getString(o, "ability")));
                    if (type == null)
                        throw new JsonSyntaxException("Expected 'ability' to be an ability, was unknown string '" + JsonUtils.getString(o, "ability") + "'");
                    try {
                        NBTTagCompound nbt = JsonToNBT.getTagFromJson(o.toString()).copy();
                        abilityGenerators.add(new AbilityGenerator(e.getKey(), type, nbt));
                    } catch (CommandSyntaxException e1) {

                    }
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

    public static void setSuperpower(EntityLivingBase entity, Superpower superpower) {
        entity.getCapability(CapabilityAbilityContainer.ABILITY_CONTAINER).ifPresent(abilityContainer -> {
            abilityContainer.clearAbilities(entity, ability -> ability.getAdditionalData().getBoolean("IsFromSuperpower"));
            abilityContainer.addAbilities(entity, superpower);
            if (entity instanceof EntityPlayerMP)
                ThreeCore.NETWORK_CHANNEL.sendTo(new MessageSendSuperpowerToast(superpower.getName(), superpower.getIcon()), ((EntityPlayerMP) entity).connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
        });
    }
}
