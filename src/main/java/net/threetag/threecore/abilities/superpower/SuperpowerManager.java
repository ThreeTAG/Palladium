package net.threetag.threecore.abilities.superpower;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import net.threetag.threecore.abilities.AbilityGenerator;
import net.threetag.threecore.abilities.AbilityHelper;
import net.threetag.threecore.abilities.capability.CapabilityAbilityContainer;
import net.threetag.threecore.abilities.network.SendSuperpowerToastMessage;
import net.threetag.threecore.util.icon.IIcon;
import net.threetag.threecore.util.icon.IconSerializer;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class SuperpowerManager extends JsonReloadListener {

    private static Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private static SuperpowerManager INSTANCE;
    private Map<ResourceLocation, Superpower> registeredSuperpowers = Maps.newHashMap();

    public SuperpowerManager() {
        super(GSON, "superpowers");
        INSTANCE = this;
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonObject> splashList, IResourceManager resourceManagerIn, IProfiler profilerIn) {
        for (Map.Entry<ResourceLocation, JsonObject> entry : splashList.entrySet()) {
            ResourceLocation resourcelocation = entry.getKey();
            try {
                Superpower superpower = parseSuperpower(resourcelocation, entry.getValue());
                this.registeredSuperpowers.put(resourcelocation, superpower);
            } catch (Exception e) {
                ThreeCore.LOGGER.error("Parsing error loading superpower {}", resourcelocation, e);
            }
        }
        ThreeCore.LOGGER.info("Loaded {} superpowers", this.registeredSuperpowers.size());
    }

    public Superpower parseSuperpower(ResourceLocation resourceLocation, JsonObject json) throws Exception {
        ITextComponent name = ITextComponent.Serializer.fromJson(JSONUtils.getJsonObject(json, "name").toString());
        IIcon icon = IconSerializer.deserialize(JSONUtils.getJsonObject(json, "icon"));
        List<AbilityGenerator> abilityGenerators = Lists.newArrayList();
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

    public static void removeSuperpower(LivingEntity entity) {
        try {
            entity.getCapability(CapabilityAbilityContainer.ABILITY_CONTAINER).ifPresent(abilityContainer -> {
                abilityContainer.clearAbilities(entity, ability -> ability.getAdditionalData().getBoolean("IsFromSuperpower"));
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
