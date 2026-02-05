package net.threetag.palladium.client.gui.ui.screen;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.threetag.palladium.client.gui.screen.DelayedRenderCallReceiver;
import net.threetag.palladium.client.gui.ui.component.UiComponent;
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

    private final UiScreenConfiguration configuration;
    protected int innerLeftPos, outerLeftPos;
    protected int innerTopPos, outerTopPos;
    private final Map<UiComponent, AbstractWidget> widgets = new HashMap<>();
    private final List<Consumer<GuiGraphics>> delayedRenderCalls = new ArrayList<>();

    public UiScreen(UiScreenConfiguration configuration) {
        super(Component.empty());
        this.configuration = configuration;
    }

    @Override
    protected void init() {
        super.init();

        this.outerLeftPos = (this.width - this.configuration.width()) / 2;
        this.outerTopPos = (this.height - this.configuration.height()) / 2;
        this.innerLeftPos = this.outerLeftPos + this.configuration.padding().left();
        this.innerTopPos = this.outerTopPos + this.configuration.padding().top();
        this.widgets.clear();

        for (UiComponent component : this.configuration.components()) {
            var widget = component.buildWidget(this);
            widget.visible = component.getProperties().visibility().test(DataContext.forEntity(this.minecraft.player));
            this.widgets.put(component, widget);
            this.addRenderableWidget(widget);
        }
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
        this.configuration.background().render(guiGraphics, this.outerLeftPos, this.outerTopPos, this.configuration.width(), this.configuration.height());
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        for (Consumer<GuiGraphics> renderCall : this.delayedRenderCalls) {
            renderCall.accept(guiGraphics);
        }

        this.delayedRenderCalls.clear();
    }

    public ScreenRectangle getInnerRectangle() {
        return new ScreenRectangle(
                this.innerLeftPos,
                this.innerTopPos,
                this.configuration.width() - this.configuration.padding().left() - this.configuration.padding().right(),
                this.configuration.height() - this.configuration.padding().top() - this.configuration.padding().bottom()
        );
    }

    @Override
    public void renderDelayed(Consumer<GuiGraphics> consumer) {
        this.delayedRenderCalls.add(consumer);
    }
}
