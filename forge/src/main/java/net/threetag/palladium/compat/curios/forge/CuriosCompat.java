package net.threetag.palladium.compat.curios.forge;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.threetag.palladium.item.CurioTrinket;
import net.threetag.palladium.client.renderer.item.CurioTrinketRenderer;
import net.threetag.palladium.power.provider.PowerProvider;
import net.threetag.palladiumcore.registry.DeferredRegister;
import net.threetag.palladiumcore.registry.RegistrySupplier;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;
import top.theillusivec4.curios.api.client.ICurioRenderer;
import top.theillusivec4.curios.api.type.capability.ICurio;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.HashMap;
import java.util.Map;

public class CuriosCompat {

    public static final DeferredRegister<PowerProvider> FACTORIES = DeferredRegister.create(CuriosApi.MODID, PowerProvider.REGISTRY);
    public static final RegistrySupplier<PowerProvider> CURIOS = FACTORIES.register("curios", CuriosPowerProvider::new);
    private static final Map<Item, CurioTrinket> HANDLERS = new HashMap<>();

    public static void registerCurioTrinket(Item item, CurioTrinket curioTrinket) {
        HANDLERS.put(item, curioTrinket);
    }

    public static void init() {
        MinecraftForge.EVENT_BUS.register(new CuriosCompat());
        FACTORIES.register();
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerRenderer(Item item, CurioTrinketRenderer renderer) {
        CuriosRendererRegistry.register(item, () -> new Renderer(renderer));
    }

    @SubscribeEvent
    public void attachStackCapabilities(AttachCapabilitiesEvent<ItemStack> evt) {
        ItemStack stack = evt.getObject();

        if (HANDLERS.containsKey(stack.getItem())) {
            Capability capability = new Capability(stack, HANDLERS.get(stack.getItem()));
            evt.addCapability(CuriosCapability.ID_ITEM, new Provider(capability));
        }
    }

    public static class Capability implements ICurio {

        private final ItemStack stack;
        private final CurioTrinket curioTrinket;

        public Capability(ItemStack stack, CurioTrinket curioTrinket) {
            this.stack = stack;
            this.curioTrinket = curioTrinket;
        }

        @Override
        public ItemStack getStack() {
            return this.stack;
        }

        @Override
        public void curioTick(SlotContext slotContext) {
            this.curioTrinket.tick(slotContext.entity(), this.stack);
        }

        @Override
        public void onEquip(SlotContext slotContext, ItemStack prevStack) {
            this.curioTrinket.onEquip(this.stack, slotContext.entity());
        }

        @Override
        public void onUnequip(SlotContext slotContext, ItemStack newStack) {
            this.curioTrinket.onUnequip(this.stack, slotContext.entity());
        }

        @Override
        public boolean canEquip(SlotContext slotContext) {
            return this.curioTrinket.canEquip(this.stack, slotContext.entity());
        }

        @Override
        public boolean canUnequip(SlotContext slotContext) {
            return this.curioTrinket.canUnequip(this.stack, slotContext.entity());
        }

        @Override
        public boolean canEquipFromUse(SlotContext slotContext) {
            return this.curioTrinket.canRightClickEquip();
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

    @OnlyIn(Dist.CLIENT)
    public static class Renderer implements ICurioRenderer {

        private final CurioTrinketRenderer renderer;

        public Renderer(CurioTrinketRenderer renderer) {
            this.renderer = renderer;
        }

        @Override
        public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack, SlotContext slotContext, PoseStack matrixStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource renderTypeBuffer, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            this.renderer.render(stack, matrixStack, renderLayerParent.getModel(), slotContext.entity(), renderTypeBuffer, light, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
        }
    }

}
