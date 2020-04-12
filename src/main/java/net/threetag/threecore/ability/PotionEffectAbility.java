package net.threetag.threecore.ability;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Items;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.threetag.threecore.util.icon.ItemIcon;
import net.threetag.threecore.util.threedata.BooleanThreeData;
import net.threetag.threecore.util.threedata.EffectThreeData;
import net.threetag.threecore.util.threedata.IntegerThreeData;
import net.threetag.threecore.util.threedata.ThreeData;

public class PotionEffectAbility extends Ability {

    public static final ThreeData<Effect> EFFECT = new EffectThreeData("effect").enableSetting("Determines the type of effec the entity will get");
    public static final ThreeData<Integer> DURATION = new IntegerThreeData("duration").enableSetting("Duration for the effect");
    public static final ThreeData<Integer> AMPLIFIER = new IntegerThreeData("amplifier").enableSetting("The amplifier/level for the effect");
    public static final ThreeData<Boolean> SHOW_PARTICLES = new BooleanThreeData("show_particles").enableSetting("Determines if the particles should display");
    public static final ThreeData<Boolean> SHOW_ICON = new BooleanThreeData("show_icon").enableSetting("Determines if the icon should display/render");

    public PotionEffectAbility() {
        super(AbilityType.POTION_EFFECT);
    }

    @Override
    public void registerData() {
        super.registerData();
        this.register(ICON, new ItemIcon(Items.POTION));
        this.register(EFFECT, Effects.WATER_BREATHING);
        this.register(DURATION, 20);
        this.register(AMPLIFIER, 0);
        this.register(SHOW_PARTICLES, false);
        this.register(SHOW_ICON, true);
    }

    @Override
    public void action(LivingEntity entity) {
        entity.addPotionEffect(new EffectInstance(this.get(EFFECT), this.get(DURATION), this.get(AMPLIFIER), false, this.get(SHOW_PARTICLES), this.get(SHOW_ICON)));
    }
}
