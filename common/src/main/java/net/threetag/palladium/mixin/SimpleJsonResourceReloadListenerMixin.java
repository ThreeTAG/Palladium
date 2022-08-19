package net.threetag.palladium.mixin;

import com.google.gson.JsonElement;
import dev.architectury.platform.Platform;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.crafting.RecipeManager;
import net.threetag.palladium.recipe.condition.RecipeCondition;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Iterator;
import java.util.Map;

@Mixin(SimpleJsonResourceReloadListener.class)
public class SimpleJsonResourceReloadListenerMixin {

    @Shadow
    @Final
    private String directory;

    @SuppressWarnings("ConstantConditions")
    @Inject(at = @At("RETURN"), method = "prepare(Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)Ljava/util/Map;")
    private void fromJson(ResourceManager resourceManager, ProfilerFiller profiler, CallbackInfoReturnable<Map<ResourceLocation, JsonElement>> callback) {
        if (Platform.isForge() && (Object) this instanceof RecipeManager) {
            return;
        }

        profiler.push(String.format("Palladium resource conditions: %s", this.directory));

        Iterator<Map.Entry<ResourceLocation, JsonElement>> it = callback.getReturnValue().entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<ResourceLocation, JsonElement> entry = it.next();
            JsonElement resourceData = entry.getValue();

            if (resourceData.isJsonObject() && !RecipeCondition.processConditions(resourceData.getAsJsonObject(), "conditions"))
                it.remove();
        }

        profiler.pop();
    }

}
