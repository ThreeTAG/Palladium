package com.threetag.threecore.abilities.condition;

import com.threetag.threecore.ThreeCore;
import com.threetag.threecore.abilities.Ability;
import com.threetag.threecore.abilities.AbilityType;
import com.threetag.threecore.abilities.AttributeModifierAbility;
import com.threetag.threecore.abilities.data.ThreeData;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Created by Nictogen on 2019-06-08.
 */
@Mod.EventBusSubscriber(modid = ThreeCore.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ConditionType extends ForgeRegistryEntry<ConditionType>
{
	public static IForgeRegistry<ConditionType> REGISTRY;

	public static final ConditionType ACTION = new ConditionType(ActionCondition::new, ThreeCore.MODID, "action");
	public static final ConditionType HELD = new ConditionType(HeldCondition::new, ThreeCore.MODID, "held");
	public static final ConditionType TOGGLE = new ConditionType(ToggleCondition::new, ThreeCore.MODID, "toggle");
	public static final ConditionType COOLDOWN = new ConditionType(CooldownCondition::new, ThreeCore.MODID, "cooldown");
	public static final ConditionType ABILITY_ENABLED = new ConditionType(AbilityEnabledCondition::new, ThreeCore.MODID, "ability_enabled");
	public static final ConditionType ABILITY_UNLOCKED = new ConditionType(AbilityUnlockedCondition::new, ThreeCore.MODID, "ability_unlocked");
	public static final ConditionType KARMA = new ConditionType(KarmaCondition::new, ThreeCore.MODID, "karma");

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

	@OnlyIn(Dist.CLIENT)
	public static void generateHtmlFile(File file) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			bw.write("<html><head><title>Conditions</title><style>\n" +
					"table{font-family:arial, sans-serif;border-collapse:collapse;}\n" +
					"td,th{border:1px solid #666666;text-align:left;padding:8px;min-width:45px;}\n" +
					"th{background-color:#CCCCCC;}\n" +
					"p{margin:0;}\n" +
					"tr:nth-child(even){background-color:#D8D8D8;}\n" +
					"tr:nth-child(odd){background-color:#EEEEEE;}\n" +
					"td.true{background-color:#72FF85AA;}\n" +
					"td.false{background-color:#FF6666AA;}\n" +
					"td.other{background-color:#42A3FFAA;}\n" +
					"td.error{color:#FF0000;}\n" +
					"th,td.true,td.false,td.other{text-align:center;}\n" +
					"</style></head><body>");

			List<Condition> conditions = new ArrayList<>();
			Map<String, List<Condition>> sorted = new HashMap<>();

			// Sort conditions by mods
			Ability ability_ = new AttributeModifierAbility();
			for (ConditionType types : REGISTRY.getValues()) {
				Condition condition = types.create(ability_);
				conditions.add(condition);
				String modName = AbilityType.getModContainerFromId(types.getRegistryName().getNamespace()) != null ? AbilityType.getModContainerFromId(types.getRegistryName().getNamespace()).getDisplayName() : types.getRegistryName().getNamespace();
				List<Condition> modsConditions = sorted.containsKey(modName) ? sorted.get(modName) : new ArrayList<>();
				modsConditions.add(condition);
				sorted.put(modName, modsConditions);
			}

			// Generate overview lists
			sorted.forEach((s, l) -> {
				try {
					bw.write("<h1>" + s + "</h1>\n");
					bw.write("<ul>\n");
					for (Condition condition : l) {
						bw.write("<li><a href=\"#" + condition.type.getRegistryName().toString() + "\">" + StringUtils.stripControlCodes(condition.dataManager.get(Condition.TITLE).getFormattedText()) + "</a></li>\n");
					}
					bw.write("</ul>\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
			});

			bw.write("\n");

			// Write ability info
			for (Condition condition : conditions) {
				ConditionType entry = condition.type;
				bw.write("<hr>\n");

				// Title
				bw.write("<p><h1 id=\"" + entry.getRegistryName().toString() + "\">" + StringUtils.stripControlCodes(condition.dataManager.get(Condition.TITLE).getFormattedText()) + "</h1>\n");
				bw.write("<h3>" + entry.getRegistryName().toString() + "</h3>\n");
				List<ThreeData<?>> dataList = condition.dataManager.getSettingData();

				// Example
				bw.write("<p>Example:<br>\n");

				StringBuilder jsonText = new StringBuilder("[{\"type\":\"").append(entry.getRegistryName().toString()).append("\",");
				for (int i = 0; i < dataList.size(); i++) {
					ThreeData threeData = dataList.get(i);
					Object value = threeData.getDisplay(condition.getDataManager().getDefaultValue(threeData));
					String s = threeData.displayAsString(condition.getDataManager().getDefaultValue(threeData)) ? "\"" + value.toString() + "\"" : value.toString();
					jsonText.append("  \"").append(threeData.getJsonKey()).append("\": ").append(s).append(i < dataList.size() - 1 ? "," : "");
				}
				jsonText.append("}]");

				bw.write("<code><pre id=\"" + entry.getRegistryName().toString() + "_example\"></pre></code>");

				bw.write("<script> var json = JSON.parse('" + jsonText.toString() + "');");
				bw.write("document.getElementById('" + entry.getRegistryName().toString() + "_example').innerHTML = JSON.stringify(json, undefined, 2);</script>");

				bw.write("\n");

				// Table
				bw.write("<table>\n<tr><th>Setting</th><th>Type</th><th>Default</th><th>Description</th></tr>\n");
				for (ThreeData threeData : dataList) {
					Object value = threeData.getDisplay(condition.getDataManager().getDefaultValue(threeData));
					String s = threeData.displayAsString(condition.getDataManager().getDefaultValue(threeData)) ? "\"" + value.toString() + "\"" : value.toString() + "";
					bw.write("<tr>\n" +
							"<td><code>" + threeData.getJsonKey() + "</code></td>\n" +
							"<td><code>" + threeData.getType().getTypeName().substring(threeData.getType().getTypeName().lastIndexOf(".") + 1) + "</code></td>\n" +
							"<td><code>" + s + "</code></td>\n" +
							"<td><p>" + (threeData.getDescription() == null || threeData.getDescription().isEmpty() ? "/" : threeData.getDescription()) + "</p>\n" +
							"</td></tr><br>");
				}
				bw.write("</table>\n\n\n");
			}
			bw.write("</body></html>");
			bw.close();

			ThreeCore.LOGGER.info("Successfully generated conditions.html!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

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
