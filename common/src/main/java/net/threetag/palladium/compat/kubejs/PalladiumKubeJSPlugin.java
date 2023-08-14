package net.threetag.palladium.compat.kubejs;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.item.custom.BasicItemJS;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.kubejs.util.AttachedData;
import dev.latvian.mods.rhino.util.wrap.TypeWrappers;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.threetag.palladium.client.model.animation.AnimationUtil;
import net.threetag.palladium.client.model.animation.PalladiumAnimation;
import net.threetag.palladium.compat.kubejs.ability.AbilityBuilder;
import net.threetag.palladium.compat.kubejs.condition.ConditionBuilder;
import net.threetag.palladium.condition.ConditionSerializer;
import net.threetag.palladium.event.PalladiumClientEvents;
import net.threetag.palladium.event.PalladiumEvents;
import net.threetag.palladium.power.SuperpowerUtil;
import net.threetag.palladium.power.ability.Ability;
import net.threetag.palladium.power.ability.AbilityUtil;
import net.threetag.palladium.util.Easing;
import net.threetag.palladium.util.PlayerSlot;
import net.threetag.palladiumcore.registry.client.OverlayRegistry;

public class PalladiumKubeJSPlugin extends KubeJSPlugin {

    public static RegistryInfo ABILITY = RegistryInfo.of(Ability.REGISTRY.getRegistryKey()).type(Ability.class);
    public static RegistryInfo CONDITION = RegistryInfo.of(ConditionSerializer.REGISTRY.getRegistryKey()).type(ConditionSerializer.class);

    @Override
    public void init() {
        ABILITY.addType("basic", AbilityBuilder.class, AbilityBuilder::new);
        CONDITION.addType("basic", ConditionBuilder.class, ConditionBuilder::new);

        PalladiumJSEvents.GROUP.register();

        PalladiumEvents.REGISTER_PROPERTY.register(handler -> {
            if (handler.getEntity().level.isClientSide) {
                PalladiumJSEvents.CLIENT_REGISTER_PROPERTIES.post(new RegisterPalladiumPropertyEventJS(handler.getEntity(), handler));
            } else {
                PalladiumJSEvents.REGISTER_PROPERTIES.post(new RegisterPalladiumPropertyEventJS(handler.getEntity(), handler));
            }
        });
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void clientInit() {
        PalladiumClientEvents.REGISTER_ANIMATIONS.register(registry -> {
            PalladiumJSEvents.REGISTER_ANIMATIONS.post(new RegisterAnimationsEventJS(registry));
            PalladiumJSEvents.REGISTER_GUI_OVERLAYS.post(new RegisterGuiOverlaysEventJS());
        });

        OverlayRegistry.registerOverlay("palladium/kube_js_overlays", new RegisterGuiOverlaysEventJS.Overlay());
    }

    @Override
    public void registerBindings(BindingsEvent event) {
        event.add("palladium", PalladiumBinding.class);
        event.add("superpowerUtil", SuperpowerUtil.class);
        event.add("abilityUtil", AbilityUtil.class);
        if (event.getType() == ScriptType.CLIENT) {
            event.add("animationUtil", AnimationUtil.class);
            event.add("guiUtil", GuiUtilJS.class);
        }
    }

    @Override
    public void registerTypeWrappers(ScriptType type, TypeWrappers typeWrappers) {
        if (type == ScriptType.CLIENT) {
            typeWrappers.registerSimple(Easing.class, o -> Easing.fromString(o.toString()));
            typeWrappers.registerSimple(PalladiumAnimation.PlayerModelPart.class, o -> PalladiumAnimation.PlayerModelPart.fromName(o.toString()));
            typeWrappers.registerSimple(PlayerSlot.class, o -> PlayerSlot.get(o.toString()));
        }
    }

    @Override
    public void attachPlayerData(AttachedData<Player> event) {
        event.add("powers", new PowerHandlerJS(event.getParent()));
    }

    @Override
    public void attachLevelData(AttachedData<Level> event) {
        event.add("powers", new PowerManagerJS(event.getParent()));
    }

}
