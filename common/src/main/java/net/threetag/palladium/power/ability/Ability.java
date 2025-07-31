package net.threetag.palladium.power.ability;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec2;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.documentation.HTMLBuilder;
import net.threetag.palladium.documentation.IDefaultDocumentedConfigurable;
import net.threetag.palladium.documentation.JsonDocumentationBuilder;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.util.icon.IIcon;
import net.threetag.palladium.util.icon.ItemIcon;
import net.threetag.palladium.util.property.*;
import net.threetag.palladiumcore.registry.PalladiumRegistry;

import java.util.*;
import java.util.stream.Collectors;

public class Ability implements IDefaultDocumentedConfigurable {

    public static final PalladiumRegistry<Ability> REGISTRY = PalladiumRegistry.create(Ability.class, Palladium.id("abilities"));

    public static final PalladiumProperty<Component> TITLE = new ComponentProperty("title").configurable("Allows you to set a custom title for this ability");
    public static final PalladiumProperty<IIcon> ICON = new IconProperty("icon").configurable("Icon for the ability");
    public static final PalladiumProperty<AbilityDescription> DESCRIPTION = new AbilityDescriptionProperty("description").configurable("Description of the ability. Visible in ability menu");
    public static final PalladiumProperty<AbilityColor> COLOR = new AbilityColorProperty("bar_color").configurable("Changes the color of the ability in the ability bar");
    public static final PalladiumProperty<Boolean> HIDDEN_IN_GUI = new BooleanProperty("hidden").sync(SyncType.SELF).configurable("Determines if the ability is visible in the powers screen");
    public static final PalladiumProperty<Boolean> HIDDEN_IN_BAR = new BooleanProperty("hidden_in_bar").sync(SyncType.SELF).configurable("Determines if the ability is visible in the ability bar on your screen");
    public static final PalladiumProperty<Integer> LIST_INDEX = new IntegerProperty("list_index").configurable("Determines the list index for custom ability lists. Starts at 0. Going beyond 4 (which is the 5th place in the ability) will start a new list. Keeping it at -1 will automatically arrange the abilities.");
    public static final PalladiumProperty<Vec2> GUI_POSITION = new Vec2Property("gui_position").configurable("Position of the ability in the ability menu. Leave null for automatic positioning. 0/0 is center");
    final PropertyManager propertyManager = new PropertyManager();
    private String documentationDescription;

    public Ability() {
        this.withProperty(ICON, new ItemIcon(Items.BLAZE_ROD));
        this.withProperty(TITLE, null);
        this.withProperty(COLOR, AbilityColor.LIGHT_GRAY);
        this.withProperty(HIDDEN_IN_GUI, this.isEffect());
        this.withProperty(HIDDEN_IN_BAR, this.isEffect());
        this.withProperty(LIST_INDEX, -1);
        this.withProperty(GUI_POSITION, null);
        this.withProperty(DESCRIPTION, null);
    }

    public void registerUniqueProperties(PropertyManager manager) {

    }

    public boolean isEffect() {
        return false;
    }

    public boolean isExperimental() {
        return false;
    }

    public void tick(LivingEntity entity, AbilityInstance entry, IPowerHolder holder, boolean enabled) {

    }

    public void firstTick(LivingEntity entity, AbilityInstance entry, IPowerHolder holder, boolean enabled) {

    }

    public void lastTick(LivingEntity entity, AbilityInstance entry, IPowerHolder holder, boolean enabled) {

    }

    public <T> Ability withProperty(PalladiumProperty<T> data, T value) {
        this.propertyManager.register(data, value);
        return this;
    }

    public static HTMLBuilder documentationBuilder() {
        return new HTMLBuilder(new ResourceLocation(Palladium.MOD_ID, "abilities"), "Abilities")
                .add(HTMLBuilder.heading("Abilities"))
                .addDocumentationSettings(REGISTRY.getValues().stream().filter(ab -> !ab.isExperimental()).sorted(Comparator.comparing(o -> o.getId().toString())).collect(Collectors.toList()));
    }

    public static List<AbilityInstance> findParentsWithinHolder(AbilityConfiguration ability, IPowerHolder powerHolder) {
        List<AbilityInstance> list = new ArrayList<>();
        for (String key : ability.getDependencies()) {
            AbilityInstance parent = powerHolder.getAbilities().get(key);

            if (parent != null) {
                list.add(parent);
            }
        }
        return list;
    }

    public static List<AbilityInstance> findChildrenWithinHolder(AbilityConfiguration ability, IPowerHolder powerHolder) {
        List<AbilityInstance> list = new ArrayList<>();
        for (Map.Entry<String, AbilityInstance> entries : powerHolder.getAbilities().entrySet()) {
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
        return REGISTRY.getKey(this);
    }

    public Ability setDocumentationDescription(String documentationDescription) {
        this.documentationDescription = documentationDescription;
        return this;
    }

    public String getDocumentationDescription() {
        return this.documentationDescription;
    }

    @Override
    public void generateDocumentation(JsonDocumentationBuilder builder) {
        IDefaultDocumentedConfigurable.super.generateDocumentation(builder);
        builder.setTitle(this.getId().getPath());

        var desc = this.getDocumentationDescription();
        if (desc != null && !desc.isEmpty()) {
            builder.setDescription(desc);
        }
    }

    public void postParsing(AbilityConfiguration configuration) {
    }
}
