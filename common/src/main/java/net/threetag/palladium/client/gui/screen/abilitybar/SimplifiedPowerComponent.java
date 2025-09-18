package net.threetag.palladium.client.gui.screen.abilitybar;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.RenderType;
import net.threetag.palladium.client.gui.component.UiAlignment;
import net.threetag.palladium.client.gui.component.UiComponent;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.power.ability.AbilityInstance;

public class SimplifiedPowerComponent implements UiComponent {

    private final AbilityBar.AbilityList abilityList;
    private final AbilityInstance<?> abilityInstance;

    public SimplifiedPowerComponent(AbilityBar.AbilityList abilityList, AbilityInstance<?> abilityInstance) {
        this.abilityList = abilityList;
        this.abilityInstance = abilityInstance;
    }

    @Override
    public int getWidth() {
        return 24;
    }

    @Override
    public int getHeight() {
        return 24;
    }

    @Override
    public void render(Minecraft minecraft, GuiGraphics gui, DeltaTracker deltaTracker, int x, int y, UiAlignment alignment) {
        var texture = this.abilityList.getTexture(DataContext.forAbility(minecraft.player, this.abilityInstance));
        gui.blit(RenderPipelines.GUI_TEXTURED,texture, x, y, 0, 168, 24, 24, 256, 256);
        AbilityListComponent.renderAbility(minecraft, texture, gui, deltaTracker, x + 3, y + 3, alignment, this.abilityInstance, 0);
    }
}
