package net.threetag.palladium.client.animation;

import com.zigythebird.playeranim.animation.PlayerAnimationController;
import com.zigythebird.playeranim.api.PlayerAnimationFactory;
import com.zigythebird.playeranimcore.enums.PlayState;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.addonpack.log.AddonPackLog;

import java.util.HashMap;
import java.util.Map;

public class PalladiumAnimationManager extends SimpleJsonResourceReloadListener<PalladiumAnimation> {

    public static final ResourceLocation ID = Palladium.id("animations");
    public static final PalladiumAnimationManager INSTANCE = new PalladiumAnimationManager();

    public static final ResourceLocation COSMETIC_ANIMATION = Palladium.id("cosmetic_animation");
    public static final ResourceLocation IDLE_ANIMATION = Palladium.id("idle_animation");
    public static final ResourceLocation ACTIVE_ANIMATION = Palladium.id("active_animation");

    private final Map<ResourceLocation, PalladiumAnimation> byName = new HashMap<>();

    public PalladiumAnimationManager() {
        super(PalladiumAnimation.CODEC, FileToIdConverter.json("palladium/animations"));
    }

    public static void registerLayers(FMLClientSetupEvent e){
        e.enqueueWork(() -> PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(COSMETIC_ANIMATION, 500,
                player -> new PlayerAnimationController(player, (controller, state, animSetter) -> PlayState.STOP)
        ));
        e.enqueueWork(() -> PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(IDLE_ANIMATION, 1000,
                player -> new PlayerAnimationController(player, (controller, state, animSetter) -> PlayState.STOP)
        ));
        e.enqueueWork(() -> PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(ACTIVE_ANIMATION, 1500,
                player -> new PlayerAnimationController(player, (controller, state, animSetter) -> PlayState.STOP)
        ));
    }

    @Override
    protected void apply(Map<ResourceLocation, PalladiumAnimation> objects, ResourceManager resourceManager, ProfilerFiller profiler) {
        this.byName.clear();
        this.byName.putAll(objects);
        AddonPackLog.info("Loaded " + objects.size() + " animations");
    }

    public PalladiumAnimation get(ResourceLocation id) {
        return this.byName.get(id);
    }
}
