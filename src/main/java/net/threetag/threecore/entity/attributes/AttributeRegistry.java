package net.threetag.threecore.entity.attributes;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.SimpleRegistry;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.util.icon.IIcon;
import net.threetag.threecore.util.icon.ItemIcon;
import net.threetag.threecore.util.icon.TexturedIcon;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.function.Supplier;

public class AttributeRegistry {

    public static final SimpleRegistry<AttributeEntry> REGISTRY = new SimpleRegistry<AttributeEntry>();

    static {
        REGISTRY.register(new ResourceLocation("minecraft", "max_health"), new AttributeEntry(SharedMonsterAttributes.MAX_HEALTH, () -> new TexturedIcon(TexturedIcon.ICONS_TEXTURE, 176, 0, 16, 16)));
        REGISTRY.register(new ResourceLocation("minecraft", "follow_range"), new AttributeEntry(SharedMonsterAttributes.FOLLOW_RANGE));
        REGISTRY.register(new ResourceLocation("minecraft", "knockback_resistance"), new AttributeEntry(SharedMonsterAttributes.KNOCKBACK_RESISTANCE, () -> new TexturedIcon(TexturedIcon.ICONS_TEXTURE, 208, 0, 16, 16)));
        REGISTRY.register(new ResourceLocation("minecraft", "movement_speed"), new AttributeEntry(SharedMonsterAttributes.MOVEMENT_SPEED, () -> new TexturedIcon(TexturedIcon.ICONS_TEXTURE, 48, 0, 16, 16)));
        REGISTRY.register(new ResourceLocation("minecraft", "flying_speed"), new AttributeEntry(SharedMonsterAttributes.FLYING_SPEED));
        REGISTRY.register(new ResourceLocation("minecraft", "attack_damage"), new AttributeEntry(SharedMonsterAttributes.ATTACK_DAMAGE, () -> new TexturedIcon(TexturedIcon.ICONS_TEXTURE, 32, 0, 16, 16)));
        REGISTRY.register(new ResourceLocation("minecraft", "attack_speed"), new AttributeEntry(SharedMonsterAttributes.ATTACK_SPEED));
        REGISTRY.register(new ResourceLocation("minecraft", "armor"), new AttributeEntry(SharedMonsterAttributes.ARMOR, () -> new TexturedIcon(TexturedIcon.ICONS_TEXTURE, 80, 0, 16, 16)));
        REGISTRY.register(new ResourceLocation("minecraft", "armor_toughness"), new AttributeEntry(SharedMonsterAttributes.ARMOR_TOUGHNESS));
        REGISTRY.register(new ResourceLocation("minecraft", "luck"), new AttributeEntry(SharedMonsterAttributes.LUCK, () -> new ItemIcon(Items.RABBIT_FOOT)));
        REGISTRY.register(new ResourceLocation("minecraft", "reach_distance"), new AttributeEntry(PlayerEntity.REACH_DISTANCE));
        REGISTRY.register(new ResourceLocation("forge", "nametag_distance"), new AttributeEntry(LivingEntity.NAMETAG_DISTANCE));
        REGISTRY.register(new ResourceLocation("forge", "swim_speed"), new AttributeEntry(LivingEntity.SWIM_SPEED));
        REGISTRY.register(new ResourceLocation("forge", "gravity"), new AttributeEntry(LivingEntity.ENTITY_GRAVITY));
        REGISTRY.register(new ResourceLocation(ThreeCore.MODID, "size_width"), new AttributeEntry(TCAttributes.SIZE_WIDTH));
        REGISTRY.register(new ResourceLocation(ThreeCore.MODID, "size_height"), new AttributeEntry(TCAttributes.SIZE_HEIGHT));
        REGISTRY.register(new ResourceLocation(ThreeCore.MODID, "step_height"), new AttributeEntry(TCAttributes.STEP_HEIGHT));
        REGISTRY.register(new ResourceLocation(ThreeCore.MODID, "fall_resistance"), new AttributeEntry(TCAttributes.FALL_RESISTANCE));
        REGISTRY.register(new ResourceLocation(ThreeCore.MODID, "jump_height"), new AttributeEntry(TCAttributes.JUMP_HEIGHT));
        REGISTRY.register(new ResourceLocation(ThreeCore.MODID, "sprint_speed"), new AttributeEntry(TCAttributes.SPRINT_SPEED));
    }

    public static AttributeEntry getEntry(IAttribute attribute) {
        Iterator<AttributeEntry> iterator = REGISTRY.iterator();

        while (iterator.hasNext()) {
            AttributeEntry entry = iterator.next();
            if (entry.getAttribute() == attribute) {
                return entry;
            }
        }

        return null;
    }

    public static class AttributeEntry {

        protected IAttribute attribute;
        @Nullable
        protected Supplier<IIcon> iconSupplier;

        public AttributeEntry(IAttribute attribute) {
            this.attribute = attribute;
        }

        public AttributeEntry(IAttribute attribute, @Nullable Supplier<IIcon> iconSupplier) {
            this.attribute = attribute;
            this.iconSupplier = iconSupplier;
        }

        public IAttribute getAttribute() {
            return attribute;
        }

        public IIcon makeIcon() {
            return this.iconSupplier != null ? this.iconSupplier.get() : null;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof AttributeEntry))
                return false;
            return attribute.equals(obj);
        }
    }

}
