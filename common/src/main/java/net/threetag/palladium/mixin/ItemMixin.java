package net.threetag.palladium.mixin;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.KnowledgeBookItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.threetag.palladium.item.recipe.TailoringRecipe;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Item.class)
public class ItemMixin {

    @Inject(method = "appendHoverText", at = @At("RETURN"))
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced, CallbackInfo ci) {
        if ((Object) this instanceof KnowledgeBookItem) {
            CompoundTag compoundTag = stack.getTag();

            if (compoundTag != null && compoundTag.contains("Recipes", Tag.TAG_LIST)) {
                ListTag listTag = compoundTag.getList("Recipes", Tag.TAG_STRING);
                RecipeManager recipeManager = level.getRecipeManager();
                tooltipComponents.add(Component.translatable("item.palladium.knowledge_book.grants").withStyle(ChatFormatting.GRAY));

                for (Tag tag : listTag) {
                    var id = new ResourceLocation(tag.getAsString());
                    recipeManager.byKey(id).ifPresent(recipe -> {
                        if (recipe instanceof TailoringRecipe tailoringRecipe) {
                            tooltipComponents.add(CommonComponents.space().append(tailoringRecipe.getTitle().copy().withStyle(ChatFormatting.BLUE)));
                        } else {
                            tooltipComponents.add(CommonComponents.space().append(recipe.getResultItem(level.registryAccess()).getHoverName().copy().withStyle(ChatFormatting.BLUE)));
                        }
                    });
                }
            }

        }
    }

}
