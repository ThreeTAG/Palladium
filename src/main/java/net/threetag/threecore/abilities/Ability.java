package net.threetag.threecore.abilities;

import com.google.gson.JsonObject;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.INBTSerializable;
import net.threetag.threecore.abilities.client.EnumAbilityColor;
import net.threetag.threecore.abilities.client.gui.AbilitiesScreen;
import net.threetag.threecore.abilities.client.gui.AbilityScreen;
import net.threetag.threecore.abilities.condition.AbilityConditionManager;
import net.threetag.threecore.abilities.condition.Condition;
import net.threetag.threecore.util.icon.IIcon;
import net.threetag.threecore.util.icon.ItemIcon;
import net.threetag.threecore.util.scripts.events.AbilityTickScriptEvent;
import net.threetag.threecore.util.threedata.*;

public abstract class Ability implements INBTSerializable<CompoundNBT>, IThreeDataHolder {

    public static final ThreeData<Boolean> SHOW_IN_BAR = new BooleanThreeData("show_in_bar").setSyncType(EnumSync.SELF)
            .enableSetting("show_in_bar", "Determines if this ability should be displayed in the ability bar without a condition that displays it there.");
    public static final ThreeData<Boolean> HIDDEN = new BooleanThreeData("hidden").setSyncType(EnumSync.SELF);
    public static final ThreeData<ITextComponent> TITLE = new TextComponentThreeData("title").setSyncType(EnumSync.SELF)
            .enableSetting("title", "Allows you to set a custom title for this ability");
    public static final ThreeData<IIcon> ICON = new IconThreeData("icon").setSyncType(EnumSync.SELF)
            .enableSetting("icon", "Lets you customize the icon for the ability");
    public static final ThreeData<Integer> KEYBIND = new IntegerThreeData("key").setSyncType(EnumSync.SELF);

    public final AbilityType type;
    String id;
    public IAbilityContainer container;
    protected ThreeDataManager dataManager = new ThreeDataManager(this);
    protected AbilityConditionManager conditionManager = new AbilityConditionManager(this);
    protected int ticks = 0;
    public EnumSync sync = EnumSync.NONE;
    public boolean dirty = false;
    protected CompoundNBT additionalData;
    /**
     * Current wielder of this ability, mainly used for event managing, dont use within the actual ability!
     */
    public LivingEntity entity;

    public Ability(AbilityType type) {
        this.type = type;
        this.registerData();
    }

    public void readFromJson(JsonObject jsonObject) {
        this.dataManager.readFromJson(jsonObject);
        this.conditionManager.readFromJson(jsonObject);
    }

    public void registerData() {
        this.dataManager.register(SHOW_IN_BAR, false);
        this.dataManager.register(HIDDEN, isEffect());
        this.dataManager.register(TITLE,
                new TranslationTextComponent("ability." + this.type.getRegistryName().getNamespace() + "." + this.type.getRegistryName().getPath()));
        this.dataManager.register(ICON, new ItemIcon(Blocks.BARRIER));
        this.dataManager.register(KEYBIND, -1);
    }

    public CompoundNBT getAdditionalData() {
        if (this.additionalData == null)
            this.additionalData = new CompoundNBT();
        return additionalData;
    }

    @OnlyIn(Dist.CLIENT)
    public void drawIcon(Minecraft mc, AbstractGui gui, int x, int y) {
        if (this.getDataManager().has(ICON))
            this.getDataManager().get(ICON).draw(mc, x, y);
    }

    public boolean isEffect() {
        return false;
    }

    public void tick(LivingEntity entity) {
        this.entity = entity;
        this.conditionManager.update(entity);
        if (this.conditionManager.isEnabled()) {
            if (ticks == 0) {
                this.conditionManager.firstTick();
                firstTick(entity);
            }
            ticks++;
            action(entity);
        } else if (ticks != 0) {
            lastTick(entity);
            this.conditionManager.lastTick();
            ticks = 0;
        }

        new AbilityTickScriptEvent(entity, this).fire();
    }

    public void action(LivingEntity entity) {
        this.updateTick(entity);
    }

    @Deprecated
    public void updateTick(LivingEntity entity) {

    }

    public void firstTick(LivingEntity entity) {
    }

    public void lastTick(LivingEntity entity) {
    }

    public ThreeDataManager getDataManager() {
        return dataManager;
    }

    public AbilityConditionManager getConditionManager() {
        return conditionManager;
    }

    public final String getId() {
        return id;
    }

    public final IAbilityContainer getContainer() {
        return container;
    }

    @Override
    public <T> void update(ThreeData<T> data, T value) {
        this.sync = this.sync.add(data.getSyncType());
    }

    @Override
    public void setDirty() {
        this.dirty = true;
    }

    @OnlyIn(Dist.CLIENT)
    public EnumAbilityColor getColor() {
        return EnumAbilityColor.LIGHT_GRAY;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putString("AbilityType", this.type.getRegistryName().toString());
        nbt.put("Data", this.dataManager.serializeNBT());
        nbt.put("Conditions", this.conditionManager.serializeNBT());
        if (this.additionalData != null)
            nbt.put("AdditionalData", this.additionalData);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.dataManager.deserializeNBT(nbt.getCompound("Data"));
        this.conditionManager.deserializeNBT(nbt.getCompound("Conditions"));
        this.additionalData = nbt.getCompound("AdditionalData");
    }

    public CompoundNBT getUpdateTag() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putString("AbilityType", this.type.getRegistryName().toString());
        nbt.put("Data", this.dataManager.getUpdatePacket());
        nbt.put("Conditions", this.conditionManager.getUpdatePacket());
        if (this.additionalData != null)
            nbt.put("AdditionalData", this.additionalData);
        return nbt;
    }

    public void readUpdateTag(CompoundNBT nbt) {
        this.dataManager.readUpdatePacket(nbt.getCompound("Data"));
        this.conditionManager.readUpdatePacket(nbt.getCompound("Conditions"));
        this.additionalData = nbt.getCompound("AdditionalData");
    }

    @OnlyIn(Dist.CLIENT)
    public Screen getScreen(AbilitiesScreen screen) {
        for (Condition c : this.getConditionManager().getConditions()) {
            Screen s = c.getScreen(screen);
            if (s != null) {
                return s;
            }
        }

        return new AbilityScreen(this, screen);
    }
}
