package net.threetag.palladium.client.gui.ui.screen;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.threetag.palladium.client.gui.screen.DelayedRenderCallReceiver;
import net.threetag.palladium.client.gui.ui.component.UiComponent;
import net.threetag.palladium.client.gui.ui.layout.UiLayout;
import net.threetag.palladium.logic.context.DataContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class UiScreen extends Screen implements DelayedRenderCallReceiver {

    // codecs are my passion
    public static final Codec<UiScreenBackground> BACKGROUND_CODEC = Codec.either(Codec.BOOL,
            Codec.either(UiScreenBackground.Simple.CODEC,
                            Codec.either(UiScreenBackground.Sprite.CODEC, UiScreenBackground.RepeatingTexture.CODEC).xmap(
                                    either -> either.map(
                                            Function.identity(), Function.identity()
                                    ), background -> background instanceof UiScreenBackground.Sprite sprite ? Either.left(sprite) : Either.right((UiScreenBackground.RepeatingTexture) background)
                            )
                    )
                    .xmap(
                            either -> either.map(
                                    Function.identity(), Function.identity()
                            ), background -> background instanceof UiScreenBackground.Simple simple ? Either.left(simple) : Either.right((UiScreenBackground.Sprite) background)
                    )
    ).xmap(
            either -> either.map(
                    bool -> bool ? UiScreenBackground.Sprite.DEFAULT : UiScreenBackground.Empty.INSTANCE,
                    Function.identity()
            ),
            background -> background instanceof UiScreenBackground.Empty ? Either.left(false) : Either.right(background)
    );

    private final UiLayout layout;
    protected int layoutWidth, layoutHeight;
    protected int innerLeftPos, leftPos;
    protected int innerTopPos, topPos;
    private final Map<UiComponent, AbstractWidget> widgets = new HashMap<>();
    private final List<Consumer<GuiGraphics>> delayedRenderCalls = new ArrayList<>();

    public UiScreen(UiLayout layout) {
        super(Component.empty());
        this.layout = layout;
        this.layoutWidth = layout.getWidth();
        this.layoutHeight = layout.getHeight();
    }

    @Override
    protected void init() {
        super.init();

        this.leftPos = (this.width - this.layoutWidth) / 2;
        this.topPos = (this.height - this.layoutHeight) / 2;
        this.widgets.clear();

        this.layout.addComponents(this, this.leftPos, this.topPos, (component, widget) -> {
            widget.visible = component.getProperties().visibility().test(DataContext.forEntity(this.minecraft.player));
            this.widgets.put(component, widget);
            this.addRenderableWidget(widget);
        });
    }

    @Override
    public void tick() {
        super.tick();
        var context = DataContext.forEntity(this.minecraft.player);

        for (Map.Entry<UiComponent, AbstractWidget> e : this.widgets.entrySet()) {
            e.getValue().visible = e.getKey().getProperties().visibility().test(context);
        }
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        this.layout.renderBackground(guiGraphics, this.leftPos, this.topPos);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        for (Consumer<GuiGraphics> renderCall : this.delayedRenderCalls) {
            renderCall.accept(guiGraphics);
        }

        this.delayedRenderCalls.clear();
    }

    @Override
    public void renderDelayed(Consumer<GuiGraphics> consumer) {
        this.delayedRenderCalls.add(consumer);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
