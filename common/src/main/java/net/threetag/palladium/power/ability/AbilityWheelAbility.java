package net.threetag.palladium.power.ability;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.client.dynamictexture.TextureReference;
import net.threetag.palladium.client.screen.AbilityWheelRenderer;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.util.property.*;
import net.threetag.palladiumcore.util.Platform;

import java.util.ArrayList;
import java.util.List;

public class AbilityWheelAbility extends Ability {

    public static final PalladiumProperty<String[]> ABILITIES = new StringArrayProperty("abilities").configurable("List of ability keys to be used in the ability wheel.").sync(SyncType.SELF);
    public static final PalladiumProperty<TextureReference> TEXTURE = new TextureReferenceProperty("texture").configurable("Lets you use a custom texture for the wheel. If left null it will use the default rendering.").sync(SyncType.SELF);
    public static final PalladiumProperty<Boolean> DISABLE_MOUSE_SCROLLING = new BooleanProperty("disable_mouse_scrolling").configurable("Disables the ability to use the mouse wheel to scroll between the options.").sync(SyncType.SELF);

    public AbilityWheelAbility() {
        this.withProperty(ABILITIES, new String[]{"example_ability"})
                .withProperty(TEXTURE, null)
                .withProperty(DISABLE_MOUSE_SCROLLING, false);
    }

    @Override
    public void firstTick(LivingEntity entity, AbilityInstance instance, IPowerHolder holder, boolean enabled) {
        if (Platform.isClient() && enabled) {
            setWheel(entity, instance, holder);
        }
    }

    @Override
    public void lastTick(LivingEntity entity, AbilityInstance entry, IPowerHolder holder, boolean enabled) {
        if (Platform.isClient()) {
            resetWheel(entity);
        }
    }

    @Environment(EnvType.CLIENT)
    private void setWheel(LivingEntity entity, AbilityInstance instance, IPowerHolder holder) {
        if (entity != Minecraft.getInstance().player) {
            List<AbilityInstance> list = new ArrayList<>();

            for (String s : instance.getProperty(ABILITIES)) {
                AbilityInstance ability = holder.getAbilities().get(s);
                if (ability != null) {
                    list.add(ability);
                }
            }

            AbilityWheelRenderer.setWheel(new AbilityWheelRenderer.Wheel(list, instance.getProperty(TEXTURE), instance.getProperty(DISABLE_MOUSE_SCROLLING)));
        }
    }

    @Environment(EnvType.CLIENT)
    private void resetWheel(LivingEntity entity) {
        if (entity != Minecraft.getInstance().player) {
            AbilityWheelRenderer.setWheel(null);
        }
    }
}
