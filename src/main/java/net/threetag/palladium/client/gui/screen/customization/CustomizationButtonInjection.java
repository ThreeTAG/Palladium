package net.threetag.palladium.client.gui.screen.customization;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.options.SkinCustomizationScreen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.ClientHooks;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.client.gui.widget.EditButton;
import net.threetag.palladium.customization.CustomizationHelper;
import net.threetag.palladium.customization.EntityCustomizationHandler;
import net.threetag.palladium.flag.PalladiumFeatureFlags;
import org.joml.Vector2i;

import java.util.Collections;
import java.util.Objects;

@EventBusSubscriber(modid = Palladium.MOD_ID, value = Dist.CLIENT)
public class CustomizationButtonInjection {

    @SubscribeEvent
    static void screenInit(ScreenEvent.Init.Post e) {
        var screen = e.getScreen();
        var pos = getButtonPosition(screen);

        if (pos != null && screen instanceof AbstractContainerScreen<?> gui) {
            Button button = new EditButton(gui.getGuiLeft() + pos.x, gui.getGuiTop() + pos.y, b -> Minecraft.getInstance().setScreen(new PlayerCustomizationScreen(screen)));
            button.setTooltip(Tooltip.create(Component.translatable(PlayerCustomizationScreen.TITLE_TRANSLATION_KEY)));
            button.active = screen.getMinecraft().player != null && CustomizationHelper.hasSelectableCustomization(screen.getMinecraft().player);
            screen.addRenderableWidget(button);
        }

        if (screen instanceof SkinCustomizationScreen && Objects.requireNonNull(screen.getMinecraft().getConnection()).isFeatureEnabled(FeatureFlagSet.of(PalladiumFeatureFlags.EYE_SELECTION))) {
            for (GuiEventListener child : screen.children()) {
                if (child instanceof OptionsList widget) {
                    var eyeSelectionButton = SpriteIconButton.builder(Component.translatable(EyeSelectionScreen.TRANS_TITLE),
                                    b -> ClientHooks.pushGuiLayer(screen.getMinecraft(), new EyeSelectionScreen(screen.getMinecraft().player, EntityCustomizationHandler.get(screen.getMinecraft().player).getEyeSelection()).disableBackgroundRendering()),
                                    false)
                            .sprite(Palladium.id("widget/eye_button"), 20, 20)
                            .build();
                    widget.addSmall(Collections.singletonList(eyeSelectionButton));
                    return;
                }
            }
        }
    }

    @SubscribeEvent
    static void screenInit(ScreenEvent.Render.Pre e) {
        var screen = e.getScreen();
        var pos = getButtonPosition(screen);

        if (pos != null && screen instanceof AbstractContainerScreen<?> gui) {
            for (GuiEventListener child : e.getScreen().children()) {
                if (child instanceof EditButton editButton) {
                    editButton.visible = !(screen instanceof CreativeModeInventoryScreen) || CreativeModeInventoryScreen.selectedTab == BuiltInRegistries.CREATIVE_MODE_TAB.getValue(CreativeModeTabs.INVENTORY);
                    editButton.active = screen.getMinecraft().player != null && CustomizationHelper.hasSelectableCustomization(screen.getMinecraft().player);
                    editButton.setPosition(gui.getGuiLeft() + pos.x, gui.getGuiTop() + pos.y);
                }
            }
        }
    }

    private static Vector2i getButtonPosition(Screen screen) {
        if (screen instanceof InventoryScreen) {
            return new Vector2i(63, 66);
        } else if (screen instanceof CreativeModeInventoryScreen) {
            return new Vector2i(93, 37);
        } else {
            return null;
        }
    }

}
