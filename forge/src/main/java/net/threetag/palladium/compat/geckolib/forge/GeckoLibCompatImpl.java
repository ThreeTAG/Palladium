package net.threetag.palladium.compat.geckolib.forge;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.threetag.palladium.compat.geckolib.GeckoArmorRenderer;
import net.threetag.palladium.compat.geckolib.PackGeckoArmorItem;
import net.threetag.palladium.item.ExtendedArmor;
import net.threetag.palladium.item.IAddonItem;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.item.GeoArmorItem;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeckoLibCompatImpl {

    public static void init() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(GeckoLibCompatImpl::registerRenderers);
    }

    public static void registerRenderers(final EntityRenderersEvent.AddLayers event) {
        GeoArmorRenderer.registerArmorRenderer(ArmorItemImpl.class, () -> GeckoArmorRenderer.INSTANCE);
    }

    public static ArmorItem createArmorItem(ArmorMaterial armorMaterial, EquipmentSlot slot, Item.Properties properties, boolean hideSecondLayer) {
        var item = new ArmorItemImpl(armorMaterial, slot, properties);

        if (hideSecondLayer) {
            item.hideSecondLayer();
        }

        return item;
    }

    public static class ArmorItemImpl extends GeoArmorItem implements IAnimatable, IAddonItem, ExtendedArmor, PackGeckoArmorItem {

        private List<Component> tooltipLines;
        private final Map<EquipmentSlot, Multimap<Attribute, AttributeModifier>> attributeModifiers = new HashMap<>();
        private RenderLayerContainer renderLayerContainer = null;
        private boolean hideSecondLayer = false;
        private ResourceLocation texture, model, animation;
        private String animationName = "animation.armor.loop";
        private AnimationFactory factory = GeckoLibUtil.createFactory(this);

        public ArmorItemImpl(ArmorMaterial materialIn, EquipmentSlot equipmentSlot, Properties builder) {
            super(materialIn, equipmentSlot, builder);

            for (EquipmentSlot slot : EquipmentSlot.values()) {
                Multimap<Attribute, AttributeModifier> multimap = ArrayListMultimap.create();
                multimap.putAll(super.getDefaultAttributeModifiers(slot));
                this.attributeModifiers.put(slot, multimap);
            }
        }

        @Override
        public void registerControllers(AnimationData data) {
            if(this.animation != null) {
                data.addAnimationController(new AnimationController<>(this, "controller", 20, this::predicate));
            }
        }

        private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(this.animationName, ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }

        @Override
        public AnimationFactory getFactory() {
            return this.factory;
        }

        public ArmorItemImpl hideSecondLayer() {
            this.hideSecondLayer = true;
            return this;
        }

        @Override
        public boolean hideSecondPlayerLayer(Player player, ItemStack stack, EquipmentSlot slot) {
            return this.hideSecondLayer;
        }

        @Override
        public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
            super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
            if (this.tooltipLines != null) {
                tooltipComponents.addAll(this.tooltipLines);
            }
        }

        @Override
        public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
            var modifiers = this.attributeModifiers.get(slot);
            if (modifiers != null) {
                return modifiers;
            } else {
                return super.getDefaultAttributeModifiers(slot);
            }
        }

        @Override
        public void setTooltip(List<Component> lines) {
            this.tooltipLines = lines;
        }

        @Override
        public void addAttributeModifier(@Nullable EquipmentSlot slot, Attribute attribute, AttributeModifier modifier) {
            if (slot != null) {
                this.attributeModifiers.get(slot).put(attribute, modifier);
            } else {
                for (EquipmentSlot slot1 : EquipmentSlot.values()) {
                    this.attributeModifiers.get(slot1).put(attribute, modifier);
                }
            }
        }

        @Override
        public void setRenderLayerContainer(RenderLayerContainer container) {
            this.renderLayerContainer = container;
        }

        @Override
        public RenderLayerContainer getRenderLayerContainer() {
            return this.renderLayerContainer;
        }

        @Override
        public PackGeckoArmorItem setGeckoLocations(ResourceLocation modelLocation, ResourceLocation textureLocation, ResourceLocation animationLocation, String animationName) {
            this.model = modelLocation;
            this.texture = textureLocation;
            this.animation = animationLocation;
            this.animationName = animationName;
            return this;
        }

        @Override
        public ResourceLocation getGeckoModelLocation() {
            return this.model;
        }

        @Override
        public ResourceLocation getGeckoTextureLocation() {
            return this.texture;
        }

        @Override
        public ResourceLocation getGeckoAnimationLocation() {
            return this.animation;
        }
    }

}
