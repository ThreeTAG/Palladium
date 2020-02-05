package net.threetag.threecore.ability.condition;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.threetag.threecore.ability.Ability;
import net.threetag.threecore.client.gui.ability.AbilitiesScreen;
import net.threetag.threecore.client.gui.ability.BuyAbilityScreen;
import net.threetag.threecore.util.icon.ExperienceIcon;
import net.threetag.threecore.util.threedata.EnumSync;
import net.threetag.threecore.util.threedata.ExperienceThreeData;
import net.threetag.threecore.util.threedata.ThreeData;

public class XPBuyableAbilityCondition extends BuyableAbilityCondition {

    public static ThreeData<ExperienceThreeData.Experience> EXPERIENCE = new ExperienceThreeData("experience").setSyncType(EnumSync.SELF).enableSetting("experience", "Determines the amount of XP the player has to spend to activate this condition.");

    public XPBuyableAbilityCondition(Ability ability) {
        super(ConditionType.XP_BUY, ability);
    }

    @Override
    public void registerData() {
        super.registerData();
        this.dataManager.register(EXPERIENCE, new ExperienceThreeData.Experience(false, 5));
    }

    @Override
    public ITextComponent createTitle() {
        ExperienceThreeData.Experience xp = this.dataManager.get(EXPERIENCE);
        if (xp == null)
            return super.createTitle();
        else if (xp.isLevels())
            return new TranslationTextComponent(Util.makeTranslationKey("ability.condition", this.type.getRegistryName()) + ".levels" + (this.dataManager.get(INVERT) ? ".not" : ""), xp.getValue());
        else
            return new TranslationTextComponent(Util.makeTranslationKey("ability.condition", this.type.getRegistryName()) + ".points" + (this.dataManager.get(INVERT) ? ".not" : ""), xp.getValue());
    }

    @Override
    public boolean isAvailable(LivingEntity entity) {
        return entity instanceof PlayerEntity && this.dataManager.get(EXPERIENCE).has((PlayerEntity) entity);
    }

    @Override
    public boolean takeFromEntity(LivingEntity entity) {
        boolean available = isAvailable(entity);
        if (entity instanceof PlayerEntity)
            this.dataManager.get(EXPERIENCE).take((PlayerEntity) entity);
        return available;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public Screen getScreen(AbilitiesScreen screen) {
        return this.dataManager.get(BOUGHT) ? null : new BuyAbilityScreen(this.ability, this,
                new ExperienceIcon(this.dataManager.get(EXPERIENCE)),
                new TranslationTextComponent("ability.condition.threecore.xp_buy.info." + (this.dataManager.get(EXPERIENCE).isLevels() ? "levels" : "points"),
                        this.dataManager.get(EXPERIENCE).getValue()), screen);
    }
}
