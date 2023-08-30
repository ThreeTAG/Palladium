package net.threetag.palladium.compat.trinkets.fabric;

import dev.emi.trinkets.TrinketsMain;
import dev.emi.trinkets.api.SlotGroup;
import dev.emi.trinkets.api.TrinketsApi;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.threetag.palladium.client.renderer.renderlayer.IPackRenderLayer;
import net.threetag.palladium.client.renderer.renderlayer.PackRenderLayerManager;
import net.threetag.palladium.compat.curiostinkets.CuriosTrinketsUtil;
import net.threetag.palladium.item.IAddonItem;
import net.threetag.palladium.power.provider.PowerProvider;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladiumcore.registry.DeferredRegister;
import net.threetag.palladiumcore.registry.RegistrySupplier;

import java.util.Map;

public class TrinketsCompat {

    public static final DeferredRegister<PowerProvider> FACTORIES = DeferredRegister.create(TrinketsMain.MOD_ID, PowerProvider.REGISTRY);
    public static final RegistrySupplier<PowerProvider> TRINKETS = FACTORIES.register("trinkets", TrinketsPowerProvider::new);

    public static void init() {
        CuriosTrinketsUtil.setInstance(new TrinketsUtil());
        FACTORIES.register();
    }

    @Environment(EnvType.CLIENT)
    public static void initClient() {
        PackRenderLayerManager.registerProvider((entity, layers) -> {
            if (entity instanceof LivingEntity livingEntity) {
                for (Map.Entry<String, SlotGroup> entry : TrinketsApi.getPlayerSlots(entity.level()).entrySet()) {
                    TrinketsApi.getTrinketComponent(livingEntity).ifPresent(trinketComponent -> {
                        if (trinketComponent.getInventory().containsKey(entry.getKey())) {
                            trinketComponent.getInventory().get(entry.getKey()).forEach((key, trinketInventory) -> {
                                for (int i = 0; i < trinketInventory.getContainerSize(); i++) {
                                    ItemStack stack = trinketInventory.getItem(i);

                                    if (!stack.isEmpty() && stack.getItem() instanceof IAddonItem addonItem && addonItem.getRenderLayerContainer() != null) {
                                        var container = addonItem.getRenderLayerContainer();

                                        for (ResourceLocation id : container.get("trinkets:" + entry.getKey() + "/" + key)) {
                                            IPackRenderLayer layer = PackRenderLayerManager.getInstance().getLayer(id);

                                            if (layer != null) {
                                                layers.accept(DataContext.forItem(livingEntity, stack), layer);
                                            }
                                        }
                                    }
                                }
                            });
                        }
                    });
                }
            }
        });
    }


}
