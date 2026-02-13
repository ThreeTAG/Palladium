package net.threetag.palladium.dialog;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.resources.Identifier;
import net.minecraft.server.dialog.action.Action;

import java.util.Map;
import java.util.Optional;

public record OpenScreenAction(Identifier screenId) implements Action {

    public static final MapCodec<OpenScreenAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Identifier.CODEC.fieldOf("screen").forGetter(OpenScreenAction::screenId)
    ).apply(instance, OpenScreenAction::new));

    @Override
    public MapCodec<? extends Action> codec() {
        return CODEC;
    }

    @Override
    public Optional<ClickEvent> createAction(Map<String, ValueGetter> valueGetters) {
        return Optional.of(new ClickEvent.Custom(PalladiumDialogActions.OPEN_SCREEN.getId(), Optional.of(StringTag.valueOf(this.screenId().toString()))));
    }

}
