package net.threetag.palladium.client.animation;

import com.zigythebird.playeranim.api.PlayerAnimationFactory;
import com.zigythebird.playeranimcore.enums.PlayState;
import net.minecraft.resources.Identifier;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.threetag.palladium.Palladium;

public class PalladiumAnimationManager {

    public static final Identifier EMPTY_ANIMATION = Palladium.id("empty");
    public static final Identifier CONTROLLER_MOVEMENT = Palladium.id("movement");
    public static final Identifier CONTROLLER_FLIGHT = Palladium.id("flight");
    public static final Identifier CONTROLLER_SPECIAL = Palladium.id("special");

    public static void registerLayers(FMLClientSetupEvent e) {
        e.enqueueWork(() -> PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(CONTROLLER_MOVEMENT, 500,
                player -> {
                    var controller = new RenderLayerAwareAnimationController(player, (animationController, animationData, animationSetter) -> PlayState.STOP);
                    ClientPlayerAnimationHandler.getHandler(player).setContainer(PalladiumAnimationLayer.MOVEMENT, new PackControllerAnimationContainer(PalladiumAnimationLayer.MOVEMENT, player, controller));
                    return controller;
                }
        ));

        e.enqueueWork(() -> PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(CONTROLLER_FLIGHT, 1000,
                player -> {
                    var controller = new RenderLayerAwareAnimationController(player, (animationController, animationData, animationSetter) -> PlayState.STOP);
                    ClientPlayerAnimationHandler.getHandler(player).setContainer(PalladiumAnimationLayer.FLIGHT, new FlightAnimationContainer(player, controller));
                    return controller;
                }
        ));

        e.enqueueWork(() -> PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(CONTROLLER_SPECIAL, 1500,
                player -> {
                    var controller = new RenderLayerAwareAnimationController(player, (animationController, animationData, animationSetter) -> PlayState.STOP);
                    ClientPlayerAnimationHandler.getHandler(player).setContainer(PalladiumAnimationLayer.SPECIAL, new PackControllerAnimationContainer(PalladiumAnimationLayer.MOVEMENT, player, controller));
                    return controller;
                }
        ));
    }
}
