package net.threetag.palladium.power.ability;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.energybar.EnergyBarUsage;
import net.threetag.palladium.registry.PalladiumRegistries;
import net.threetag.palladium.util.PalladiumCodecs;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class Ability {

    public static final Codec<Ability> CODEC = PalladiumRegistries.ABILITY_SERIALIZER.byNameCodec().dispatch(Ability::getSerializer, AbilitySerializer::codec);

    private final AbilityProperties properties;
    private final AbilityStateManager stateManager;
    private final List<EnergyBarUsage> energyBarUsages;
    private String key;

    public Ability(AbilityProperties properties, AbilityStateManager stateManager, List<EnergyBarUsage> energyBarUsages) {
        this.properties = properties;
        this.stateManager = stateManager;
        this.energyBarUsages = energyBarUsages;
    }

    /**
     * Do NOT use!
     */
    @Deprecated
    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }

    public abstract AbilitySerializer<?> getSerializer();

    public void registerDataComponents(DataComponentMap.Builder components) {
        this.getStateManager().getUnlockingHandler().registerDataComponents(components);
        this.getStateManager().getEnablingHandler().registerDataComponents(components);
    }

    public AbilityProperties getProperties() {
        return this.properties;
    }

    public AbilityStateManager getStateManager() {
        return this.stateManager;
    }

    public List<EnergyBarUsage> getEnergyBarUsages() {
        return this.energyBarUsages;
    }

    public Component getDisplayName() {
        Component title = this.properties.getTitle();
        Identifier id = PalladiumRegistries.ABILITY_SERIALIZER.getKey(this.getSerializer());
        return title != null ? title : Component.translatable("ability." + Objects.requireNonNull(id).getNamespace() + "." + id.getPath());
    }

    public void tick(LivingEntity entity, AbilityInstance<?> abilityInstance, boolean enabled) {

    }

    public void animationTimerTick(LivingEntity entity, AbilityInstance<?> abilityInstance, boolean enabled, AnimationTimer animationTimer) {
        animationTimer.tickAndUpdate(enabled);
    }

    public void firstTick(LivingEntity entity, AbilityInstance<?> abilityInstance) {

    }

    public void lastTick(LivingEntity entity, AbilityInstance<?> abilityInstance) {

    }

    protected static <B extends Ability> RecordCodecBuilder<B, AbilityProperties> propertiesCodec() {
        return AbilityProperties.CODEC.optionalFieldOf("properties", AbilityProperties.BASIC).forGetter(Ability::getProperties);
    }

    protected static <B extends Ability> RecordCodecBuilder<B, AbilityStateManager> stateCodec() {
        return AbilityStateManager.CODEC.optionalFieldOf("state", AbilityStateManager.EMPTY).forGetter(Ability::getStateManager);
    }

    protected static <B extends Ability> RecordCodecBuilder<B, List<EnergyBarUsage>> energyBarUsagesCodec() {
        return PalladiumCodecs.listOrPrimitive(EnergyBarUsage.CODEC).optionalFieldOf("energy_bar_usage", Collections.emptyList()).forGetter(Ability::getEnergyBarUsages);
    }

}
