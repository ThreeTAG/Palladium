package net.threetag.threecore.abilities;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.util.threedata.ThreeData;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = ThreeCore.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AbilityType extends ForgeRegistryEntry<AbilityType> {

    public static IForgeRegistry<AbilityType> REGISTRY;

    public static final AbilityType DUMMY = new AbilityType(DummyAbility::new, ThreeCore.MODID, "dummy");
    public static final AbilityType COMMAND = new AbilityType(CommandAbility::new, ThreeCore.MODID, "command");
    public static final AbilityType HEALING = new AbilityType(HealingAbility::new, ThreeCore.MODID, "healing");
    public static final AbilityType FLIGHT = new AbilityType(FlightAbility::new, ThreeCore.MODID, "flight");
    public static final AbilityType TELEPORT = new AbilityType(TeleportAbility::new, ThreeCore.MODID, "teleport");
    public static final AbilityType ATTRIBUTE_MODIFIER = new AbilityType(AttributeModifierAbility::new, ThreeCore.MODID, "attribute_modifier");
    public static final AbilityType INVISIBILITY = new AbilityType(InvisibilityAbility::new, ThreeCore.MODID, "invisibility");
    public static final AbilityType SLOWFALL = new AbilityType(SlowfallAbility::new, ThreeCore.MODID, "slowfall");
    public static final AbilityType WATER_BREATHING = new AbilityType(WaterBreathingAbility::new, ThreeCore.MODID, "water_breathing");
    public static final AbilityType SIZE_CHANGE = new AbilityType(SizeChangeAbility::new, ThreeCore.MODID, "size_change");

    @SubscribeEvent
    public static void onRegisterNewRegistries(RegistryEvent.NewRegistry e) {
        REGISTRY = new RegistryBuilder<AbilityType>().setName(new ResourceLocation(ThreeCore.MODID, "ability_types")).setType(AbilityType.class).setIDRange(0, 2048).create();
    }

    @SubscribeEvent
    public static void onRegisterAbilityTypes(RegistryEvent.Register<AbilityType> e) {
        e.getRegistry().register(DUMMY);
        e.getRegistry().register(COMMAND);
        e.getRegistry().register(HEALING);
        e.getRegistry().register(FLIGHT);
        e.getRegistry().register(TELEPORT);
        e.getRegistry().register(ATTRIBUTE_MODIFIER);
        e.getRegistry().register(INVISIBILITY);
        e.getRegistry().register(SLOWFALL);
        e.getRegistry().register(WATER_BREATHING);
        e.getRegistry().register(SIZE_CHANGE);
    }

    @OnlyIn(Dist.CLIENT)
    public static void generateHtmlFile(File file) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write("<html><head><title>Abilities</title><style>\n" +
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
                    "</style><link rel=\"shortcut icon\" type=\"image/x-icon\" href=\"https://i.imgur.com/am80ox1.png\">" +
                    "</head><body>");

            List<Ability> abilities = new ArrayList<>();
            Map<String, List<Ability>> sorted = new HashMap<>();

            // Sort abilities by mods
            for (AbilityType types : REGISTRY.getValues()) {
                Ability ability = types.create();
                abilities.add(ability);
                String modName = getModContainerFromId(types.getRegistryName().getNamespace()) != null ? getModContainerFromId(types.getRegistryName().getNamespace()).getDisplayName() : types.getRegistryName().getNamespace();
                List<Ability> modsAbilities = sorted.containsKey(modName) ? sorted.get(modName) : new ArrayList<>();
                modsAbilities.add(ability);
                sorted.put(modName, modsAbilities);
            }

            // Generate overview lists
            sorted.forEach((s, l) -> {
                try {
                    bw.write("<h1>" + s + "</h1>\n");
                    bw.write("<ul>\n");
                    for (Ability ability : l) {
                        bw.write("<li><a href=\"#" + ability.type.getRegistryName().toString() + "\">" + StringUtils.stripControlCodes(ability.dataManager.get(Ability.TITLE).getFormattedText()) + "</a></li>\n");
                    }
                    bw.write("</ul>\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            bw.write("\n");

            // Write ability info
            for (Ability ability : abilities) {
                AbilityType entry = ability.type;
                bw.write("<hr>\n");

                // Title
                bw.write("<p><h1 id=\"" + entry.getRegistryName().toString() + "\">" + StringUtils.stripControlCodes(ability.dataManager.get(Ability.TITLE).getFormattedText()) + "</h1>\n");
                bw.write("<h3>" + entry.getRegistryName().toString() + "</h3>\n");
                List<ThreeData<?>> dataList = ability.getDataManager().getSettingData();

                // Example
                bw.write("<p>Example:<br>\n");

                StringBuilder jsonText = new StringBuilder("{\"example_ability\":{\"ability\":\"").append(entry.getRegistryName().toString()).append("\",");
                for (int i = 0; i < dataList.size(); i++) {
                    ThreeData threeData = dataList.get(i);
                    Object value = threeData.getDisplay(ability.getDataManager().getDefaultValue(threeData));
                    String s = threeData.displayAsString(ability.getDataManager().getDefaultValue(threeData)) ? "\"" + value.toString() + "\"" : value.toString();
                    jsonText.append("  \"").append(threeData.getJsonKey()).append("\": ").append(s).append(i < dataList.size() - 1 ? "," : "");
                }
                jsonText.append("}}");

                bw.write("<code><pre id=\"" + entry.getRegistryName().toString() + "_example\"></pre></code>");

                bw.write("<script> var json = JSON.parse('" + jsonText.toString() + "');");
                bw.write("document.getElementById('" + entry.getRegistryName().toString() + "_example').innerHTML = JSON.stringify(json, undefined, 2);</script>");

                bw.write("\n");

                // Table
                bw.write("<table>\n<tr><th>Setting</th><th>Type</th><th>Default</th><th>Description</th></tr>\n");
                for (ThreeData threeData : dataList) {
                    Object value = threeData.getDisplay(ability.getDataManager().getDefaultValue(threeData));
                    String s = threeData.displayAsString(ability.getDataManager().getDefaultValue(threeData)) ? "\"" + value.toString() + "\"" : value.toString() + "";
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

            ThreeCore.LOGGER.info("Successfully generated abilities.html!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ModInfo getModContainerFromId(String modid) {
        for (ModInfo modInfo : ModList.get().getMods()) {
            if (modInfo.getModId().equals(modid)) {
                return modInfo;
            }
        }

        return null;
    }


    // ----------------------------------------------------------------------------------------------------------------

    private Supplier<Ability> supplier;

    public AbilityType(Supplier<Ability> supplier) {
        this.supplier = supplier;
    }

    public AbilityType(Supplier<Ability> supplier, String modid, String name) {
        this.supplier = supplier;
        this.setRegistryName(modid, name);
    }

    public Ability create() {
        return this.supplier.get();
    }

}
