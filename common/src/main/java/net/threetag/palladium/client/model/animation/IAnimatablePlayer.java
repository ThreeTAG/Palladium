package net.threetag.palladium.client.model.animation;

import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;

public interface IAnimatablePlayer {

    ModifierLayer<IAnimation> palladium_getModifierLayer();

}
