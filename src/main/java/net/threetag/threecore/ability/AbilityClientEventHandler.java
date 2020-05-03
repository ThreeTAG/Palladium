package net.threetag.threecore.ability;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
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
import net.threetag.threecore.network.MultiJumpMessage;
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
                AbilityKeyBinding keyBinding = new AbilityKeyBinding("key.threecore.ability_" + i, KeyConflictContext.IN_GAME, InputMappings.Type.KEYSYM, i == 1 ? 86 : i == 2 ? 66 : i == 3 ? 78 : i == 4 ? 77 : i == 5 ? 44 : -1, i, CATEGORY);
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

        // Multi Jump
        if (Minecraft.getInstance().gameSettings.keyBindJump.isKeyDown() && !Minecraft.getInstance().player.onGround) {
            for (MultiJumpAbility ability : AbilityHelper.getAbilitiesFromClass(Minecraft.getInstance().player, MultiJumpAbility.class)) {
                if (ability.getConditionManager().isEnabled()) {
                    ThreeCore.NETWORK_CHANNEL.sendToServer(new MultiJumpMessage(ability.container.getId(), ability.getId()));
//                    Minecraft.getInstance().player.jump();
                }
            }
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

        // Set all keys to unpressed; when an ability opens a GUI, the unpressing of the button will not register and therefore you will need to hit the button twice the next time
        for (Integer i : KEY_STATE.keySet()) {
            KEY_STATE.put(i, false);
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
        List<Ability> abilities = AbilityHelper.getAbilities(Minecraft.getInstance().player);

        for (CustomHotbarAbility ability : AbilityHelper.getAbilitiesFromClass(abilities, CustomHotbarAbility.class)) {
            if (ability.getConditionManager().isEnabled() && e.getType() == ability.getDataManager().get(CustomHotbarAbility.HOTBAR_ELEMENT)) {
                AbstractGui.GUI_ICONS_LOCATION = new ResourceLocation("textures/gui/icons.png");
                Minecraft.getInstance().getTextureManager().bindTexture(AbstractGui.GUI_ICONS_LOCATION);
            }
        }

        if (e.getType() == RenderGameOverlayEvent.ElementType.HELMET && Minecraft.getInstance().gameSettings.thirdPersonView == 0) {
            for (HUDAbility ability : AbilityHelper.getAbilitiesFromClass(abilities, HUDAbility.class)) {
                if (ability.getConditionManager().isEnabled()) {
                    GlStateManager.disableDepthTest();
                    GlStateManager.depthMask(false);
                    GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                    GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
                    GlStateManager.disableAlphaTest();
                    Minecraft.getInstance().getTextureManager().bindTexture(ability.get(HUDAbility.TEXTURE));
                    Tessellator tessellator = Tessellator.getInstance();
                    BufferBuilder bufferbuilder = tessellator.getBuffer();
                    bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
                    bufferbuilder.pos(0.0D, e.getWindow().getScaledHeight(), -90.0D).tex(0.0D, 1.0D).endVertex();
                    bufferbuilder.pos(e.getWindow().getScaledWidth(), e.getWindow().getScaledHeight(), -90.0D).tex(1.0D, 1.0D).endVertex();
                    bufferbuilder.pos(e.getWindow().getScaledWidth(), 0.0D, -90.0D).tex(1.0D, 0.0D).endVertex();
                    bufferbuilder.pos(0.0D, 0.0D, -90.0D).tex(0.0D, 0.0D).endVertex();
                    tessellator.draw();
                    GlStateManager.depthMask(true);
                    GlStateManager.enableDepthTest();
                    GlStateManager.enableAlphaTest();
                    GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
                }
            }
        }
    }

}
