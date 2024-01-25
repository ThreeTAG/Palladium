package net.threetag.palladium.compat.curios.forge;

import com.google.gson.JsonElement;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.threetag.palladium.addonpack.AddonPackManager;
import net.threetag.palladium.addonpack.PackData;
import net.threetag.palladium.addonpack.log.AddonPackLog;
import net.threetag.palladium.client.renderer.renderlayer.IPackRenderLayer;
import net.threetag.palladium.client.renderer.renderlayer.PackRenderLayerManager;
import net.threetag.palladium.compat.curiostinkets.CuriosTrinketsUtil;
import net.threetag.palladium.item.IAddonItem;
import net.threetag.palladium.power.provider.PowerProvider;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladium.util.json.GsonUtil;
import net.threetag.palladiumcore.registry.DeferredRegister;
import net.threetag.palladiumcore.registry.RegistrySupplier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

import java.util.Map;

public class CuriosCompat {

    public static final DeferredRegister<PowerProvider> FACTORIES = DeferredRegister.create(CuriosApi.MODID, PowerProvider.REGISTRY);
    public static final RegistrySupplier<PowerProvider> CURIOS = FACTORIES.register("curios", CuriosPowerProvider::new);

    public static void init() {
        CuriosTrinketsUtil.setInstance(new CuriosUtil());
        MinecraftForge.EVENT_BUS.register(CuriosTrinketsUtil.getInstance());
        FMLJavaModLoadingContext.get().getModEventBus().addListener(CuriosCompat::interModQueue);
        FACTORIES.register();
    }

    @OnlyIn(Dist.CLIENT)
    public static void initClient() {
        PackRenderLayerManager.registerProvider((entity, layers) -> {
            if(entity instanceof LivingEntity livingEntity) {
                CuriosApi.getCuriosHelper().getCuriosHandler(livingEntity)
                        .ifPresent(handler -> handler.getCurios().forEach((id, stacksHandler) -> {
                            IDynamicStackHandler stackHandler = stacksHandler.getStacks();
                            IDynamicStackHandler cosmeticStacksHandler = stacksHandler.getCosmeticStacks();

                            for (int i = 0; i < stackHandler.getSlots(); i++) {
                                ItemStack stack = cosmeticStacksHandler.getStackInSlot(i);
                                boolean cosmetic = true;
                                NonNullList<Boolean> renderStates = stacksHandler.getRenders();
                                boolean renderable = renderStates.size() > i && renderStates.get(i);

                                if (stack.isEmpty() && renderable) {
                                    stack = stackHandler.getStackInSlot(i);
                                    cosmetic = false;
                                }

                                if (!stack.isEmpty() && stack.getItem() instanceof IAddonItem addonItem && addonItem.getRenderLayerContainer() != null) {
                                    var container = addonItem.getRenderLayerContainer();

                                    for (ResourceLocation layerId : container.get("curios:" + id)) {
                                        IPackRenderLayer layer = PackRenderLayerManager.getInstance().getLayer(layerId);

                                        if (layer != null) {
                                            layers.accept(DataContext.forItem(entity, stack), layer);
                                        }
                                    }
                                }
                            }
                        }));
            }
        });
    }

    @Deprecated
    public static void interModQueue(InterModEnqueueEvent e) {
        for (PackData pack : AddonPackManager.getInstance().getPacks()) {
            if (pack.getCustomData().has("curios")) {

                AddonPackLog.warning("Addon Pack " + pack.getId() + " adds Curios slots in pack.mcmeta, this is deprecated! Please use the new system: https://docs.illusivesoulworks.com/curios/datapack-example");

                var curios = GsonHelper.getAsJsonObject(pack.getCustomData(), "curios");
                for (Map.Entry<String, JsonElement> entry : curios.entrySet()) {
                    String name = entry.getKey();
                    InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> {
                        if (entry.getValue().isJsonPrimitive() && entry.getValue().getAsBoolean()) {
                            return new SlotTypeMessage.Builder(name).build();
                        } else {
                            var curiosJson = entry.getValue().getAsJsonObject();
                            var builder = new SlotTypeMessage.Builder(name);

                            if (GsonHelper.isValidNode(curiosJson, "icon")) {
                                builder.icon(GsonUtil.getAsResourceLocation(curiosJson, "icon"));
                            }

                            if (GsonHelper.isValidNode(curiosJson, "priority")) {
                                builder.priority(GsonHelper.getAsInt(curiosJson, "priority"));
                            }

                            if (GsonHelper.isValidNode(curiosJson, "size")) {
                                builder.size(GsonHelper.getAsInt(curiosJson, "size"));
                            }

                            if (GsonHelper.isValidNode(curiosJson, "hide") && GsonHelper.getAsBoolean(curiosJson, "hide")) {
                                builder.hide();
                            }

                            if (GsonHelper.isValidNode(curiosJson, "cosmetic") && GsonHelper.getAsBoolean(curiosJson, "cosmetic")) {
                                builder.cosmetic();
                            }

                            return builder.build();
                        }
                    });
                }
            }
        }
    }

    public static class Provider implements ICapabilityProvider {

        final LazyOptional<ICurio> capability;

        Provider(ICurio curio) {
            this.capability = LazyOptional.of(() -> curio);
        }

        @NotNull
        @Override
        public <T> LazyOptional<T> getCapability(@NotNull net.minecraftforge.common.capabilities.Capability<T> cap, @Nullable Direction side) {
            return CuriosCapability.ITEM.orEmpty(cap, this.capability);
        }
    }

}
