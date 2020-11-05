package net.threetag.threecore.ability.superpower;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.network.NetworkDirection;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.ability.Ability;
import net.threetag.threecore.ability.AbilityHelper;
import net.threetag.threecore.ability.container.IAbilityContainer;
import net.threetag.threecore.ability.container.SuperpowerAbilityContainer;
import net.threetag.threecore.capability.CapabilityAbilityContainer;
import net.threetag.threecore.network.SendSuperpowerToastMessage;
import net.threetag.threecore.scripts.events.SuperpowerSetScriptEvent;
import net.threetag.threecore.util.icon.IIcon;
import net.threetag.threecore.util.icon.IconSerializer;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public class SuperpowerManager extends JsonReloadListener {

    private static Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private static SuperpowerManager INSTANCE;
    private Map<ResourceLocation, Superpower> registeredSuperpowers = Maps.newHashMap();

    public SuperpowerManager() {
        super(GSON, "superpowers");
        INSTANCE = this;
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> splashList, IResourceManager resourceManagerIn, IProfiler profilerIn) {
        for (Map.Entry<ResourceLocation, JsonElement> entry : splashList.entrySet()) {
            ResourceLocation resourcelocation = entry.getKey();
            try {
                Superpower superpower = parseSuperpower(resourcelocation, (JsonObject) entry.getValue());
                this.registeredSuperpowers.put(resourcelocation, superpower);
            } catch (Exception e) {
                ThreeCore.LOGGER.error("Parsing error loading superpower {}", resourcelocation, e);
            }
        }
        ThreeCore.LOGGER.info("Loaded {} superpowers", this.registeredSuperpowers.size());
    }

    public Superpower parseSuperpower(ResourceLocation resourceLocation, JsonObject json) throws Exception {
        ITextComponent name = ITextComponent.Serializer.getComponentFromJson(JSONUtils.getJsonObject(json, "name").toString());
        IIcon icon = IconSerializer.deserialize(JSONUtils.getJsonObject(json, "icon"));
        List<Supplier<Ability>> abilityGenerators = Lists.newArrayList();
        if (JSONUtils.hasField(json, "abilities")) {
            JsonObject abilities = JSONUtils.getJsonObject(json, "abilities");
            abilityGenerators.addAll(AbilityHelper.parseAbilityGenerators(abilities));
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
        setSuperpower(entity, superpower, -1);
    }

    public static void setSuperpower(LivingEntity entity, Superpower superpower, int lifetime) {
        try {
            entity.getCapability(CapabilityAbilityContainer.MULTI_ABILITY_CONTAINER).ifPresent(multiContainer -> {
                List<ResourceLocation> toRemove = Lists.newArrayList();
                for (IAbilityContainer container : multiContainer.getAllContainers()) {
                    if (container instanceof SuperpowerAbilityContainer) {
                        toRemove.add(container.getId());
                    }
                }
                for (ResourceLocation id : toRemove) {
                    multiContainer.removeContainer(entity, id);
                }
                multiContainer.addContainer(entity, new SuperpowerAbilityContainer(superpower, lifetime));
                new SuperpowerSetScriptEvent(entity, superpower.getId().toString()).fire();
                if (entity instanceof ServerPlayerEntity)
                    ThreeCore.NETWORK_CHANNEL.sendTo(new SendSuperpowerToastMessage(superpower.getName(), superpower.getIcon()), ((ServerPlayerEntity) entity).connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds the abilities of a superpower without removing your old abilities.
     */
    public static boolean addSuperpower(LivingEntity entity, Superpower superpower) {
        return addSuperpower(entity, superpower, -1);
    }

    /**
     * Adds the abilities of a superpower without removing your old abilities.
     */
    public static boolean addSuperpower(LivingEntity entity, Superpower superpower, int lifetime) {
        AtomicBoolean result = new AtomicBoolean(false);
        try {
            entity.getCapability(CapabilityAbilityContainer.MULTI_ABILITY_CONTAINER).ifPresent(multiContainer -> result.set(multiContainer.addContainer(entity, new SuperpowerAbilityContainer(superpower, lifetime))));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.get();
    }

    public static int removeSuperpowers(LivingEntity entity) {
        AtomicInteger result = new AtomicInteger(0);
        try {
            entity.getCapability(CapabilityAbilityContainer.MULTI_ABILITY_CONTAINER).ifPresent(multiContainer -> {
                List<ResourceLocation> toRemove = Lists.newArrayList();
                for (IAbilityContainer container : multiContainer.getAllContainers()) {
                    if (container instanceof SuperpowerAbilityContainer) {
                        toRemove.add(container.getId());
                    }
                }
                for (ResourceLocation id : toRemove) {
                    multiContainer.removeContainer(entity, id);
                }
                result.set(toRemove.size());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.get();
    }

    public static boolean removeSuperpower(LivingEntity entity, ResourceLocation id) {
        AtomicBoolean result = new AtomicBoolean(false);
        try {
            entity.getCapability(CapabilityAbilityContainer.MULTI_ABILITY_CONTAINER).ifPresent(multiContainer -> {
                result.set(multiContainer.removeContainer(entity, id));
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.get();
    }

    public static boolean hasSuperpower(LivingEntity entity) {
        AtomicBoolean b = new AtomicBoolean(false);
        entity.getCapability(CapabilityAbilityContainer.MULTI_ABILITY_CONTAINER).ifPresent(multiContainer -> {
            for (IAbilityContainer container : multiContainer.getAllContainers()) {
                if (container instanceof SuperpowerAbilityContainer) {
                    b.set(true);
                    return;
                }
            }
        });
        return b.get();
    }

    public static Collection<ResourceLocation> getSuperpowers(LivingEntity entity) {
        List<ResourceLocation> ids = Lists.newArrayList();
        entity.getCapability(CapabilityAbilityContainer.MULTI_ABILITY_CONTAINER).ifPresent(multiContainer -> {
            for (IAbilityContainer container : multiContainer.getAllContainers()) {
                if (container instanceof SuperpowerAbilityContainer) {
                    ids.add(container.getId());
                }
            }
        });
        return ids;
    }
}
