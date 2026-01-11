package net.threetag.palladium.client.gui.screen.abilitybar;

import io.netty.util.collection.IntObjectHashMap;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.gui.GuiLayer;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.client.gui.component.CompoundUiComponent;
import net.threetag.palladium.client.gui.component.UiAlignment;
import net.threetag.palladium.client.gui.component.UiComponent;
import net.threetag.palladium.client.texture.TextureReference;
import net.threetag.palladium.config.PalladiumClientConfig;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.power.PowerHolder;
import net.threetag.palladium.power.PowerUtil;
import net.threetag.palladium.power.ability.AbilityInstance;
import net.threetag.palladium.power.energybar.EnergyBarInstance;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = Palladium.MOD_ID, value = Dist.CLIENT)
public class AbilityBar implements GuiLayer, UiComponent {

    public static final AbilityBar INSTANCE = new AbilityBar();
    private static final Identifier TEXTURE = Palladium.id("textures/gui/ability_bar.png");
    public static int KEY_ROTATION = 0;
    public static boolean KEY_ROTATION_FORWARD = true;

    private List<AbilityList> lists = new ArrayList<>();
    private int selectedList = -1;
    private AbilityList currentList = null;
    private UiComponent toRender = null;

    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        if (this.currentList != null) {
            this.toRender = this.lists.size() == 1 && this.lists.getFirst().simple ? this.currentList.simplified : this.currentList.completeBar;
            var alignment = PalladiumClientConfig.ABILITY_BAR_ALIGNMENT.get();
            var pos = UiComponent.getPosition(this, Minecraft.getInstance().getWindow(), alignment);
            this.render(Minecraft.getInstance(), guiGraphics, deltaTracker, pos.x, pos.y, alignment);
        } else {
            this.toRender = null;
        }
    }

    @Override
    public int getWidth() {
        return this.toRender != null ? this.toRender.getWidth() : 0;
    }

    @Override
    public int getHeight() {
        return this.toRender != null ? this.toRender.getHeight() : 0;
    }

    @Override
    public void render(Minecraft minecraft, GuiGraphics gui, DeltaTracker deltaTracker, int x, int y, UiAlignment alignment) {
        if (this.currentList != null && this.toRender != null) {
            if (this.currentList.abilitiesAndEnergyBars != null)
                this.currentList.abilitiesAndEnergyBars.reverseOrder = alignment.isLeft();
            if (this.currentList.completeBar != null)
                this.currentList.completeBar.reverseOrder = alignment.isBottom();

            this.toRender.render(minecraft, gui, deltaTracker, x, y, alignment);
        }
    }

    public void populate() {
        var player = Minecraft.getInstance().player;
        this.currentList = null;
        this.lists.clear();

        for (PowerHolder holder : PowerUtil.getPowerHandler(player).getPowerHolders().values()) {
            List<AbilityList> containerList = new ArrayList<>();
            List<AbilityList> remainingLists = new ArrayList<>();
            List<AbilityInstance<?>> remaining = new ArrayList<>();
            for (AbilityInstance<?> abilityInstance : holder.getAbilities().values()) {
                if (abilityInstance.getAbility().getStateManager().isKeyBound() && !abilityInstance.getAbility().getProperties().isHiddenInBar()) {
                    int i = abilityInstance.getAbility().getProperties().getListIndex();

                    if (i >= 0) {
                        int listIndex = Math.floorDiv(i, 5);
                        int index = i % 5;

                        while (!(containerList.size() - 1 >= listIndex)) {
                            containerList.add(new AbilityList(holder));
                        }

                        AbilityList abilityList = containerList.get(listIndex);
                        abilityList.addAbility(index, abilityInstance);
                    } else {
                        remaining.add(abilityInstance);
                    }
                }
            }

            for (int i = 0; i < remaining.size(); i++) {
                AbilityInstance<?> abilityInstance = remaining.get(i);
                int listIndex = Math.floorDiv(i, 5);
                int index = i % 5;

                while (!(remainingLists.size() - 1 >= listIndex)) {
                    remainingLists.add(new AbilityList(holder));
                }

                AbilityList abilityList = remainingLists.get(listIndex);
                abilityList.addAbility(index, abilityInstance);
            }

            for (AbilityList list : containerList) {
                if (!list.isEmpty() && list.hasUnlocked()) {
                    this.lists.add(list);
                }
            }

            for (AbilityList list : remainingLists) {
                if (!list.isEmpty() && list.hasUnlocked()) {
                    this.lists.add(list);
                }
            }
        }

        if (this.lists.size() <= 1) {
            this.lists.forEach(AbilityList::simplify);
        }

        this.lists.forEach(list -> list.build(this.lists.size() > 1));

        if (this.lists.isEmpty()) {
            this.selectedList = -1;
        } else if (this.selectedList < 0) {
            this.selectedList = 0;
        }

        if (this.selectedList >= 0) {
            if (this.selectedList >= this.lists.size()) {
                this.selectedList = this.lists.size() - 1;
            }
            this.currentList = this.lists.get(this.selectedList);
        }
    }

    public void rotateList(boolean forward) {
        if (this.lists.isEmpty()) {
            return;
        }

        if (forward) {
            this.selectedList = this.selectedList + 1 >= this.lists.size() ? 0 : this.selectedList + 1;
        } else {
            this.selectedList = this.selectedList - 1 < 0 ? this.lists.size() - 1 : this.selectedList - 1;
        }

        KEY_ROTATION = 10;
        KEY_ROTATION_FORWARD = forward;
        this.currentList = this.lists.get(this.selectedList);
    }

    public AbilityList getCurrentList() {
        return this.currentList;
    }

    @SubscribeEvent
    static void clientTick(ClientTickEvent.Post e) {
        var client = Minecraft.getInstance();

        if (client.player != null) {
            if (KEY_ROTATION > 0) {
                KEY_ROTATION--;

                if (KEY_ROTATION == 0) {
                    KEY_ROTATION_FORWARD = !client.player.isCrouching();
                }
            } else {
                KEY_ROTATION_FORWARD = !client.player.isCrouching();
            }
        }
    }

    public static class AbilityList {

        public static final int MAX_ABILITIES = 5;
        private final PowerHolder powerHolder;
        private final IntObjectHashMap<List<AbilityInstance<?>>> abilities = new IntObjectHashMap<>();
        public boolean simple = false;
        private TextureReference texture;
        public SimplifiedPowerComponent simplified = null;
        public CompoundUiComponent completeBar = null;
        public CompoundUiComponent abilitiesAndEnergyBars = null;

        public AbilityList(PowerHolder powerHolder) {
            this.powerHolder = powerHolder;
            this.texture = powerHolder.getPower().value().getAbilityBarTexture();

            if (this.texture == null) {
                this.texture = TextureReference.normal(TEXTURE);
            }
        }

        public Identifier getTexture(DataContext context) {
            return this.texture.getTexture(context);
        }

        public void build(boolean showButton) {
            List<UiComponent> components = new ArrayList<>();
            components.add(new AbilityListComponent(this));

            for (EnergyBarInstance barInstance : this.powerHolder.getEnergyBars().values()) {
                components.add(new EnergyBarComponent(this, barInstance));
            }

            this.abilitiesAndEnergyBars = new CompoundUiComponent(components, false);
            this.abilitiesAndEnergyBars.padding = 1;
            this.completeBar = new CompoundUiComponent(true, new PowerIndicatorComponent(this, showButton), this.abilitiesAndEnergyBars);
            this.completeBar.padding = 1;
        }

        public PowerHolder getPowerHolder() {
            return powerHolder;
        }

        public AbilityList addAbility(int index, AbilityInstance<?> ability) {
            this.abilities.computeIfAbsent(index, integer -> new ArrayList<>()).add(ability);
            return this;
        }

        public boolean isEmpty() {
            for (int i = 0; i < MAX_ABILITIES; i++) {
                if (this.abilities.get(i) != null && !this.abilities.get(i).isEmpty()) {
                    return false;
                }
            }
            return true;
        }

        public boolean hasUnlocked() {
            for (AbilityInstance<?> entry : this.getDisplayedAbilities()) {
                if (entry != null && entry.isUnlocked()) {
                    return true;
                }
            }

            return false;
        }

        public AbilityInstance<?>[] getDisplayedAbilities() {
            AbilityInstance<?>[] entries = new AbilityInstance[MAX_ABILITIES];

            for (int i = 0; i < MAX_ABILITIES; i++) {
                if (this.abilities.get(i) != null) {
                    for (AbilityInstance<?> entry : this.abilities.get(i)) {
                        var current = entries[i];

                        if (current == null) {
                            entries[i] = entry;
                        } else if (!current.isUnlocked() && entry.isUnlocked()) {
                            entries[i] = entry;
                        }
                    }
                }
            }

            return entries;
        }

        public AbilityInstance<?> getAbility(int index) {
            return this.getDisplayedAbilities()[index];
        }

        public void simplify() {
            if (!this.powerHolder.getEnergyBars().isEmpty()) {
                return;
            }

            int abilities = 0;
            AbilityInstance<?> entry = null;

            for (AbilityInstance<?> ability : this.getDisplayedAbilities()) {
                if (ability != null && ability.isUnlocked()) {
                    abilities++;
                    entry = ability;
                }
            }

            this.simple = abilities == 1;

            if (this.simple) {
                this.abilities.clear();
                this.addAbility(0, entry);
                this.simplified = new SimplifiedPowerComponent(this, entry);
            }
        }
    }

}
