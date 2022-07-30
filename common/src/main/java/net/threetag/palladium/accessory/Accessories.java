package net.threetag.palladium.accessory;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.Palladium;

public class Accessories {

    public static final DeferredRegister<Accessory> ACCESSORIES = DeferredRegister.create(Palladium.MOD_ID, Accessory.RESOURCE_KEY);

    public static final RegistrySupplier<Accessory> LUCRAFT_ARC_REACTOR = ACCESSORIES.register("lucraft_arc_reactor",
            () -> new OverlayAccessory("lucraft_arc_reactor").glowing().slot(AccessorySlot.SPECIAL));

    public static final RegistrySupplier<Accessory> HEROBRINE_EYES = ACCESSORIES.register("herobrine_eyes",
            () -> new OverlayAccessory("herobrine_eyes").glowing().slot(AccessorySlot.FACE));

    public static final RegistrySupplier<Accessory> FACE_MASK = ACCESSORIES.register("face_mask",
            () -> new OverlayAccessory("face_mask").slot(AccessorySlot.FACE));

    public static final RegistrySupplier<Accessory> GLASSES_3D = ACCESSORIES.register("3d_glasses",
            () -> new HumanoidModelOverlay(new ModelLayerLocation(Palladium.id("humanoid"), "glasses"), "3d_glasses").slot(AccessorySlot.FACE));

    public static final RegistrySupplier<Accessory> SUN_GLASSES = ACCESSORIES.register("sun_glasses",
            () -> new HumanoidModelOverlay(new ModelLayerLocation(Palladium.id("humanoid"), "glasses"), "sun_glasses").slot(AccessorySlot.FACE));

    public static final RegistrySupplier<Accessory> HEART_GLASSES = ACCESSORIES.register("heart_glasses",
            () -> new HumanoidModelOverlay(new ModelLayerLocation(Palladium.id("humanoid"), "glasses"), "heart_glasses").slot(AccessorySlot.FACE));

    public static final RegistrySupplier<Accessory> OWCA_FEDORA = ACCESSORIES.register("owca_fedora",
            () -> new HumanoidModelOverlay(new ModelLayerLocation(Palladium.id("humanoid"), "fedora"), "owca_fedora").slot(AccessorySlot.HAT));

    public static final RegistrySupplier<Accessory> ELTON_HAT = ACCESSORIES.register("elton_hat",
            () -> new HumanoidModelOverlay(new ModelLayerLocation(Palladium.id("humanoid"), "fedora"), "elton_hat").slot(AccessorySlot.HAT));

    public static final RegistrySupplier<Accessory> STRAWHAT = ACCESSORIES.register("strawhat",
            () -> new HumanoidModelOverlay(new ModelLayerLocation(Palladium.id("humanoid"), "strawhat"), new ResourceLocation("textures/entity/villager/profession/farmer.png")).slot(AccessorySlot.HAT));

    public static final RegistrySupplier<Accessory> FEZ = ACCESSORIES.register("fez",
            () -> new HumanoidModelOverlay(new ModelLayerLocation(Palladium.id("humanoid"), "fez"), "fez").slot(AccessorySlot.HAT));

    public static final RegistrySupplier<Accessory> ANTENNA = ACCESSORIES.register("antenna",
            () -> new HumanoidModelOverlay(new ModelLayerLocation(Palladium.id("humanoid"), "antenna"), "antenna").slot(AccessorySlot.HAT));

    public static final RegistrySupplier<Accessory> KRUSTY_KRAB_HAT = ACCESSORIES.register("krusty_krab_hat",
            () -> new HumanoidModelOverlay(new ModelLayerLocation(Palladium.id("humanoid"), "krusty_krab_hat"), "krusty_krab_hat").slot(AccessorySlot.HAT));

    public static final RegistrySupplier<Accessory> SEA_PICKLE_HAT = ACCESSORIES.register("sea_pickle_hat", SeaPickleHatAccessory::new);

    public static final RegistrySupplier<Accessory> WINTER_SOLDIER_ARM = ACCESSORIES.register("winter_soldier_arm",
            () -> new OverlayAccessory("winter_soldier_arms", "winter_soldier_slim_arms").onlyRenderSlot().slot(AccessorySlot.MAIN_ARM, AccessorySlot.OFF_ARM));

    public static final RegistrySupplier<Accessory> HYPERION_ARM = ACCESSORIES.register("hyperion_arm",
            () -> new OverlayAccessory("hyperion_arms", "hyperion_slim_arms").onlyRenderSlot().slot(AccessorySlot.MAIN_ARM, AccessorySlot.OFF_ARM));

    public static final RegistrySupplier<Accessory> MECHANICAL_ARM = ACCESSORIES.register("mechanical_arm",
            () -> new HumanoidModelOverlay(new ModelLayerLocation(Palladium.id("player"), "mechanical_arms"), new ModelLayerLocation(Palladium.id("player_slim"), "mechanical_arms"), "mechanical_arm", "mechanical_slim_arm").onlyRenderSlot().slot(AccessorySlot.MAIN_ARM, AccessorySlot.OFF_ARM));

    public static final RegistrySupplier<Accessory> HAMMOND_CANE = ACCESSORIES.register("hammond_cane",
            () -> new HumanoidModelOverlay(new ModelLayerLocation(Palladium.id("humanoid"), "hammond_cane"), "hammond_cane").handVisibilityFix().slot(AccessorySlot.MAIN_HAND, AccessorySlot.OFF_HAND));

    public static final RegistrySupplier<Accessory> WOODEN_LEG = ACCESSORIES.register("wooden_leg", WoodenLegAccessory::new);

}
