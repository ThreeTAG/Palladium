package net.threetag.palladium.power.ability;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec2;
import net.threetag.palladium.icon.Icon;
import net.threetag.palladium.icon.ItemIcon;
import net.threetag.palladium.util.PalladiumCodecs;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class AbilityProperties {

    public static final AbilityProperties BASIC = new AbilityProperties();

    public static final Codec<AbilityProperties> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ComponentSerialization.CODEC.optionalFieldOf("title").forGetter(p -> Optional.ofNullable(p.title)),
            Icon.CODEC.optionalFieldOf("icon", new ItemIcon(Items.BARRIER)).forGetter(AbilityProperties::getIcon),
            AbilityDescription.CODEC.optionalFieldOf("description").forGetter(p -> Optional.ofNullable(p.description)),
            AbilityColor.CODEC.optionalFieldOf("color", AbilityColor.GRAY).forGetter(AbilityProperties::getColor),
            Codec.BOOL.optionalFieldOf("hidden_in_gui", false).forGetter(AbilityProperties::isHiddenInGUI),
            Codec.BOOL.optionalFieldOf("hidden_in_bar", false).forGetter(AbilityProperties::isHiddenInBar),
            Codec.intRange(-1, Integer.MAX_VALUE).optionalFieldOf("list_index", -1).forGetter(AbilityProperties::getListIndex),
            PalladiumCodecs.VEC2_CODEC.optionalFieldOf("gui_position").forGetter(p -> Optional.ofNullable(p.guiPosition)),
            AnimationTimerSetting.CODEC.optionalFieldOf("animation_timer").forGetter(p -> Optional.ofNullable(p.animationTimerSetting)),
            PalladiumCodecs.listOrPrimitive(Identifier.CODEC).optionalFieldOf("render_layer", Collections.emptyList()).forGetter(p -> p.renderLayers),
            Codec.BOOL.optionalFieldOf("allow_dampening", true).forGetter(p -> p.allowDampening)
    ).apply(instance, (title, icon, desc, color, hiddenGui, hiddenBar, listIndex, guiPos, timer, renderLayers, allowDampening) ->
            new AbilityProperties(title.orElse(null), icon, desc.orElse(null), color, hiddenGui, hiddenBar, listIndex, guiPos.orElse(null), timer.orElse(null), renderLayers, allowDampening)));

    private Component title = null;
    private Icon icon = new ItemIcon(Items.BARRIER);
    private AbilityDescription description = null;
    private AbilityColor color = AbilityColor.GRAY;
    private boolean hiddenInGUI = false;
    private boolean hiddenInBar = false;
    private int listIndex = -1;
    private Vec2 guiPosition = null;
    private AnimationTimerSetting animationTimerSetting = null;
    private List<Identifier> renderLayers = Collections.emptyList();
    private boolean allowDampening = true;

    public AbilityProperties() {

    }

    public AbilityProperties(Component title, Icon icon, AbilityDescription description, AbilityColor color,
                              boolean hiddenInGUI, boolean hiddenInBar, int listIndex, Vec2 guiPosition,
                              AnimationTimerSetting animationTimerSetting, List<Identifier> renderLayers,
                              boolean allowDampening) {
        this.title = title;
        this.icon = icon;
        this.description = description;
        this.color = color;
        this.hiddenInGUI = hiddenInGUI;
        this.hiddenInBar = hiddenInBar;
        this.listIndex = listIndex;
        this.guiPosition = guiPosition;
        this.animationTimerSetting = animationTimerSetting;
        this.renderLayers = ImmutableList.copyOf(renderLayers);
        this.allowDampening = allowDampening;
    }

    public AbilityProperties title(Component title) {
        this.title = title;
        return this;
    }

    public AbilityProperties icon(Icon icon) {
        this.icon = icon;
        return this;
    }

    public AbilityProperties description(AbilityDescription description) {
        this.description = description;
        return this;
    }

    public AbilityProperties color(AbilityColor color) {
        this.color = color;
        return this;
    }

    public AbilityProperties hiddenInGUI(boolean hiddenInGUI) {
        this.hiddenInGUI = hiddenInGUI;
        return this;
    }

    public AbilityProperties hiddenInBar(boolean hiddenInBar) {
        this.hiddenInBar = hiddenInBar;
        return this;
    }

    public AbilityProperties listIndex(int listIndex) {
        this.listIndex = listIndex;
        return this;
    }

    public AbilityProperties guiPosition(Vec2 guiPosition) {
        this.guiPosition = guiPosition;
        return this;
    }

    public AbilityProperties animationTimer(AnimationTimerSetting setting) {
        this.animationTimerSetting = setting;
        return this;
    }

    public List<Identifier> getRenderLayers() {
        return this.renderLayers;
    }

    @Nullable
    public Component getTitle() {
        return this.title;
    }

    public Icon getIcon() {
        return this.icon;
    }

    public AbilityDescription getDescription() {
        return this.description;
    }

    public AbilityColor getColor() {
        return this.color;
    }

    public boolean isHiddenInGUI() {
        return this.hiddenInGUI;
    }

    public boolean isHiddenInBar() {
        return this.hiddenInBar;
    }

    public int getListIndex() {
        return this.listIndex;
    }

    public Vec2 getGuiPosition() {
        return this.guiPosition;
    }

    public AnimationTimerSetting getAnimationTimerSetting() {
        return this.animationTimerSetting;
    }

    public boolean canBeDampened() {
        return this.allowDampening;
    }
}
