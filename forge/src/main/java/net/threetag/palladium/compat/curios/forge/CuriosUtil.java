package net.threetag.palladium.compat.curios.forge;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.threetag.palladium.client.renderer.item.CurioTrinketRenderer;
import net.threetag.palladium.compat.curiostinkets.CurioTrinket;
import net.threetag.palladium.compat.curiostinkets.CuriosTrinketsSlotInv;
import net.threetag.palladium.compat.curiostinkets.CuriosTrinketsUtil;
import net.threetag.palladium.power.ability.RestrictSlotsAbility;
import net.threetag.palladium.util.PlayerSlot;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;
import top.theillusivec4.curios.api.client.ICurioRenderer;
import top.theillusivec4.curios.api.event.CurioEquipEvent;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CuriosUtil extends CuriosTrinketsUtil {

    private static final Map<Item, CurioTrinket> HANDLERS = new HashMap<>();

    @SubscribeEvent
    public void attachStackCapabilities(AttachCapabilitiesEvent<ItemStack> evt) {
        ItemStack stack = evt.getObject();

        if (HANDLERS.containsKey(stack.getItem())) {
            Capability capability = new Capability(stack, HANDLERS.get(stack.getItem()));
            evt.addCapability(CuriosCapability.ID_ITEM, new CuriosCompat.Provider(capability));
        }
    }

    @SubscribeEvent
    public void onCurioEquip(CurioEquipEvent e) {
        var key = "curios:" + e.getSlotContext().identifier();

        if (RestrictSlotsAbility.isRestricted(e.getEntity(), key)) {
            e.setResult(Event.Result.DENY);
        }
    }

    @Override
    public boolean isCurios() {
        return true;
    }

    @Override
    public void registerCurioTrinket(Item item, CurioTrinket curioTrinket) {
        HANDLERS.put(item, curioTrinket);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void registerRenderer(Item item, CurioTrinketRenderer renderer) {
        CuriosRendererRegistry.register(item, () -> new Renderer(renderer));
    }

    @Override
    public CuriosTrinketsSlotInv getSlot(LivingEntity entity, String slot) {
        final CuriosTrinketsSlotInv[] slotHandler = {CuriosTrinketsSlotInv.EMPTY};
        CuriosApi.getCuriosHelper().getCuriosHandler(entity).ifPresent(curios -> {
            curios.getStacksHandler(slot).ifPresent(stacks -> {
                slotHandler[0] = new SlotInv(stacks.getStacks());
            });
        });
        return slotHandler[0];
    }

    public static class SlotInv implements CuriosTrinketsSlotInv {

        private final IDynamicStackHandler stackHandler;

        public SlotInv(IDynamicStackHandler stackHandler) {
            this.stackHandler = stackHandler;
        }

        @Override
        public int getSlots() {
            return this.stackHandler.getSlots();
        }

        @Override
        public ItemStack getStackInSlot(int index) {
            return this.stackHandler.getStackInSlot(index);
        }

        @Override
        public void setStackInSlot(int index, ItemStack stack) {
            this.stackHandler.setStackInSlot(index, stack);
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

        @Override
        public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid) {
            Multimap<Attribute, AttributeModifier> map = ArrayListMultimap.create();
            map.putAll(ICurio.super.getAttributeModifiers(slotContext, uuid));
            map.putAll(this.curioTrinket.getModifiers(PlayerSlot.get("curios:" + slotContext.identifier()), slotContext.entity()));
            return map;
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
