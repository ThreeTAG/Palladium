package net.threetag.threecore.ability;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.client.gui.ability.AbilitiesScreen;
import net.threetag.threecore.client.gui.widget.TranslucentButton;
import net.threetag.threecore.client.renderer.AbilityBarRenderer;
import net.threetag.threecore.client.settings.AbilityKeyBinding;
import net.threetag.threecore.network.AbilityKeyMessage;
import org.lwjgl.glfw.GLFW;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AbilityClientEventHandler {

    // TODO Add key modifiers to custom keybindings

    public static final String CATEGORY = "ThreeCore";
    public static final KeyBinding SCROLL_UP = new KeyBinding("key.threecore.scroll_up", KeyConflictContext.IN_GAME, InputMappings.Type.KEYSYM, 73, CATEGORY);
    public static final KeyBinding SCROLL_DOWN = new KeyBinding("key.threecore.scroll_down", KeyConflictContext.IN_GAME, InputMappings.Type.KEYSYM, 80, CATEGORY);

    public static List<AbilityKeyBinding> ABILITY_KEYS = Lists.newArrayList();
    public static Map<Integer, Boolean> KEY_STATE = Maps.newHashMap();

    public AbilityClientEventHandler() {
        if (Minecraft.getInstance() != null) {
            ClientRegistry.registerKeyBinding(SCROLL_UP);
            ClientRegistry.registerKeyBinding(SCROLL_DOWN);

            for (int i = 1; i <= AbilityBarRenderer.ENTRY_SHOW_AMOUNT; i++) {
                AbilityKeyBinding keyBinding = new AbilityKeyBinding("key.threecore.ability_" + i, KeyConflictContext.IN_GAME, InputMappings.Type.KEYSYM, i == 1 ? 86 : i == 2 ? 66 : i == 3 ? 78 : i == 4 ? 77 : i == 5 ? 188 : -1, i, CATEGORY);
                ClientRegistry.registerKeyBinding(keyBinding);
                ABILITY_KEYS.add(keyBinding);
            }
        }
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent e) {
        if (Minecraft.getInstance().player == null || Minecraft.getInstance().currentScreen != null)
            return;

        if (e.getAction() < GLFW.GLFW_REPEAT) {
            List<Ability> abilities = AbilityHelper.getAbilities(Minecraft.getInstance().player).stream().filter(a -> a.getConditionManager().isUnlocked() && a.getConditionManager().needsKey() && a.getDataManager().get(Ability.KEYBIND) > -1).collect(Collectors.toList());

            for (Ability ability : abilities) {
                if (ability != null && ability.getContainer() != null && ability.getDataManager().get(Ability.KEYBIND) == e.getKey()) {
                    if (!KEY_STATE.containsKey(e.getKey()))
                        KEY_STATE.put(e.getKey(), false);

                    if (KEY_STATE.get(e.getKey()) != (e.getAction() == GLFW.GLFW_PRESS))
                        ThreeCore.NETWORK_CHANNEL.sendToServer(new AbilityKeyMessage(ability.getContainer().getId(), ability.getId(), e.getAction() == GLFW.GLFW_PRESS));
                    KEY_STATE.put(e.getKey(), e.getAction() == GLFW.GLFW_PRESS);
                }
            }

            abilities = AbilityBarRenderer.getCurrentDisplayedAbilities(AbilityHelper.getAbilities(Minecraft.getInstance().player));
            for (AbilityKeyBinding keyBinding : ABILITY_KEYS) {
                if (e.getKey() == keyBinding.getKey().getKeyCode()) {
                    if (!KEY_STATE.containsKey(e.getKey()))
                        KEY_STATE.put(e.getKey(), false);
                    if (keyBinding.index <= abilities.size()) {
                        Ability ability = abilities.get(keyBinding.index - 1);
                        if (ability != null && ability.getContainer() != null && KEY_STATE.get(e.getKey()) != (e.getAction() == GLFW.GLFW_PRESS)) {
                            ThreeCore.NETWORK_CHANNEL.sendToServer(new AbilityKeyMessage(ability.getContainer().getId(), ability.getId(), e.getAction() == GLFW.GLFW_PRESS));
                            KEY_STATE.put(e.getKey(), e.getAction() == GLFW.GLFW_PRESS);
                        }
                    }
                }
            }
        }

        if (SCROLL_UP.isKeyDown()) {
            AbilityBarRenderer.scroll(true);
        } else if (SCROLL_DOWN.isKeyDown()) {
            AbilityBarRenderer.scroll(false);
        }
    }

    @SubscribeEvent
    public void onMouse(GuiScreenEvent.MouseScrollEvent.Pre e) {
        if (Minecraft.getInstance().player == null)
            return;

        if (Minecraft.getInstance().player.isSneaking() && e.getScrollDelta() != 0F) {
            AbilityBarRenderer.scroll(e.getScrollDelta() > 0);
            e.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onGuiInit(GuiScreenEvent.InitGuiEvent e) {
        if (e.getGui() instanceof ChatScreen) {
            e.addWidget(new TranslucentButton(e.getGui().width - 1 - 75, e.getGui().height - 40, 75, 20, I18n.format("gui.threecore.abilities"), b -> Minecraft.getInstance().displayGuiScreen(new AbilitiesScreen())));
        }
    }

    @SubscribeEvent
    public void onRenderLivingPre(RenderLivingEvent.Pre e) {
        for (InvisibilityAbility invisibilityAbility : AbilityHelper.getAbilitiesFromClass(e.getEntity(), InvisibilityAbility.class)) {
            if (invisibilityAbility.getConditionManager().isEnabled()) {
                e.setCanceled(true);
                return;
            }
        }
    }

    @SubscribeEvent
    public void onHeartsPre(RenderGameOverlayEvent.Pre e) {
        for (CustomHotbarAbility ability : AbilityHelper.getAbilitiesFromClass(Minecraft.getInstance().player, CustomHotbarAbility.class)) {
            if (ability.getConditionManager().isEnabled() && e.getType() == ability.getDataManager().get(CustomHotbarAbility.HOTBAR_ELEMENT)) {
                AbstractGui.GUI_ICONS_LOCATION = ability.getDataManager().get(CustomHotbarAbility.TEXTURE);
                Minecraft.getInstance().getTextureManager().bindTexture(AbstractGui.GUI_ICONS_LOCATION);
            }
        }
    }

    @SubscribeEvent
    public void onHeartsPost(RenderGameOverlayEvent.Post e) {
        for (CustomHotbarAbility ability : AbilityHelper.getAbilitiesFromClass(Minecraft.getInstance().player, CustomHotbarAbility.class)) {
            if (ability.getConditionManager().isEnabled() && e.getType() == ability.getDataManager().get(CustomHotbarAbility.HOTBAR_ELEMENT)) {
                AbstractGui.GUI_ICONS_LOCATION = new ResourceLocation("textures/gui/icons.png");
                Minecraft.getInstance().getTextureManager().bindTexture(AbstractGui.GUI_ICONS_LOCATION);
            }
        }
    }

}
