package net.threetag.palladium.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.item.recipe.TailoringRecipe;
import net.threetag.palladium.menu.TailoringMenu;
import net.threetag.palladiumcore.network.MessageC2S;
import net.threetag.palladiumcore.network.MessageContext;
import net.threetag.palladiumcore.network.MessageType;
import org.jetbrains.annotations.NotNull;

public class TailoringCraftMessage extends MessageC2S {

    private final ResourceLocation recipeId;

    public TailoringCraftMessage(FriendlyByteBuf buf) {
        this.recipeId = buf.readResourceLocation();
    }

    public TailoringCraftMessage(ResourceLocation recipeId) {
        this.recipeId = recipeId;
    }

    @Override
    public @NotNull MessageType getType() {
        return PalladiumNetwork.TAILORING_CRAFT;
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeResourceLocation(this.recipeId);
    }

    @Override
    public void handle(MessageContext context) {
        var recipe = context.getPlayer().level().getRecipeManager().byKey(this.recipeId).orElseThrow();

        if (recipe instanceof TailoringRecipe r && context.getPlayer().containerMenu instanceof TailoringMenu menu && menu.canCraft(context.getPlayer(), r)) {
            menu.craft(context.getPlayer(), r);
        }
    }
}
