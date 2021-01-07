package net.threetag.threecore.ability.condition;

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
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.ability.AbilitiesLockedCondition;
import net.threetag.threecore.ability.Ability;
import net.threetag.threecore.ability.AbilityType;
import net.threetag.threecore.ability.AttributeModifierAbility;
import net.threetag.threecore.util.documentation.DocumentationBuilder;
import net.threetag.threecore.util.threedata.ThreeData;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static net.threetag.threecore.util.documentation.DocumentationBuilder.*;

@Mod.EventBusSubscriber(modid = ThreeCore.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ConditionType extends ForgeRegistryEntry<ConditionType> {
    public static IForgeRegistry<ConditionType> REGISTRY;

    public static final ConditionType ACTION = new ConditionType(ActionCondition::new, ThreeCore.MODID, "action");
    public static final ConditionType HELD = new ConditionType(HeldCondition::new, ThreeCore.MODID, "held");
    public static final ConditionType TOGGLE = new ConditionType(ToggleCondition::new, ThreeCore.MODID, "toggle");
    public static final ConditionType COOLDOWN = new ConditionType(CooldownCondition::new, ThreeCore.MODID, "cooldown");
    public static final ConditionType ABILITY_ENABLED = new ConditionType(AbilityEnabledCondition::new, ThreeCore.MODID, "ability_enabled");
    public static final ConditionType ABILITY_UNLOCKED = new ConditionType(AbilityUnlockedCondition::new, ThreeCore.MODID, "ability_unlocked");
    public static final ConditionType KARMA = new ConditionType(KarmaCondition::new, ThreeCore.MODID, "karma");
    public static final ConditionType XP_BUY = new ConditionType(XPBuyableAbilityCondition::new, ThreeCore.MODID, "xp_buy");
    public static final ConditionType ITEM_BUY = new ConditionType(ItemBuyableAbilityCondition::new, ThreeCore.MODID, "item_buy");
    public static final ConditionType EQUIPMENT_SLOT = new ConditionType(EquipmentSlotCondition::new, ThreeCore.MODID, "equipment_slot");
    public static final ConditionType XP = new ConditionType(XPCondition::new, ThreeCore.MODID, "xp");
    public static final ConditionType WEARING_ITEM = new ConditionType(WearingItemCondition::new, ThreeCore.MODID, "wearing_item");
    public static final ConditionType WEARING_ITEM_TAG = new ConditionType(WearingItemTagCondition::new, ThreeCore.MODID, "wearing_item_tag");
    public static final ConditionType ITEM_INTEGER_NBT = new ConditionType(ItemIntegerNbtCondition::new, ThreeCore.MODID, "item_integer_nbt");
    public static final ConditionType EYES_IN_FLUID = new ConditionType(EyesInFluidCondition::new, ThreeCore.MODID, "eyes_in_fluid");
    public static final ConditionType SIZE = new ConditionType(SizeCondition::new, ThreeCore.MODID, "size");
    public static final ConditionType ABILITIES_LOCKED = new ConditionType(AbilitiesLockedCondition::new, ThreeCore.MODID, "abilities_locked");
    public static final ConditionType LIVING_VALUES = new ConditionType(LivingValuesCondition::new, ThreeCore.MODID, "living_values");
    public static final ConditionType POTION = new ConditionType(PotionCondition::new, ThreeCore.MODID, "has_potions");
    public static final ConditionType THREE_DATA = new ConditionType(ThreeDataCondition::new, ThreeCore.MODID, "three_data");
    public static final ConditionType EXCLUSIVE = new ConditionType(ExclusiveCondition::new, ThreeCore.MODID, "exclusive");
    public static final ConditionType COMBO = new ConditionType(ComboCondition::new, ThreeCore.MODID, "combo");
    public static final ConditionType CURIOS_SLOT = new ConditionType(CuriosSlotCondition::new, ThreeCore.MODID, "curios_slot");
    public static final ConditionType WEARING_CURIOS = new ConditionType(WearingCuriosCondition::new, ThreeCore.MODID, "wearing_curios");

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
        e.getRegistry().register(XP_BUY);
        e.getRegistry().register(ITEM_BUY);
        e.getRegistry().register(EQUIPMENT_SLOT);
        e.getRegistry().register(XP);
        e.getRegistry().register(WEARING_ITEM);
        e.getRegistry().register(WEARING_ITEM_TAG);
        e.getRegistry().register(ITEM_INTEGER_NBT);
        e.getRegistry().register(EYES_IN_FLUID);
        e.getRegistry().register(SIZE);
        e.getRegistry().register(ABILITIES_LOCKED);
        e.getRegistry().register(LIVING_VALUES);
        e.getRegistry().register(POTION);
        e.getRegistry().register(THREE_DATA);
        e.getRegistry().register(EXCLUSIVE);
        e.getRegistry().register(COMBO);
        e.getRegistry().register(CURIOS_SLOT);
        e.getRegistry().register(WEARING_CURIOS);
    }

    @OnlyIn(Dist.CLIENT)
    public static void generateDocumentation() {
        Map<String, List<Condition>> sorted = new HashMap<>();

        // Sort abilities by mods
        Ability ability_ = new AttributeModifierAbility();
        for (ConditionType types : REGISTRY.getValues()) {
            Condition condition = types.create(ability_);
            String modName = AbilityType.getModContainerFromId(types.getRegistryName().getNamespace()) != null ? AbilityType.getModContainerFromId(types.getRegistryName().getNamespace()).getDisplayName() : types.getRegistryName().getNamespace();
            List<Condition> modsConditions = sorted.containsKey(modName) ? sorted.get(modName) : new ArrayList<>();
            modsConditions.add(condition);
            sorted.put(modName, modsConditions);
        }

        DocumentationBuilder.HTMLObject overview;
        DocumentationBuilder builder = new DocumentationBuilder(new ResourceLocation(ThreeCore.MODID, "conditions"), "Conditions")
                .addStyle(".json-block { background-color: lightgray; display: inline-block; border: 5px solid darkgray; padding: 10px }")
                .add(heading("Conditions")).add(hr())
                .add(overview = paragraph(subHeading("Overview")));

        sorted.forEach((mod, conditions) -> {
            overview.add(subSubHeading(mod));
            overview.add(list(conditions.stream().map(condition -> link(StringUtils.stripControlCodes(condition.getDisplayName().getString()), "#" + condition.type.getRegistryName().toString())).collect(Collectors.toList())));
        });

        sorted.values().forEach(modConditions -> {
            modConditions.forEach(condition -> {
                List<Iterable<?>> data = new LinkedList<>();
                List<ThreeData<?>> dataList = condition.getDataManager().getSettingData();
                StringBuilder jsonText = new StringBuilder("[{\"type\":\"").append(condition.type.getRegistryName().toString()).append("\",");

                for (int i = 0; i < dataList.size(); i++) {
                    ThreeData threeData = dataList.get(i);
                    String s = threeData.getJsonString(condition.getDataManager().getDefaultValue(threeData));
                    List<Object> settings = new LinkedList<>();
                    settings.add(threeData.getJsonKey());
                    settings.add(threeData.getType().getTypeName().substring(threeData.getType().getTypeName().lastIndexOf(".") + 1));
                    settings.add(new HTMLObject("code", s));
                    settings.add((threeData.getDescription() == null || threeData.getDescription().isEmpty() ? "/" : threeData.getDescription()));
                    data.add(settings);
                    jsonText.append("  \"").append(threeData.getJsonKey()).append("\": ").append(s).append(i < dataList.size() - 1 ? "," : "");
                }
                jsonText.append("}]");

                builder.add(hr()).add(div().setId(condition.type.getRegistryName().toString())
                        .add(subHeading(StringUtils.stripControlCodes(condition.getDisplayName().getString())).add(new HTMLObject("code", condition.type.getRegistryName().toString())))
                        .add(subSubHeading("Data Settings:"))
                        .add(table(Arrays.asList("Setting", "Type", "Fallback Value", "Description"), data))
                        .add(subSubHeading("Example:"))
                        .add(new HTMLObject("pre").addAttribute("class", "json-block").setId(condition.type.getRegistryName().toString() + "_example"))
                        .add(js("var json = JSON.parse('" + jsonText.toString() + "'); document.getElementById('" + condition.type.getRegistryName().toString() + "_example').innerHTML = JSON.stringify(json, undefined, 2);")));
            });
        });

        builder.save();
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
