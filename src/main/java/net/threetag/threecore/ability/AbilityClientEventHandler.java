package net.threetag.threecore.ability;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.CustomizeSkinScreen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.CreativeScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.ability.container.DefaultAbilityContainer;
import net.threetag.threecore.ability.container.IAbilityContainer;
import net.threetag.threecore.client.gui.AccessoireScreen;
import net.threetag.threecore.client.gui.ability.AbilitiesScreen;
import net.threetag.threecore.client.gui.widget.IconButton;
import net.threetag.threecore.client.renderer.AbilityBarRenderer;
import net.threetag.threecore.client.settings.AbilityKeyBinding;
import net.threetag.threecore.network.AbilityKeyMessage;
import net.threetag.threecore.network.MultiJumpMessage;
import net.threetag.threecore.util.icon.IIcon;
import net.threetag.threecore.util.icon.ItemIcon;
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
    public void onKeyInput(TickEvent.PlayerTickEvent e) {
        if (e.phase == TickEvent.Phase.END && e.player == Minecraft.getInstance().player) {
            for (IAbilityContainer container : AbilityHelper.getAbilityContainers(e.player)) {
                if (container instanceof DefaultAbilityContainer && ((DefaultAbilityContainer) container).getLifetime() > -1) {

                }
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
        if (!Minecraft.getInstance().player.isCreative() && Minecraft.getInstance().gameSettings.keyBindJump.isKeyDown() && !Minecraft.getInstance().player.isOnGround()) {
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

        if (Minecraft.getInstance().player.isCrouching() && e.getScrollDelta() != 0F) {
            AbilityBarRenderer.scroll(e.getScrollDelta() > 0);
            e.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onGuiInit(GuiScreenEvent.InitGuiEvent e) {
        int abilityButtonXPos = -1;
        int abilityButtonYPos = -1;

        if (e.getGui() instanceof InventoryScreen || e.getGui().getClass().toString().equals("class top.theillusivec4.curios.client.gui.CuriosScreen")) {
            abilityButtonXPos = ((ContainerScreen<?>) e.getGui()).getGuiLeft() + 134;
            abilityButtonYPos = e.getGui().height / 2 - 23;
        } else if (e.getGui() instanceof CreativeScreen) {
            abilityButtonXPos = ((ContainerScreen<?>) e.getGui()).getGuiLeft() + 148;
            abilityButtonYPos = e.getGui().height / 2 - 50;
        }

        if (abilityButtonXPos > 0 && abilityButtonYPos > 0) {
            int finalAbilityButtonXPos = abilityButtonXPos;
            int finalAbilityButtonYPos = abilityButtonYPos;
            e.addWidget(new IconButton(finalAbilityButtonXPos, finalAbilityButtonYPos, new ItemIcon(ItemStack.EMPTY), b -> Minecraft.getInstance().displayGuiScreen(new AbilitiesScreen()), (button, matrixStack, mouseX, mouseY) -> e.getGui().renderTooltip(matrixStack, new StringTextComponent(I18n.format("gui.threecore.abilities")), mouseX, mouseY)) {
                @Override
                public IIcon getIcon() {
                    List<IIcon> icons = Lists.newArrayList();
                    Minecraft mc = Minecraft.getInstance();
                    AbilityHelper.getAbilityContainers(mc.player).forEach(container -> icons.add(container.getIcon()));
                    if (icons.size() <= 0) {
                        icons.add(new ItemIcon(Blocks.BARRIER));
                    }
                    int i = (mc.player.ticksExisted / 20) % icons.size();
                    return icons.get(i);
                }

                @Override
                public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
                    this.setPosition(finalAbilityButtonXPos, finalAbilityButtonYPos);
                    this.visible = e.getGui() instanceof CreativeScreen && ((CreativeScreen) e.getGui()).getSelectedTabIndex() == ItemGroup.INVENTORY.getIndex();
                    this.active = this.visible && AbilityHelper.getAbilityContainers(Minecraft.getInstance().player).size() > 0;
                    super.render(matrixStack, mouseX, mouseY, partialTicks);
                }
            });
        }

        if (e.getGui() instanceof CustomizeSkinScreen && Minecraft.getInstance().player != null) {
            e.addWidget(new Button(e.getGui().width / 2 - 100, e.getGui().height / 6 + 24 * (12 >> 1), 200, 20, new TranslationTextComponent("gui.threecore.accessoires"), (p_213079_1_) -> {
                e.getGui().getMinecraft().displayGuiScreen(new AccessoireScreen(e.getGui()));
            }));
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
    public void onRenderLivingPre(RenderNameplateEvent e) {
        if (e.getEntity() instanceof LivingEntity && !(e.getEntity() instanceof ArmorStandEntity)) {
            for (NameChangeAbility ability : AbilityHelper.getAbilitiesFromClass((LivingEntity) e.getEntity(), NameChangeAbility.class)) {
                if (ability.getConditionManager().isEnabled()) {
                    if (Minecraft.getInstance().player.isCreative()) {
                        //TODO does this work right?
                        e.setContent(new StringTextComponent(ability.get(NameChangeAbility.NAME).getString() + " (" + e.getOriginalContent() + ")"));
                    } else {
                        e.setContent(ability.get(NameChangeAbility.NAME));
                    }
                    return;
                }
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

        if (e.getType() == RenderGameOverlayEvent.ElementType.HELMET && Minecraft.getInstance().gameSettings.getPointOfView().func_243192_a()) {
            for (HUDAbility ability : AbilityHelper.getAbilitiesFromClass(abilities, HUDAbility.class)) {
                if (ability.getConditionManager().isEnabled()) {

                    RenderSystem.disableDepthTest();
                    RenderSystem.depthMask(false);
                    RenderSystem.defaultBlendFunc();
                    RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
                    RenderSystem.disableAlphaTest();
                    Minecraft.getInstance().getTextureManager().bindTexture(ability.get(HUDAbility.TEXTURE));
                    Tessellator tessellator = Tessellator.getInstance();
                    BufferBuilder bufferbuilder = tessellator.getBuffer();
                    bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
                    bufferbuilder.pos(0.0D, e.getWindow().getScaledHeight(), -90.0D).tex(0.0F, 1.0F).endVertex();
                    bufferbuilder.pos(e.getWindow().getScaledWidth(), e.getWindow().getScaledHeight(), -90.0D).tex(1.0F, 1.0F).endVertex();
                    bufferbuilder.pos(e.getWindow().getScaledWidth(), 0.0D, -90.0D).tex(1.0F, 0.0F).endVertex();
                    bufferbuilder.pos(0.0D, 0.0D, -90.0D).tex(0.0F, 0.0F).endVertex();
                    tessellator.draw();
                    RenderSystem.depthMask(true);
                    RenderSystem.enableDepthTest();
                    RenderSystem.enableAlphaTest();
                    RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
                }
            }
        }

        if (e.getType() == RenderGameOverlayEvent.ElementType.EXPERIENCE) {
            for (EnergyAbility ability : AbilityHelper.getAbilitiesFromClass(abilities, EnergyAbility.class)) {
                if (ability.getConditionManager().isUnlocked()) {
                    Minecraft mc = Minecraft.getInstance();
                    Integer[] energy_data = ability.dataManager.get(EnergyAbility.ENERGY_TEXTURE_INFO);
                    Integer[] base_data = ability.dataManager.get(EnergyAbility.BASE_TEXTURE_INFO);
                    double energy = ability.dataManager.get(EnergyAbility.AMOUNT);
                    double max = ability.dataManager.get(EnergyAbility.MAX_AMOUNT);

                    int scaledWidth = e.getWindow().getScaledWidth();
                    int scaledHeight = e.getWindow().getScaledHeight();

                    mc.getTextureManager().bindTexture(ability.dataManager.get(EnergyAbility.BASE_TEXTURE));

                    int baseX = base_data[0] >= 0 ? base_data[0] : scaledWidth + base_data[0];
                    int baseY = base_data[1] >= 0 ? base_data[1] : scaledHeight + base_data[1];

                    mc.ingameGUI.blit(e.getMatrixStack(), baseX, baseY, 0, 0, base_data[2], base_data[3]);

                    mc.getTextureManager().bindTexture(ability.dataManager.get(EnergyAbility.ENERGY_TEXTURE));

                    int energyX = energy_data[0] > 0 ? energy_data[0] : scaledWidth + energy_data[0];
                    int energyY = energy_data[1] > 0 ? energy_data[1] : scaledHeight + energy_data[1];
                    int energyWidth = energy_data[2] + (int) ((double) (Math.abs(energy_data[4]) - energy_data[2]) * (energy / max));
                    int energyHeight = energy_data[3] + (int) ((double) (Math.abs(energy_data[5]) - energy_data[3]) * (energy / max));
                    int energyU = energy_data[4] > 0 ? 0 : Math.abs(energy_data[4]) - energyWidth;
                    int energyV = energy_data[5] > 0 ? 0 : Math.abs(energy_data[5]) - energyHeight;

                    energyX = energy_data[4] >= 0 ? energyX : energyX + Math.abs(energy_data[4]) - energyWidth;
                    energyY = energy_data[5] >= 0 ? energyY : energyY + Math.abs(energy_data[5]) - energyHeight;

                    mc.ingameGUI.blit(e.getMatrixStack(), energyX, energyY, energyU, energyV, energyWidth, energyHeight);
                }
            }
        }
    }

}
