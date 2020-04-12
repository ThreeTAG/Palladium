package net.threetag.threecore.potion;

import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.threetag.threecore.ThreeCore;

public class TCEffects {

    public static final DeferredRegister<Effect> EFFECTS = new DeferredRegister<>(ForgeRegistries.POTIONS, ThreeCore.MODID);

    public static final RegistryObject<Effect> UNCONSCIOUS = EFFECTS.register("unconscious", () -> new UnconsciousEffect(EffectType.BENEFICIAL, 9740385));

}
