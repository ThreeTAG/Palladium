package net.threetag.palladium.power.ability;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.util.property.ComponentProperty;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.PropertyManager;
import net.threetag.palladium.util.property.SyncType;
import net.threetag.palladiumcore.util.PlayerUtil;

public class NameChangeAbility extends Ability {

    public static final PalladiumProperty<Component> NAME = new ComponentProperty("name").configurable("The name the player's one will turn into");
    public static final PalladiumProperty<Component> NAME_CACHED = new ComponentProperty("name_cached").sync(SyncType.EVERYONE);

    public NameChangeAbility() {
        this.withProperty(NAME, Component.literal("John Doe"));
    }

    @Override
    public void registerUniqueProperties(PropertyManager manager) {
        manager.register(NAME_CACHED, null);
    }

    @Override
    public void firstTick(LivingEntity entity, AbilityInstance entry, IPowerHolder holder, boolean enabled) {
        if (entity instanceof Player player) {
            if (!entity.level().isClientSide) {
                try {
                    entry.setUniqueProperty(NAME_CACHED, ComponentUtils.updateForEntity(player.createCommandSourceStack(), entry.getProperty(NameChangeAbility.NAME), player, 0));
                } catch (CommandSyntaxException e) {
                    entry.setUniqueProperty(NAME_CACHED, entry.getProperty(NameChangeAbility.NAME));
                }
            }

            PlayerUtil.refreshDisplayName(player);
        }
    }

    @Override
    public void lastTick(LivingEntity entity, AbilityInstance entry, IPowerHolder holder, boolean enabled) {
        if (entity instanceof Player player) {
            if (!entity.level().isClientSide) {
                entry.setUniqueProperty(NAME_CACHED, null);
            }
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
