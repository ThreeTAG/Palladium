package net.threetag.palladium.item;

import com.google.common.collect.Multimap;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.addonpack.parser.ItemParser;
import net.threetag.palladium.documentation.JsonDocumentationBuilder;
import net.threetag.palladium.util.PlayerSlot;
import net.threetag.palladium.util.json.GsonUtil;
import net.threetag.palladiumcore.registry.client.ItemPropertyRegistry;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public class AddonCrossbowItem extends CrossbowItem implements IAddonItem {

    private List<Component> tooltipLines;
    private RenderLayerContainer renderLayerContainer = null;
    private final AddonAttributeContainer attributeContainer = new AddonAttributeContainer();
    private final float velocityMultiplier, inaccuracy;
    private final int useDuration;
    private final Predicate<ItemStack> projectiles;
    @Nullable
    private final Predicate<ItemStack> heldProjectiles;

    public AddonCrossbowItem(float velocityMultiplier, float inaccuracy, int useDuration, Predicate<ItemStack> projectiles, @Nullable Predicate<ItemStack> heldProjectiles, Properties properties) {
        super(properties);
        this.velocityMultiplier = velocityMultiplier;
        this.inaccuracy = inaccuracy;
        this.useDuration = useDuration;
        this.projectiles = projectiles;
        this.heldProjectiles = heldProjectiles;

        ItemPropertyRegistry.register(
                this,
                new ResourceLocation("pull"),
                (itemStack, clientLevel, livingEntity, i) -> {
                    if (livingEntity == null) {
                        return 0.0F;
                    } else {
                        return CrossbowItem.isCharged(itemStack)
                                ? 0.0F
                                : (float) (itemStack.getUseDuration() - livingEntity.getUseItemRemainingTicks()) / (float) CrossbowItem.getChargeDuration(itemStack);
                    }
                }
        );
        ItemPropertyRegistry.register(
                this,
                new ResourceLocation("pulling"),
                (itemStack, clientLevel, livingEntity, i) -> livingEntity != null
                        && livingEntity.isUsingItem()
                        && livingEntity.getUseItem() == itemStack
                        && !CrossbowItem.isCharged(itemStack)
                        ? 1.0F
                        : 0.0F
        );
        ItemPropertyRegistry.register(
                this,
                new ResourceLocation("charged"),
                (itemStack, clientLevel, livingEntity, i) -> livingEntity != null && CrossbowItem.isCharged(itemStack) ? 1.0F : 0.0F
        );
        ItemPropertyRegistry.register(
                this,
                new ResourceLocation("firework"),
                (itemStack, clientLevel, livingEntity, i) -> livingEntity != null
                        && CrossbowItem.isCharged(itemStack)
                        && CrossbowItem.containsChargedProjectile(itemStack, Items.FIREWORK_ROCKET)
                        ? 1.0F
                        : 0.0F
        );
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return this.useDuration;
    }

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return this.projectiles;
    }

    @Override
    public Predicate<ItemStack> getSupportedHeldProjectiles() {
        return this.heldProjectiles == null ? this.getAllSupportedProjectiles() : this.heldProjectiles;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack itemStack = player.getItemInHand(usedHand);
        if (isCharged(itemStack)) {
            performShooting(level, player, usedHand, itemStack, getShootingPower(itemStack) * this.velocityMultiplier, this.inaccuracy);
            setCharged(itemStack, false);
            return InteractionResultHolder.consume(itemStack);
        } else if (!player.getProjectile(itemStack).isEmpty()) {
            if (!isCharged(itemStack)) {
                this.startSoundPlayed = false;
                this.midLoadSoundPlayed = false;
                player.startUsingItem(usedHand);
            }

            return InteractionResultHolder.consume(itemStack);
        } else {
            return InteractionResultHolder.fail(itemStack);
        }
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

    public static class Parser implements ItemParser.ItemTypeSerializer {

        @Override
        public IAddonItem parse(JsonObject json, Properties properties) {
            float velocityMultiplier = GsonHelper.getAsFloat(json, "velocity_multiplier", 1F);
            float inaccuracy = GsonHelper.getAsFloat(json, "inaccuracy", 1F);
            int useDuration = GsonHelper.getAsInt(json, "use_duration", 72000);
            TagKey<Item> projectiles = TagKey.create(Registry.ITEM_REGISTRY, GsonUtil.getAsResourceLocation(json, "projectiles", new ResourceLocation("minecraft:arrows")));
            TagKey<Item> heldProjectiles = json.has("held_projectiles") ? TagKey.create(Registry.ITEM_REGISTRY, GsonUtil.getAsResourceLocation(json, "held_projectiles")) : null;

            return new AddonCrossbowItem(velocityMultiplier, inaccuracy, useDuration, stack -> stack.is(projectiles), heldProjectiles == null ? null : stack -> stack.is(heldProjectiles), properties);
        }

        @Override
        public void generateDocumentation(JsonDocumentationBuilder builder) {
            builder.setTitle("Crossbow");

            builder.addProperty("velocity_multiplier", Float.class)
                    .description("Velocity multiplier for the shot projectile, works differently to the bow one.")
                    .fallback(1F).exampleJson(new JsonPrimitive(1F));

            builder.addProperty("inaccuracy", Float.class)
                    .description("Inaccuracy for the shot projectile")
                    .fallback(1F).exampleJson(new JsonPrimitive(1F));

            builder.addProperty("use_duration", Integer.class)
                    .description("Amount of ticks the bow can be used for")
                    .fallback(72000).exampleJson(new JsonPrimitive(72000));

            builder.addProperty("projectiles", ResourceLocation.class)
                    .description("Item tag which contains all items that can be shot. By default all Minecraft arrows")
                    .fallback(new ResourceLocation("arrows")).exampleJson(new JsonPrimitive("minecraft:arrows"));

            builder.addProperty("held_projectiles", ResourceLocation.class)
                    .description("Item tag which contains all items that can be shot by being in the off hand. Can be left out to fallback to the 'projectiles' option")
                    .fallback(null).exampleJson(new JsonPrimitive("minecraft:arrows"));
        }

        @Override
        public ResourceLocation getId() {
            return Palladium.id("crossbow");
        }
    }
}
