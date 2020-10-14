package net.threetag.threecore.item;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.DistExecutor;
import net.threetag.threecore.ability.*;
import net.threetag.threecore.addonpacks.item.ItemParser;
import net.threetag.threecore.capability.ItemAbilityContainerProvider;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

public class AbilityItem extends Item implements IAbilityProvider {

    private List<Supplier<Ability>> abilityGenerators;
    private List<ITextComponent> description;

    public AbilityItem(Properties properties) {
        super(properties);
    }

    public AbilityItem(JsonObject json, Properties properties) {
        super(properties);
        this.setAbilities(JSONUtils.hasField(json, "abilities") ? AbilityHelper.parseAbilityGenerators(JSONUtils.getJsonObject(json, "abilities"), true) : null);
        this.setDescription(JSONUtils.hasField(json, "description") ? ItemParser.parseDescriptionLines(json.get("description")) : null);
    }

    public AbilityItem setAbilities(List<Supplier<Ability>> abilities) {
        this.abilityGenerators = abilities;
        return this;
    }

    public AbilityItem addAbility(Supplier<Ability> abilityGenerator) {
        if (this.abilityGenerators == null)
            this.abilityGenerators = Lists.newArrayList();
        this.abilityGenerators.add(abilityGenerator);
        return this;
    }

    public AbilityItem setDescription(List<ITextComponent> description) {
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> this.description = description);
        return this;
    }

    public AbilityItem addDescriptionLine(ITextComponent line) {
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
            if (this.description == null)
                this.description = Lists.newArrayList();
            this.description.add(line);
        });
        return this;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (this.description != null)
            tooltip.addAll(this.description);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new ItemAbilityContainerProvider(stack);
    }

    @Override
    public AbilityMap getAbilities() {
        return new AbilityMap(this.abilityGenerators);
    }
}
