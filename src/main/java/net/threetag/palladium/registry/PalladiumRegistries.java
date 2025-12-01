package net.threetag.palladium.registry;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.*;
import net.minecraft.data.worldgen.biome.BiomeData;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.gametest.framework.GameTestEnvironments;
import net.minecraft.gametest.framework.GameTestInstances;
import net.minecraft.network.chat.ChatType;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.dialog.Dialogs;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.animal.CatVariants;
import net.minecraft.world.entity.animal.ChickenVariants;
import net.minecraft.world.entity.animal.CowVariants;
import net.minecraft.world.entity.animal.PigVariants;
import net.minecraft.world.entity.animal.frog.FrogVariants;
import net.minecraft.world.entity.animal.wolf.WolfSoundVariants;
import net.minecraft.world.entity.animal.wolf.WolfVariants;
import net.minecraft.world.entity.decoration.PaintingVariants;
import net.minecraft.world.item.Instruments;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.JukeboxSongs;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.providers.VanillaEnchantmentProviders;
import net.minecraft.world.item.equipment.trim.TrimMaterials;
import net.minecraft.world.item.equipment.trim.TrimPatterns;
import net.minecraft.world.level.biome.MultiNoiseBiomeSourceParameterLists;
import net.minecraft.world.level.block.entity.BannerPatterns;
import net.minecraft.world.level.block.entity.trialspawner.TrialSpawnerConfigs;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseRouterData;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorPresets;
import net.minecraft.world.level.levelgen.presets.WorldPresets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.customization.Customization;
import net.threetag.palladium.customization.CustomizationCategory;
import net.threetag.palladium.customization.CustomizationSerializer;
import net.threetag.palladium.entity.data.PalladiumEntityDataType;
import net.threetag.palladium.entity.effect.EntityEffect;
import net.threetag.palladium.entity.flight.FlightType;
import net.threetag.palladium.entity.flight.FlightTypeSerializer;
import net.threetag.palladium.icon.IconSerializer;
import net.threetag.palladium.logic.condition.ConditionSerializer;
import net.threetag.palladium.logic.value.ValueSerializer;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.ability.AbilitySerializer;
import net.threetag.palladium.power.ability.enabling.EnablingHandlerSerializer;
import net.threetag.palladium.power.ability.keybind.KeyBindTypeSerializer;
import net.threetag.palladium.power.ability.unlocking.UnlockingHandlerSerializer;
import net.threetag.palladium.power.dampening.PowerDampeningSource;
import net.threetag.palladium.power.provider.PowerProvider;

import java.util.List;

@EventBusSubscriber(modid = Palladium.MOD_ID)
public class PalladiumRegistries {

    private static final RegistrySetBuilder BUILDER = (new RegistrySetBuilder()).add(PalladiumRegistryKeys.POWER, bootstrapContext -> {}).add(Registries.DIMENSION_TYPE, DimensionTypes::bootstrap).add(Registries.CONFIGURED_CARVER, Carvers::bootstrap).add(Registries.CONFIGURED_FEATURE, FeatureUtils::bootstrap).add(Registries.PLACED_FEATURE, PlacementUtils::bootstrap).add(Registries.STRUCTURE, Structures::bootstrap).add(Registries.STRUCTURE_SET, StructureSets::bootstrap).add(Registries.PROCESSOR_LIST, ProcessorLists::bootstrap).add(Registries.TEMPLATE_POOL, Pools::bootstrap).add(Registries.BIOME, BiomeData::bootstrap).add(Registries.MULTI_NOISE_BIOME_SOURCE_PARAMETER_LIST, MultiNoiseBiomeSourceParameterLists::bootstrap).add(Registries.NOISE, NoiseData::bootstrap).add(Registries.DENSITY_FUNCTION, NoiseRouterData::bootstrap).add(Registries.NOISE_SETTINGS, NoiseGeneratorSettings::bootstrap).add(Registries.WORLD_PRESET, WorldPresets::bootstrap).add(Registries.FLAT_LEVEL_GENERATOR_PRESET, FlatLevelGeneratorPresets::bootstrap).add(Registries.CHAT_TYPE, ChatType::bootstrap).add(Registries.TRIM_PATTERN, TrimPatterns::bootstrap).add(Registries.TRIM_MATERIAL, TrimMaterials::bootstrap).add(Registries.TRIAL_SPAWNER_CONFIG, TrialSpawnerConfigs::bootstrap).add(Registries.WOLF_VARIANT, WolfVariants::bootstrap).add(Registries.WOLF_SOUND_VARIANT, WolfSoundVariants::bootstrap).add(Registries.PAINTING_VARIANT, PaintingVariants::bootstrap).add(Registries.DAMAGE_TYPE, DamageTypes::bootstrap).add(Registries.BANNER_PATTERN, BannerPatterns::bootstrap).add(Registries.ENCHANTMENT, Enchantments::bootstrap).add(Registries.ENCHANTMENT_PROVIDER, VanillaEnchantmentProviders::bootstrap).add(Registries.JUKEBOX_SONG, JukeboxSongs::bootstrap).add(Registries.INSTRUMENT, Instruments::bootstrap).add(Registries.PIG_VARIANT, PigVariants::bootstrap).add(Registries.COW_VARIANT, CowVariants::bootstrap).add(Registries.CHICKEN_VARIANT, ChickenVariants::bootstrap).add(Registries.TEST_ENVIRONMENT, GameTestEnvironments::bootstrap).add(Registries.TEST_INSTANCE, GameTestInstances::bootstrap).add(Registries.FROG_VARIANT, FrogVariants::bootstrap).add(Registries.CAT_VARIANT, CatVariants::bootstrap).add(Registries.DIALOG, Dialogs::bootstrap);
    public static final List<? extends ResourceKey<? extends Registry<?>>> DATAPACK_REGISTRY_KEYS = BUILDER.getEntryKeys();

