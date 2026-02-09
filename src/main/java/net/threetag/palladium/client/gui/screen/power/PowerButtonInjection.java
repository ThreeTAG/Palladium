package net.threetag.palladium.client.gui.screen.power;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.client.gui.ui.screen.TabUiScreen;
import net.threetag.palladium.client.gui.ui.screen.UiLayoutManager;
import net.threetag.palladium.client.gui.widget.IconButton;
import net.threetag.palladium.icon.Icon;
import net.threetag.palladium.icon.ItemIcon;
import net.threetag.palladium.power.PowerInstance;
import net.threetag.palladium.power.PowerUtil;
import org.joml.Vector2i;

import java.util.*;

@EventBusSubscriber(modid = Palladium.MOD_ID, value = Dist.CLIENT)
public class PowerButtonInjection {

    @SubscribeEvent
    static void screenInit(ScreenEvent.Init.Post e) {
        var screen = e.getScreen();
        var pos = getButtonPosition(screen);

        if (pos != null && screen instanceof AbstractContainerScreen<?> gui) {
            Button button = new RotatingIconButton(gui.getGuiLeft() + pos.x, gui.getGuiTop() + pos.y, b -> openPowerScreen(screen.getMinecraft().player));
            button.setTooltip(Tooltip.create(Component.translatable("gui.palladium.powers")));
            screen.addRenderableWidget(button);
        }
    }

    @SubscribeEvent
    static void screenInit(ScreenEvent.Render.Pre e) {
        var screen = e.getScreen();
        var pos = getButtonPosition(screen);

        if (pos != null && screen instanceof AbstractContainerScreen<?> gui) {
            for (GuiEventListener child : e.getScreen().children()) {
                if (child instanceof RotatingIconButton powerButton) {
                    powerButton.visible = !(screen instanceof CreativeModeInventoryScreen) || CreativeModeInventoryScreen.selectedTab == BuiltInRegistries.CREATIVE_MODE_TAB.getValue(CreativeModeTabs.INVENTORY);
                    powerButton.active = screen.getMinecraft().player != null && !getAvailablePowers(Minecraft.getInstance().player).isEmpty();
                    powerButton.setPosition(gui.getGuiLeft() + pos.x, gui.getGuiTop() + pos.y);
                }
            }
        }
    }

    public static void openPowerScreen(Player player) {
        List<TabUiScreen.Tab> tabs = new ArrayList<>();
        Map<TabUiScreen.Tab, PowerInstance> map = new HashMap<>();

        for (PowerInstance powerInstance : getAvailablePowers(player)) {
            var layout = UiLayoutManager.INSTANCE.get(powerInstance.getPower().value().getScreenId());

            if (layout != null) {
                var tab = new TabUiScreen.Tab(layout, powerInstance.getPower().value().getName(), powerInstance.getPower().value().getIcon());
                tabs.add(tab);
                map.put(tab, powerInstance);
            }
        }

        Minecraft.getInstance().setScreen(new PowerUiScreen(tabs, map));
    }

    public static List<PowerInstance> getAvailablePowers(Player player) {
        return PowerUtil.getPowerHandler(player).getPowerInstances().values().stream().filter(instance -> !instance.getPower().value().isHidden()).toList();
    }

    public static Vector2i getButtonPosition(Screen screen) {
        if (screen instanceof InventoryScreen) {
            return new Vector2i(134, 60);
        } else if (screen instanceof CreativeModeInventoryScreen) {
            return new Vector2i(148, 18);
        }
        return null;
    }

    private static class RotatingIconButton extends IconButton {

        public RotatingIconButton(int x, int y, OnPress onPress) {
            super(x, y, new ItemIcon(ItemStack.EMPTY), onPress, DEFAULT_NARRATION);
        }

        @Override
        public Icon getIcon() {
            Minecraft mc = Minecraft.getInstance();
            List<Icon> icons = getAvailablePowers(mc.player).stream().map(instance -> instance.getPower().value().getIcon()).toList();

            if (icons.isEmpty()) {
                icons = Collections.singletonList(new ItemIcon(Blocks.BARRIER));
            }

            int i = (Objects.requireNonNull(mc.player).tickCount / 20) % icons.size();
            return icons.get(i);
        }
    }
}
