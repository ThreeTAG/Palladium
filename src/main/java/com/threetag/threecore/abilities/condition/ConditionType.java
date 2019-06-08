package com.threetag.threecore.abilities.condition;

import com.threetag.threecore.ThreeCore;
import com.threetag.threecore.abilities.Ability;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.function.Function;

/**
 * Created by Nictogen on 2019-06-08.
 */
@Mod.EventBusSubscriber(modid = ThreeCore.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ConditionType extends ForgeRegistryEntry<ConditionType>
{
	public static IForgeRegistry<ConditionType> REGISTRY;

	public static final ConditionType ACTION = new ConditionType(ConditionAction::new, ThreeCore.MODID, "action");
	public static final ConditionType HELD = new ConditionType(ConditionHeld::new, ThreeCore.MODID, "held");
	public static final ConditionType TOGGLE = new ConditionType(ConditionToggle::new, ThreeCore.MODID, "toggle");
	public static final ConditionType COOLDOWN = new ConditionType(ConditionCooldown::new, ThreeCore.MODID, "cooldown");
	public static final ConditionType ABILITY_ENABLED = new ConditionType(ConditionAbilityEnabled::new, ThreeCore.MODID, "ability_enabled");
	public static final ConditionType ABILITY_UNLOCKED = new ConditionType(ConditionAbilityUnlocked::new, ThreeCore.MODID, "ability_unlocked");
	public static final ConditionType KARMA = new ConditionType(ConditionKarma::new, ThreeCore.MODID, "karma");

	@SubscribeEvent
	public static void onRegisterNewRegistries(RegistryEvent.NewRegistry e) {
		REGISTRY = new RegistryBuilder<ConditionType>().setName(new ResourceLocation(ThreeCore.MODID, "condition_types")).setType(ConditionType.class).setIDRange(0, 2048).create();
	}

	@SubscribeEvent
	public static void onRegisterConditionTypes(RegistryEvent.Register<ConditionType> e) {
		e.getRegistry().register(ACTION);
		e.getRegistry().register(HELD);
		e.getRegistry().register(TOGGLE);
		e.getRegistry().register(COOLDOWN);
		e.getRegistry().register(ABILITY_ENABLED);
		e.getRegistry().register(ABILITY_UNLOCKED);
		e.getRegistry().register(KARMA);
	}

	//TODO HTML file

	// ----------------------------------------------------------------------------------------------------------------

	private Function<Ability, Condition> function;

	public ConditionType(Function<Ability, Condition> function) {
		this.function = function;
	}

	public ConditionType(Function<Ability, Condition> function, String modid, String name) {
		this.function = function;
		this.setRegistryName(modid, name);
	}

	public Condition create(Ability ability) {
		return this.function.apply(ability);
	}
}
