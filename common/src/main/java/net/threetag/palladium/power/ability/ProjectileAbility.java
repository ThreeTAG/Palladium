package net.threetag.palladium.power.ability;

import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Items;
import net.threetag.palladium.entity.CustomProjectile;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.util.icon.ItemIcon;
import net.threetag.palladium.util.property.*;

public class ProjectileAbility extends Ability {

    public static final PalladiumProperty<EntityType<?>> ENTITY_TYPE = new EntityTypeProperty("entity_type").configurable("Entity type ID for the projectile entity");
    public static final PalladiumProperty<CompoundTag> ENTITY_DATA = new CompoundTagProperty("entity_data").configurable("Entity NBT data");
    public static final PalladiumProperty<Float> INACCURACY = new FloatProperty("inaccuracy").configurable("Determines the inaccuracy when shooting the projectile");
    public static final PalladiumProperty<Float> VELOCITY = new FloatProperty("velocity").configurable("Determines the velocity when shooting the projectile");
    public static final PalladiumProperty<ArmTypeProperty.ArmType> SWINGING_ARM = new ArmTypeProperty("swinging_arm").configurable("Determines which arm(s) should swing upon shooting");
    public static final PalladiumProperty<Boolean> DAMAGE_FROM_PLAYER = new BooleanProperty("damage_from_player").configurable("If this is set to true and a custom projectile is used, the damage will automatically be set the player damage value");

    public ProjectileAbility() {
        this.withProperty(ICON, new ItemIcon(Items.SNOWBALL));
        this.withProperty(ENTITY_TYPE, EntityType.SNOWBALL);
        this.withProperty(ENTITY_DATA, null);
        this.withProperty(INACCURACY, 0F);
        this.withProperty(VELOCITY, 1.5F);
        this.withProperty(SWINGING_ARM, ArmTypeProperty.ArmType.MAIN_ARM);
        this.withProperty(DAMAGE_FROM_PLAYER, false);
    }

    @Override
    public void tick(LivingEntity entity, AbilityEntry entry, IPowerHolder holder, boolean enabled) {
        if (!entity.level.isClientSide && enabled) {
            CompoundTag compound = entry.getProperty(ENTITY_DATA);
            compound = compound == null ? new CompoundTag() : compound;
            compound.putString("id", Registry.ENTITY_TYPE.getKey(entry.getProperty(ENTITY_TYPE)).toString());

            ServerLevel world = (ServerLevel) entity.level;
            EntityType.loadEntityRecursive(compound, world, (en) -> {
                if (!(en instanceof Projectile projectile))
                    return null;

                projectile.moveTo(entity.getX(), entity.getY() + entity.getEyeHeight() - 0.1D, entity.getZ(), projectile.getYRot(), projectile.getXRot());
                float velocity = entry.getProperty(VELOCITY);
                float inaccuracy = entry.getProperty(INACCURACY);
                projectile.shootFromRotation(entity, entity.getXRot(), entity.getYRot(), 0, velocity, inaccuracy);
                projectile.setOwner(entity);

                for (InteractionHand hand : entry.getProperty(SWINGING_ARM).getHand(entity)) {
                    entity.swing(hand, true);
                }

                if (entry.getProperty(DAMAGE_FROM_PLAYER) && projectile instanceof CustomProjectile customProjectile) {
                    customProjectile.damage = (float) entity.getAttributeValue(Attributes.ATTACK_DAMAGE);
                }

                return !world.addWithUUID(projectile) ? null : projectile;
            });
        }
    }
}
