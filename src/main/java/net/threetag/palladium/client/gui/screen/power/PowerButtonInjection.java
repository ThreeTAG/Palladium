package net.threetag.palladium.client.gui.screen.power;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.EventBusSubscriber;
import net.threetag.palladium.Palladium;

@EventBusSubscriber(modid = Palladium.MOD_ID, value = Dist.CLIENT)
public class PowerButtonInjection {

//    @SubscribeEvent
//    static void screenInit(ScreenEvent.Init.Post e) {
//        var screen = e.getScreen();
//        var pos = getButtonPosition(screen);
//
//        if (pos != null && screen instanceof AbstractContainerScreen<?> gui) {
//            Button button = new EditButton(gui.getGuiLeft() + pos.x, gui.getGuiTop() + pos.y, b -> Minecraft.getInstance().setScreen(new PlayerCustomizationScreen(screen)));
//            button.setTooltip(Tooltip.create(Component.translatable(PlayerCustomizationScreen.TITLE_TRANSLATION_KEY)));
//            button.active = screen.getMinecraft().player != null && CustomizationHelper.hasSelectableCustomization(screen.getMinecraft().player);
//            screen.addRenderableWidget(button);
//        }
//    }
//
//    @SubscribeEvent
//    static void screenInit(ScreenEvent.Render.Pre e) {
//        var screen = e.getScreen();
//        var pos = getButtonPosition(screen);
//
//        if (pos != null && screen instanceof AbstractContainerScreen<?> gui) {
//            for (GuiEventListener child : e.getScreen().children()) {
//                if (child instanceof EditButton editButton) {
//                    editButton.visible = !(screen instanceof CreativeModeInventoryScreen) || CreativeModeInventoryScreen.selectedTab == BuiltInRegistries.CREATIVE_MODE_TAB.getValue(CreativeModeTabs.INVENTORY);
//                    editButton.active = screen.getMinecraft().player != null && CustomizationHelper.hasSelectableCustomization(screen.getMinecraft().player);
//                    editButton.setPosition(gui.getGuiLeft() + pos.x, gui.getGuiTop() + pos.y);
//                }
//            }
//        }
//    }
//
//    private static Vector2i getButtonPosition(Screen screen) {
//        if (screen instanceof InventoryScreen) {
//            return new Vector2i(63, 66);
//        } else if (screen instanceof CreativeModeInventoryScreen) {
//            return new Vector2i(93, 37);
//        } else {
//            return null;
//        }
//    }

}
