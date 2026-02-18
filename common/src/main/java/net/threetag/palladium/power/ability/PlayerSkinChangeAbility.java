package net.threetag.palladium.power.ability;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.client.renderer.entity.PlayerSkinFetcher;
import net.threetag.palladium.client.renderer.entity.PlayerSkinHandler;
import net.threetag.palladium.client.renderer.entity.PlayerSkinInfo;
import net.threetag.palladium.util.property.EntityPropertyHandler;
import net.threetag.palladium.util.property.IntegerProperty;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.StringProperty;

import java.util.concurrent.atomic.AtomicReference;

public class PlayerSkinChangeAbility extends Ability {

    public static final PalladiumProperty<String> USERNAME = new StringProperty("username").configurable("Name of the player you want to change into");
    public static final PalladiumProperty<String> PROPERTY = new StringProperty("property").configurable("In case you want to change the skin dynamically, you can specify the Palladium Property that contains a MC username here. This will override the 'username' property.");
    public static final PalladiumProperty<Integer> PRIORITY = new IntegerProperty("priority").configurable("Priority for the skin (in case multiple skin changes are applied, the one with the highest priority will be used)");

    public PlayerSkinChangeAbility() {
        this.withProperty(USERNAME, "Steve");
        this.withProperty(PROPERTY, "");
        this.withProperty(PRIORITY, 50);
    }

    public static String getPlayerName(AbilityInstance instance, LivingEntity entity) {
        var property = instance.getProperty(PROPERTY);
        AtomicReference<String> nameRef = new AtomicReference<>(instance.getProperty(USERNAME));

        if (property != null && !property.isEmpty()) {
            EntityPropertyHandler.getHandler(entity).ifPresent(handler -> {
                var prop = handler.getPropertyByName(property);
                if (prop != null && handler.get(prop) instanceof String s) {
                    nameRef.set(s);
                }
            });
        }

        return nameRef.get();
    }

    @Override
    public boolean isEffect() {
        return true;
    }

    @Override
    public String getDocumentationDescription() {
        return "Allows you to change a player's skin to a MC profile one.";
    }

    @Environment(EnvType.CLIENT)
    public static class SkinProvider implements PlayerSkinHandler.ISkinProvider {

        @Override
        public PlayerSkinInfo getSkin(AbstractClientPlayer player, PlayerSkinInfo previousSkin, PlayerSkinInfo defaultSkin) {
            var abilities = AbilityUtil.getEnabledInstances(player, Abilities.PLAYER_SKIN_CHANGE.get()).stream().filter(AbilityInstance::isEnabled).sorted((a1, a2) -> a2.getProperty(PlayerSkinChangeAbility.PRIORITY) - a1.getProperty(PlayerSkinChangeAbility.PRIORITY)).toList();

            if (!abilities.isEmpty()) {
                var ability = abilities.get(0);
                var skin = PlayerSkinFetcher.getLoadedPlayerSkinInfo(getPlayerName(ability, player));
                return skin != null ? skin : previousSkin;
            }

            return previousSkin;
        }
    }

}
