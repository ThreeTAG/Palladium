package net.threetag.palladium.item.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.equipment.ArmorType;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.threetag.palladium.util.PalladiumCodecs;
import net.threetag.palladium.util.Utils;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class ItemTailoringRecipe implements TailoringRecipe {

    protected final Component title;
    protected final Map<ArmorType, ItemStack> results;
    protected final List<SizedIngredient> ingredients;
    protected final Ingredient toolIngredient;
    protected final Optional<ResourceLocation> toolIcon;
    protected final Optional<ResourceLocation> categoryId;
    protected final boolean requiresUnlocking;

    @Nullable
    private PlacementInfo placementInfo;

    public ItemTailoringRecipe(Component title, Map<ArmorType, ItemStack> results, List<SizedIngredient> ingredients, Ingredient toolIngredient, Optional<ResourceLocation> toolIcon, Optional<ResourceLocation> categoryId, boolean requiresUnlocking) {
        this.title = title;
        this.results = results;
        this.ingredients = ingredients;
        this.toolIngredient = toolIngredient;
        this.toolIcon = toolIcon;
        this.categoryId = categoryId;
        this.requiresUnlocking = requiresUnlocking;
    }

    @Override
    public @NotNull RecipeSerializer<? extends Recipe<TailoringRecipeInput>> getSerializer() {
        return PalladiumRecipeSerializers.ITEM_TAILORING.get();
    }

    @Override
    public @NotNull PlacementInfo placementInfo() {
        if (this.placementInfo == null) {
            this.placementInfo = PlacementInfo.create(List.of(this.toolIngredient));
        }

        return this.placementInfo;
    }

    @Override
    public Component title() {
        return this.title;
    }

    @Override
    public Map<ArmorType, ItemStack> getResults() {
        return this.results;
    }

    @Override
    public List<SizedIngredient> getIngredients() {
        return this.ingredients;
    }

    @Override
    public Ingredient toolIngredient() {
        return this.toolIngredient;
    }

    @Override
    public Optional<ResourceLocation> toolIcon() {
        return this.toolIcon;
    }

    @Override
    public Optional<ResourceLocation> categoryId() {
        return this.categoryId;
    }

    @Override
    public boolean requiresUnlocking() {
        return this.requiresUnlocking;
    }

    public static class Serializer implements RecipeSerializer<ItemTailoringRecipe> {

        public static final MapCodec<ItemTailoringRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                ComponentSerialization.CODEC.fieldOf("title").forGetter(ItemTailoringRecipe::title),
                Codec.unboundedMap(ArmorType.CODEC, PalladiumCodecs.SIMPLE_ITEM_STACK).fieldOf("results").forGetter(ItemTailoringRecipe::getResults),
                PalladiumCodecs.listOrPrimitive(SizedIngredient.NESTED_CODEC).fieldOf("ingredients").forGetter(ItemTailoringRecipe::getIngredients),
                Ingredient.CODEC.fieldOf("tool").forGetter(ItemTailoringRecipe::toolIngredient),
                ResourceLocation.CODEC.optionalFieldOf("tool_icon").forGetter(ItemTailoringRecipe::toolIcon),
                ResourceLocation.CODEC.optionalFieldOf("category").forGetter(ItemTailoringRecipe::categoryId),
                Codec.BOOL.optionalFieldOf("requires_unlocking", true).forGetter(ItemTailoringRecipe::requiresUnlocking)
        ).apply(instance, ItemTailoringRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, ItemTailoringRecipe> STREAM_CODEC = StreamCodec.composite(
                ComponentSerialization.STREAM_CODEC, ItemTailoringRecipe::title,
                ByteBufCodecs.map(Utils::newMap, PalladiumCodecs.ARMOR_TYPE_STREAM_CODEC, ItemStack.STREAM_CODEC), ItemTailoringRecipe::getResults,
                SizedIngredient.STREAM_CODEC.apply(ByteBufCodecs.list()), ItemTailoringRecipe::getIngredients,
                Ingredient.CONTENTS_STREAM_CODEC, ItemTailoringRecipe::toolIngredient,
                ByteBufCodecs.optional(ResourceLocation.STREAM_CODEC), ItemTailoringRecipe::toolIcon,
                ByteBufCodecs.optional(ResourceLocation.STREAM_CODEC), ItemTailoringRecipe::categoryId,
                ByteBufCodecs.BOOL, ItemTailoringRecipe::requiresUnlocking,
                ItemTailoringRecipe::new
        );

        @Override
        public @NotNull MapCodec<ItemTailoringRecipe> codec() {
            return CODEC;
        }

        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, ItemTailoringRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