    public static final Registry<PalladiumEntityDataType<?>> ENTITY_DATA_TYPE = new RegistryBuilder<>(PalladiumRegistryKeys.ENTITY_DATA_TYPE).create();
    public static final Registry<AbilitySerializer<?>> ABILITY_SERIALIZER = new RegistryBuilder<>(PalladiumRegistryKeys.ABILITY_SERIALIZER).create();
    public static final Registry<UnlockingHandlerSerializer<?>> ABILITY_UNLOCKING_HANDLER_SERIALIZER = new RegistryBuilder<>(PalladiumRegistryKeys.ABILITY_UNLOCKING_HANDLER_SERIALIZER).create();
    public static final Registry<EnablingHandlerSerializer<?>> ABILITY_ENABLING_HANDLER_SERIALIZER = new RegistryBuilder<>(PalladiumRegistryKeys.ABILITY_ENABLING_HANDLER_SERIALIZER).create();
    public static final Registry<KeyBindTypeSerializer<?>> KEY_BIND_TYPE_SERIALIZER = new RegistryBuilder<>(PalladiumRegistryKeys.KEY_BIND_TYPE_SERIALIZER).create();
    public static final Registry<PowerProvider> POWER_PROVIDER = new RegistryBuilder<>(PalladiumRegistryKeys.POWER_PROVIDER).create();
    public static final Registry<PowerDampeningSource> POWER_DAMPENING_SOURCE = new RegistryBuilder<>(PalladiumRegistryKeys.POWER_DAMPENING_SOURCE).create();
    public static final Registry<ConditionSerializer<?>> CONDITION_SERIALIZER = new RegistryBuilder<>(PalladiumRegistryKeys.CONDITION_SERIALIZER).create();
    public static final Registry<ValueSerializer<?>> VALUE_SERIALIZER = new RegistryBuilder<>(PalladiumRegistryKeys.VALUE_SERIALIZER).create();
    public static final Registry<IconSerializer<?>> ICON_SERIALIZER = new RegistryBuilder<>(PalladiumRegistryKeys.ICON_SERIALIZER).create();
    public static final Registry<MapCodec<? extends Item>> ITEM_TYPE = new RegistryBuilder<>(PalladiumRegistryKeys.ITEM_TYPE).create();
    public static final Registry<EntityEffect> ENTITY_EFFECT = new RegistryBuilder<>(PalladiumRegistryKeys.ENTITY_EFFECT).create();
    public static final Registry<CustomizationSerializer<?>> CUSTOMIZATION_SERIALIZERS = new RegistryBuilder<>(PalladiumRegistryKeys.CUSTOMIZATION_SERIALIZERS).create();
    public static final Registry<FlightTypeSerializer<?>> FLIGHT_TYPE_SERIALIZERS = new RegistryBuilder<>(PalladiumRegistryKeys.FLIGHT_TYPE_SERIALIZERS).create();

    @SubscribeEvent
    static void registerRegistries(NewRegistryEvent e) {
        e.register(ENTITY_DATA_TYPE);
        e.register(ABILITY_SERIALIZER);
        e.register(ABILITY_UNLOCKING_HANDLER_SERIALIZER);
        e.register(ABILITY_ENABLING_HANDLER_SERIALIZER);
        e.register(KEY_BIND_TYPE_SERIALIZER);
        e.register(POWER_PROVIDER);
        e.register(POWER_DAMPENING_SOURCE);
        e.register(CONDITION_SERIALIZER);
        e.register(VALUE_SERIALIZER);
        e.register(ICON_SERIALIZER);
        e.register(ITEM_TYPE);
        e.register(ENTITY_EFFECT);
        e.register(CUSTOMIZATION_SERIALIZERS);
        e.register(FLIGHT_TYPE_SERIALIZERS);
    }

    @SubscribeEvent
    static void dataPackRegistries(DataPackRegistryEvent.NewRegistry e) {
        e.dataPackRegistry(PalladiumRegistryKeys.POWER, Power.CODEC, Power.CODEC);
        e.dataPackRegistry(PalladiumRegistryKeys.CUSTOMIZATION_CATEGORY, CustomizationCategory.CODEC, CustomizationCategory.CODEC);
        e.dataPackRegistry(PalladiumRegistryKeys.CUSTOMIZATION, Customization.Codecs.SIMPLE_CODEC, Customization.Codecs.SIMPLE_CODEC);
        e.dataPackRegistry(PalladiumRegistryKeys.FLIGHT_TYPE, FlightType.CODEC, FlightType.CODEC);
    }

    public static HolderLookup.Provider createLookup() {
        RegistryAccess.Frozen registryaccess$frozen = RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);
        return BUILDER.build(registryaccess$frozen);
    }

}
