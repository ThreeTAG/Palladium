package net.threetag.palladium.client;

import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.platform.Platform;
import dev.architectury.registry.ReloadListenerRegistry;
import net.minecraft.server.packs.PackType;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.client.energybeam.EnergyBeamManager;
import net.threetag.palladium.client.energybeam.EnergyBeamRendererSerializers;
import net.threetag.palladium.client.gui.screen.abilitybar.AbilityBar;
import net.threetag.palladium.client.gui.screen.power.PowersScreen;
import net.threetag.palladium.client.model.ModelLayerManager;
import net.threetag.palladium.client.particleemitter.ParticleEmitterManager;
import net.threetag.palladium.client.renderer.WatcherRenderer;
import net.threetag.palladium.client.renderer.entity.layer.PackRenderLayerManager;
import net.threetag.palladium.client.renderer.entity.layer.PackRenderLayerSerializers;
import net.threetag.palladium.compat.geckolib.GeckoLibCompat;
import net.threetag.palladium.core.registry.GuiLayerRegistry;
import net.threetag.palladium.entity.PalladiumEntityTypes;

import java.util.Collections;

public class PalladiumClient {

    public static void init() {
        PalladiumKeyMappings.init();

        // Entity Renderers
        PalladiumEntityTypes.initRenderers();

        // Overlay Renderer
        GuiLayerRegistry.register(Palladium.id("ability_bar"), AbilityBar.INSTANCE);
        ClientTickEvent.CLIENT_POST.register(AbilityBar.INSTANCE);

        // Screens
        PowersScreen.register();

        // Render Layers
        ReloadListenerRegistry.register(PackType.CLIENT_RESOURCES, PackRenderLayerManager.INSTANCE, Palladium.id("render_layers"), Collections.singletonList(ModelLayerManager.ID));
        PackRenderLayerSerializers.init();

        // Misc
        ReloadListenerRegistry.register(PackType.CLIENT_RESOURCES, ParticleEmitterManager.INSTANCE, Palladium.id("particle_emitters"));
        ReloadListenerRegistry.register(PackType.CLIENT_RESOURCES, EnergyBeamManager.INSTANCE, Palladium.id("energy_beams"));
        WatcherRenderer.init();
        EnergyBeamRendererSerializers.init();

        // Compat
        if (Platform.isModLoaded("geckolib")) {
            GeckoLibCompat.init();
        }
    }

}
