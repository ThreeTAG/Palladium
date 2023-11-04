package net.threetag.palladium.compat.geckolib.ability;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.threetag.palladium.compat.geckolib.armor.AddonGeoArmorItem;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.ability.Ability;
import net.threetag.palladium.power.ability.AbilityEntry;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.ResourceLocationProperty;
import net.threetag.palladium.util.property.StringProperty;
import software.bernie.geckolib.animatable.GeoItem;

public class ArmorAnimationAbility extends Ability {

    public static final PalladiumProperty<ResourceLocation> ITEM = new ResourceLocationProperty("item").configurable("ID of the gecko armor item that must be worn currently.");
    public static final PalladiumProperty<String> CONTROLLER = new StringProperty("controller").configurable("Name of the animation controller the animation is played on. Leave it as 'main' if you didnt specify one.");
    public static final PalladiumProperty<String> ANIMATION_TRIGGER = new StringProperty("animation_trigger").configurable("Name of the animation trigger");

    public ArmorAnimationAbility() {
        this.withProperty(ITEM, new ResourceLocation("test", "example_item"));
        this.withProperty(CONTROLLER, "main");
        this.withProperty(ANIMATION_TRIGGER, "animation_trigger_name");
    }

    @Override
    public boolean isEffect() {
        return true;
    }

    @Override
    public void tick(LivingEntity entity, AbilityEntry entry, IPowerHolder holder, boolean enabled) {
        if (enabled) {
            if (!entity.level().isClientSide) {
                Item item = BuiltInRegistries.ITEM.get(entry.getProperty(ITEM));

                if (item instanceof AddonGeoArmorItem geo) {
                    for (EquipmentSlot slot : EquipmentSlot.values()) {
                        if (slot.getType() == EquipmentSlot.Type.ARMOR) {
                            if (entity.getItemBySlot(slot).is(item) && !entity.getItemBySlot(slot).isEmpty()) {
                                long geoId = GeoItem.getId(entity.getItemBySlot(slot)) + entity.getId();
                                geo.triggerAnim(entity, geoId, entry.getProperty(CONTROLLER), entry.getProperty(ANIMATION_TRIGGER));
                            }
                        }
                    }
                }
            }
        }
    }
}
