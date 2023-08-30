package net.threetag.palladium.entity.fabric;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.threetag.palladiumcore.registry.EntityAttributeRegistry;

public class ForgeAttributes {

    public static Attribute SWIM_SPEED = new RangedAttribute("forge.swimSpeed", 1.0D, 0.0D, 1024.0D).setSyncable(true);

    public static void init() {
        if (!BuiltInRegistries.ATTRIBUTE.containsKey(new ResourceLocation("forge", "swim_speed"))) {
            Registry.register(BuiltInRegistries.ATTRIBUTE, "forge:swim_speed", SWIM_SPEED);
            EntityAttributeRegistry.registerModification(() -> EntityType.PLAYER, () -> SWIM_SPEED);
        }
    }

}
