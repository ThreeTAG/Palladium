package net.threetag.palladium.compat.geckolib.forge;

import com.google.common.collect.Multimap;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;
import net.threetag.palladium.compat.geckolib.*;
import net.threetag.palladium.item.AddonAttributeContainer;
import net.threetag.palladium.item.ExtendedArmor;
import net.threetag.palladium.item.IAddonItem;
import net.threetag.palladium.power.ability.Ability;
import net.threetag.palladium.util.PlayerSlot;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.GeckoLib;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.item.GeoArmorItem;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.List;

@SuppressWarnings("rawtypes")
public class GeckoLibCompatImpl {

    @OnlyIn(Dist.CLIENT)
    public static void initClient() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(GeckoLibCompatImpl::registerRenderers);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(GeckoLibCompatImpl::registerAbility);
    }

    public static void registerAbility(RegisterEvent e) {
        e.register(Ability.REGISTRY.getRegistryKey(), new ResourceLocation(GeckoLib.ModID, "render_layer_animation"), RenderLayerAnimationAbility::new);
        e.register(Ability.REGISTRY.getRegistryKey(), new ResourceLocation(GeckoLib.ModID, "armor_animation"), ArmorAnimationAbility::new);
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

    public static GeoArmorRenderer getArmorRenderer(Class<? extends ArmorItem> clazz, LivingEntity wearer) {
        return GeoArmorRenderer.getRenderer(clazz, wearer);
    }

    public static class ArmorItemImpl extends GeoArmorItem implements IAnimatable, IAddonItem, ExtendedArmor, PackGeckoArmorItem {

        private List<Component> tooltipLines;
        private final AddonAttributeContainer attributeContainer = new AddonAttributeContainer();
        private RenderLayerContainer renderLayerContainer = null;
        private boolean hideSecondLayer = false;
        private ResourceLocation texture, model, animationLocation;
        public List<ParsedAnimationController<IAnimatable>> animationControllers;
        private AnimationFactory factory = GeckoLibUtil.createFactory(this);

        public ArmorItemImpl(ArmorMaterial materialIn, EquipmentSlot equipmentSlot, Properties builder) {
            super(materialIn, equipmentSlot, builder);
        }

        @Override
        public void registerControllers(AnimationData data) {
            if (this.animationLocation != null) {
                for (ParsedAnimationController<IAnimatable> parsed : this.animationControllers) {
                    var controller = parsed.createController(this);
                    data.addAnimationController(controller);
                }
            }
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
        public PackGeckoArmorItem setGeckoLocations(ResourceLocation modelLocation, ResourceLocation textureLocation, ResourceLocation animationLocation, List<ParsedAnimationController<IAnimatable>> animationControllers) {
            this.model = modelLocation;
            this.texture = textureLocation;
            this.animationLocation = animationLocation;
            this.animationControllers = animationControllers;
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
            return this.animationLocation;
        }
    }

}
