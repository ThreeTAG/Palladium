package net.threetag.palladium.entity;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.threetag.palladium.Palladium;

import java.util.function.Supplier;

@EventBusSubscriber(modid = Palladium.MOD_ID)
public class PalladiumEntityTypes {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, Palladium.MOD_ID);

    public static final DeferredHolder<EntityType<?>, EntityType<SuitStand>> SUIT_STAND = register("suit_stand", () -> EntityType.Builder.<SuitStand>of(SuitStand::new, MobCategory.MISC).sized(0.6F, 1.8F));
    public static final DeferredHolder<EntityType<?>, EntityType<EffectEntity>> EFFECT = register("effect", () -> EntityType.Builder.<EffectEntity>of(EffectEntity::new, MobCategory.MISC).sized(0.1F, 0.1F));
    public static final DeferredHolder<EntityType<?>, EntityType<SwingAnchor>> SWING_ANCHOR = register("swing_anchor", () -> EntityType.Builder.<SwingAnchor>of(SwingAnchor::new, MobCategory.MISC).sized(0.1F, 0.1F));

    @SubscribeEvent
    static void entityAttributes(EntityAttributeCreationEvent e) {
        e.put(SUIT_STAND.get(), SuitStand.createAttributes().build());
    }

    private static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> register(String id, Supplier<EntityType.Builder<T>> builderSupplier) {
        return ENTITIES.register(id, () -> builderSupplier.get().build(ResourceKey.create(Registries.ENTITY_TYPE, Palladium.id(id))));
    }
}
