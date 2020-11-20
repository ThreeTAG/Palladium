package net.threetag.threecore.accessoires;

import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.threetag.threecore.ThreeCore;

public class Accessoires {

    public static DeferredRegister<Accessoire> ACCESSOIRES = DeferredRegister.create(Accessoire.class, ThreeCore.MODID);

    public static final RegistryObject<Accessoire> SUPPORTER_CLOAK = ACCESSOIRES.register("supporter_cloak", SupporterCloakAccessoire::new);
    public static final RegistryObject<Accessoire> LUCRAFT_ARC_REACTOR = ACCESSOIRES.register("lucraft_arc_reactor", LucraftArcReactorAccessoire::new);
    public static final RegistryObject<Accessoire> WINTER_SOLDIER_ARM = ACCESSOIRES.register("winter_soldier_arm", WinterSoldierArmAccessoire::new);
    public static final RegistryObject<Accessoire> HEROBRINE_EYES = ACCESSOIRES.register("herobrine_eyes", HerobrineEyesAccessoire::new);
    public static final RegistryObject<Accessoire> WOODEN_LEG = ACCESSOIRES.register("wooden_leg", WoodenLegAccessoire::new);
    public static final RegistryObject<Accessoire> HYPERION_ARM = ACCESSOIRES.register("hyperion_arm", HyperionArmAccessoire::new);
    public static final RegistryObject<Accessoire> STRAWHAT = ACCESSOIRES.register("strawhat", StrawhatAccessoire::new);
    public static final RegistryObject<Accessoire> HALO = ACCESSOIRES.register("halo", HaloAccessoire::new);
    public static final RegistryObject<Accessoire> WINGS = ACCESSOIRES.register("wings", WingsAccessoire::new);
    public static final RegistryObject<Accessoire> GUZZLER_HELMET = ACCESSOIRES.register("guzzler_helmet", GuzzlerHelmetAccessoire::new);
    public static final RegistryObject<Accessoire> SONIC_HAND = ACCESSOIRES.register("sonic_hand", SonicHandAccessoire::new);
    public static final RegistryObject<Accessoire> HAMMOND_CANE = ACCESSOIRES.register("hammond_cane", HammondCaneAccessoire::new);
    public static final RegistryObject<Accessoire> FACE_MASK = ACCESSOIRES.register("face_mask", FaceMaskAccessoire::new);
    public static final RegistryObject<Accessoire> MECHANICAL_ARM = ACCESSOIRES.register("mechanical_arm", MechanicalArmAccessoire::new);
    public static final RegistryObject<Accessoire> FEZ = ACCESSOIRES.register("fez", FezAccessoire::new);

}
