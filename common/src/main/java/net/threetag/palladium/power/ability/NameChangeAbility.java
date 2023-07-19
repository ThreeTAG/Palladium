package net.threetag.palladium.power.ability;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.util.property.BooleanProperty;
import net.threetag.palladium.util.property.ComponentProperty;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.PropertyManager;
import net.threetag.palladiumcore.util.PlayerUtil;

public class NameChangeAbility extends Ability {

    public static final PalladiumProperty<Component> NAME = new ComponentProperty("name").configurable("The name the player's one will turn into");
    public static final PalladiumProperty<Boolean> ACTIVE = new BooleanProperty("active");

    public NameChangeAbility() {
        this.withProperty(NAME, Component.literal("John Doe"));
    }

    @Override
    public void registerUniqueProperties(PropertyManager manager) {
        manager.register(ACTIVE, false);
    }

    @Override
    public void firstTick(LivingEntity entity, AbilityEntry entry, IPowerHolder holder, boolean enabled) {
        if (entity instanceof Player player) {
            entry.setUniqueProperty(ACTIVE, true);
            PlayerUtil.refreshDisplayName(player);
        }
    }

    @Override
    public void lastTick(LivingEntity entity, AbilityEntry entry, IPowerHolder holder, boolean enabled) {
        if (entity instanceof Player player) {
            entry.setUniqueProperty(ACTIVE, false);
            PlayerUtil.refreshDisplayName(player);
        }
    }

    @Override
    public boolean isEffect() {
        return true;
    }

    @Override
    public String getDocumentationDescription() {
        return "Lets you change the name of the player (in chat, tablist, above head).";
    }
}
