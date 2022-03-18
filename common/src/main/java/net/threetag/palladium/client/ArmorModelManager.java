package net.threetag.palladium.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

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

    public static Handler get(Item item) {
        return HANDLERS.get(item);
    }

    public interface Handler {

        HumanoidModel<?> getArmorModel(ItemStack stack, LivingEntity entity, EquipmentSlot slot);

        void onReload(EntityModelSet entityModelSet);
    }

    public static class Simple implements Handler {

        private final ModelLayerLocation modelLayerLocation;
        private HumanoidModel<?> model;

        public Simple(ModelLayerLocation modelLayerLocation) {
            this.modelLayerLocation = modelLayerLocation;
        }

        @Override
        public HumanoidModel<?> getArmorModel(ItemStack stack, LivingEntity entity, EquipmentSlot slot) {
            return this.model;
        }

        @Override
        public void onReload(EntityModelSet entityModelSet) {
            this.model = new HumanoidModel<>(entityModelSet.bakeLayer(this.modelLayerLocation));
        }
    }

}
