package net.threetag.palladium.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.client.screen.TailoringScreen;
import net.threetag.palladium.item.recipe.TailoringRecipe;
import net.threetag.palladiumcore.network.MessageContext;
import net.threetag.palladiumcore.network.MessageS2C;
import net.threetag.palladiumcore.network.MessageType;
import net.threetag.palladiumcore.util.Platform;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class SyncAvailableTailoringRecipes extends MessageS2C {

    private final List<ResourceLocation> recipes;

    public SyncAvailableTailoringRecipes(List<ResourceLocation> recipes) {
        this.recipes = recipes;
    }

    public SyncAvailableTailoringRecipes(FriendlyByteBuf buf) {
        this.recipes = buf.readList(FriendlyByteBuf::readResourceLocation);
    }

    @Override
    public @NotNull MessageType getType() {
        return PalladiumNetwork.SYNC_AVAILABLE_TAILORING_RECIPES;
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeCollection(this.recipes, FriendlyByteBuf::writeResourceLocation);
    }

    @Override
    public void handle(MessageContext context) {
        if (Platform.isClient()) {
            this.handleClient(context);
        }
    }

    @Environment(EnvType.CLIENT)
    private void handleClient(MessageContext context) {
        var recipeManager = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();
        List<TailoringRecipe> recipes = this.recipes.stream().map(id -> (TailoringRecipe) recipeManager.byKey(id).orElseThrow()).toList();
        TailoringScreen.setAvailableRecipes(recipes);
    }
}
