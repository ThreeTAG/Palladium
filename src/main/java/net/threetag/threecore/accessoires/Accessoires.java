package net.threetag.threecore.accessoires;

import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.threetag.threecore.ThreeCore;

public class Accessoires {

    public static DeferredRegister<Accessoire> ACCESSOIRES = new DeferredRegister<>(Accessoire.REGISTRY, ThreeCore.MODID);

    public static final RegistryObject<Accessoire> WINTER_SOLDIER_ARM = ACCESSOIRES.register("winter_soldier_arm", WinterSoldierArmAccessoire::new);
    public static final RegistryObject<Accessoire> HEROBRINE_EYES = ACCESSOIRES.register("herobrine_eyes", HerobrineEyesAccessoire::new);
    public static final RegistryObject<Accessoire> WOODEN_LEG = ACCESSOIRES.register("wooden_leg", WoodenLegAccessoire::new);
    public static final RegistryObject<Accessoire> HYPERION_ARM = ACCESSOIRES.register("hyperion_arm", HyperionArmAccessoire::new);
    public static final RegistryObject<Accessoire> STRAWHAT = ACCESSOIRES.register("strawhat", StrawhatAccessoire::new);
    public static final RegistryObject<Accessoire> JAY_GARRICK_HELMET = ACCESSOIRES.register("jay_garrick_helmet", JayGarrickHelmetAccessoire::new);
    public static final RegistryObject<Accessoire> HALO = ACCESSOIRES.register("halo", HaloAccessoire::new);
    public static final RegistryObject<Accessoire> WINGS = ACCESSOIRES.register("wings", WingsAccessoire::new);

}
