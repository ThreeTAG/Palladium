package net.threetag.palladium.item;

import com.google.common.collect.Multimap;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.addonpack.parser.ItemParser;
import net.threetag.palladium.documentation.JsonDocumentationBuilder;
import net.threetag.palladium.util.json.GsonUtil;
import net.threetag.palladiumcore.registry.client.ItemPropertyRegistry;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class AddonBowItem extends BowItem implements IAddonItem {

    private List<Component> tooltipLines;
    private final Map<EquipmentSlot, Multimap<Attribute, AttributeModifier>> attributeModifiers = new HashMap<>();
    private RenderLayerContainer renderLayerContainer = null;
    private final float velocity, inaccuracy;
    private final int useDuration;
    private final Predicate<ItemStack> projectiles;
    @Nullable
    private final Predicate<ItemStack> heldProjectiles;

    public AddonBowItem(float velocity, float inaccuracy, int useDuration, Predicate<ItemStack> projectiles, @Nullable Predicate<ItemStack> heldProjectiles, Properties properties) {
        super(properties);
        this.velocity = velocity;
        this.inaccuracy = inaccuracy;
        this.useDuration = useDuration;
        this.projectiles = projectiles;
        this.heldProjectiles = heldProjectiles;

        ItemPropertyRegistry.register(this, new ResourceLocation("pull"), (itemStack, clientLevel, livingEntity, i) -> {
            if (livingEntity == null) {
                return 0.0F;
            } else {
                return livingEntity.getUseItem() != itemStack ? 0.0F : (float) (itemStack.getUseDuration() - livingEntity.getUseItemRemainingTicks()) / 20.0F;
            }
        });

        ItemPropertyRegistry.register(
                this,
                new ResourceLocation("pulling"),
                (itemStack, clientLevel, livingEntity, i) -> livingEntity != null && livingEntity.isUsingItem() && livingEntity.getUseItem() == itemStack ? 1.0F : 0.0F
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
    public void releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeCharged) {
        if (livingEntity instanceof Player player) {
            boolean bl = player.getAbilities().instabuild || EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, stack) > 0;
            ItemStack itemStack = player.getProjectile(stack);
            if (!itemStack.isEmpty() || bl) {
                if (itemStack.isEmpty()) {
                    itemStack = new ItemStack(Items.ARROW);
                }

                int i = this.getUseDuration(stack) - timeCharged;
                float f = getPowerForTime(i);
                if (!((double) f < 0.1)) {
                    boolean bl2 = bl && itemStack.is(Items.ARROW);
                    if (!level.isClientSide) {
                        ArrowItem arrowItem = (ArrowItem) (itemStack.getItem() instanceof ArrowItem ? itemStack.getItem() : Items.ARROW);
                        AbstractArrow abstractArrow = arrowItem.createArrow(level, itemStack, player);
                        abstractArrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, f * this.velocity, this.inaccuracy);
                        if (f == 1.0F) {
                            abstractArrow.setCritArrow(true);
                        }

                        int j = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, stack);
                        if (j > 0) {
                            abstractArrow.setBaseDamage(abstractArrow.getBaseDamage() + (double) j * 0.5 + 0.5);
                        }

                        int k = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, stack);
                        if (k > 0) {
                            abstractArrow.setKnockback(k);
                        }

                        if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, stack) > 0) {
                            abstractArrow.setSecondsOnFire(100);
                        }

                        stack.hurtAndBreak(1, player, player2 -> player2.broadcastBreakEvent(player.getUsedItemHand()));
                        if (bl2 || player.getAbilities().instabuild && (itemStack.is(Items.SPECTRAL_ARROW) || itemStack.is(Items.TIPPED_ARROW))) {
                            abstractArrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                        }

                        level.addFreshEntity(abstractArrow);
                    }

                    level.playSound(
                            null,
                            player.getX(),
                            player.getY(),
                            player.getZ(),
                            SoundEvents.ARROW_SHOOT,
                            SoundSource.PLAYERS,
                            1.0F,
                            1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F
                    );
                    if (!bl2 && !player.getAbilities().instabuild) {
                        itemStack.shrink(1);
                        if (itemStack.isEmpty()) {
                            player.getInventory().removeItem(itemStack);
                        }
                    }

                    player.awardStat(Stats.ITEM_USED.get(this));
                }
            }
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

    public static class Parser implements ItemParser.ItemTypeSerializer {

        @Override
        public IAddonItem parse(JsonObject json, Properties properties) {
            float velocity = GsonHelper.getAsFloat(json, "velocity", 3F);
            float inaccuracy = GsonHelper.getAsFloat(json, "inaccuracy", 1F);
            int useDuration = GsonHelper.getAsInt(json, "use_duration", 72000);
            TagKey<Item> projectiles = TagKey.create(Registry.ITEM_REGISTRY, GsonUtil.getAsResourceLocation(json, "projectiles", new ResourceLocation("minecraft:arrows")));
            TagKey<Item> heldProjectiles = json.has("held_projectiles") ? TagKey.create(Registry.ITEM_REGISTRY, GsonUtil.getAsResourceLocation(json, "held_projectiles")) : null;

            return new AddonBowItem(velocity, inaccuracy, useDuration, stack -> stack.is(projectiles), heldProjectiles == null ? null : stack -> stack.is(heldProjectiles), properties);
        }

        @Override
        public void generateDocumentation(JsonDocumentationBuilder builder) {
            builder.setTitle("Bow");

            builder.addProperty("velocity", Float.class)
                    .description("Velocity multiplier for the shot projectile")
                    .fallback(3F).exampleJson(new JsonPrimitive(3F));

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
            return Palladium.id("bow");
        }
    }
}
