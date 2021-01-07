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
import net.threetag.threecore.scripts.events.RegisterAbilityThreeDataScriptEvent;
import net.threetag.threecore.util.documentation.DocumentationBuilder;
import net.threetag.threecore.util.threedata.ThreeData;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static net.threetag.threecore.util.documentation.DocumentationBuilder.*;

@Mod.EventBusSubscriber(modid = ThreeCore.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AbilityType extends ForgeRegistryEntry<AbilityType> {

    public static IForgeRegistry<AbilityType> REGISTRY;

    // TODO use DeferredRegistry

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
    public static final AbilityType HUD = new AbilityType(HUDAbility::new, ThreeCore.MODID, "hud");
    public static final AbilityType SKIN_CHANGE = new AbilityType(SkinChangeAbility::new, ThreeCore.MODID, "skin_change");
    public static final AbilityType HIDE_BODY_PARTS = new AbilityType(HideBodyPartsAbility::new, ThreeCore.MODID, "hide_body_parts");
    public static final AbilityType CHANGE_ABILITY_TAB_TEXTURE = new AbilityType(ChangeAbilityTabTextureAbility::new, ThreeCore.MODID, "change_ability_tab_texture");
    public static final AbilityType ENERGY = new AbilityType(EnergyAbility::new, ThreeCore.MODID, "energy");
    public static final AbilityType DROP_ARMOR = new AbilityType(DropArmorAbility::new, ThreeCore.MODID, "drop_armor");

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
        e.getRegistry().register(HUD);
        e.getRegistry().register(SKIN_CHANGE);
        e.getRegistry().register(HIDE_BODY_PARTS);
        e.getRegistry().register(CHANGE_ABILITY_TAB_TEXTURE);
        e.getRegistry().register(ENERGY);
        e.getRegistry().register(DROP_ARMOR);
    }

    @OnlyIn(Dist.CLIENT)
    public static void generateDocumentation() {
        Map<String, List<Ability>> sorted = new HashMap<>();

        // Sort abilities by mods
        for (AbilityType types : REGISTRY.getValues()) {
            Ability ability = types.create("");
            String modName = getModContainerFromId(types.getRegistryName().getNamespace()) != null ? getModContainerFromId(types.getRegistryName().getNamespace()).getDisplayName() : types.getRegistryName().getNamespace();
            List<Ability> modsAbilities = sorted.containsKey(modName) ? sorted.get(modName) : new ArrayList<>();
            modsAbilities.add(ability);
            sorted.put(modName, modsAbilities);
        }

        DocumentationBuilder.HTMLObject overview;
        DocumentationBuilder builder = new DocumentationBuilder(new ResourceLocation(ThreeCore.MODID, "abilities"), "Abilities")
                .addStyle(".json-block { background-color: lightgray; display: inline-block; border: 5px solid darkgray; padding: 10px }")
                .add(heading("Abilities")).add(hr())
                .add(overview = paragraph(subHeading("Overview")));

        sorted.forEach((mod, abilities) -> {
            overview.add(subSubHeading(mod));
            overview.add(list(abilities.stream().map(ability -> link(StringUtils.stripControlCodes(ability.dataManager.get(Ability.TITLE).getString()), "#" + ability.type.getRegistryName().toString())).collect(Collectors.toList())));
        });

        sorted.values().forEach(modAbilities -> {
            modAbilities.forEach(ability -> {
                List<Iterable<?>> data = new LinkedList<>();
                List<ThreeData<?>> dataList = ability.getDataManager().getSettingData();
                StringBuilder jsonText = new StringBuilder("{\"example_ability\":{\"ability\":\"").append(ability.type.getRegistryName().toString()).append("\",");

                for (int i = 0; i < dataList.size(); i++) {
                    ThreeData threeData = dataList.get(i);
                    String s = threeData.getJsonString(ability.getDataManager().getDefaultValue(threeData));
                    List<Object> settings = new LinkedList<>();
                    settings.add(threeData.getJsonKey());
                    settings.add(threeData.getType().getTypeName().substring(threeData.getType().getTypeName().lastIndexOf(".") + 1));
                    settings.add(new HTMLObject("code", s));
                    settings.add((threeData.getDescription() == null || threeData.getDescription().isEmpty() ? "/" : threeData.getDescription()));
                    data.add(settings);
                    jsonText.append("  \"").append(threeData.getJsonKey()).append("\": ").append(s).append(i < dataList.size() - 1 ? "," : "");
                }
                jsonText.append("}}");

                builder.add(hr()).add(div().setId(ability.type.getRegistryName().toString())
                        .add(subHeading(StringUtils.stripControlCodes(ability.dataManager.get(Ability.TITLE).getString())).add(new HTMLObject("code", ability.type.getRegistryName().toString())))
                        .add(subSubHeading("Data Settings:"))
                        .add(table(Arrays.asList("Setting", "Type", "Fallback Value", "Description"), data))
                        .add(subSubHeading("Example:"))
                        .add(new HTMLObject("pre").addAttribute("class", "json-block").setId(ability.type.getRegistryName().toString() + "_example"))
                        .add(js("var json = JSON.parse('" + jsonText.toString() + "'); document.getElementById('" + ability.type.getRegistryName().toString() + "_example').innerHTML = JSON.stringify(json, undefined, 2);")));
            });
        });

        builder.save();
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

    public Ability create(String id) {
        Ability a = this.supplier.get();
        a.id = id;
        new RegisterAbilityThreeDataScriptEvent(a).fire();
        return a;
    }

}
