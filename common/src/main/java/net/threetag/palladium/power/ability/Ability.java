package net.threetag.palladium.power.ability;

import dev.architectury.core.RegistryEntry;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.Registries;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Items;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.documentation.HTMLBuilder;
import net.threetag.palladium.documentation.IDefaultDocumentedConfigurable;
import net.threetag.palladium.documentation.JsonDocumentationBuilder;
import net.threetag.palladium.power.IPowerHandler;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.PowerManager;
import net.threetag.palladium.util.icon.IIcon;
import net.threetag.palladium.util.icon.ItemIcon;
import net.threetag.palladium.util.property.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Ability extends RegistryEntry<Ability> implements IDefaultDocumentedConfigurable {

    public static final ResourceKey<Registry<Ability>> RESOURCE_KEY = ResourceKey.createRegistryKey(new ResourceLocation(Palladium.MOD_ID, "abilities"));
    public static final Registrar<Ability> REGISTRY = Registries.get(Palladium.MOD_ID).builder(RESOURCE_KEY.location(), new Ability[0]).build();

    public static final PalladiumProperty<Component> TITLE = new ComponentProperty("title").configurable("Allows you to set a custom title for this ability");
    public static final PalladiumProperty<IIcon> ICON = new IconProperty("icon").configurable("Icon for the ability");
    public static final PalladiumProperty<AbilityColor> COLOR = new AbilityColorProperty("bar_color").configurable("Changes the color of the ability in the ability bar");
    public static final PalladiumProperty<Boolean> HIDDEN = new BooleanProperty("hidden").configurable("Determines if the ability is visible in the ability bar and powers screen");
    public static final PalladiumProperty<Integer> LIST_INDEX = new IntegerProperty("list_index").configurable("Determines the list index for custom ability lists. Starts at 0. Going beyond 4 (which is the 5th place in the ability) will start a new list. Keeping it at -1 will automatically arrange the abilities.");

    final PropertyManager propertyManager = new PropertyManager();

    public Ability() {
        this.withProperty(ICON, new ItemIcon(Items.BLAZE_ROD));
        this.withProperty(TITLE, null);
        this.withProperty(COLOR, AbilityColor.LIGHT_GRAY);
        this.withProperty(HIDDEN, this.isEffect());
        this.withProperty(LIST_INDEX, -1);
    }

    public void registerUniqueProperties(PropertyManager manager) {

    }

    public boolean isEffect() {
        return false;
    }

    public void tick(LivingEntity entity, AbilityEntry entry, IPowerHolder holder, boolean enabled) {

    }

    public void firstTick(LivingEntity entity, AbilityEntry entry, IPowerHolder holder, boolean enabled) {

    }

    public void lastTick(LivingEntity entity, AbilityEntry entry, IPowerHolder holder, boolean enabled) {

    }

    public <T> Ability withProperty(PalladiumProperty<T> data, T value) {
        this.propertyManager.register(data, value);
        return this;
    }

    public static Collection<AbilityEntry> getEntries(LivingEntity entity) {
        List<AbilityEntry> entries = new ArrayList<>();
        PowerManager.getPowerHandler(entity).ifPresent(handler -> handler.getPowerHolders().values().stream().map(holder -> holder.getAbilities().values()).forEach(entries::addAll));
        return entries;
    }

    public static Collection<AbilityEntry> getEntries(LivingEntity entity, Ability ability) {
        List<AbilityEntry> entries = new ArrayList<>();
        PowerManager.getPowerHandler(entity).ifPresent(handler -> handler.getPowerHolders().values().stream().map(holder -> holder.getAbilities().values().stream().filter(entry -> entry.getConfiguration().getAbility() == ability).collect(Collectors.toList())).forEach(entries::addAll));
        return entries;
    }

    public static Collection<AbilityEntry> getEnabledEntries(LivingEntity entity, Ability ability) {
        List<AbilityEntry> entries = new ArrayList<>();
        PowerManager.getPowerHandler(entity).ifPresent(handler -> handler.getPowerHolders().values().stream().map(holder -> holder.getAbilities().values().stream().filter(entry -> entry.isEnabled() && entry.getConfiguration().getAbility() == ability).collect(Collectors.toList())).forEach(entries::addAll));
        return entries;
    }

    public static AbilityEntry getEntry(LivingEntity entity, ResourceLocation powerId, String abilityId) {
        Power power = PowerManager.getInstance(entity.level).getPower(powerId);

        if (power == null) {
            return null;
        }

        IPowerHandler handler = PowerManager.getPowerHandler(entity).orElse(null);

        if (handler == null) {
            return null;
        }

        IPowerHolder holder = handler.getPowerHolder(power);

        if (holder == null) {
            return null;
        }

        return holder.getAbilities().get(abilityId);
    }

    public static HTMLBuilder documentationBuilder() {
        return new HTMLBuilder(new ResourceLocation(Palladium.MOD_ID, "abilities"), "Abilities")
                .add(HTMLBuilder.heading("Abilities"))
                .addDocumentationSettings(REGISTRY.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toList()));
    }

    public static List<AbilityEntry> findParentAbilities(LivingEntity entity, AbilityConfiguration ability, IPowerHolder powerHolder) {
        List<AbilityEntry> list = new ArrayList<>();
        for (String key : ability.getDependencies()) {
            AbilityEntry parent = powerHolder.getAbilities().get(key);

            if (parent != null) {
                list.add(parent);
            }
        }
        return list;
    }

    public static List<AbilityEntry> findChildrenAbilities(LivingEntity entity, AbilityConfiguration ability, IPowerHolder powerHolder) {
        List<AbilityEntry> list = new ArrayList<>();
        for (Map.Entry<String, AbilityEntry> entries : powerHolder.getAbilities().entrySet()) {
            for (String key : ability.getDependencies()) {
                if (key.equals(entries.getKey())) {
                    list.add(entries.getValue());
                }
            }
        }
        return list;
    }

    @Override
    public PropertyManager getPropertyManager() {
        return this.propertyManager;
    }

    @Override
    public ResourceLocation getId() {
        return REGISTRY.getId(this);
    }

    @Override
    public void generateDocumentation(JsonDocumentationBuilder builder) {
        IDefaultDocumentedConfigurable.super.generateDocumentation(builder);
        builder.setTitle(new TranslatableComponent("ability." + this.getId().getNamespace() + "." + this.getId().getPath()).getString());
    }
}
