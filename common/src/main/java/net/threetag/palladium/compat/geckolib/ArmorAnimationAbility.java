package net.threetag.palladium.compat.geckolib;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.ability.Ability;
import net.threetag.palladium.power.ability.AbilityEntry;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.ResourceLocationProperty;
import net.threetag.palladium.util.property.StringProperty;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.util.GeckoLibUtil;

@SuppressWarnings("unchecked")
public class ArmorAnimationAbility extends Ability {

    public static final PalladiumProperty<ResourceLocation> ITEM = new ResourceLocationProperty("item").configurable("ID of the gecko armor item that must be worn currently.");
    public static final PalladiumProperty<String> CONTROLLER = new StringProperty("controller").configurable("Name of the animation controller the animation is played on. Leave it as 'main' if you didnt specify one.");
    public static final PalladiumProperty<String> ANIMATION = new StringProperty("animation").configurable("Animation name that is supposed to be played");

    public ArmorAnimationAbility() {
        this.withProperty(ITEM, new ResourceLocation("test", "example_item"));
        this.withProperty(CONTROLLER, "main");
        this.withProperty(ANIMATION, "animation_name");
    }

    @Override
    public boolean isEffect() {
        return true;
    }

    @Override
    public void tick(LivingEntity entity, AbilityEntry entry, IPowerHolder holder, boolean enabled) {
        if (enabled) {
            if (entity.level.isClientSide) {
                this.playAnimation(entity, entry);
            } else {
                Item item = Registry.ITEM.get(entry.getProperty(ITEM));

                if (item instanceof PackGeckoArmorItem) {
                    for (EquipmentSlot slot : EquipmentSlot.values()) {
                        if (slot.getType() == EquipmentSlot.Type.ARMOR) {
                            if (entity.getItemBySlot(slot).is(item) && !entity.getItemBySlot(slot).isEmpty()) {
                                GeckoLibUtil.guaranteeIDForStack(entity.getItemBySlot(slot), (ServerLevel) entity.level);
                            }
                        }
                    }
                }
            }
        }
    }

    @Environment(EnvType.CLIENT)
    public void playAnimation(LivingEntity entity, AbilityEntry entry) {
        Item item = Registry.ITEM.get(entry.getProperty(ITEM));

        if (item instanceof PackGeckoArmorItem gecko) {
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                if (slot.getType() == EquipmentSlot.Type.ARMOR) {
                    if (entity.getItemBySlot(slot).is(item)) {
                        var renderer = GeckoLibCompat.getArmorRenderer(((ArmorItem)item).getClass(), entity);

                        if(renderer != null) {
                            var controller = GeckoLibUtil.getControllerForID(gecko.getFactory(), renderer.getInstanceId((ArmorItem) item), entry.getProperty(CONTROLLER));
                            if (controller != null) {
                                controller.markNeedsReload();
                                controller.setAnimation(new AnimationBuilder().addAnimation(entry.getProperty(ANIMATION)));
                            }
                        }
                    }
                }
            }
        }
    }
}
