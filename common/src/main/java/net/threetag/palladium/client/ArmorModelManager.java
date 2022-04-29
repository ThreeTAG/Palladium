package net.threetag.palladium.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.threetag.palladium.client.renderer.renderlayer.ModelLookup;
import net.threetag.palladium.util.SkinTypedValue;

import java.util.HashMap;
import java.util.Map;

public class ArmorModelManager implements ResourceManagerReloadListener {

    private static final Map<Item, Handler> HANDLERS = new HashMap<>();

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        HANDLERS.values().forEach(handler -> handler.onReload(Minecraft.getInstance().getEntityModels()));
    }

    public static void register(Item item, Handler handler) {
        HANDLERS.put(item, handler);
    }

    public static void register(Item item, SkinTypedValue<ModelLookup.Model> modelLookup, SkinTypedValue<ModelLayerLocation> modelLayerLocation) {
        register(item, new Simple(modelLookup, modelLayerLocation));
    }

    public static void register(Item item, ModelLayerLocation modelLayerLocation) {
        register(item, new Simple(new SkinTypedValue<>(ModelLookup.HUMANOID), new SkinTypedValue<>(modelLayerLocation)));
    }

    public static Handler get(Item item) {
        return HANDLERS.get(item);
    }

    public interface Handler {

        HumanoidModel<?> getArmorModel(ItemStack stack, LivingEntity entity, EquipmentSlot slot);

        void onReload(EntityModelSet entityModelSet);
    }

    public static class Simple implements Handler {

        private final SkinTypedValue<ModelLayerLocation> modelLayerLocation;
        private final SkinTypedValue<ModelLookup.Model> modelLookup;
        private SkinTypedValue<EntityModel<LivingEntity>> model;

        public Simple(SkinTypedValue<ModelLookup.Model> modelLookup, SkinTypedValue<ModelLayerLocation> modelLayerLocation) {
            this.modelLayerLocation = modelLayerLocation;
            this.modelLookup = modelLookup;
        }

        @Override
        public HumanoidModel<?> getArmorModel(ItemStack stack, LivingEntity entity, EquipmentSlot slot) {
            return (HumanoidModel<?>) this.model.get(entity);
        }

        @SuppressWarnings({"unchecked", "rawtypes"})
        @Override
        public void onReload(EntityModelSet entityModelSet) {
            this.model = new SkinTypedValue(modelLookup.getNormal().getModel(entityModelSet.bakeLayer(modelLayerLocation.getNormal())),
                    modelLookup.getSlim().getModel(entityModelSet.bakeLayer(modelLayerLocation.getSlim())));
        }
    }

}
