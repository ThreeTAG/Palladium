package net.threetag.palladium.fabric.mixin;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.threetag.palladium.core.event.PalladiumPlayerEvents;
import net.threetag.palladium.fabric.platform.FabricEntities;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.concurrent.atomic.AtomicReference;

@Mixin(Player.class)
public abstract class PlayerMixin implements FabricEntities.RefreshableDisplayName {

    @Shadow
    public abstract Component getName();

    @Unique
    private Component palladium_displayname;

    @Override
    public void palladium$refreshDisplayName() {
        AtomicReference<Component> name = new AtomicReference<>(this.getName());
        PalladiumPlayerEvents.NAME_FORMAT.invoker().playerNameFormat((Player) (Object) this, this.getName(), name);
        this.palladium_displayname = name.get();
    }

    @ModifyArg(method = "getDisplayName", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/scores/PlayerTeam;formatNameForTeam(Lnet/minecraft/world/scores/Team;Lnet/minecraft/network/chat/Component;)Lnet/minecraft/network/chat/MutableComponent;"), index = 1)
    private Component injected(Component x) {
        if (this.palladium_displayname == null) {
            this.palladium$refreshDisplayName();
        }
        return this.palladium_displayname;
    }
}
