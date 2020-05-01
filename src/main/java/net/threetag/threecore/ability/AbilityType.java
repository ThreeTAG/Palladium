package net.threetag.threecore.ability;

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
    public static final AbilityType ACCELERATING_FLIGHT = new AbilityType(AcceleratingFlightAbility::new, ThreeCore.MODID, "accelerating_flight");
    public static final AbilityType TELEPORT = new AbilityType(TeleportAbility::new, ThreeCore.MODID, "teleport");
    public static final AbilityType ATTRIBUTE_MODIFIER = new AbilityType(AttributeModifierAbility::new, ThreeCore.MODID, "attribute_modifier");
    public static final AbilityType INVISIBILITY = new AbilityType(InvisibilityAbility::new, ThreeCore.MODID, "invisibility");
    public static final AbilityType SLOWFALL = new AbilityType(SlowfallAbility::new, ThreeCore.MODID, "slowfall");
    public static final AbilityType WATER_BREATHING = new AbilityType(WaterBreathingAbility::new, ThreeCore.MODID, "water_breathing");
    public static final AbilityType SIZE_CHANGE = new AbilityType(SizeChangeAbility::new, ThreeCore.MODID, "size_change");
    public static final AbilityType CUSTOM_HOTBAR = new AbilityType(CustomHotbarAbility::new, ThreeCore.MODID, "custom_hotbar");
    public static final AbilityType OPENING_NBT_TIMER = new AbilityType(OpeningNbtTimerAbility::new, ThreeCore.MODID, "opening_nbt_timer");
    public static final AbilityType MODEL_LAYER = new AbilityType(ModelLayerAbility::new, ThreeCore.MODID, "model_layer");
    public static final AbilityType PROJECTILE = new AbilityType(ProjectileAbility::new, ThreeCore.MODID, "projectile");
    public static final AbilityType DAMAGE_IMMUNITY = new AbilityType(DamageImmunityAbility::new, ThreeCore.MODID, "damage_immunity");
    public static final AbilityType POTION_EFFECT = new AbilityType(PotionEffectAbility::new, ThreeCore.MODID, "potion_effect");
    public static final AbilityType MULTI_JUMP = new AbilityType(MultiJumpAbility::new, ThreeCore.MODID, "multi_jump");
    public static final AbilityType NAME_CHANGE = new AbilityType(NameChangeAbility::new, ThreeCore.MODID, "name_change");

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
        e.getRegistry().register(ACCELERATING_FLIGHT);
        e.getRegistry().register(TELEPORT);
        e.getRegistry().register(ATTRIBUTE_MODIFIER);
        e.getRegistry().register(INVISIBILITY);
        e.getRegistry().register(SLOWFALL);
        e.getRegistry().register(WATER_BREATHING);
        e.getRegistry().register(SIZE_CHANGE);
        e.getRegistry().register(CUSTOM_HOTBAR);
        e.getRegistry().register(OPENING_NBT_TIMER);
        e.getRegistry().register(MODEL_LAYER);
        e.getRegistry().register(PROJECTILE);
        e.getRegistry().register(DAMAGE_IMMUNITY);
        e.getRegistry().register(POTION_EFFECT);
        e.getRegistry().register(MULTI_JUMP);
        e.getRegistry().register(NAME_CHANGE);
    }

    @OnlyIn(Dist.CLIENT)
    public static void generateHtmlFile(File file) {
        try {
            if (!file.getParentFile().exists())
                file.getParentFile().mkdirs();

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
                    String s = threeData.getJsonString(ability.getDataManager().getDefaultValue(threeData));
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
                    String s = threeData.getJsonString(ability.getDataManager().getDefaultValue(threeData));
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

            ThreeCore.LOGGER.info("Successfully generated " + file.getName() + "!");
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
