package net.threetag.palladium.compat.geckolib.forge;

import com.google.common.collect.Multimap;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.threetag.palladium.compat.geckolib.GeckoArmorRenderer;
import net.threetag.palladium.compat.geckolib.PackGeckoArmorItem;
import net.threetag.palladium.item.AddonAttributeContainer;
import net.threetag.palladium.item.ExtendedArmor;
import net.threetag.palladium.item.IAddonItem;
import net.threetag.palladium.util.PlayerSlot;
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

import java.util.List;

public class GeckoLibCompatImpl {

    @OnlyIn(Dist.CLIENT)
    public static void initClient() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(GeckoLibCompatImpl::registerRenderers);
    }

    @OnlyIn(Dist.CLIENT)
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
        private final AddonAttributeContainer attributeContainer = new AddonAttributeContainer();
        private RenderLayerContainer renderLayerContainer = null;
        private boolean hideSecondLayer = false;
        private ResourceLocation texture, model, animation;
        private String animationName = "animation.armor.loop";
        private AnimationFactory factory = GeckoLibUtil.createFactory(this);

        public ArmorItemImpl(ArmorMaterial materialIn, EquipmentSlot equipmentSlot, Properties builder) {
            super(materialIn, equipmentSlot, builder);
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
            return this.attributeContainer.get(PlayerSlot.get(slot), super.getDefaultAttributeModifiers(slot));
        }

        @Override
        public AddonAttributeContainer getAttributeContainer() {
            return this.attributeContainer;
        }

        @Override
        public void setTooltip(List<Component> lines) {
            this.tooltipLines = lines;
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
